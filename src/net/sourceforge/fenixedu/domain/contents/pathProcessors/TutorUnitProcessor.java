package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.TutorSite;

public class TutorUnitProcessor extends AbstractAcronymPathProcess<TutorSite> {

	private static final String ACRONYM = "Tutor";

	@Override
	protected String getAcronym() {
		return ACRONYM;
	}
}
