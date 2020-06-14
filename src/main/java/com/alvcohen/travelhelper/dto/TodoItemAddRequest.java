package com.alvcohen.travelhelper.dto;

public class TodoItemAddRequest {

    public String name;
    public String description;

    public TodoItemAddRequest(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
