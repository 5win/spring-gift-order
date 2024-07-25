package gift.dto;

public class OrderRequest {

    private Long productId;
    private Long optionId;
    private int quantity;
    private String message;

    protected OrderRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
