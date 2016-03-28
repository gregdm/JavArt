package com.gregdm.javart.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Album.
 */
@Entity
@Table(name = "album")
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Album album = (Album) o;
        if(album.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, album.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Album{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
