/*
 * Created on 22/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IOldPublication extends IDomainObject {
    public ITeacher getTeacher();

    public OldPublicationType getOldPublicationType();

    public String getPublication();

    public Date getLastModificationDate();

    public void setTeacher(ITeacher teacher);

    public void setOldPublicationType(OldPublicationType oldPublicationType);

    public void setPublication(String publication);

    public void setLastModificationDate(Date lastModificationDate);
}