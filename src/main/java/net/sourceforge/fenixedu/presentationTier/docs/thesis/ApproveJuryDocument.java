package net.sourceforge.fenixedu.presentationTier.docs.thesis;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;

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

        final ThesisEvaluationParticipant thesisEvaluationParticipant = thesis.getProposalApprover();
        final String author;
        final String date;
        final String ccAuthor;
        final String ccDate;
        if (thesisEvaluationParticipant == null) {
            author = date = ccAuthor = ccDate = StringUtils.EMPTY;
        } else {
            final Person person = thesisEvaluationParticipant.getPerson();
            if (person != null && person.hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
                author = date = StringUtils.EMPTY;
                ccAuthor = thesisEvaluationParticipant.getPersonName();
                ccDate = String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", thesis.getApproval().toDate());
            } else {
                ccAuthor = ccDate = StringUtils.EMPTY;
                author = thesisEvaluationParticipant.getPersonName();
                date = String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", thesis.getApproval().toDate());
            }
        }

        addParameter("author", author);
        addParameter("date", date);
        addParameter("ccAuthor", ccAuthor);
        addParameter("ccDate", ccDate);
    }

    @Override
    public String getReportFileName() {
        Thesis thesis = getThesis();
        return "pedido-homologacao-aluno-" + thesis.getStudent().getNumber();
    }

}
