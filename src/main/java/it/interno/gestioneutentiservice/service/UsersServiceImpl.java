package it.interno.gestioneutentiservice.service;

import it.interno.gestioneutentiservice.dto.FiltroUsersDto;
import it.interno.gestioneutentiservice.dto.UfficioDto;
import it.interno.gestioneutentiservice.dto.UsersDto;
import it.interno.gestioneutentiservice.entity.GroupMembers;
import it.interno.gestioneutentiservice.entity.OrarioLavoro;
import it.interno.gestioneutentiservice.entity.Users;
import it.interno.gestioneutentiservice.exception.OrarioLavoroObbligatorioException;
import it.interno.gestioneutentiservice.mapper.OrarioLavoroMapper;
import it.interno.gestioneutentiservice.mapper.UfficioMapper;
import it.interno.gestioneutentiservice.mapper.UsersMapper;
import it.interno.gestioneutentiservice.repository.GroupMembersRepository;
import it.interno.gestioneutentiservice.repository.OrarioLavoroRepository;
import it.interno.gestioneutentiservice.repository.UfficioRepository;
import it.interno.gestioneutentiservice.repository.UsersRepository;
import it.interno.gestioneutentiservice.utils.GenericUtils;
import it.interno.gestioneutentiservice.utils.MailUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    @Transactional
    public void resetPassword(String codiceUtente, String utenteOperatore) {
        usersRepository.resetPassword(codiceUtente);
        usersRepository.logResetPassword(codiceUtente, utenteOperatore);
    }

    @Override
    public Page<UsersDto> getUtentiAbilitati(FiltroUsersDto filtro, String ufficioRichiedente, String ruoloUtenteRichiedente) {

        Pageable pageable = PageRequest.of(
                filtro.getPaginazione().getNumeroPagina(),
                filtro.getPaginazione().getNumeroElementi(),
                Sort.by(new Sort.Order(Sort.Direction.fromString(filtro.getPaginazione().getOrderDirection()),
                        filtro.getPaginazione().getOrderBy()).ignoreCase())
        );

        Page<Users> usersPage = usersRepository.getUtentiAbilitatiByFiltro(StringUtils.isBlank(filtro.getNomeCognome()) ? "" : filtro.getNomeCognome(),
                filtro.getForzaPolizia(), filtro.getCodiceUfficio(), filtro.getTipoComandante(), ufficioRichiedente, ruoloUtenteRichiedente, pageable);

        List<UsersDto> usersDto = usersPage
                .stream()
                .map(el -> {
                    UsersDto temp = usersMapper.toDto(el, usersRepository);

                    List<UfficioDto> listaUfficiComandati = ufficioRepository.getUfficiByComandante(temp.getCodiceUtente())
                            .stream()
                            .map(uf -> ufficioMapper.toDto(uf))
                            .collect(Collectors.toList());

                    temp.setUfficiComandati(listaUfficiComandati);

                    return temp;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(usersDto, pageable, usersPage.getTotalElements());
    }

    @Override
    public UsersDto getUserByCodiceUtente(String codiceUtente) {

        UsersDto userDto = usersMapper.toDto(usersRepository.getUtenteByCodiceUtente(codiceUtente), usersRepository);

        if(userDto == null)
            return null;

        userDto.setOrarioLavoro(orarioLavoroMapper.toDto(orarioLavoroRepository.getOrarioLavoroByUtente(codiceUtente)));

        List<UfficioDto> listaUfficiComandati = ufficioRepository.getUfficiByComandante(codiceUtente)
                .stream()
                .map(el -> ufficioMapper.toDto(el))
                .collect(Collectors.toList());

        userDto.setUfficiComandati(listaUfficiComandati);

        return userDto;
    }

    @Override
    @Transactional
    public void unlockUser(String codiceUtente) {

        usersRepository.unlockUser(codiceUtente);
        Users utente = usersRepository.getUtenteByCodiceUtente(codiceUtente);

        MailUtils.sendMail(
                utente.getEmailPrimaria() == 0 ? utente.getEmail() : utente.getEmailPrivata(),
                "Richiesta sblocco utenza " + codiceUtente,
                "La richiesta di sblocco è stata effettuata.\nRiprovare tra qualche minuto.",
                usersRepository
        );
    }

    @Override
    public Integer getNumeroUtentiAbilitati() {
        return usersRepository.getNumeroUtentiAbilitati();
    }

    @Override
    @Transactional
    public UsersDto updateUser(UsersDto input, String utenteAggiornamento, String ufficioAggiornamento, boolean sprofilazioneUtente) {

        Timestamp currentTimestamp = GenericUtils.getCurrentTimestamp();

        Users utente = usersMapper.toEntity(input);
        utente.setUtenteAggiornamento(utenteAggiornamento);
        utente.setDataAggiornamento(currentTimestamp);

        Users utenteDB = usersRepository.getUtenteByCodiceUtente(input.getCodiceUtente());
        checkCambioEmail(utenteDB, utente, utenteAggiornamento);

        // Aggiorno Utente
        usersRepository.save(utente);

        // Aggiornamento Orario lavoro
        OrarioLavoro orarioLavoro = orarioLavoroMapper.toEntity(input.getOrarioLavoro());
        if(orarioLavoro == null)
            throw new OrarioLavoroObbligatorioException("L'orario di lavoro è obbligatorio.");

        OrarioLavoro suDB = orarioLavoroRepository.getOrarioLavoroByUtente(input.getCodiceUtente());
        if(!suDB.equals(orarioLavoro)){
            // Se è stato aggiornato - chiudo il vecchio e inserisco il nuovo
            suDB.setDataCancellazione(currentTimestamp);
            suDB.setUtenteCancellazione(utenteAggiornamento);
            suDB.setUfficioCancellazione(ufficioAggiornamento);
            orarioLavoro.setDataInsermento(currentTimestamp);
            orarioLavoro.setUtenteInserimento(utenteAggiornamento);
            orarioLavoro.setUfficioInserimento(ufficioAggiornamento);
            orarioLavoroRepository.save(suDB);
            orarioLavoroRepository.save(orarioLavoro);
        }

        // Se l'utente è stato cambiato di ufficio controllo se devo sprofilare i ruoli
        if(!orarioLavoro.getCodiceUfficio().equalsIgnoreCase(suDB.getCodiceUfficio())){
            List<GroupMembers> ruoliAssociati = groupMembersRepository.getAllByUtente(utente.getCodiceUtente());

            if(sprofilazioneUtente){
                // SI SPROFILANO TUTTI I RUOLI DELL'UTENTE
                ruoliAssociati.forEach(el -> {
                    el.setUtenteCancellazione(utenteAggiornamento);
                    el.setUfficioCancellazione(ufficioAggiornamento);
                    el.setDataCancellazione(currentTimestamp);
                    groupMembersRepository.save(el);
                });
            }else{
                // SI SPROFILANO TUTTI I RUOLI DELL'UTENTE CHE NON RISPETTANO PIU' LE REGOLE
                ruoliAssociati.forEach(el -> {
                    if(groupMembersRepository.isRuoloApplicativoAssegnabile(utente.getCodiceUtente(), el.getAppId(), el.getNomeRuolo()) == 0){
                        el.setUtenteCancellazione(utenteAggiornamento);
                        el.setUfficioCancellazione(ufficioAggiornamento);
                        el.setDataCancellazione(currentTimestamp);
                        groupMembersRepository.save(el);
                    }
                });
            }
        }

        return this.getUserByCodiceUtente(utente.getCodiceUtente());
    }

    @Override
    @Transactional
    public UsersDto deleteUser(String codiceUtente, String utenteCancellazione, String ufficioCancellazione){

        Users utente = usersRepository.getUtenteByCodiceUtente(codiceUtente);
        utente.setDataCancellazione(GenericUtils.getCurrentTimestamp());
        utente.setUtenteCancellazione(utenteCancellazione);
        utente.setUfficioCancellazione(ufficioCancellazione);

        usersRepository.save(utente);

        return usersMapper.toDto(utente, usersRepository);
    }

    @Override
    public List<UsersDto> getUsersByAutocompleteCodiceCognomeEFP(String parametro, Integer codiceForzaPolizia) {
        return usersRepository.getUsersByAutocompleteCodiceCognomeEFP(parametro, codiceForzaPolizia)
                .stream()
                .map(el -> usersMapper.toDto(el, usersRepository))
                .collect(Collectors.toList());
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

}
