/*
 * Created on 21/Mar/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IGuide;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoGuideWithPerson extends InfoGuide {

    public void copyFromDomain(IGuide guide) {
        super.copyFromDomain(guide);
        if (guide != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(guide.getPerson()));
        }
    }

    public static InfoGuideWithPerson newInfoFromDomain(IGuide guide) {
        InfoGuideWithPerson infoGuide = null;
        if (guide != null) {
            infoGuide = new InfoGuideWithPerson();
            infoGuide.copyFromDomain(guide);
        }
        return infoGuide;
    }

}