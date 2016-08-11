package com.cooksys.serialization.assignment.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Instructor {
    private Contact contact;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
