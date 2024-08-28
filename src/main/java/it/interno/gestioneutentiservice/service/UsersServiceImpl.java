package it.interno.gestioneutentiservice.service;

import it.interno.gestioneutentiservice.client.OimClient;
import it.interno.gestioneutentiservice.dto.FiltroUsersDto;
import it.interno.gestioneutentiservice.dto.UfficioDto;
import it.interno.gestioneutentiservice.dto.UsersDto;
import it.interno.gestioneutentiservice.dto.oim.UtenteOimDto;
import it.interno.gestioneutentiservice.entity.*;
import it.interno.gestioneutentiservice.mapper.OrarioLavoroMapper;
import it.interno.gestioneutentiservice.mapper.UfficioMapper;
import it.interno.gestioneutentiservice.mapper.UsersMapper;
import it.interno.gestioneutentiservice.repository.*;
import it.interno.gestioneutentiservice.utils.GenericUtils;
import it.interno.gestioneutentiservice.utils.MailUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService{

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private OrarioLavoroRepository orarioLavoroRepository;
    @Autowired
    private OrarioLavoroMapper orarioLavoroMapper;
    @Autowired
    private UfficioRepository ufficioRepository;
    @Autowired
    private UfficioMapper ufficioMapper;
    @Autowired
    private GroupMembersRepository groupMembersRepository;
    @Autowired
    private OimClient oimClient;
    @Autowired
    private QualificaRepository qualificaRepository;
    @Autowired
    private ForzaPoliziaRepository forzaPoliziaRepository;

    @Override
    @Transactional
    public void resetPassword(String codiceUtente, String utenteOperatore) {
        oimClient.resetPassword(codiceUtente);
        usersRepository.logResetPassword(codiceUtente, utenteOperatore);
    }

    @Override
    public Page<UsersDto> getUtentiAbilitati(FiltroUsersDto filtro, String ufficioOperatore, String ruoloOperatore) {

        Pageable pageable = PageRequest.of(
                filtro.getPaginazione().getNumeroPagina(),
                filtro.getPaginazione().getNumeroElementi(),
                Sort.by(new Sort.Order(Sort.Direction.fromString(filtro.getPaginazione().getOrderDirection()),
                        filtro.getPaginazione().getOrderBy()).ignoreCase())
        );

        Page<Users> usersPage = usersRepository.getUtentiAbilitatiByFiltro(StringUtils.isBlank(filtro.getNomeCognome()) ? "" : filtro.getNomeCognome(),
                filtro.getForzaPolizia(), filtro.getCodiceUfficio(), filtro.getTipoComandante(), ufficioOperatore, ruoloOperatore, pageable);

        List<UsersDto> usersDto = usersPage
                .stream()
                .map(el -> {
                    UsersDto temp = usersMapper.toDto(el, usersRepository);
                    temp.setOrarioLavoro(orarioLavoroMapper.toDto(orarioLavoroRepository.getOrarioLavoroByUtente(temp.getCodiceUtente())));

                    List<UfficioDto> listaUfficiComandati = ufficioRepository.getUfficiByComandante(temp.getCodiceUtente())
                            .stream()
                            .map(uf -> ufficioMapper.toDto(uf))
                            .toList();

                    temp.setUfficiComandati(listaUfficiComandati);

                    return temp;
                })
                .toList();

        return new PageImpl<>(usersDto, pageable, usersPage.getTotalElements());
    }

    @Override
    @Transactional
    public UsersDto getUserByCodiceUtente(String codiceUtente) {

        UsersDto userDto = usersMapper.toDto(usersRepository.getUtenteByCodiceUtente(codiceUtente), usersRepository);

        if(userDto == null)
            return null;

        userDto.setOrarioLavoro(orarioLavoroMapper.toDto(orarioLavoroRepository.getOrarioLavoroByUtente(codiceUtente)));

        List<UfficioDto> listaUfficiComandati = ufficioRepository.getUfficiByComandante(codiceUtente)
                .stream()
                .map(el -> ufficioMapper.toDto(el))
                .toList();

        userDto.setUfficiComandati(listaUfficiComandati);

        return userDto;
    }

    @Override
    @Transactional
    public void unlockUser(String codiceUtente) {

        oimClient.unlockUser(codiceUtente);
        Users utente = usersRepository.getUtenteByCodiceUtente(codiceUtente);

        MailUtils.sendMail(
                utente.getEmailPrimaria() == 0 ? utente.getEmail() : utente.getEmailPrivata(),
                "Richiesta sblocco utenza " + codiceUtente,
                "La richiesta di sblocco è stata effettuata.\nRiprovare tra qualche minuto.",
                usersRepository
        );
    }

    @Override
    public Integer getNumeroUtentiAbilitati(String ufficioOperatore, String ruoloOperatore) {
        return usersRepository.getNumeroUtentiAbilitati(ufficioOperatore, ruoloOperatore);
    }

    @Override
    @Transactional
    public UsersDto updateUser(UsersDto input, String utenteAggiornamento, String ufficioAggiornamento, boolean sprofilazioneUtente) {

        Timestamp currentTimestamp = GenericUtils.getCurrentTimestamp();

        Users utente = usersMapper.toEntity(input);
        utente.setUtenteAggiornamento(utenteAggiornamento);
        utente.setDataAggiornamento(currentTimestamp);

        Users utenteDB = usersRepository.getUtenteByCodiceUtente(input.getCodiceUtente());

        //eliminazione condizione come richiesto da documentazione


        //if(utente.getCodiceUfficio() == null)
        //    utente.setCodiceUfficio(utenteDB.getCodiceUfficio());

        // Aggiorno Utente
        usersRepository.save(utente);

        // Aggiornamento Orario lavoro
        OrarioLavoro orarioLavoro = orarioLavoroMapper.toEntity(input.getOrarioLavoro());
        OrarioLavoro suDB = orarioLavoroRepository.getOrarioLavoroByUtente(input.getCodiceUtente());

        // Ufficio rimosso
        if(orarioLavoro == null && suDB != null){
            suDB.setDataCancellazione(currentTimestamp);
            suDB.setUtenteCancellazione(utenteAggiornamento);
            suDB.setUfficioCancellazione(ufficioAggiornamento);
            orarioLavoroRepository.save(suDB);
            this.sprofilazioneRuoli(input.getCodiceUtente(), sprofilazioneUtente, utenteAggiornamento, ufficioAggiornamento, currentTimestamp);

        //Ufficio reinserito
        } else if(orarioLavoro != null && suDB == null){
            orarioLavoro.setDataInsermento(currentTimestamp);
            orarioLavoro.setUtenteInserimento(utenteAggiornamento);
            orarioLavoro.setUfficioInserimento(ufficioAggiornamento);
            orarioLavoroRepository.save(orarioLavoro);
        // Orario Lavoro Modificato
        } else if(orarioLavoro != null && !suDB.equals(orarioLavoro)){
            suDB.setDataCancellazione(currentTimestamp);
            suDB.setUtenteCancellazione(utenteAggiornamento);
            suDB.setUfficioCancellazione(ufficioAggiornamento);
            orarioLavoro.setDataInsermento(currentTimestamp);
            orarioLavoro.setUtenteInserimento(utenteAggiornamento);
            orarioLavoro.setUfficioInserimento(ufficioAggiornamento);
            orarioLavoroRepository.save(suDB);
            orarioLavoroRepository.save(orarioLavoro);

            // Ufficio Modificato
            if(!suDB.getCodiceUfficio().equals(orarioLavoro.getCodiceUfficio()))
                this.sprofilazioneRuoli(input.getCodiceUtente(), sprofilazioneUtente, utenteAggiornamento, ufficioAggiornamento, currentTimestamp);
        }

        // Sprofilazione dei ruoli che non rispettano le regole di sicurezza per motivi diversi dall'ufficio
        this.sprofilazioneRuoli(input.getCodiceUtente(), false, utenteAggiornamento, ufficioAggiornamento, currentTimestamp);

        // UPDATE SU OIM
        Qualifica qualifica = qualificaRepository.findById(utente.getQualifica()).orElse(null);
        Ufficio ufficio = ufficioRepository.findById(orarioLavoro == null ? " " : orarioLavoro.getCodiceUfficio()).orElse(null);
        //check chiusura ufficio

        // fix come richiesto da documentazione
        // Ufficio rimosso
        if(orarioLavoro == null && suDB != null){
            ufficio = null;
            qualifica.setCategoria(null);
        }


        ForzaPolizia fp = forzaPoliziaRepository.findById(utente.getForzaPolizia().getIdGruppo()).orElse(null);
        oimClient.modificaUtente(new UtenteOimDto(
                utente.getCodiceUtente(),
                utente.getCognome(),
                utente.getNome(),
                utente.getEmailPrimaria() == 0 ? utente.getEmail() : utente.getEmailPrivata(),
                ufficio == null ? null : ufficio.getCodiceUfficio(),  //codice ufficio address oim
                ufficio == null ? null : ufficio.getDescrizioneUfficio(), // descrizione ufficio address oim
                utente.getTelefono(),
                qualifica == null ? null : GenericUtils.getDescrizioneQualifica(qualifica, utente.getForzaPolizia().getIdGruppo()), // descrizione qualifica
                qualifica == null ? null : qualifica.getCategoria(), // categoria address oim
                fp == null ? null : fp.getNome(), // descrizione ente
                utente.getDataNascita(),
                utente.getCodiceLuogoNascita().getDescrizioneLuogo(),
                utente.getSesso().toString(),
                utente.getCodiceFiscale(),
                utente.getEmailPrimaria() == 0 ? utente.getEmailPrivata() : utente.getEmail()

                //new UtenteAggiornamento.Address("work", input.getDescrizioneUfficio(), input.getCategoria(), input.getCodiceUfficio()));
        ));

        checkCambioEmail(utenteDB, utente, utenteAggiornamento);

        return this.getUserByCodiceUtente(utente.getCodiceUtente());
    }

    @Transactional
    public void sprofilazioneRuoli(String codiceUtente, boolean sprofilazioneUtente, String utenteAggiornamento, String ufficioAggiornamento, Timestamp dataAggiornamento){
        List<GroupMembers> ruoliAssociati = groupMembersRepository.getAllByUtente(codiceUtente);
        List<String> ruoliDissociati = new ArrayList<>();

        if(sprofilazioneUtente){
            // SI SPROFILANO TUTTI I RUOLI DELL'UTENTE
            ruoliAssociati.forEach(el -> {
                el.setUtenteCancellazione(utenteAggiornamento);
                el.setUfficioCancellazione(ufficioAggiornamento);
                el.setDataCancellazione(dataAggiornamento);
                groupMembersRepository.save(el);

                ruoliDissociati.add(el.getNomeRuolo());
            });
        }else{
            // SI SPROFILANO TUTTI I RUOLI DELL'UTENTE CHE NON RISPETTANO PIU' LE REGOLE
            ruoliAssociati.forEach(el -> {
                if(groupMembersRepository.isRuoloApplicativoAssegnabile(codiceUtente, el.getAppId(), el.getNomeRuolo()) == 0){
                    el.setUtenteCancellazione(utenteAggiornamento);
                    el.setUfficioCancellazione(ufficioAggiornamento);
                    el.setDataCancellazione(dataAggiornamento);
                    groupMembersRepository.save(el);

                    ruoliDissociati.add(el.getNomeRuolo());
                }
            });
        }

        // DISASSOCIAZIONE RUOLI OIM
        if(!ruoliDissociati.isEmpty() && ruoliDissociati.size() <= 10){
            oimClient.rimozioneRuoliAUtente(ruoliDissociati, codiceUtente);
        } else if(!ruoliDissociati.isEmpty()){
            ruoliDissociati.forEach(ruolo -> {
                groupMembersRepository.deleteGroupMembersOimInStagingByUtenteERuolo(codiceUtente, ruolo);
                groupMembersRepository.deleteGroupMembersOimByUtenteERuolo(codiceUtente, ruolo);
            });
        }
    }

    @Override
    @Transactional
    public UsersDto deleteUser(String codiceUtente, String utenteCancellazione, String ufficioCancellazione){

        if(!checkDeleteUser(codiceUtente)){
            throw new RuntimeException("Non è possibile cancellare l'utente, poichè è comandante di uno o più uffici associati ad altri utenti.");
        }
        Users utente = usersRepository.getUtenteByCodiceUtente(codiceUtente);
        utente.setDataCancellazione(GenericUtils.getCurrentTimestamp());
        utente.setUtenteCancellazione(utenteCancellazione);
        utente.setUfficioCancellazione(ufficioCancellazione);

        usersRepository.save(utente);
        oimClient.deleteUtente(codiceUtente);

        return usersMapper.toDto(utente, usersRepository);
    }


    private Boolean checkDeleteUser(String codiceUtente){
       List<String> codiciUfficiComandati =  usersRepository.getUfficiComandatiDaUtente(codiceUtente);
         return codiciUfficiComandati.stream().allMatch(el -> checkOnlyComandantePresenteInUfficio(el,codiceUtente));
    }



    private Boolean checkOnlyComandantePresenteInUfficio(String codiceUfficio,String codiceUtente){
        return usersRepository.countUserByUfficioEsclusoUtente(codiceUfficio,codiceUtente) == 0;
    }

    @Override
    public List<UsersDto> getUsersByAutocompleteCodiceCognomeEFP(String parametro, Integer codiceForzaPolizia, String ufficioOperatore, String ruoloOperatore) {
        return usersRepository.getUsersByAutocompleteCodiceCognomeEFP(parametro, codiceForzaPolizia, ufficioOperatore, ruoloOperatore)
                .stream()
                .map(el -> usersMapper.toDto(el, usersRepository))
                .toList();
    }

    @Override
    public boolean checkComandantePresenteInUfficio(String codiceUfficio) {
        return usersRepository.checkComandantePresenteInUfficio(codiceUfficio) != null;
    }

    @Override
    public boolean utentePresenteSuSDI(String codiceUtente) {
        return usersRepository.utentePresenteSuSDI(codiceUtente) != null;
    }

    private void checkCambioEmail(Users utenteDB, Users utenteModificato, String utenteFP){

        String subject = "La tua mail è stata cambiata";
        String indicazioneOperatore = "L'operatore che ha eseguito il cambio è: " + utenteFP;

        if(!StringUtils.isBlank(utenteDB.getEmail()) && StringUtils.isBlank(utenteModificato.getEmail())){
            // La mail istituzionale è stata rimossa
            MailUtils.sendMail(
                    utenteDB.getEmail(),
                    subject,
                    "La tua mail istituzionale è stata cambiata, adesso è la seguente: NESSUNA\n" +
                            indicazioneOperatore,
                    usersRepository
            );
        }else if(!StringUtils.isBlank(utenteDB.getEmail()) && !utenteDB.getEmail().equalsIgnoreCase(utenteModificato.getEmail())){
            // La mail istituzionale è stata cambiata
            MailUtils.sendMail(
                    utenteDB.getEmail(),
                    subject,
                    "La tua mail istituzionale è stata cambiata, adesso è la seguente: " + utenteModificato.getEmail() + "\n" +
                            indicazioneOperatore,
                    usersRepository
            );
        }else if(StringUtils.isBlank(utenteDB.getEmail()) && !StringUtils.isBlank(utenteModificato.getEmail())){
            // La mail istituzionale è stata valorizzata, mando mail alla vecchia privata
            MailUtils.sendMail(
                    utenteDB.getEmailPrivata(),
                    subject,
                    "La tua mail istituzionale è stata impostata a: " + utenteModificato.getEmail() + "\n" +
                            indicazioneOperatore,
                    usersRepository
            );
        }

        if(!StringUtils.isBlank(utenteDB.getEmailPrivata()) && StringUtils.isBlank(utenteModificato.getEmailPrivata())){
            // La mail privata è stata rimossa
            MailUtils.sendMail(
                    utenteDB.getEmailPrivata(),
                    subject,
                    "La tua mail privata è stata cambiata, adesso è la seguente: NESSUNA \n" +
                            indicazioneOperatore,
                    usersRepository
            );
        }else if(!StringUtils.isBlank(utenteDB.getEmailPrivata()) && !utenteDB.getEmailPrivata().equalsIgnoreCase(utenteModificato.getEmailPrivata())){
            // La mail privata è stata cambiata
            MailUtils.sendMail(
                    utenteDB.getEmailPrivata(),
                    subject,
                    "La tua mail privata è stata cambiata, adesso è la seguente: " + utenteModificato.getEmailPrivata() + "\n" +
                            indicazioneOperatore,
                    usersRepository
            );
        }else if(StringUtils.isBlank(utenteDB.getEmailPrivata()) && !StringUtils.isBlank(utenteModificato.getEmailPrivata())){
            // La mail privata è stata valorizzata, mando mail alla vecchia istituzionale
            MailUtils.sendMail(
                    utenteDB.getEmail(),
                    subject,
                    "La tua mail privata è stata impostata a: " + utenteModificato.getEmailPrivata() + "\n" +
                            indicazioneOperatore,
                    usersRepository
            );
        }
    }

    @Override
    public List<String> getListaRuoliApplicazioneAbilitatoUtente(String codiceUtente, String nomeApplicazione, String tipoGruppo) {
        return usersRepository.getListaRuoliApplicazioneAbilitatoUtente(codiceUtente, nomeApplicazione, tipoGruppo);
    }

    @Override
    public UsersDto getUtenteSenzaUfficio(String codiceUtente) {
        return usersMapper.toDto(usersRepository.getUtenteSenzaUfficio(codiceUtente), usersRepository);
    }
}
