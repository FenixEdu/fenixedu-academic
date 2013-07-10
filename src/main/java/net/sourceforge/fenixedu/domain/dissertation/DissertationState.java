package net.sourceforge.fenixedu.domain.dissertation;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DissertationState extends DissertationState_Base {

	public  DissertationState() {
        super();
	}
	
	public enum DissertationStateValue {
	    DRAFT, SUBMITTED, APPROVED, CONFIRMED, REVISION, EVALUATED, PROPOSAL_PUBLISHED, PROPOSAL_FOR_PUBLICATION;
	}
	
	DissertationStateValue dissertationStateValue;
	
	public MultiLanguageString getValue() {
		MultiLanguageString result = new MultiLanguageString();
		switch(dissertationStateValue) {
		case DRAFT:						result.with(Language.pt, "Rascunho");
										result.with(Language.en, "Draft");
										break;
		case SUBMITTED:					result.with(Language.pt, "Documentos submetidos pelo aluno");
										result.with(Language.en, "Submitted");
										break;
		case APPROVED:					result.with(Language.pt, "Aprovada");
										result.with(Language.en, "Approved");
										break;
		case CONFIRMED:					result.with(Language.pt, "Confirmada");
										result.with(Language.en, "Confirmed");
										break;
		case REVISION:					result.with(Language.pt, "Revista");
										result.with(Language.en, "Revision");
										break;
		case EVALUATED:					result.with(Language.pt, "Avaliada");
										result.with(Language.en, "Evaluated");
										break;
		case PROPOSAL_PUBLISHED:		result.with(Language.pt, "Propota Publicada");
										result.with(Language.en, "Proposal Published");
										break;
		case PROPOSAL_FOR_PUBLICATION:	result.with(Language.pt, "Proposta para Publicação");
										result.with(Language.en, "Proposal for Publication");
										break;
		default:						result.with(Language.pt, "Dissertação sem Estado");
										result.with(Language.en, "Dissertation Without State");
										break;
		}
		return result;
	}
}
