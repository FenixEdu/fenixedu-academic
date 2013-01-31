package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.AssemblySite;

public class AssemblySiteProcess extends AbstractAcronymPathProcess<AssemblySite> {

	private static final String ACRONYM = "aestatutaria";

	@Override
	protected String getAcronym() {
		return ACRONYM;
	}
}
