/*
 * Created on 15/Nov/2003
 *
 */
package Dominio.teacher;

import Dominio.IDomainObject;
import Dominio.ITeacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public interface IWeeklyOcupation extends IDomainObject
{
    public Integer getResearch();
    public Integer getManagement();
    public Integer getOther();
    public ITeacher getTeacher();
    public void setResearch(Integer research);
    public void setManagement(Integer management);
    public void setOther(Integer other);
    public void setTeacher(ITeacher teacher); 
}
