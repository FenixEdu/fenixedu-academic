/*
 * Created on 13/Nov/2003
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
public interface ICareer extends IDomainObject {

    public Integer getBeginYear();

    public Integer getEndYear();

    public ITeacher getTeacher();

    public Date getLastModificationDate();

    public void setBeginYear(Integer beginYear);

    public void setEndYear(Integer endYear);

    public void setTeacher(ITeacher teacher);

    public void setLastModificationDate(Date lastModificationDate);
}