/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IDomainObject;

/**
 * @author Barbosa
 * @author Pica
 */
public interface IGrantInsurance extends IDomainObject {

    public IGrantContract getGrantContract();

    public IGrantPaymentEntity getGrantPaymentEntity();

    public Date getDateBeginInsurance();

    public Date getDateEndInsurance();

    public Double getTotalValue();

    public void setGrantContract(IGrantContract grantContract);

    public void setGrantPaymentEntity(IGrantPaymentEntity grantPaymentEntity);

    public void setDateBeginInsurance(Date dateBeginInsurance);

    public void setDateEndInsurance(Date dateEndInsurance);

    public void setTotalValue(Double totalValue);
}