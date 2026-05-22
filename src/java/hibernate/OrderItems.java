
package hibernate;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_item")
public class OrderItems implements Serializable{
    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
     
     @Column(name = "qty", nullable = false)
    private double qty;
     
     @Column(name = "raiting", nullable = false)
    private int raiting;
     
     @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
     
     @ManyToOne
    @JoinColumn(name = "orders_id")
    private Order orders;
     
     @ManyToOne
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;
     
     @ManyToOne
    @JoinColumn(name = "delivery_type_id")
     private DeliveryType delivertType;
    
    
    public OrderItems(){}

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the qty
     */
    public double getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(double qty) {
        this.qty = qty;
    }

    /**
     * @return the raiting
     */
    public int getRaiting() {
        return raiting;
    }

    /**
     * @param raiting the raiting to set
     */
    public void setRaiting(int raiting) {
        this.raiting = raiting;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the order
     */
    public Order getOrder() {
        return orders;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(Order order) {
        this.orders = order;
    }

    /**
     * @return the orderStatus
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * @param orderStatus the orderStatus to set
     */
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * @return the delivertType
     */
    public DeliveryType getDelivertType() {
        return delivertType;
    }

    /**
     * @param delivertType the delivertType to set
     */
    public void setDelivertType(DeliveryType delivertType) {
        this.delivertType = delivertType;
    }
    
}
