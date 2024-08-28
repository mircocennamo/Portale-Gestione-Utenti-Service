package it.interno.gestioneutentiservice.client;

import it.interno.gestioneutentiservice.dto.oim.UtenteOimDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "oim", path = "/oim")
public interface OimClient {

    @PutMapping("/utente")
    void modificaUtente(@RequestBody UtenteOimDto input);

    @DeleteMapping("/utente")
    void deleteUtente(@RequestParam String codiceUtente);

    @PutMapping("/utente/unlock")
    void unlockUser(@RequestParam String codiceUtente);

    @PutMapping("/utente/reset-password")
    void resetPassword(@RequestParam String codiceUtente);

    @PostMapping("/ruolo-utente/disassociazione/ruoli")
    void rimozioneRuoliAUtente(@RequestBody List<String> nomiRuoli, @RequestParam String codiceUtente);
}
