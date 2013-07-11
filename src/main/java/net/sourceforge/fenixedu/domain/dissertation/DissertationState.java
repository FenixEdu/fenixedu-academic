package net.sourceforge.fenixedu.domain.dissertation;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DissertationState extends DissertationState_Base {

	public  DissertationState(DissertationStateValue dissertationStateValue) {
		super();
		setDissertationStateValueDescription(dissertationStateValue);
	}
	
	public enum DissertationStateValue {
	    DRAFT, SUBMITTED, APPROVED, CONFIRMED, REVISION, EVALUATED, PROPOSAL_PUBLISHED, PROPOSAL_FOR_PUBLICATION;
	}

	MultiLanguageString dissertationStateValueDescription = new MultiLanguageString();
	
	public void setDissertationStateValueDescription(DissertationStateValue dissertationStateValue) {
		switch(dissertationStateValue) {
		case DRAFT:						dissertationStateValueDescription = dissertationStateValueDescription.with(Language.pt, "Rascunho");
										dissertationStateValueDescription = dissertationStateValueDescription.with(Language.en, "Draft");
										break;
		case SUBMITTED:					dissertationStateValueDescription = dissertationStateValueDescription.with(Language.pt, "Documentos submetidos pelo aluno");
										dissertationStateValueDescription = dissertationStateValueDescription.with(Language.en, "Submitted");
										break;
		case APPROVED:					dissertationStateValueDescription = dissertationStateValueDescription.with(Language.pt, "Aprovada");
										dissertationStateValueDescription = dissertationStateValueDescription.with(Language.en, "Approved");
										break;
		case CONFIRMED:					dissertationStateValueDescription = dissertationStateValueDescription.with(Language.pt, "Confirmada");
										dissertationStateValueDescription = dissertationStateValueDescription.with(Language.en, "Confirmed");
										break;
		case REVISION:					dissertationStateValueDescription = dissertationStateValueDescription.with(Language.pt, "Revista");
										dissertationStateValueDescription = dissertationStateValueDescription.with(Language.en, "Revision");
										break;
		case EVALUATED:					dissertationStateValueDescription = dissertationStateValueDescription.with(Language.pt, "Avaliada");
										dissertationStateValueDescription = dissertationStateValueDescription.with(Language.en, "Evaluated");
										break;
		case PROPOSAL_PUBLISHED:		dissertationStateValueDescription = dissertationStateValueDescription.with(Language.pt, "Propota Publicada");
										dissertationStateValueDescription = dissertationStateValueDescription.with(Language.en, "Proposal Published");
										break;
		case PROPOSAL_FOR_PUBLICATION:	dissertationStateValueDescription.with(Language.pt, "Proposta para Publicação");
										dissertationStateValueDescription = dissertationStateValueDescription.with(Language.en, "Proposal for Publication");
										break;
		default:						dissertationStateValueDescription.with(Language.pt, "Dissertação sem Estado");
										dissertationStateValueDescription = dissertationStateValueDescription.with(Language.en, "Dissertation Without State");
										break;
		}
	}
	
	public MultiLanguageString getDissertationStateValueDescription() {
		return dissertationStateValueDescription;
	}
}
