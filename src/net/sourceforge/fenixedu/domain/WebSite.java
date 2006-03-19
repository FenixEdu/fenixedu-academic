package net.sourceforge.fenixedu.domain;

/**
 * @author Fernanda Quit�rio 23/09/2003
 *  
 */
public class WebSite extends WebSite_Base {

    public WebSite() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
        this.setMail("");
        this.setStyle("");
        this.setOjbConcreteClass(this.getClass().getName());
    }

}
