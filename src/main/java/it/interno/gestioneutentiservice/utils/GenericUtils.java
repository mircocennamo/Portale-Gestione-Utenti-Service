package it.interno.gestioneutentiservice.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public interface GenericUtils {
    static Timestamp getCurrentTimestamp(){
        ZoneId fusoOrario = ZoneId.of("Europe/Rome");
        return Timestamp.valueOf(LocalDateTime.now(fusoOrario));
    }
}
