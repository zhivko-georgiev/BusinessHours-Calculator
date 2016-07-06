package com.example.calculator.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BusinessHours.
 */
@Entity
@Table(name = "business_hours")
public class BusinessHours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
    @Column(name = "default_opening_hours", nullable = false)
    private String defaultOpeningHours;

    @NotNull
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
    @Column(name = "default_closing_hours", nullable = false)
    private String defaultClosingHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefaultOpeningHours() {
        return defaultOpeningHours;
    }

    public void setDefaultOpeningHours(String defaultOpeningHours) {
        this.defaultOpeningHours = defaultOpeningHours;
    }

    public String getDefaultClosingHours() {
        return defaultClosingHours;
    }

    public void setDefaultClosingHours(String defaultClosingHours) {
        this.defaultClosingHours = defaultClosingHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BusinessHours businessHours = (BusinessHours) o;
        if(businessHours.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, businessHours.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BusinessHours{" +
            "id=" + id +
            ", defaultOpeningHours='" + defaultOpeningHours + "'" +
            ", defaultClosingHours='" + defaultClosingHours + "'" +
            '}';
    }
}
