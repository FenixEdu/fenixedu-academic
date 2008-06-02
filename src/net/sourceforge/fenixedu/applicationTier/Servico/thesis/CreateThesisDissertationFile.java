package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class CreateThesisDissertationFile extends CreateThesisFile {

    @Override
    protected void removePreviousFile(Thesis thesis) {
        ThesisFile dissertation = thesis.getDissertation();
        if (dissertation != null) {
            if (AccessControl.getUserView().hasRoleType(RoleType.SCIENTIFIC_COUNCIL)) {
        	dissertation.deleteWithoutStateCheck();
            } else {
        	dissertation.delete();
            }
        }
    }

    @Override
    protected void updateThesis(Thesis thesis, ThesisFile file, String title, String subTitle, Language language) {
        if (title == null || subTitle == null) {
            throw new DomainException("thesis.files.dissertation.title.required");
        }
        
        file.setTitle(title);
        file.setSubTitle(subTitle);
        file.setLanguage(language);
        
        thesis.setDissertation(file);
    }

}
