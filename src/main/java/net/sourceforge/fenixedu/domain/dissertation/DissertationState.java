package net.sourceforge.fenixedu.domain.dissertation;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DissertationState extends DissertationState_Base {

	public  DissertationState(DissertationStateValue dissertationStateValue) {
		super();
		setRootDomainObject(getRootDomainObject());
		setDescription(dissertationStateValue);
	}
	
	public enum DissertationStateValue {
	    DRAFT, SUBMITTED, APPROVED, CONFIRMED, REVISION, EVALUATED, PROPOSAL_PUBLISHED, PROPOSAL_FOR_PUBLICATION;
	}
	
	public void setDescription(DissertationStateValue dissertationStateValue) {
		setDissertationStateValueDescription(new MultiLanguageString());
		switch(dissertationStateValue) {
		case DRAFT:						setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.pt, "Rascunho"));
										setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.en, "Draft"));
										break;
		case SUBMITTED:					setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.pt, "Documentos submetidos pelo aluno"));
										setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.en, "Submitted"));
										break;
		case APPROVED:					setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.pt, "Aprovada"));
										setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.en, "Approved"));
										break;
		case CONFIRMED:					setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.pt, "Confirmada"));
										setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.en, "Confirmed"));
										break;
		case REVISION:					setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.pt, "Revista"));
										setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.en, "Revision"));
										break;
		case EVALUATED:					setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.pt, "Avaliada"));
										setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.en, "Evaluated"));
										break;
		case PROPOSAL_PUBLISHED:		setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.pt, "Propota Publicada"));
										setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.en, "Proposal Published"));
										break;
		case PROPOSAL_FOR_PUBLICATION:	setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.pt, "Proposta para Publicação"));
										setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.en, "Proposal for Publication"));
										break;
		default:						setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.pt, "Dissertação sem Estado"));
										setDissertationStateValueDescription(getDissertationStateValueDescription().with(Language.en, "Dissertation Without State"));
										break;
		}
	}
}
