/*
 * Created on 29/10/2003
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.grant.owner;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
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
    
    public InfoPerson getPersonInfo() {
        return personInfo;
    }

    public Integer getGrantOwnerNumber() {
        return grantOwnerNumber;
    }

    public Integer getCardCopyNumber() {
        return cardCopyNumber;
    }

    public Date getDateSendCGD() {
        return dateSendCGD;
    }
    
    public void setPersonInfo(InfoPerson infoPerson) {
        this.personInfo = infoPerson;
    }

    public void setGrantOwnerNumber(Integer number) {
        this.grantOwnerNumber = number;
    }

    public void setCardCopyNumber(Integer copyNumber) {
        this.cardCopyNumber = copyNumber;
    }

    public void setDateSendCGD(Date dateSend) {
        this.dateSendCGD = dateSend;
    }

    public void copyFromDomain(GrantOwner grantOwner) {
        super.copyFromDomain(grantOwner);
        if (grantOwner != null) {
            setGrantOwnerNumber(grantOwner.getNumber());
            setCardCopyNumber(grantOwner.getCardCopyNumber());
            setDateSendCGD(grantOwner.getDateSendCGD());
        }
    }

    public static InfoGrantOwner newInfoFromDomain(GrantOwner grantOwner) {
        InfoGrantOwner infoGrantOwner = null;
        if (grantOwner != null) {
            infoGrantOwner = new InfoGrantOwner();
            infoGrantOwner.copyFromDomain(grantOwner);
        }
        return infoGrantOwner;
    }

    public void copyToDomain(InfoGrantOwner infoGrantOwner, GrantOwner grantOwner)
            throws ExcepcaoPersistencia {
        super.copyToDomain(infoGrantOwner, grantOwner);

        grantOwner.setCardCopyNumber(infoGrantOwner.getCardCopyNumber());
        grantOwner.setDateSendCGD(infoGrantOwner.getDateSendCGD());
        grantOwner.setNumber(infoGrantOwner.getGrantOwnerNumber());
    }
    
    /*
     * - Temporary solution to remove create InfoPerson wrapper
     * - This attribute should be removed and placed in InfoGrantOwnerEditor 
     */
    private InfoPersonEditor infoPersonEditor;

    public InfoPersonEditor getInfoPersonEditor() {
        return infoPersonEditor;
    }

    public void setInfoPersonEditor(InfoPersonEditor infoPersonEditor) {
        this.infoPersonEditor = infoPersonEditor;
    }

}
