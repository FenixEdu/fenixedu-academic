package net.sourceforge.fenixedu.domain;


/** 
 * @author mrsp
 * @author shezad
 */

public class User extends User_Base {
    
    public User() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }    
    
    public User(String username){
        this();
        this.setUsername(username);        
    }
    
    public User(String username, String istUsername, Boolean isPassInKerberos, String password, Person person){
        this(username);        
        this.setPerson(person);
        this.setPassword(password);
        this.setIstUsername(istUsername);
        this.setIsPassInKerberos(isPassInKerberos);
    }

    public static User readUserByUsername(final String username) {
        for (final User user : RootDomainObject.getInstance().getUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public static User readUserByIstUsername(final String istUsername) {
        for (final User user : RootDomainObject.getInstance().getUsers()) {
            if (user.getIstUsername() != null && user.getIstUsername().equalsIgnoreCase(istUsername)) {
                return user;
            }
        }
        return null;
    }

}
