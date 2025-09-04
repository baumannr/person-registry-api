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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
 * Address entity.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Address")
public class Address implements Serializable {

    @Serial
    private static final long serialVersionUID = -1173984292191236072L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_gen")
    @SequenceGenerator(name = "address_gen", sequenceName = "AddressSeq")
    @Column(name = "Id", nullable = false)
    private Long id;

    @Column(name = "Type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressType type;

    @Column(name = "Country", nullable = false)
    private String country;

    @Column(name = "City", nullable = false)
    private String city;

    @Column(name = "Zip", nullable = false)
    private String zip;

    @Column(name = "Street", nullable = false)
    private String street;

    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "PersonId")
    private Person person;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
