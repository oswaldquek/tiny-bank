package com.tinybank.main.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class UserEntity {

    private String id;
    private Status status;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;

    public UserEntity(String id, Status status, String firstName, String lastName, Date dateOfBirth, String address) {
        this.id = id;
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public static UserEntity newUser(CreateUserRequest createUserRequest, String id, Status status) {
        return new UserEntity(id, status, createUserRequest.firstName(), createUserRequest.lastName(),
                createUserRequest.dateOfBirth(), createUserRequest.address());
    }

    public String getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        return new EqualsBuilder().append(id, that.id).append(status, that.status).append(firstName, that.firstName).append(lastName, that.lastName).append(dateOfBirth, that.dateOfBirth).append(address, that.address).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(status).append(firstName).append(lastName).append(dateOfBirth).append(address).toHashCode();
    }
}
