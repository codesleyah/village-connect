package zw.co.icetech.villageconnect3;

public class SecondaryPapersModel {
    String title, grade, url;
    SecondaryPapersModel(){}
    SecondaryPapersModel(String title, String grade, String url){
        this.title = title;
        this.grade = grade;
        this.url =url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String year) {
        this.grade = year;
    }
}
