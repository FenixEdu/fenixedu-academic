/*
 * Created on 20/Nov/2003
 * 
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public interface IGrantOrientationTeacher extends IDomainObject {

    public Date getBeginDate();

    public Date getEndDate();

    public ITeacher getOrientationTeacher();

    public IGrantContract getGrantContract();

    public void setBeginDate(Date beginDate);

    public void setEndDate(Date endDate);

    public void setGrantContract(IGrantContract grantContract);

    public void setOrientationTeacher(ITeacher OrientationTeacher);
}