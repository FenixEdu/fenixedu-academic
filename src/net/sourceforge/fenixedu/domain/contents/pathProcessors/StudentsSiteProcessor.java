package net.sourceforge.fenixedu.domain.contents.pathProcessors;

import net.sourceforge.fenixedu.domain.StudentsSite;

public class StudentsSiteProcessor extends AbstractAcronymPathProcess<StudentsSite> {

	private static final String ACRONYM = "ACD";

	@Override
	protected String getAcronym() {
		return ACRONYM;
	}
}
