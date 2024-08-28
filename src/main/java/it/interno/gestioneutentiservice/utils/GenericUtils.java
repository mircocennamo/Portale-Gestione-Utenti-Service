package it.interno.gestioneutentiservice.utils;

import it.interno.gestioneutentiservice.entity.Qualifica;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public interface GenericUtils {
    static Timestamp getCurrentTimestamp(){
        ZoneId fusoOrario = ZoneId.of("Europe/Rome");
        return Timestamp.valueOf(LocalDateTime.now(fusoOrario));
    }

    static String getDescrizioneQualifica(Qualifica qualifica, Integer idEnte){
        if(idEnte == 1)
            return qualifica.getPolizia();
        if(idEnte == 2)
            return qualifica.getCarabinieri();
        if(idEnte == 3)
            return qualifica.getGuardiaDiFinanza();
        if(idEnte == 4)
            return qualifica.getPoliziaPenitenziaria();

        else return qualifica.getPersonaleCivile();
    }
}
