package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;

public class CreateThesisAbstractFile extends CreateThesisFile {

    @Override
    protected void removePreviousFile(Thesis thesis) {
        ThesisFile extendedAbstract = thesis.getExtendedAbstract();
        if (extendedAbstract != null) {
            extendedAbstract.delete();
        }
    }

    @Override
    protected void updateThesis(Thesis thesis, ThesisFile file) {
        thesis.setExtendedAbstract(file);
    }

}
