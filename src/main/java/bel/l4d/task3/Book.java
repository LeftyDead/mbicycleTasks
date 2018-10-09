package bel.l4d.task3;

public class Book {
    private String title = "";
    private int year = 0;

    public Book(String title, int year) {
        this.title = title;
        this.year = year;
    }

    public String getTitle() {
        return this.title;
    }

    public int getYear() {
        return this.year;
    }
}