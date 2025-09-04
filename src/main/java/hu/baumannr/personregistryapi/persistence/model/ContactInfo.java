package hu.baumannr.personregistryapi.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Contact info entity.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ContactInfo")
public class ContactInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = -619235190449078747L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_info_gen")
    @SequenceGenerator(name = "contact_info_gen", sequenceName = "ContactInfoSeq")
    @Column(name = "Id", nullable = false)
    private Long id;

    @Column(name = "Type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContactInfoType type;

    @Column(name = "ContactValue", nullable = false, length = 100)
    private String value;

    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "PersonId")
    private Person person;
}
