package com.ridhoazh.obs.order;

import com.ridhoazh.obs.item.Item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 23, 2025
 */
// @formatter:on

@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @Column(name = "order_no")
    private String orderNo;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "price")
    private Integer price;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}
