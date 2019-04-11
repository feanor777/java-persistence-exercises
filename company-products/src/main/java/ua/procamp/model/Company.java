package ua.procamp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * todo: - implement no arguments constructor - implement getters and setters for all fields -
 * implement equals() and hashCode() based on identifier field - make setter for field {@link
 * Company#products} private - initialize field {@link Company#products} as new {@link ArrayList} -
 * implement a helper {@link Company#addProduct(Product)} that establishes a relation on both sides
 * - implement a helper {@link Company#removeProduct(Product)} that drops a relation on both sides
 * <p>
 * - configure JPA entity - specify table name: "company" - configure auto generated identifier -
 * configure mandatory column "name" for field {@link Company#name}
 * <p>
 * - configure one to many relationship as mapped on the child side
 */
@Getter
@Setter
@Entity
@Table(name = "company")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Setter(value = AccessLevel.PRIVATE)
  @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
  private List<Product> products = new ArrayList<>();

  public void addProduct(Product product) {
    getProducts().add(product);
    product.setCompany(this);
  }

  public void removeProduct(Product product) {
    getProducts().remove(product);
    product.setCompany(null);
  }
}
