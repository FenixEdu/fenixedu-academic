/*
 * Created on 17/Dez/2003
 *
 */
package Dominio;

import java.util.List;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IScientificArea extends IDomainObject
{
    public String getName();
    public List getCurricularCourseGroups();

    public void setCurricularCourseGroups(List curricularCourseGroups);
    public void setName(String name);
}
