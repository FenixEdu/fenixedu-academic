package net.sourceforge.fenixedu.presentationTier.docs.thesis;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.apache.commons.lang.StringUtils;

public class ApproveJuryDocument extends ThesisDocument {

    private static final long serialVersionUID = 1L;

    public ApproveJuryDocument(Thesis thesis) {
	super(thesis);
    }

    @Override
    protected void fillGeneric() {
	super.fillGeneric();

	Thesis thesis = getThesis();

	addParameter("author", thesis.getProposalApprover() != null ? thesis.getProposalApprover().getPersonName()
		: StringUtils.EMPTY);
	addParameter("date", thesis.getApproval() != null ? String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", thesis
		.getApproval().toDate()) : StringUtils.EMPTY);
    }

    @Override
    public String getReportFileName() {
	Thesis thesis = getThesis();
	return "pedido-homologacao-aluno-" + thesis.getStudent().getNumber();
    }

}
