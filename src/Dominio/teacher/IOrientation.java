/*
 * Created on 21/Nov/2003
 *
 */
package Dominio.teacher;

import java.util.Date;

import Dominio.IDomainObject;
import Dominio.ITeacher;
import Util.OrientationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IOrientation extends IDomainObject {
    public ITeacher getTeacher();

    public OrientationType getOrientationType();

    public Integer getNumberOfStudents();

    public String getDescription();

    public Date getLastModificationDate();

    public void setTeacher(ITeacher teacher);

    public void setOrientationType(OrientationType orientationType);

    public void setNumberOfStudents(Integer numberOfStudents);

    public void setDescription(String description);

    public void setLastModificationDate(Date lastModificationDate);
}