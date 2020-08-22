package com.alvcohen.travelhelper

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.time.LocalDate
import javax.persistence.*

enum class AuthProvider {
    LOCAL, GOOGLE
}

@Entity
@Table(name = "users", indexes = [Index(name = "users_email_idx", columnList = "email")])
@SequenceGenerator(name = "mainSequence", sequenceName = "main_seq")
class User (@Id @GeneratedValue(generator = "mainSequence", strategy = GenerationType.SEQUENCE) var id: Long? = null,
            @Column(unique = true) var email: String,
            var name: String,
            var emailVerified: Boolean,
            @OneToMany(cascade = [CascadeType.ALL]) @JoinColumn(name = "fk_visa_usage") @LazyCollection(LazyCollectionOption.FALSE) var visaUsages: MutableSet<VisaUsage>,
            @OneToMany(cascade = [CascadeType.ALL]) @JoinColumn(name = "fk_todo_items") @LazyCollection(LazyCollectionOption.FALSE) var toDoItems: MutableSet<ToDoItem>,
            @JsonIgnore var password: String?,
            @Enumerated(EnumType.STRING) var provider: AuthProvider,
            var providerId: String?) {

    constructor(email: String, name: String, provider: AuthProvider)  : this(null, email, name, false, mutableSetOf(), mutableSetOf(), null, provider, null)
}




@Entity
@Table(name = "todo_items")
@SequenceGenerator(name = "mainSequence", sequenceName = "main_seq")
class ToDoItem(@Id @GeneratedValue(generator = "mainSequence", strategy = GenerationType.SEQUENCE) var id: Long? = null,
               var name: String,
               var description: String?)


@Entity
@Table(name = "visa_usages")
@SequenceGenerator(name = "mainSequence", sequenceName = "main_seq")
class VisaUsage(
        @Id @GeneratedValue(generator = "mainSequence", strategy = GenerationType.SEQUENCE) var id: Long? = null,
        var arrival: LocalDate,
        var departure: LocalDate,
        var days: Long)

