package it.interno.gestioneutentiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.interno.gestioneutentiservice.dto.ResponseDto;
import it.interno.gestioneutentiservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/email", produces = {MediaType.APPLICATION_JSON_VALUE})
@Tag(name = "Email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Operation(summary = "API per recuperare i domini mail in autocomplete")
    @GetMapping("/{dominio}")
    public ResponseEntity<ResponseDto<List<String>>> getDominioEmailLike(@PathVariable String dominio,
                                                                         @RequestParam Integer primaryEmail){

        return ResponseEntity.ok(ResponseDto.<List<String>>builder()
                .code(HttpStatus.OK.value())
                .body(emailService.getDominioEmailLike(dominio, primaryEmail))
                .build());
    }

    @Operation(summary = "Api per controllare se una o entrambe tra email istituzionale o privata siano già utilizzate",
            description = "La chiamata ritorna FALSE se entrambe le mail non sono già utilizzate")
    @GetMapping("/email-utilizzate")
    public ResponseEntity<ResponseDto<Boolean>> checkEmailsAlreadyEsist(@RequestParam String codiceFiscale,
                                                                        @RequestParam String email){

        return ResponseEntity.ok(ResponseDto.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .body(emailService.checkEmailsAlreadyExist(codiceFiscale, email))
                .build());
    }
}
