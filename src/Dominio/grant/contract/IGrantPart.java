/*
 * Created on Jan 21, 2004
 */
package Dominio.grant.contract;

import Dominio.IDomainObject;
import Dominio.ITeacher;

/**
 * @author pica
 * @author barbosa
 */
public interface IGrantPart extends IDomainObject {

    public IGrantPaymentEntity getGrantPaymentEntity();

    public ITeacher getResponsibleTeacher();

    public IGrantSubsidy getGrantSubsidy();

    public Integer getPercentage();

    public void setGrantPaymentEntity(IGrantPaymentEntity grantPaymentEntity);

    public void setPercentage(Integer percentage);

    public void setResponsibleTeacher(ITeacher responsibleTeacher);

    public void setGrantSubsidy(IGrantSubsidy grantSubsidy);
}