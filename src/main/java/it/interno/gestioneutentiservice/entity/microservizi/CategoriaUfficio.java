package it.interno.gestioneutentiservice.entity.microservizi;

import it.interno.gestioneutentiservice.entity.ForzaPolizia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "SEC_CATEGORIA_UFFICIO", schema = "SSD_SECURITY")
@NoArgsConstructor
@AllArgsConstructor
@Data
@IdClass(CategoriaUfficioPK.class)
public class CategoriaUfficio {

    @Id
    @Column(name = "COD_CATEG_UFF")
    private String codiceCategoriaUfficio;
    @Id
    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private ForzaPolizia forzaPolizia;

}
