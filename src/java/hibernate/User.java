
package hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User  implements Serializable{
    
    @Id
    @Column(name = "email",length = 60)
    private String email;
    
    @Column(name = "first_name",length = 45,nullable = false)
    private String first_name;
    
    @Column(name = "last_name",length = 45,nullable = false)
    private String last_name;
    
    @Column(name = "mobile",length = 15,nullable = false)
    private String mobile;
    
    @Column(name = "password",length = 15,nullable = false)
    private String password;
    
    @Column(name = "verification",length = 15,nullable = false)
    private String verification;
    
    @Column(name = "registerd_time",nullable = false)
   private Date registerd_time;
   
   public User(){}

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
     * @return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
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
     * @return the verification
     */
    public String getVerification() {
        return verification;
    }

    /**
     * @param verification the verification to set
     */
    public void setVerification(String verification) {
        this.verification = verification;
    }

    /**
     * @return the registerd_time
     */
    public Date getRegisterd_time() {
        return registerd_time;
    }

    /**
     * @param registerd_time the registerd_time to set
     */
    public void setRegisterd_time(Date registerd_time) {
        this.registerd_time = registerd_time;
    }
   
   
    
}
