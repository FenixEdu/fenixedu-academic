/*
 * Created on 21/Nov/2003
 *
 */
package Dominio.teacher;

import java.util.Date;

import Dominio.IDomainObject;
import Dominio.ITeacher;
import Util.PublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPublicationsNumber extends IDomainObject {
    public PublicationType getPublicationType();

    public Date getLastModificationDate();

    public Integer getNational();

    public Integer getInternational();

    public ITeacher getTeacher();

    public void setPublicationType(PublicationType publicationType);

    public void setLastModificationDate(Date lastModificationDate);

    public void setNational(Integer national);

    public void setInternational(Integer international);

    public void setTeacher(ITeacher teacher);
}