/*
 * Created on 16/Nov/2003
 *
 */
package Dominio.teacher;

import java.util.Date;

import Dominio.IDomainObject;
import Dominio.ITeacher;
import Util.ProviderRegimeType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IServiceProviderRegime extends IDomainObject {
    public ProviderRegimeType getProviderRegimeType();

    public ITeacher getTeacher();

    public Date getLastModificationDate();

    public void setProviderRegimeType(ProviderRegimeType providerRegimeType);

    public void setTeacher(ITeacher teacher);

    public void setLastModificationDate(Date lastModificationDate);
}