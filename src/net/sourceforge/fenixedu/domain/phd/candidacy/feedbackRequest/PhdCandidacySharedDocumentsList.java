package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.phd.PhdElementsList;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.util.StringUtils;

public class PhdCandidacySharedDocumentsList extends PhdElementsList<PhdIndividualProgramDocumentType> {

	static private final long serialVersionUID = 1L;

	protected PhdCandidacySharedDocumentsList() {
		super();
	}

	protected PhdCandidacySharedDocumentsList(final String types) {
		super(types);
	}

	public PhdCandidacySharedDocumentsList(Collection<PhdIndividualProgramDocumentType> types) {
		super(types);
	}

	@Override
	protected PhdIndividualProgramDocumentType convertElementToSet(String valueToParse) {
		return PhdIndividualProgramDocumentType.valueOf(valueToParse);
	}

	@Override
	protected String convertElementToString(PhdIndividualProgramDocumentType element) {
		return element.name();
	}

	@Override
	protected PhdCandidacySharedDocumentsList createNewInstance() {
		return new PhdCandidacySharedDocumentsList();
	}

	@Override
	public PhdCandidacySharedDocumentsList addAccessTypes(PhdIndividualProgramDocumentType... types) {
		return (PhdCandidacySharedDocumentsList) super.addAccessTypes(types);
	}

	static public PhdCandidacySharedDocumentsList importFromString(String value) {
		return StringUtils.isEmpty(value) ? EMPTY : new PhdCandidacySharedDocumentsList(value);
	}

	final static private PhdCandidacySharedDocumentsList EMPTY = new PhdCandidacySharedDocumentsList() {

		static private final long serialVersionUID = 1L;

		@Override
		public Set<PhdIndividualProgramDocumentType> getTypes() {
			return Collections.emptySet();
		}

		@Override
		public String toString() {
			return "";
		}
	};
}
