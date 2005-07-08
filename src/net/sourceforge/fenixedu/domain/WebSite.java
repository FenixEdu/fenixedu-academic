package net.sourceforge.fenixedu.domain;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public class WebSite extends WebSite_Base {

    public WebSite() {
        this.setMail("");
        this.setStyle("");
        this.setOjbConcreteClass(this.getClass().getName());
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[WEBSITE";
        result += ", codInt=" + getIdInternal();
        result += ", name=" + getName();
        result += ", mail =" + getMail();
        result += ", style=" + getStyle();
        result += ", ojbConcreteClass=" + getOjbConcreteClass();
        result += "]";
        return result;
    }
    
}
