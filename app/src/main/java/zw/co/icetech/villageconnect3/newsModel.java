package zw.co.icetech.villageconnect3;

public class newsModel {

    String auther, headline, date, image, story;

    public newsModel() {
    }

    public newsModel(String auther, String headline, String date, String image, String story) {
        this.auther = auther;
        this.headline = headline;
        this.date = date;
        this.image = image;
        this.story = story;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }


}
