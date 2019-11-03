package com.example.project;

public class Record {
    private int id;
    private int correct;
    private int incorrect;
    private long date;

    public Record(int id, int correct, int incorrect, long date) {
        this.id = id;
        this.correct = correct;
        this.incorrect = incorrect;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getCorrect() {
        return correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public long getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
