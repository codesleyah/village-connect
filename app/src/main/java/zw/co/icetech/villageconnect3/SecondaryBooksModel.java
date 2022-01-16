package zw.co.icetech.villageconnect3;

public class SecondaryBooksModel {
    private String grade, title, url;
    SecondaryBooksModel(){}
    SecondaryBooksModel(String grade, String title, String url){
        this.title = title;
        this.grade = grade;
        this.url = url;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

