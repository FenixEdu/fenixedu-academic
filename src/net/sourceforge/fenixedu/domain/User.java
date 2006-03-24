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
    
    public User(Person person){
        this();
        this.setPerson(person);
    }

    public static User readUserByUserUId(final String userUId) {
        for (final User user : RootDomainObject.getInstance().getUsers()) {
            if (user.getUserUId() != null && user.getUserUId().equalsIgnoreCase(userUId)) {
                return user;
            }
        }
        return null;
    }
    
    public Login readUserLoginIdentification(){
        
        /// In present exist only one Person Login Identification
        for (Identification identification : this.getIdentifications()) {
            if (identification instanceof Login) {
                return (Login)identification;                
            }     
        }
        return null;
    }
}
