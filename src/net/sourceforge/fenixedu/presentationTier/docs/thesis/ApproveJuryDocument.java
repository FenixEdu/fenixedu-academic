package net.sourceforge.fenixedu.presentationTier.docs.thesis;

import java.util.Date;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class ApproveJuryDocument extends ThesisDocument {

    /**
     * Serialization id. 
     */
    private static final long serialVersionUID = 1L;
    
    public ApproveJuryDocument(Thesis thesis) {
        	super(thesis);
    }

    @Override
    protected void fillGeneric() {
        super.fillGeneric();

        Thesis thesis = getThesis();

        String date = String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", new Date());
        
        parameters.put("author", thesis.getSubmitter().getPersonName());
        parameters.put("date", date);
    }

    @Override
    public String getReportFileName() {
        Thesis thesis = getThesis();
        return "pedido-homologacao-aluno-" + thesis.getStudent().getNumber();
    }

}
