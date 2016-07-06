package com.example.calculator.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BusinessHoursCalculator.
 */
@Entity
@Table(name = "business_hours_calculator")
public class BusinessHoursCalculator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "time_interval", nullable = false)
    private Long timeInterval;

    @NotNull
    @Column(name = "starting_date_time", nullable = false)
    private String startingDateTime;

    @Column(name = "expected_pickup_time")
    private ZonedDateTime expectedPickupTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getStartingDateTime() {
        return startingDateTime;
    }

    public void setStartingDateTime(String startingDateTime) {
        this.startingDateTime = startingDateTime;
    }

    public ZonedDateTime getExpectedPickupTime() {
        return expectedPickupTime;
    }

    public void setExpectedPickupTime(ZonedDateTime expectedPickupTime) {
        this.expectedPickupTime = expectedPickupTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BusinessHoursCalculator businessHoursCalculator = (BusinessHoursCalculator) o;
        if(businessHoursCalculator.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, businessHoursCalculator.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BusinessHoursCalculator{" +
            "id=" + id +
            ", timeInterval='" + timeInterval + "'" +
            ", startingDateTime='" + startingDateTime + "'" +
            ", expectedPickupTime='" + expectedPickupTime + "'" +
            '}';
    }
}
