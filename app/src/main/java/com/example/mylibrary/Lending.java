package com.example.mylibrary;

public class Lending {
    private String owner;
    private String borrower;
    private String book_id;
    private Boolean notified;

    public Lending() {
    }

    public Lending(String owner, String borrower, String book_id, Boolean notified) {
        this.owner = owner;
        this.borrower = borrower;
        this.book_id = book_id;
        this.notified = notified;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getBookId() {
        return book_id;
    }

    public void setBookId(String book_id) {
        this.book_id = book_id;
    }

    public Boolean getNotified() {
        return notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }
}
