/*
 * ICountry.java
 *
 * Created on 28 of December 2002, 10:02
 */

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */

package net.sourceforge.fenixedu.domain;

public interface ICountry extends IDomainObject {
    public java.lang.String getName();

    public java.lang.String getNationality();

    public java.lang.String getCode();

    public void setName(java.lang.String name);

    public void setNationality(java.lang.String nationality);

    public void setCode(java.lang.String code);
}