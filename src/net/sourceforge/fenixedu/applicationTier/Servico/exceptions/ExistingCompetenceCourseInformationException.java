package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

public class ExistingCompetenceCourseInformationException extends FenixServiceException {

    private final String key;
    private final String[] args;

    public ExistingCompetenceCourseInformationException(final String key, final String... args) {
	super(key);
	this.key = key;
	this.args = args;
    }

    public String getKey() {
	return key;
    }

    public String[] getArgs() {
	return args;
    }
}
