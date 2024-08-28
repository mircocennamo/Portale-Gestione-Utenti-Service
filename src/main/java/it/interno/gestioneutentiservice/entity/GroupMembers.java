package it.interno.gestioneutentiservice.entity;

import it.interno.gestioneutentiservice.entity.pk.GroupMembersKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "GROUPMEMBERS", schema = "SSD_SECURITY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GroupMembersKey.class)
@DynamicUpdate
public class GroupMembers {

    @Id
    @Column(name = "G_MEMBER")
    private String nomeUtente;
    @Id
    @Column(name = "G_NAME")
    private String nomeRuolo;

    @Column(name = "UTE_INS")
    private String utenteInserimento;
    @Column(name = "DATE_INS")
    private Timestamp dataInserimento;
    @Column(name = "UFF_INS")
    private String ufficioInserimento;
    @Column(name = "APP_ID")
    private String appId;
    @Column(name = "UTE_CAN")
    private String utenteCancellazione;
    @Column(name = "DATA_CAN")
    private Timestamp dataCancellazione;
    @Column(name = "UFF_CAN")
    private String ufficioCancellazione;
}
