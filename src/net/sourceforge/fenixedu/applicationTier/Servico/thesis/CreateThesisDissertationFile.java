package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;

public class CreateThesisDissertationFile extends CreateThesisFile {

    @Override
    protected void removePreviousFile(Thesis thesis) {
        ThesisFile dissertation = thesis.getDissertation();
        if (dissertation != null) {
            dissertation.delete();
        }
    }

    @Override
    protected void updateThesis(Thesis thesis, ThesisFile file) {
        thesis.setDissertation(file);
    }

}
