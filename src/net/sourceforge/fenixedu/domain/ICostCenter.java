/*
 * Created on 12/Jul/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author jpvl
 */
public interface ICostCenter extends IDomainObject {
    /**
     * @return String
     */
    public abstract String getDepartament();

    /**
     * @return String
     */
    public abstract String getSection1();

    /**
     * @return String
     */
    public abstract String getSection2();

    /**
     * @return String
     */
    public abstract String getCode();

    /**
     * Sets the departamento.
     * 
     * @param departamento
     *            The departamento to set
     */
    public abstract void setDepartament(String departament);

    /**
     * Sets the section1.
     * 
     * @param section1
     *            The section1 to set
     */
    public abstract void setSection1(String section1);

    /**
     * Sets the section2.
     * 
     * @param section2
     *            The section2 to set
     */
    public abstract void setSection2(String section2);

    /**
     * Sets the sigla.
     * 
     * @param sigla
     *            The sigla to set
     */
    public abstract void setCode(String code);
}