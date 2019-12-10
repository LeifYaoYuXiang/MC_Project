package com.example.project;

/**
 * @author Leif(Yuxiang Yao)
 */
public class Word {
    private String English;
    private String Chinese;
    private int id;
    private String ChineseDetail;
    private String pronunciation;

    public Word(int id,String English, String Chinese){
        this.id=id;
        this.English=English;
        this.Chinese=Chinese;
    }

    public Word(String English, String pronunciation,String ChineseBasic, String ChineseDetail){
        this.English=English;
        this.Chinese=ChineseBasic;
        this.ChineseDetail=ChineseDetail;
        this.pronunciation=pronunciation;
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


    public String getChineseDetail() {
        return ChineseDetail;
    }

    public void setChineseDetail(String chineseDetail) {
        ChineseDetail = chineseDetail;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }
}
