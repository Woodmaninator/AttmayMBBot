package attmayMBBot.util;

public class Recipe {
    private String title;
    private String imageUrl;
    private String summaryText;
    private String recipeUrl;

    public Recipe(String title, String imageUrl, String summaryText, String recipeUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.summaryText = summaryText;
        this.recipeUrl = recipeUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }
}
