/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;

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