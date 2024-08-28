package it.interno.gestioneutentiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.interno.gestioneutentiservice.dto.FiltroUsersDto;
import it.interno.gestioneutentiservice.dto.ResponseDto;
import it.interno.gestioneutentiservice.dto.UsersDto;
import it.interno.gestioneutentiservice.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@Tag(name = "Users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Operation(summary = "Api per eseguire il reset della password")
    @PutMapping("/reset-password/{codiceUtente}")
    public ResponseEntity<ResponseDto<Boolean>> resetPassword(@PathVariable String codiceUtente,
                                                              @RequestParam String utenteOperatore){

        usersService.resetPassword(codiceUtente.toUpperCase(), utenteOperatore);

        return ResponseEntity.ok(ResponseDto.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .body(true)
                .build()
        );
    }

    @Operation(summary = "Api per recuperare la lista di utenti abilitati")
    @PostMapping
    public ResponseEntity<ResponseDto<Page<UsersDto>>> getUtentiAbilitati(@RequestBody FiltroUsersDto filtro,
                                                                          @RequestParam String ufficioOperatore,
                                                                          @RequestParam String ruoloOperatore){
        return ResponseEntity.ok(ResponseDto.<Page<UsersDto>>builder()
                .code(HttpStatus.OK.value())
                .body(usersService.getUtentiAbilitati(filtro, ufficioOperatore, ruoloOperatore))
                .build()
        );
    }

    @Operation(summary = "Api per recuperare l'utente dato il codice")
    @GetMapping("/{codiceUtente}")
    public ResponseEntity<ResponseDto<UsersDto>> getUserByCodiceUtente(@PathVariable String codiceUtente){
        return ResponseEntity.ok(ResponseDto.<UsersDto>builder()
                .code(HttpStatus.OK.value())
                .body(usersService.getUserByCodiceUtente(codiceUtente.toUpperCase()))
                .build()
        );
    }

    @Operation(summary = "Api per sbloccare l'utenza")
    @PutMapping("/unlock-utenza/{codiceUtente}")
    public ResponseEntity<ResponseDto<Object>> unlockUser(@PathVariable String codiceUtente){

        usersService.unlockUser(codiceUtente.toUpperCase());

        return ResponseEntity.ok(ResponseDto.builder()
                .code(HttpStatus.OK.value())
                .build()
        );
    }

    @Operation(summary = "Api per recuperare il numero di utenti abilitati")
    @GetMapping("/numero-utenti-abilitati")
    public ResponseEntity<ResponseDto<Integer>> getNumeroUtentiAbilitati(@RequestParam String ufficioOperatore,
                                                                         @RequestParam String ruoloOperatore){

        return ResponseEntity.ok(ResponseDto.<Integer>builder()
                .code(HttpStatus.OK.value())
                .body(usersService.getNumeroUtentiAbilitati(ufficioOperatore, ruoloOperatore))
                .build()
        );
    }

    @Operation(summary = "Api per l'aggiornamento dell'utente")
    @PutMapping
    public ResponseEntity<ResponseDto<UsersDto>> updateUser(@RequestParam String utenteAggiornamento,
                                                            @RequestParam String ufficioAggiornamento,
                                                            @RequestParam(required = false) boolean sprofilazioneUtente,
                                                            @RequestBody UsersDto input){

        return ResponseEntity.ok(ResponseDto.<UsersDto>builder()
                .code(HttpStatus.OK.value())
                .body(usersService.updateUser(input, utenteAggiornamento, ufficioAggiornamento, sprofilazioneUtente))
                .build()
        );
    }

    @Operation(summary = "Api per la cancellazione dell'utente")
    @DeleteMapping
    public ResponseEntity<ResponseDto<UsersDto>> deleteUser(@RequestParam String codiceUtente,
                                                            @RequestParam String utenteCancellazione,
                                                            @RequestParam String ufficioCancellazione){

        return ResponseEntity.ok(ResponseDto.<UsersDto>builder()
                .code(HttpStatus.OK.value())
                .body(usersService.deleteUser(codiceUtente, utenteCancellazione, ufficioCancellazione))
                .build()
        );
    }

    @Operation(summary = "API per eseguire la ricerca degli utenti con FP in base a codice o cognome LIKE")
    @GetMapping("/autocomplete")
    public ResponseEntity<ResponseDto<List<UsersDto>>> getUsersByAutocompleteCodiceCognomeEFP(@RequestParam String parametro,
                                                                                              @RequestParam Integer codiceForzaPolizia,
                                                                                              @RequestParam String ufficioOperatore,
                                                                                              @RequestParam String ruoloOperatore){

        return ResponseEntity.ok(ResponseDto.<List<UsersDto>>builder()
                .code(HttpStatus.OK.value())
                .body(usersService.getUsersByAutocompleteCodiceCognomeEFP(parametro, codiceForzaPolizia, ufficioOperatore, ruoloOperatore))
                .build()
        );
    }

    @Operation(summary = "API per controllare se il comandante è già presente in un ufficio")
    @GetMapping("/comandante-presente")
    public ResponseEntity<ResponseDto<Boolean>> comandantePresente(@RequestParam String codiceUfficio){

        return ResponseEntity.ok(ResponseDto.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .body(usersService.checkComandantePresenteInUfficio(codiceUfficio))
                .build()
        );
    }

    @Operation(summary = "Api per il controllo della presenza di un utente su SDI",
            description = "La chiamata ritorna TRUE se è presente su SDI, FALSE altrimenti")
    @GetMapping("/utente-presente-sdi")
    public ResponseEntity<ResponseDto<Boolean>> utentePresenteSuSDI(@RequestParam String codiceUtente){

        return ResponseEntity.ok(ResponseDto.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .body(usersService.utentePresenteSuSDI(codiceUtente))
                .build()
        );
    }

    @Operation(summary = "Api per estrarre i ruoli a cui un utente è abilitato in relazione ad una specifica applicazione")
    @GetMapping(path = "/{codiceUtente}/ruoli-abilitati-applicazione/{nomeApplicazione}")
    public ResponseEntity<ResponseDto<List<String>>> getListaRuoliApplicazioneAbilitatoUtente(@PathVariable String codiceUtente,
                                                                                              @PathVariable String nomeApplicazione,
                                                                                              @RequestParam String tipoGruppo) {

        return ResponseEntity.ok(ResponseDto.<List<String>>builder()
                .code(HttpStatus.OK.value())
                .body(usersService.getListaRuoliApplicazioneAbilitatoUtente(codiceUtente, nomeApplicazione, tipoGruppo))
                .build()
        );
    }

    @Operation(summary = "API per recuperare un utente senza ufficio dato il suo codice")
    @GetMapping("/senza-ufficio")
    public ResponseEntity<ResponseDto<UsersDto>> getUtenteSenzaUfficio(@RequestParam String codiceUtente){

        return ResponseEntity.ok(ResponseDto.<UsersDto>builder()
                .code(HttpStatus.OK.value())
                .body(usersService.getUtenteSenzaUfficio(codiceUtente))
                .build()
        );
    }
}
