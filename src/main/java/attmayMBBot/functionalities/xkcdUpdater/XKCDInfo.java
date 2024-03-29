package attmayMBBot.functionalities.xkcdUpdater;

public class XKCDInfo {
    private Integer number;
    private String title;
    private String imageUrl;
    private String altText;

    public XKCDInfo(Integer number, String title, String imageUrl, String altText) {
        this.number = number;
        this.title = title;
        this.imageUrl = imageUrl;
        this.altText = altText;
    }

    public Integer getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAltText() {
        return altText;
    }
}
