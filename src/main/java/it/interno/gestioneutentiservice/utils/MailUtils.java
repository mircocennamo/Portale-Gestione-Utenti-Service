package it.interno.gestioneutentiservice.utils;

import it.interno.gestioneutentiservice.repository.UsersRepository;

public interface MailUtils {

    static void sendMail(String to, String subject, String message, UsersRepository repository){
        try{
            repository.sendMail(
                    to,
                    subject,
                    message
            );
        }catch(Exception ex){
            // E' previsto
        }
    }
}
