
package hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin implements Serializable{
    
    @Id
    @Column(name = "email",length = 60)
    private String email;
    
    @Column(name = "first_name",length = 45,nullable = false)
    private String firstName;
    
    @Column(name = "last_name",length = 45,nullable = false)
    private String lastName;
    
    @Column(name = "password",length = 15,nullable = false)
    private String password;
    
    @Column(name = "registerd_time",nullable = false)
    private Date registerdTime;
    
    public Admin(){}

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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
    
}
