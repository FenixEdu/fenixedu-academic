/**
 * IDepartment.java
 * 
 * Created on 3/Jul/2003 by jpvl
 * 
 */
package Dominio;

import java.util.Set;

/**
 * 
 * @author jpvl
 */
public interface IDepartment extends IDomainObject {
    public String getName();

    public String getCode();

    public Set getDisciplinasAssociadas();

    public void setDisciplinasAssociadas(java.util.Set disciplinasAssociadas);

    public void setCode(String code);

    public void setName(String name);
}