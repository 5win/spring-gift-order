package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    private final Long memberEmail;
    private final Long productId;
    private final Long optionId;
    private final int quantity;
}
