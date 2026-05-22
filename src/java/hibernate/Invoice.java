
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
@Table(name = "invoice")
public class Invoice implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "sub_total", nullable = false)
    private double subTotal;
    
    @Column(name = "shippung_cost", nullable = false)
    private double shippingCost;
    
    @Column(name = "grand_total", nullable = false)
    private double grandTotal;
    
    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Order order;
    
    public Invoice(){}

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
     * @return the subTotal
     */
    public double getSubTotal() {
        return subTotal;
    }

    /**
     * @param subTotal the subTotal to set
     */
    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    /**
     * @return the shippingCost
     */
    public double getShippingCost() {
        return shippingCost;
    }

    /**
     * @param shippingCost the shippingCost to set
     */
    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    /**
     * @return the grandTotal
     */
    public double getGrandTotal() {
        return grandTotal;
    }

    /**
     * @param grandTotal the grandTotal to set
     */
    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    /**
     * @return the order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(Order order) {
        this.order = order;
    }
    
}
