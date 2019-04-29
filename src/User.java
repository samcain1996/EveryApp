
public class User {
	private UserType type;
	private String name;
	
	public User(UserType type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public UserType getUserType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
}

