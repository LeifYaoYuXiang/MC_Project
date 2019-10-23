package com.example.project;

public class Word {
    private String English;
    private String Chinese;
    private int id;

    public Word(int id,String English, String Chinese){
        this.id=id;
        this.English=English;
        this.Chinese=Chinese;
    }

    public String getEnglish() {
        return English;
    }

    public String getChinese() {
        return Chinese;
    }

    public void setEnglish(String english) {
        English = english;
    }

    public void setChinese(String chinese) {
        Chinese = chinese;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
