/*
 * Created on Jan 21, 2004
 */
package Dominio.grant.contract;

import Dominio.IDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public interface IGrantPaymentEntity extends IDomainObject
{
    public Integer getNumber();
    public String getDesignation();
    
    public void setNumber(Integer number);
    public void setDesignation(String designation);
}