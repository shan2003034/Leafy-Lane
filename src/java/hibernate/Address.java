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
@Table(name = "address")
public class Address implements Serializable {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
     
     @Column(name = "frist_name", length = 45, nullable = false)
     private String fristName;
     
     @Column(name = "last_name", length = 45, nullable = false)
     private String lastName;
     
     @Column(name = "line_1", length = 45, nullable = false)
    private String line_1;
     
     @Column(name = "line_2", length = 45, nullable = false)
    private String line_2;
     
     @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
     
     @Column(name = "postal_code", length = 10, nullable = false)
    private String postalCode;
     
     @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;
     
     @Column(name = "mobile", length = 10, nullable = false)
     private String mobile;

    public Address() {
    }

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
     * @return the line_1
     */
    public String getLine_1() {
        return line_1;
    }

    /**
     * @param line_1 the line_1 to set
     */
    public void setLine_1(String line_1) {
        this.line_1 = line_1;
    }

    /**
     * @return the line_2
     */
    public String getLine_2() {
        return line_2;
    }

    /**
     * @param line_2 the line_2 to set
     */
    public void setLine_2(String line_2) {
        this.line_2 = line_2;
    }

    /**
     * @return the city
     */
    public City getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the fristName
     */
    public String getFristName() {
        return fristName;
    }

    /**
     * @param fristName the fristName to set
     */
    public void setFristName(String fristName) {
        this.fristName = fristName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
