/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class EditPrecedentDegreeInformation extends Service {

    public void run(PrecedentDegreeInformationBean precedentDegreeInformationBean) {

        String newInstitutionName = precedentDegreeInformationBean.getNewInstitutionName();
        Unit newInstitution = (newInstitutionName != null && newInstitutionName.length() > 0) ? 
                Unit.createNewExternalInstitution(newInstitutionName) : precedentDegreeInformationBean.getInstitution();

        PrecedentDegreeInformation precedentDegreeInformation = precedentDegreeInformationBean
                .getPrecedentDegreeInformation();

        if (newInstitution != null) {
            precedentDegreeInformation.setInstitution(newInstitution);
        }

        precedentDegreeInformation.setDegreeDesignation(precedentDegreeInformationBean
                .getDegreeDesignation());
        precedentDegreeInformation.setConclusionGrade(precedentDegreeInformationBean
                .getConclusionGrade());
        precedentDegreeInformation.setConclusionYear(precedentDegreeInformationBean.getConclusionYear());
        precedentDegreeInformation.setDegreeDesignation(precedentDegreeInformationBean
                .getDegreeDesignation());
        precedentDegreeInformation.setCountry(precedentDegreeInformationBean.getCountry());

    }

}
