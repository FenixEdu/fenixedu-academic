/*
 * Created on Jun 26, 2004
 */
package Dominio.grant.contract;

import Dominio.IDomainObject;

/**
 * @author Barbosa
 * @author Pica
 */
public interface IGrantInsurance extends IDomainObject {

    public IGrantContract getGrantContract();

    public Integer getState();

    public void setGrantContract(IGrantContract grantContract);

    public void setState(Integer state);
}