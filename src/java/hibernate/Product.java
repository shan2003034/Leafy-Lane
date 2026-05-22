
package hibernate;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @ManyToOne
    @JoinColumn(name = "variety_id")
    private Variety variety;
    
    @Column(name = "qty", nullable = false)
    private double qty;
    
    @Column(name = "description", nullable = false)
    private String description;
    
    @Column(name = "registerd_time", nullable = false)
    private Date registerdTime;
    
    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "availability_status_id")
    private AvailabilityStatus availabilityStatus;
    
    @ManyToOne
    @JoinColumn(name = "unit_type_id")
    private UnitType unitType;
    
    @Column(name = "price", nullable = false)
    private double price;
    
    public Product(){}

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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the variety
     */
    public Variety getVariety() {
        return variety;
    }

    /**
     * @param variety the variety to set
     */
    public void setVariety(Variety variety) {
        this.variety = variety;
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the registerdTime
     */
    public Date getRegisterdTime() {
        return registerdTime;
    }

    /**
     * @param registerdTime the registerdTime to set
     */
    public void setRegisterdTime(Date registerdTime) {
        this.registerdTime = registerdTime;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the availabilityStatus
     */
    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    /**
     * @param availabilityStatus the availabilityStatus to set
     */
    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    /**
     * @return the unitType
     */
    public UnitType getUnitType() {
        return unitType;
    }

    /**
     * @param unitType the unitType to set
     */
    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
}
