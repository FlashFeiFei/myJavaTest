public class Product {
    /**
     * 商品id
     */
    private int id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品类型
     */
    private String category;
    /**
     * 商品价格
     */
    private float price;

    public int getId() {
        return id;
    }

    public Product setId(int id) {
        this.id = id;

        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Product setCategory(String category) {
        this.category = category;
        return this;
    }

    public float getPrice() {
        return price;
    }

    public Product setPrice(float price) {
        this.price = price;

        return this;
    }

}
