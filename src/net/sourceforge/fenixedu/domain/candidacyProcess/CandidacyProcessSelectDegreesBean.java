package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class CandidacyProcessSelectDegreesBean implements Serializable {

	private static final long serialVersionUID = -3289144446453054775L;

	private List<Degree> degrees = new ArrayList<Degree>();

	public CandidacyProcessSelectDegreesBean() {
	}

	public List<Degree> getDegrees() {
		return degrees;
	}

	public void setDegrees(List<Degree> degrees) {
		this.degrees = degrees;
	}

	public Collection<Degree> getFirstCycleDegrees() {
		return filterDegrees(Degree.readAllByDegreeType(DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
	}

	public Collection<Degree> getSecondCycleDegrees() {
		return filterDegrees(Degree.readAllByDegreeType(DegreeType.BOLONHA_MASTER_DEGREE,
				DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
	}

	protected Collection<Degree> filterDegrees(Collection<Degree> degrees) {
		final Set<AcademicProgram> programs =
				AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(),
						AcademicOperationType.MANAGE_CANDIDACY_PROCESSES);
		return Collections2.filter(degrees, new Predicate<Degree>() {
			@Override
			public boolean apply(Degree degree) {
				return programs.contains(degree);
			}
		});
	}

}
