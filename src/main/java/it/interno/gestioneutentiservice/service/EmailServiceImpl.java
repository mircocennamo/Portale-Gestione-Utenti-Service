package it.interno.gestioneutentiservice.service;

import it.interno.gestioneutentiservice.repository.UsersRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public List<String> getDominioEmailLike(String dominio, Integer primaryEmail) {
        return usersRepository.getDominioEmailLike(dominio, primaryEmail);
    }

    @Override
    public boolean checkEmailsAlreadyExist(String codiceFiscale, String email) {

        return !StringUtils.isBlank(email) && !usersRepository.checkEmailAlreadyExists(email, codiceFiscale).isEmpty();
    }
}
