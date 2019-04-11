package ua.procamp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * todo:
 * - implement no arguments constructor
 * - implement getters and setters for all fields
 * - implement equals() and hashCode() based on identifier field
 * <p>
 * - configure JPA entity
 * - specify table name: "product"
 * - configure auto generated identifier
 * - configure mandatory column "name" for field {@link Product#name}
 * <p>
 * - configure lazy many-to-one relation between {@link Product} and {@link Company}
 * - configure foreign key column "company_id" references company table
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "product")
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
}
