package DataBeans.precedences;

import DataBeans.InfoObject;
import Dominio.precedences.IRestriction;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionWithPrecedence extends InfoObject {

    protected InfoPrecedence infoPrecedence;

    public InfoRestrictionWithPrecedence() {
    }

    public InfoPrecedence getInfoPrecedence() {
        return infoPrecedence;
    }

    public void setInfoPrecedence(InfoPrecedence infoPrecedence) {
        this.infoPrecedence = infoPrecedence;
    }

    public void copyFromDomain(IRestriction restriction) {
        super.copyFromDomain(restriction);
        this.setInfoPrecedence(InfoPrecedence.newInfoFromDomain(restriction.getPrecedence()));
    }

    public static InfoRestrictionWithPrecedence newInfoFromDomain(IRestriction restriction) {

        InfoRestrictionWithPrecedence infoRestrictionWithPrecedence = null;

        if (restriction != null) {
            infoRestrictionWithPrecedence = new InfoRestrictionWithPrecedence();
            infoRestrictionWithPrecedence.copyFromDomain(restriction);
        }

        return infoRestrictionWithPrecedence;
    }

}