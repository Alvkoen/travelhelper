package com.alvcohen.travelhelper.dto;

import javax.validation.constraints.Pattern;

public class VisaUsageAddRequest {
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$")
    public String arrival;
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$")
    public String departure;

    public VisaUsageAddRequest(final String arrival, final String departure) {
        this.arrival = arrival;
        this.departure = departure;
    }
}
