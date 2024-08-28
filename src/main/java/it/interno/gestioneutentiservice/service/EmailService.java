package it.interno.gestioneutentiservice.service;

import java.util.List;

public interface EmailService {

    List<String> getDominioEmailLike(String dominio, Integer primaryEmail);
    boolean checkEmailsAlreadyExist(String codiceFiscale, String email);
}
