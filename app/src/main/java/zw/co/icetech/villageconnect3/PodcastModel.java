package zw.co.icetech.villageconnect3;

public class PodcastModel {

    String title, url;

    PodcastModel(){}
    PodcastModel(String title, String url) {
        this.title = title;
        this.url = url;
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
