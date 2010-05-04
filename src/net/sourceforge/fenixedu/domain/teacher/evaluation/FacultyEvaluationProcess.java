package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.Interval;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class FacultyEvaluationProcess extends FacultyEvaluationProcess_Base {

    public static Comparator<FacultyEvaluationProcess> COMPARATOR_BY_INTERVAL = new Comparator<FacultyEvaluationProcess>() {
	@Override
	public int compare(FacultyEvaluationProcess p1, FacultyEvaluationProcess p2) {
	    return p1.getAutoEvaluationInterval().getStart().compareTo(p2.getAutoEvaluationInterval().getStart());
	}
    };

    public FacultyEvaluationProcess() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public FacultyEvaluationProcess(final MultiLanguageString title, final Interval autoEvaluationInterval,
	    final Interval evaluationInterval) {
	this();
	setTitle(title);
	setAutoEvaluationInterval(autoEvaluationInterval);
	setEvaluationInterval(evaluationInterval);
    }

}
