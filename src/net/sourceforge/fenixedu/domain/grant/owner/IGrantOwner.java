/*
 * Created on 27/Out/2003
 * 
 */
package net.sourceforge.fenixedu.domain.grant.owner;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IPerson;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public interface IGrantOwner extends IDomainObject {

    public IPerson getPerson();

    public Integer getNumber();

    public Date getDateSendCGD();

    public Integer getCardCopyNumber();

    public void setPerson(IPerson person);

    public void setNumber(Integer number);

    public void setDateSendCGD(Date dateSendCGD);

    public void setCardCopyNumber(Integer cardCopyNumber);
}