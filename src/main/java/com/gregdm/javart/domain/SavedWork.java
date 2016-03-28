package com.gregdm.javart.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SavedWork.
 */
@Entity
@Table(name = "saved_work")
public class SavedWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "work_id")
    private String workId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Album album;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SavedWork savedWork = (SavedWork) o;
        if(savedWork.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, savedWork.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SavedWork{" +
            "id=" + id +
            ", workId='" + workId + "'" +
            '}';
    }
}
