package ua.procamp.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * todo:
 * - implement no argument constructor
 * - implement getters and setters
 * - make a setter for field {@link Photo#comments} {@code private}
 * - implement equals() and hashCode() based on identifier field
 *
 * - configure JPA entity
 * - specify table name: "photo"
 * - configure auto generated identifier
 * - configure not nullable and unique column: url
 *
 * - initialize field comments
 * - map relation between Photo and PhotoComment on the child side
 * - implement helper methods {@link Photo#addComment(PhotoComment)} and {@link Photo#removeComment(PhotoComment)}
 * - enable cascade type {@link javax.persistence.CascadeType#ALL} for field {@link Photo#comments}
 * - enable orphan removal
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "photo")
@EqualsAndHashCode(of = "id")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "url", unique = true, nullable = false)
    private String url;

    @Column(name = "description")
    private String description;

    @Setter(value = AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "photo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoComment> comments = new ArrayList<>();

    public void addComment(PhotoComment comment) {
      getComments().add(comment);
      comment.setPhoto(this);
    }

    public void removeComment(PhotoComment comment) {
      getComments().remove(comment);
      comment.setPhoto(null);
    }

}
