package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.CreateSummaryBean;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorshipSummaryRelationBean;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class TutorshipSummary extends TutorshipSummary_Base {

	public TutorshipSummary() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public boolean isActive() {
		if (getSemester().getTutorshipSummaryPeriod() == null) {
			return false;
		}

		LocalDate curDate = new LocalDate();
		LocalDate beginDate = getSemester().getTutorshipSummaryPeriod().getBeginDate();
		LocalDate endDate = getSemester().getTutorshipSummaryPeriod().getEndDate();

		return !(curDate.isBefore(beginDate) || curDate.isAfter(endDate));
	}

	@Service
	public void update(final CreateSummaryBean bean, boolean relations) {
		setHowManyContactsEmail(bean.getHowManyContactsEmail());
		setHowManyContactsPhone(bean.getHowManyContactsPhone());
		setHowManyReunionsGroup(bean.getHowManyReunionsGroup());
		setHowManyReunionsIndividual(bean.getHowManyReunionsIndividual());

		setTutorshipSummarySatisfaction(bean.getTutorshipSummarySatisfaction());
		setTutorshipSummaryProgramAssessment(bean.getTutorshipSummaryProgramAssessment());
		setDifficulties(bean.getDifficulties());
		setGains(bean.getGains());
		setSuggestions(bean.getSuggestions());

		setProblemsR1(bean.getProblemsR1());
		setProblemsR2(bean.getProblemsR2());
		setProblemsR3(bean.getProblemsR3());
		setProblemsR4(bean.getProblemsR4());
		setProblemsR5(bean.getProblemsR5());
		setProblemsR6(bean.getProblemsR6());
		setProblemsR7(bean.getProblemsR7());
		setProblemsR8(bean.getProblemsR8());
		setProblemsR9(bean.getProblemsR9());
		setProblemsR10(bean.getProblemsR10());
		setProblemsOther(bean.getProblemsOther());

		setGainsR1(bean.getGainsR1());
		setGainsR2(bean.getGainsR2());
		setGainsR3(bean.getGainsR3());
		setGainsR4(bean.getGainsR4());
		setGainsR5(bean.getGainsR5());
		setGainsR6(bean.getGainsR6());
		setGainsR7(bean.getGainsR7());
		setGainsR8(bean.getGainsR8());
		setGainsR9(bean.getGainsR9());
		setGainsR10(bean.getGainsR10());
		setGainsOther(bean.getGainsOther());

		if (relations) {
			for (TutorshipSummaryRelationBean relationBean : bean.getTutorshipRelations()) {
				relationBean.save();
			}
		}
	}

	@Service
	static public TutorshipSummary create(final CreateSummaryBean bean) {
		TutorshipSummary tutorshipSummary = new TutorshipSummary();

		tutorshipSummary.setDegree(bean.getDegree());
		tutorshipSummary.setTeacher(bean.getTeacher());
		tutorshipSummary.setSemester(bean.getExecutionSemester());

		tutorshipSummary.update(bean, false);

		for (TutorshipSummaryRelationBean relationBean : bean.getTutorshipRelations()) {
			relationBean.setTutorshipSummary(tutorshipSummary);

			relationBean.save();
		}

		return tutorshipSummary;
	}

	public static Set<ExecutionSemester> getActivePeriods() {
		Set<ExecutionSemester> semesters = new HashSet<ExecutionSemester>();
		for (TutorshipSummaryPeriod tutorshipPeriod : RootDomainObject.getInstance().getTutorshipSummaryPeriodsSet()) {
			if (tutorshipPeriod.isOpenNow()) {
				semesters.add(tutorshipPeriod.getExecutionSemester());
			}
		}
		return semesters;
	}
}
