/*
 * Created on 29/10/2003
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.grant.owner;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.grant.owner.IGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class InfoGrantOwner extends InfoObject {

    private InfoPerson personInfo;

    private Integer grantOwnerNumber;

    private Integer cardCopyNumber;

    private Date dateSendCGD;

    public boolean equals(Object obj) {
        return ((obj instanceof InfoGrantOwner) && ((this.personInfo.equals(((InfoGrantOwner) obj)
                .getPersonInfo())) && (this.grantOwnerNumber.equals(((InfoGrantOwner) obj)
                .getGrantOwnerNumber()))));
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
     * 
     * @param infoPerson
     *            The personInfo to set
     */
    public void setPersonInfo(InfoPerson infoPerson) {
        this.personInfo = infoPerson;
    }

    /**
     * Sets the grantOwnerNumber.
     * 
     * @param number
     *            The grantOwnerNumber to set
     */
    public void setGrantOwnerNumber(Integer number) {
        this.grantOwnerNumber = number;
    }

    /**
     * Sets the cardCopyNumber.
     * 
     * @param copyNumber
     *            The cardCopyNumber to set
     */
    public void setCardCopyNumber(Integer copyNumber) {
        this.cardCopyNumber = copyNumber;
    }

    /**
     * Sets the dateSendCGD.
     * 
     * @param dateSend
     *            The dateSendCGD to set
     */
    public void setDateSendCGD(Date dateSend) {
        this.dateSendCGD = dateSend;
    }

    /**
     * @param grantOwner
     */
    public void copyFromDomain(IGrantOwner grantOwner) {
        super.copyFromDomain(grantOwner);
        if (grantOwner != null) {
            setGrantOwnerNumber(grantOwner.getNumber());
            setCardCopyNumber(grantOwner.getCardCopyNumber());
            setDateSendCGD(grantOwner.getDateSendCGD());
        }
    }

    /**
     * @param grantOwner
     * @return
     */
    public static InfoGrantOwner newInfoFromDomain(IGrantOwner grantOwner) {
        InfoGrantOwner infoGrantOwner = null;
        if (grantOwner != null) {
            infoGrantOwner = new InfoGrantOwner();
            infoGrantOwner.copyFromDomain(grantOwner);
        }
        return infoGrantOwner;
    }

    public void copyToDomain(InfoGrantOwner infoGrantOwner, IGrantOwner grantOwner)
            throws ExcepcaoPersistencia {
        super.copyToDomain(infoGrantOwner, grantOwner);

        grantOwner.setCardCopyNumber(infoGrantOwner.getCardCopyNumber());
        grantOwner.setDateSendCGD(infoGrantOwner.getDateSendCGD());
        grantOwner.setNumber(infoGrantOwner.getGrantOwnerNumber());
    }

}
