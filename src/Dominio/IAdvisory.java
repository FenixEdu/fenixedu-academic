/*
 * Created on Aug 26, 2003
 *
 */
package Dominio;

import java.util.Date;

/**
 * @author tfc130
 *  
 */
public interface IAdvisory extends IDomainObject {
    /**
     * @return
     */
    public Date getCreated();

    /**
     * @return
     */
    public Date getExpires();

    /**
     * @return
     */
    public String getSender();

    /**
     * @return
     */
    public String getMessage();

    /**
     * @return
     */
    public Boolean getOnlyShowOnce();

    /**
     * @return
     */
    public String getSubject();

    /**
     * @param date
     */
    public void setCreated(Date created);

    /**
     * @param date
     */
    public void setExpires(Date expires);

    /**
     * @param string
     */
    public void setSender(String sender);

    /**
     * @param string
     */
    public void setMessage(String message);

    /**
     * @param boolean1
     */
    public void setOnlyShowOnce(Boolean onlyShowOnce);

    /**
     * @param string
     */
    public void setSubject(String subject);
}