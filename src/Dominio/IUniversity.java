/*
 * Created on 3/Jul/2003
 *
 */
package Dominio;

/**
 * @author dcs-rjao
 * 
 * 3/Jul/2003
 */
public interface IUniversity extends IDomainObject {
    public abstract String getCode();

    public String getName();

    public abstract void setCode(String string);

    public void setName(String string);
}