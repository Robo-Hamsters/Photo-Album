package Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table (name="Users")
public class User {
    @Id
    @Column (name="UserId")
    private UUID userid;
    @Column (name="Username")
    private String name;
    @Column (name="Email")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column (name="Password")
    private String password;

    public User()
    {

    }

    public User(UUID userid, String email, String password) {
        this.userid = userid;
        this.email = email;
        this.password = password;
    }

    public UUID getUserid() {
        return userid;
    }

    public void setUserid(UUID userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }


}
