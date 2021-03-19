package com.example.myapplication.model;

/**
 * This class represents the DiaryEntry model used in front-end representing the DiaryEntryDTO from
 * back-end
 */
public class DiaryEntry extends AbstractModel {
    private String date;

    private Person person;

    private String notes;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
