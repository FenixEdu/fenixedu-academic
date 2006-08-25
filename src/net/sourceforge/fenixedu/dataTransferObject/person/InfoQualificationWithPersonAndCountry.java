/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.person;

import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Qualification;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoQualificationWithPersonAndCountry extends InfoQualification {

    public void copyFromDomain(Qualification qualification) {
        super.copyFromDomain(qualification);
        if (qualification != null) {
            setInfoCountry(InfoCountry.newInfoFromDomain(qualification.getCountry()));
            setInfoPerson(InfoPerson.newInfoFromDomain(qualification.getPerson()));
        }
    }

    public static InfoQualification newInfoFromDomain(Qualification qualification) {
        InfoQualificationWithPersonAndCountry infoQualification = null;
        if (qualification != null) {
            infoQualification = new InfoQualificationWithPersonAndCountry();
            infoQualification.copyFromDomain(qualification);
        }
        return infoQualification;
    }
}
