/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Category extends DomainObject implements ICategory {

    private String shortName;

    private String longName;

    private String code;

    private Boolean canBeExecutionCourseResponsible;

    public Category() {
    }

    /** Creates a new instance of Category */
    public Category(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return Returns the longName.
     */
    public String getLongName() {
        return longName;
    }

    /**
     * @param longName
     *            The longName to set.
     */
    public void setLongName(String longName) {
        this.longName = longName;
    }

    /**
     * @return Returns the shortName.
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param shortName
     *            The shortName to set.
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ICategory) {
            resultado = getCode().equals(((ICategory) obj).getCode());
        }
        return resultado;
    }

    public String toString() {
        String result = "[Dominio.teacher.Category ";
        result += ", code=" + getCode();
        result += ", shortName=" + getShortName();
        result += ", longName=" + getLongName();
        result += "]";
        return result;
    }

    /**
     * @return Returns the canBeExecutionCourseResponsible.
     */
    public Boolean getCanBeExecutionCourseResponsible() {
        return this.canBeExecutionCourseResponsible;
    }

    /**
     * @param canBeExecutionCourseResponsible
     *            The canBeExecutionCourseResponsible to set.
     */
    public void setCanBeExecutionCourseResponsible(Boolean canBeExecutionCourseResponsible) {
        this.canBeExecutionCourseResponsible = canBeExecutionCourseResponsible;
    }
}