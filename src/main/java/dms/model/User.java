package dms.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Samuel
 */
@Entity
@NamedQueries({
	@NamedQuery(
	name = "checkUserCredentials",
	query = "from User u where u.name = :name and u.password = :password"
	)
})
@Table
public class User implements Serializable {

    @Id
    @GeneratedValue
    private long ID;
    @Column(unique = true)
    private String name;
    @Column
    private String password;
    @Column
    private boolean isAdmin;

    public User() {
    }
    
    public User( String name, String password, boolean isAdmin) {
      this.name = name;
      this.password = password;
      this.isAdmin = isAdmin;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}