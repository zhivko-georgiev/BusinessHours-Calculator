package com.example.calculator.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A StoreClosedPerSpecificDate.
 */
@Entity
@Table(name = "store_closed_per_specific_date")
public class StoreClosedPerSpecificDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StoreClosedPerSpecificDate storeClosedPerSpecificDate = (StoreClosedPerSpecificDate) o;
        if(storeClosedPerSpecificDate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, storeClosedPerSpecificDate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StoreClosedPerSpecificDate{" +
            "id=" + id +
            ", date='" + date + "'" +
            '}';
    }
}
