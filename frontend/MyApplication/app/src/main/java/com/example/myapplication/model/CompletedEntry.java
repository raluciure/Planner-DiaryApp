package com.example.myapplication.model;

/**
 * This class represents the CompletedEntry model used in front-end representing the CompletedEntryDTO
 * from back-end
 */
public class CompletedEntry extends AbstractModel {
    private Person person;

    private Activity activity;

    private String dateFrom;

    private String dateTo;

    private Double rating;

    private Long completion;

    private String finalNotes;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getCompletion() {
        return completion;
    }

    public void setCompletion(Long completion) {
        this.completion = completion;
    }

    public String getFinalNotes() {
        return finalNotes;
    }

    public void setFinalNotes(String finalNotes) {
        this.finalNotes = finalNotes;
    }
}
