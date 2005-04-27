package net.sourceforge.fenixedu.domain;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public class WebSite extends WebSite_Base {

    public WebSite() {
        setMail("");
        setStyle("");
        setOjbConcreteClass(this.getClass().getName());
    }

    public WebSite(Integer idInternal) {
        setMail("");
        setStyle("");
        setIdInternal(idInternal);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;

        if (arg0 instanceof IWebSite) {
            IWebSite webSite = (IWebSite) arg0;

            if (((webSite.getName() == null && this.getName() == null) || (webSite.getName() != null
                    && this.getName() != null && webSite.getName().equals(this.getName())))
                    && ((webSite.getMail() == null && this.getMail() == null) || (webSite.getMail() != null
                            && this.getMail() != null && webSite.getMail().equals(this.getMail())))
                    && ((webSite.getStyle() == null && this.getStyle() == null) || (webSite.getStyle() != null
                            && this.getStyle() != null && webSite.getStyle().equals(this.getStyle())))) {
                result = true;
            }
        }
        return result;
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