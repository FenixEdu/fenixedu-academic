/*
 * Created on 27/Out/2003
 * 
 */
package Dominio.grant.owner;

import java.util.Date;

import Dominio.IDomainObject;
import Dominio.IPessoa;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public interface IGrantOwner extends IDomainObject {

	IPessoa getPerson();
	Integer getNumber();
	Date getDateSendCGD();
	Integer getCardCopyNumber();

	void setPerson(IPessoa person);
	void setNumber(Integer number);
	void setDateSendCGD(Date dateSendCGD);
	void setCardCopyNumber(Integer cardCopyNumber);
}