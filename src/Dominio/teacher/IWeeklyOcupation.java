/*
 * Created on 15/Nov/2003
 *
 */
package Dominio.teacher;

import java.util.Date;

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
    public Integer getLecture();
    public Integer getSupport();
    public Integer getOther();
    public ITeacher getTeacher();
    public Date getLastModificationDate();
    public void setResearch(Integer research);
    public void setManagement(Integer management);
    public void setOther(Integer other);
    public void setLecture(Integer lecture);
    public void setSupport(Integer support);
    public void setTeacher(ITeacher teacher);
    public void setLastModificationDate(Date lastModificationDate);
}
