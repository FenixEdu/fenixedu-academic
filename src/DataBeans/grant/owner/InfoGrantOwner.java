/*
 * Created on 29/10/2003
 * 
 */
package DataBeans.grant.owner;

import java.util.Date;

import DataBeans.InfoObject;
import DataBeans.InfoPerson;

/**
 * @author  Barbosa
 * @author  Pica
 * 
 */

public class InfoGrantOwner extends InfoObject {

	private InfoPerson personInfo;
	private Integer grantOwnerNumber;
	private Integer cardCopyNumber;
	private Date dateSendCGD;


	public InfoGrantOwner() {}
	
	
	public boolean equals(Object obj) {
	   return
	   ((obj instanceof InfoGrantOwner) &&
	
	   ((this.personInfo.equals(((InfoGrantOwner)obj).getPersonInfo())) &&
		(this.grantOwnerNumber.equals(((InfoGrantOwner)obj).getGrantOwnerNumber()))));         
   }
	
	/**
	 * @return InfoPerson
	 */
	public InfoPerson getPersonInfo() {
		return personInfo;
	}
	
	/**
	 * @return Integer
	 */
	public Integer getGrantOwnerNumber() {
		return grantOwnerNumber;
	}
	
	/**
	 * @return Integer
	 */
	public Integer getCardCopyNumber() {
		return cardCopyNumber;
	}
	
	/**
	 * @return Date
	 */
	public Date getDateSendCGD() {
		return dateSendCGD;
	}

	/**
	 * Sets the personInfo.
	 * @param infoPerson The personInfo to set
	 */
	public void setPersonInfo(InfoPerson infoPerson) {
		this.personInfo = infoPerson;
	}

	/**
	 * Sets the grantOwnerNumber.
	 * @param number The grantOwnerNumber to set
	 */
	public void setGrantOwnerNumber(Integer number) {
		this.grantOwnerNumber = number;
	}

	/**
	 * Sets the cardCopyNumber.
	 * @param copyNumber The cardCopyNumber to set
	 */
	public void setCardCopyNumber(Integer copyNumber) {
		this.cardCopyNumber = copyNumber;
	}
	
	/**
	 * Sets the dateSendCGD.
	 * @param dateSend The dateSendCGD to set
	 */
	public void setDateSendCGD(Date dateSend) {
		this.dateSendCGD = dateSend;
	}

}