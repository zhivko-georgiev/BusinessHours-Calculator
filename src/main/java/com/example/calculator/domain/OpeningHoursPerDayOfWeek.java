package com.example.calculator.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.example.calculator.domain.enumeration.DayOfWeek;

/**
 * A OpeningHoursPerDayOfWeek.
 */
@Entity
@Table(name = "opening_hours_per_day_of_week")
public class OpeningHoursPerDayOfWeek implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

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

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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
        OpeningHoursPerDayOfWeek openingHoursPerDayOfWeek = (OpeningHoursPerDayOfWeek) o;
        if(openingHoursPerDayOfWeek.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, openingHoursPerDayOfWeek.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OpeningHoursPerDayOfWeek{" +
            "id=" + id +
            ", dayOfWeek='" + dayOfWeek + "'" +
            ", openingHours='" + openingHours + "'" +
            ", closingHours='" + closingHours + "'" +
            '}';
    }
}
