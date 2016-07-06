package com.example.calculator.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.example.calculator.domain.enumeration.DayOfWeek;

/**
 * A StoreClosedPerDayOfWeek.
 */
@Entity
@Table(name = "store_closed_per_day_of_week")
public class StoreClosedPerDayOfWeek implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StoreClosedPerDayOfWeek storeClosedPerDayOfWeek = (StoreClosedPerDayOfWeek) o;
        if(storeClosedPerDayOfWeek.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, storeClosedPerDayOfWeek.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StoreClosedPerDayOfWeek{" +
            "id=" + id +
            ", dayOfWeek='" + dayOfWeek + "'" +
            '}';
    }
}
