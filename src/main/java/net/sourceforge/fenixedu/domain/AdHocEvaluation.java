package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.joda.time.DateTime;

public class AdHocEvaluation extends AdHocEvaluation_Base {

    public static final Comparator<AdHocEvaluation> AD_HOC_EVALUATION_CREATION_DATE_COMPARATOR =
            new Comparator<AdHocEvaluation>() {

                @Override
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

        logCreate();
    }

    @Override
    public void delete() {
        logRemove();
        super.delete();
    }

    @Override
    public EvaluationType getEvaluationType() {
        return EvaluationType.AD_HOC_TYPE;
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.adHocEvaluation") + " "
                + getName();
    }

    public void edit(final String name, final String description, final GradeScale gradeScale) {
        if (name == null || gradeScale == null) {
            throw new NullPointerException();
        }
        setName(name);
        setDescription(description);

        if (getGradeScale() != gradeScale) {
            setGradeScale(gradeScale);
        }

        logEdit();
    }

    @Deprecated
    public java.util.Date getCreation() {
        org.joda.time.DateTime dt = getCreationDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setCreation(java.util.Date date) {
        if (date == null) {
            setCreationDateTime(null);
        } else {
            setCreationDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasCreationDateTime() {
        return getCreationDateTime() != null;
    }

}
