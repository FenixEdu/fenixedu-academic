package Dominio;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public class WebSite extends DomainObject implements IWebSite {

    private String name;

    private String mail;

    private String style;

    private String ojbConcreteClass;

    public WebSite() {
        this.mail = "";
        this.style = "";
        this.ojbConcreteClass = this.getClass().getName();
    }

    public WebSite(Integer idInternal) {
        this.mail = "";
        this.style = "";
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

    /**
     * @return String
     */
    public String getOjbConcreteClass() {
        return ojbConcreteClass;
    }

    /**
     * @param string
     */
    public void setOjbConcreteClass(String string) {
        ojbConcreteClass = string;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets the mail.
     * 
     * @param mail
     *            The mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * @return String
     */
    public String getStyle() {
        return style;
    }

    /**
     * Sets the style.
     * 
     * @param style
     *            The style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }
}