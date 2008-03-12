package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.joda.time.DateTime;

public class AdHocEvaluation extends AdHocEvaluation_Base {

    public static final Comparator<AdHocEvaluation> AD_HOC_EVALUATION_CREATION_DATE_COMPARATOR = new Comparator<AdHocEvaluation>() {

	public int compare(AdHocEvaluation e1, AdHocEvaluation e2) {
	    return e1.getCreationDateTime().compareTo(e2.getCreationDateTime());
	}

    };

    public AdHocEvaluation() {
	super();
	setCreationDateTime(new DateTime());
    }

    public AdHocEvaluation(final ExecutionCourse executionCourse, final String name, final String description,
	    final GradeScale gradeScale) {
	this();

	if (name == null || executionCourse == null || gradeScale == null) {
	    throw new NullPointerException();
	}

	addAssociatedExecutionCourses(executionCourse);
	setName(name);
	setDescription(description);
	setGradeScale(gradeScale);
    }

    @Override
    public EvaluationType getEvaluationType() {
	return EvaluationType.AD_HOC_TYPE;
    }

    public void edit(final String name, final String description, final GradeScale gradeScale) {
	if (name == null || gradeScale == null) {
	    throw new NullPointerException();
	}
	setName(name);
	setDescription(description);

	if (getGradeScale() != gradeScale) {
	    if (!getMarks().isEmpty()) {
		throw new DomainException("error.adHacEvaluation.already.has.associated.marks");
	    }
	    setGradeScale(gradeScale);
	}
    }

}
