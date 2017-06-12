package ffmpeg;

/**
 * Created by yxcui on 2017/6/12.
 */
public class Media {
    private String title;
    private String descript;
    private String src;
    private String picture;
    private String uptime;

    public Media(){}
    //title, descript, src, picture, uptime
    public Media(String title, String descript, String src, String picture, String uptime) {
        this.title = title;
        this.descript = descript;
        this.src = src;
        this.picture = picture;
        this.uptime = uptime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }
}
