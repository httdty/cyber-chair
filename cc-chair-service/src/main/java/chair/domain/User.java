package chair.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Collection;

//@Entity
public class User{

    private static final long serialVersionUID = -6140085056226164016L;

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String fullname;
    private String password;
    private String email;
    private String institution;
    private String region;

    public User(){}

    public User(String username, String fullname, String password, String email, String institution, String region) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.institution = institution;
        this.region = region;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public User(Long id, String username, String fullname, String password, String email, String institution, String region) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.institution = institution;
        this.region = region;
    }
}