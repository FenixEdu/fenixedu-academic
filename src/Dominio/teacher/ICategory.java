/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation -
 * Code and Comments
 */
package Dominio.teacher;

import Dominio.IDomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public interface ICategory extends IDomainObject {

    public String getLongName();

    public String getShortName();

    public String getCode();

    public void setLongName(String longName);

    public void setShortName(String name);

    public void setCode(String code);

    public Boolean getCanBeExecutionCourseResponsible();

    public void setCanBeExecutionCourseResponsible(Boolean canBeExecutionCourseResponsible);
}