package net.sourceforge.fenixedu.domain.teacher.evaluation;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.Interval;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class FacultyEvaluationProcess extends FacultyEvaluationProcess_Base {
    
    public FacultyEvaluationProcess() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public FacultyEvaluationProcess(final MultiLanguageString title, final Interval autoEvaluationInterval, final Interval evaluationInterval) {
	this();
	setTitle(title);
	setAutoEvaluationInterval(autoEvaluationInterval);
	setEvaluationInterval(evaluationInterval);
    }
    
}
