/*
 * Created on 22/Nov/2003
 *
 */
package Dominio.teacher;

import java.util.Date;

import Dominio.IDomainObject;
import Dominio.ITeacher;
import Util.OldPublicationType;

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