package model.entites;

public enum Category {
    FOOD("Food"),
    DRINK("Drink"),
    FRUIT("Fruit");

    private final String category;
    Category(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }
}
