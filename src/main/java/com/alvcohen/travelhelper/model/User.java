package com.alvcohen.travelhelper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "users_email_idx", columnList = "email")}
)
@SequenceGenerator(name = "mainSequence", sequenceName = "main_seq")
public class User {
    @Id
    @GeneratedValue(generator = "mainSequence", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_visa_usage")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<VisaUsage> visaUsages;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_todo_items")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<ToDoItem> toDoItems;

    @JsonIgnore
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    public User() {
    }

    public User(final String email, final String name, final AuthProvider provider) {
        this.email = email;
        this.name = name;
        this.provider = provider;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<VisaUsage> getVisaUsages() {
        return visaUsages;
    }

    public void setVisaUsages(final Set<VisaUsage> visaUsages) {
        this.visaUsages = visaUsages;
    }

    public Set<ToDoItem> getToDoItems() {
        return toDoItems;
    }

    public void setToDoItems(final Set<ToDoItem> toDoItems) {
        this.toDoItems = toDoItems;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
