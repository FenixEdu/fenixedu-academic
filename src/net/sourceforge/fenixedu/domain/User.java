package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/** 
 * @author mrsp
 * @author shezad
 */

public class User extends User_Base {
    
    public User() {
        super();
    }    
    
    public User(String username){
        super();
        this.setUsername(username);        
    }
    
    public User(String username, String istUsername, Boolean isPassInKerberos, String password, Person person){
        super();
        this.setUsername(username);        
        this.setPerson(person);
        this.setPassword(password);
        this.setIstUsername(istUsername);
        this.setIsPassInKerberos(isPassInKerberos);
    }

    public static User readUserByUsername(final String username) throws ExcepcaoPersistencia {
        for (final User user : RootDomainObject.getInstance().getUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public static User readUserByIstUsername(final String istUsername) throws ExcepcaoPersistencia {
        for (final User user : RootDomainObject.getInstance().getUsers()) {
            if (user.getIstUsername() != null && user.getIstUsername().equalsIgnoreCase(istUsername)) {
                return user;
            }
        }
        return null;
    }

}
