package com.example.mylibrary;

public class Books {
    String title, author, description, owner;
    Boolean status;

    public Books() {
    }

    public Books(String title, String author, String description, String owner, Boolean status) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.owner = owner;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
