package net.sourceforge.fenixedu.domain.phd.conclusion;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisFinalGrade;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PhdConclusionProcess extends PhdConclusionProcess_Base {

	public static final Comparator<PhdConclusionProcess> VERSION_COMPARATOR = new Comparator<PhdConclusionProcess>() {
		@Override
		public int compare(PhdConclusionProcess o1, PhdConclusionProcess o2) {
			return o1.getVersion().compareTo(o2.getVersion()) * -1;
		}
	};

	protected PhdConclusionProcess() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	protected PhdConclusionProcess(final PhdConclusionProcessBean conclusionProcessBean, Person responsible) {
		this();
		init(conclusionProcessBean, responsible);
	}

	protected void init(final PhdConclusionProcessBean conclusionProcessBean, Person responsible) {
		PhdIndividualProgramProcess process = conclusionProcessBean.getPhdIndividualProgramProcess();
		LocalDate conclusionDate = conclusionProcessBean.getConclusionDate();
		PhdThesisFinalGrade grade = conclusionProcessBean.getGrade();
		BigDecimal thesisEctsCredits = conclusionProcessBean.getThesisEctsCredits();
		BigDecimal studyPlanEctsCredits = conclusionProcessBean.getStudyPlanEctsCredits();

		if (!process.isConcluded()) {
			throw new DomainException("error.phd.PhdConclusionProcess.process.is.not.concluded");
		}

		if (responsible == null) {
			throw new DomainException("error.phd.PhdConclusionProcess.responsible.is.required");
		}

		if (conclusionDate == null) {
			throw new DomainException("error.phd.PhdConclusionProcess.conclusionDate.is.required");
		}

		if (grade == null) {
			throw new DomainException("error.phd.PhdConclusionProcess.grade.is.required");
		}

		if (process.hasRegistration() && !process.getRegistration().isRegistrationConclusionProcessed()) {
			throw new DomainException("error.phd.PhdConclusionProcess.registration.must.be.concluded.first");
		}

		if (thesisEctsCredits == null) {
			throw new DomainException("error.phd.PhdConclusionProcess.thesisEctsCredits.is.required");
		}

		if (!process.getCandidacyProcess().isStudyPlanExempted()) {
			if (studyPlanEctsCredits == null) {
				throw new DomainException("error.phd.PhdConclusionProcess.studyPlanEctsCredits.is.required");
			}
		}

		setResponsible(responsible);
		setConclusionDate(conclusionDate);
		setVersion(process.getLastConclusionProcess() != null ? process.getLastConclusionProcess().getVersion() + 1 : 1);
		setPhdProcess(process);
		setWhenCreated(new DateTime());
		setFinalGrade(grade);
		setThesisEctsCredits(thesisEctsCredits);
		setStudyPlanEctsCredits(studyPlanEctsCredits);
	}

	public static PhdConclusionProcess create(final PhdConclusionProcessBean conclusionProcessBean, Person responsible) {
		return new PhdConclusionProcess(conclusionProcessBean, responsible);
	}

	public BigDecimal getTotalEctsCredits() {
		if (getPhdProcess().getCandidacyProcess().isStudyPlanExempted()) {
			return getThesisEctsCredits();
		}

		return getThesisEctsCredits().add(getStudyPlanEctsCredits());
	}

}
