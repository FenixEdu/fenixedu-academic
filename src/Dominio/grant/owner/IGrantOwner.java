/*
 * Created on 27/Out/2003
 * 
 */
package Dominio.grant.owner;

import java.sql.Timestamp;

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
	Timestamp getDateSendCGD();
	Integer getCardCopyNumber();

	void setPerson(IPessoa person);
	void setNumber(Integer number);
	void setDateSendCGD(Timestamp dateSendCGD);
	void setCardCopyNumber(Integer cardCopyNumber);
}