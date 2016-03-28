package com.gregdm.javart.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Commentary.
 */
@Entity
@Table(name = "commentary")
public class Commentary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "work_id")
    private String workId;

    @Column(name = "validated")
    private Boolean validated;

    @Column(name = "value")
    private String value;

    @ManyToOne
    private User user;

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

    public Boolean isValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        Commentary commentary = (Commentary) o;
        if(commentary.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, commentary.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Commentary{" +
            "id=" + id +
            ", workId='" + workId + "'" +
            ", validated='" + validated + "'" +
            ", value='" + value + "'" +
            '}';
    }
}
