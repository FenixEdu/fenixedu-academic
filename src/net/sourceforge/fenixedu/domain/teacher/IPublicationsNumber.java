/*
 * Created on 21/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.util.PublicationType;

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