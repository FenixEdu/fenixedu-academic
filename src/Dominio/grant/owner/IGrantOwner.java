/*
 * Created on 27/Out/2003
 * 
 */
package Dominio.grant.owner;

import java.util.Date;

import Dominio.IDomainObject;
import Dominio.IPerson;

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