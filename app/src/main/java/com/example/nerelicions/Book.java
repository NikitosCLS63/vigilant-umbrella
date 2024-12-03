package com.example.nerelicions;
public class Book {
    private String id;
    private String title;
    private String author;
    private String description;

    // Constructor
    public Book(String id, String title) {
        this.id = id;
        this.title = title;
        this.author = author; // Initialize author to an empty string or provide a parameter
        this.description = description; // Initialize description to an empty string or provide a parameter
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Setter for title
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for author
    public String getAuthor() {
        return author;
    }

    // Setter for author
    public void setAuthor(String author) {
        this.author = author;
    }

    // Getter for description
    public String getDescription() {
        return description;
    }

    // Setter for description
    public void setDescription(String description) {
        this.description = description;
    }
}