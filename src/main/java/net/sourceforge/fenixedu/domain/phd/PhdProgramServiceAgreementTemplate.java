package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PhdProgramServiceAgreementTemplate extends PhdProgramServiceAgreementTemplate_Base {

    protected PhdProgramServiceAgreementTemplate() {
        super();
    }

    public PhdProgramServiceAgreementTemplate(final PhdProgram phdProgram) {
        init(phdProgram);
    }

    private void init(PhdProgram phdProgram) {
        String[] args = {};
        if (phdProgram == null) {
            throw new DomainException("error.phd.PhdProgramServiceAgreementTemplate.phdProgram.cannot.be.null", args);
        }

        super.setPhdProgram(phdProgram);
    }

    @Override
    public void setPhdProgram(PhdProgram phdProgram) {
        throw new DomainException("error.phd.PhdProgramServiceAgreementTemplate.cannot.modify.phdProgram");
    }

}
