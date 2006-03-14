package net.sourceforge.fenixedu.domain;

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
}
