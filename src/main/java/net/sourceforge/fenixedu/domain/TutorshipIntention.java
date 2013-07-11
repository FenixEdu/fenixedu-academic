package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.joda.time.DateTime;

public class TutorshipIntention extends TutorshipIntention_Base {

    static final public Comparator<TutorshipIntention> COMPARATOR_FOR_ATTRIBUTING_TUTOR_STUDENTS =
            new Comparator<TutorshipIntention>() {

                @Override
                public int compare(TutorshipIntention ti1, TutorshipIntention ti2) {
                    return ti1.getTeacher().getPerson().getName().compareTo(ti2.getTeacher().getPerson().getName());
                }
            };

    public TutorshipIntention(DegreeCurricularPlan dcp, Teacher teacher, AcademicInterval interval) {
        super();
        setDegreeCurricularPlan(dcp);
        setTeacher(teacher);
        setAcademicInterval(interval);
        checkOverlaps();
    }

    private void checkOverlaps() {
        for (TutorshipIntention intention : getDegreeCurricularPlan().getTutorshipIntentionSet()) {
            if (!intention.equals(this) && intention.getTeacher().equals(getTeacher())
                    && intention.getAcademicInterval().overlaps(getAcademicInterval())) {
                throw new DomainException("error.tutorship.overlapingTutorshipIntentions");
            }
        }
    }

    protected RootDomainObject getRootDomainObject() {
        return getDegreeCurricularPlan().getRootDomainObject();
    }

    public boolean isDeletable() {
        for (Tutorship tutorship : getTeacher().getTutorshipsSet()) {
            if (tutorship.getStudentCurricularPlan().getDegreeCurricularPlan().equals(getDegreeCurricularPlan())
                    && getAcademicInterval().contains(tutorship.getStartDate().toDateTime(new DateTime(0)))) {
                return false;
            }
        }
        return true;
    }

    public List<Tutorship> getTutorships() {
        List<Tutorship> result = new ArrayList<Tutorship>();
        for (Tutorship tutorship : getTeacher().getTutorshipsSet()) {
            if (tutorship.getStudentCurricularPlan().getDegreeCurricularPlan().equals(getDegreeCurricularPlan())
                    && getAcademicInterval().contains(tutorship.getStartDate().toDateTime(new DateTime(0)))) {
                result.add(tutorship);
            }
        }
        return result;
    }

    public void delete() {
        setDegreeCurricularPlan(null);
        setTeacher(null);
        deleteDomainObject();
    }

    public static TutorshipIntention readByDcpAndTeacherAndInterval(DegreeCurricularPlan dcp, Teacher teacher,
            AcademicInterval academicInterval) {
        for (TutorshipIntention intention : teacher.getTutorshipIntentionSet()) {
            if (intention.getDegreeCurricularPlan().equals(dcp) && intention.getAcademicInterval().equals(academicInterval)) {
                return intention;
            }
        }
        return null;
    }
    @Deprecated
    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasAcademicInterval() {
        return getAcademicInterval() != null;
    }

}
