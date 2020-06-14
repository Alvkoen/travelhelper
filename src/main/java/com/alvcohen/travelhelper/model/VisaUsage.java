package com.alvcohen.travelhelper.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "visa_usages")
@SequenceGenerator(name = "mainSequence", sequenceName = "main_seq")
public class VisaUsage {

    @Id
    @GeneratedValue(generator = "mainSequence", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private LocalDate arrival;

    @Column(nullable = false)
    private LocalDate departure;

    @Column(nullable = false)
    private long days;

    public VisaUsage() {
    }

    public VisaUsage(final LocalDate arrival, final LocalDate departure, final long days) {
        this.arrival = arrival;
        this.departure = departure;
        this.days = days;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDate getArrival() {
        return arrival;
    }

    public void setArrival(final LocalDate arrival) {
        this.arrival = arrival;
    }

    public LocalDate getDeparture() {
        return departure;
    }

    public void setDeparture(final LocalDate departure) {
        this.departure = departure;
    }

    public long getDays() {
        return days;
    }

    public void setDays(final long days) {
        this.days = days;
    }

    public void setDays(final int days) {
        this.days = days;
    }
}
