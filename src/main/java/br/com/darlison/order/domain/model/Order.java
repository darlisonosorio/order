package br.com.darlison.order.domain.model;

import br.com.darlison.order.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order extends BaseModel {

    private String orderId;

    private OrderStatus status;

    private BigDecimal totalPrice;

    private Client client;

    private List<Product> products;

    public Order(UUID id) {
        super(id);
    }

    public Order() {}

    public Order(UUID id, String orderId, OrderStatus status, BigDecimal totalPrice, Client client, List<Product> products, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, createdAt, updatedAt);
        this.status = status;
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.client = client;
        this.products = products;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
