public class Category {
    public Integer categoryId;
    public String name;

    public Category() {}
    public Category(Integer id, String name) { this.categoryId = id; this.name = name; }
    @Override public String toString() { return categoryId + ": " + name; }
}