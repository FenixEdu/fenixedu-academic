/*
 * Created on 16/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.util.ProviderRegimeType;

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