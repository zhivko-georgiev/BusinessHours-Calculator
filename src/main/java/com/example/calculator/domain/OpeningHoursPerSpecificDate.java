package com.example.calculator.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A OpeningHoursPerSpecificDate.
 */
@Entity
@Table(name = "opening_hours_per_specific_date")
public class OpeningHoursPerSpecificDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
    @Column(name = "opening_hours", nullable = false)
    private String openingHours;

    @NotNull
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
    @Column(name = "closing_hours", nullable = false)
    private String closingHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getClosingHours() {
        return closingHours;
    }

    public void setClosingHours(String closingHours) {
        this.closingHours = closingHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OpeningHoursPerSpecificDate openingHoursPerSpecificDate = (OpeningHoursPerSpecificDate) o;
        if(openingHoursPerSpecificDate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, openingHoursPerSpecificDate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OpeningHoursPerSpecificDate{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", openingHours='" + openingHours + "'" +
            ", closingHours='" + closingHours + "'" +
            '}';
    }
}
