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
public interface IGrantPaymentEntity extends IDomainObject {

    public String getNumber();

    public String getDesignation();

    public String getOjbConcreteClass();

    public ITeacher getResponsibleTeacher();

    public void setNumber(String number);

    public void setDesignation(String designation);

    public void setOjbConcreteClass(String ojbConcreteClass);

    public void setResponsibleTeacher(ITeacher responsibleTeacher);
}