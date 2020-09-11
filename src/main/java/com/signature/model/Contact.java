package com.signature.model;

import javafx.beans.property.SimpleStringProperty;

public class Contact {
    private int id;
    SimpleStringProperty profile = new SimpleStringProperty();
    SimpleStringProperty firstName = new SimpleStringProperty();
    SimpleStringProperty lastName = new SimpleStringProperty();
    SimpleStringProperty phoneNumber = new SimpleStringProperty();
    SimpleStringProperty phoneType = new SimpleStringProperty();
    SimpleStringProperty email = new SimpleStringProperty();
    SimpleStringProperty emailType = new SimpleStringProperty();
    SimpleStringProperty address = new SimpleStringProperty();
    SimpleStringProperty company = new SimpleStringProperty();
    SimpleStringProperty website = new SimpleStringProperty();
    SimpleStringProperty date = new SimpleStringProperty();
    SimpleStringProperty dateType = new SimpleStringProperty();
    SimpleStringProperty relation = new SimpleStringProperty();
    SimpleStringProperty relationType = new SimpleStringProperty();
    SimpleStringProperty sip = new SimpleStringProperty();
    SimpleStringProperty notes = new SimpleStringProperty();
    private boolean isFavourite;

    public Contact(String profile, String firstName, String lastName, String phoneNumber, String phoneType, String email, String emailType, String address, String company, String website, String date, String dateType, String relation, String relationType, String sip, String notes) {
        this.profile.set(profile);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.phoneNumber.set(phoneNumber);
        this.phoneType.set(phoneType);
        this.email.set(email);
        this.emailType.set(emailType);
        this.address.set(address);
        this.company.set(company);
        this.website.set(website);
        this.date.set(date);
        this.dateType.set(dateType);
        this.relation.set(relation);
        this.relationType.set(relationType);
        this.sip.set(sip);
        this.notes.set(notes);
        this.isFavourite = false;
    }

    public Contact() {
        this.isFavourite = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfile() {
        return profile.get();
    }

    public SimpleStringProperty profileProperty() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile.set(profile);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getCompany() {
        return company.get();
    }

    public SimpleStringProperty companyProperty() {
        return company;
    }

    public void setCompany(String company) {
        this.company.set(company);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getPhoneType() {
        return phoneType.get();
    }

    public SimpleStringProperty phoneTypeProperty() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType.set(phoneType);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getEmailType() {
        return emailType.get();
    }

    public SimpleStringProperty emailTypeProperty() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType.set(emailType);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getWebsite() {
        return website.get();
    }

    public SimpleStringProperty websiteProperty() {
        return website;
    }

    public void setWebsite(String website) {
        this.website.set(website);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getDateType() {
        return dateType.get();
    }

    public SimpleStringProperty dateTypeProperty() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType.set(dateType);
    }

    public String getRelation() {
        return relation.get();
    }

    public SimpleStringProperty relationProperty() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation.set(relation);
    }

    public String getRelationType() {
        return relationType.get();
    }

    public SimpleStringProperty relationTypeProperty() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType.set(relationType);
    }

    public String getSip() {
        return sip.get();
    }

    public SimpleStringProperty sipProperty() {
        return sip;
    }

    public void setSip(String sip) {
        this.sip.set(sip);
    }

    public String getNotes() {
        return notes.get();
    }

    public SimpleStringProperty notesProperty() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
