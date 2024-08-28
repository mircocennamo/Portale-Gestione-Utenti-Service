package it.interno.gestioneutentiservice.service;

import it.interno.gestioneutentiservice.dto.FiltroUsersDto;
import it.interno.gestioneutentiservice.dto.UsersDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UsersService {

    void resetPassword(String codiceUtente, String utenteOperatore);
    Page<UsersDto> getUtentiAbilitati(FiltroUsersDto filtro, String ufficioOperatore, String ruoloOperatore);
    UsersDto getUserByCodiceUtente(String codiceUtente);
    void unlockUser(String codiceUtente);
    Integer getNumeroUtentiAbilitati(String ufficioOperatore, String ruoloOperatore);
    UsersDto updateUser(UsersDto input, String utenteAggiornamento, String ufficioAggiornamento, boolean sprofilazioneUtente);
    UsersDto deleteUser(String codiceUtente, String utenteCancellazione, String ufficioCancellazione);
    List<UsersDto> getUsersByAutocompleteCodiceCognomeEFP(String parametro, Integer codiceForzaPolizia, String ufficioOperatore, String ruoloOperatore);
    boolean checkComandantePresenteInUfficio(String codiceUfficio);
    boolean utentePresenteSuSDI(String codiceUtente);
    List<String> getListaRuoliApplicazioneAbilitatoUtente(String codiceUtente, String nomeApplicazione, String tipoGruppo);
    UsersDto getUtenteSenzaUfficio(String codiceUtente);

}
