package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.joda.time.DateTime;

public class TutorshipIntention extends TutorshipIntention_Base {
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

	@Override
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

	public void delete() {
		removeDegreeCurricularPlan();
		removeTeacher();
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
}
