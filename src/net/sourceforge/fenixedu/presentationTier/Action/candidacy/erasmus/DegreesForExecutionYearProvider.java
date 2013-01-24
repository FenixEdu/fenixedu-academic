/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class DegreesForExecutionYearProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

	final Set<AcademicProgram> programs = AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(),
		AcademicOperationType.MANAGE_INDIVIDUAL_CANDIDACIES);

	return Collections2.filter(getDegrees(source), new Predicate<Degree>() {
	    @Override
	    public boolean apply(Degree degree) {
		return programs.contains(degree);
	    }
	});
    }

    public Set<Degree> getDegrees(Object source) {

	final SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
	final DegreeCourseInformationBean chooseDegreeBean = (DegreeCourseInformationBean) source;

	for (final Degree degree : Degree.readAllByDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE,
		DegreeType.BOLONHA_MASTER_DEGREE)) {

	    if (degree.getSigla().equals("MSCIT")) {
		continue;
	    }

	    if (degree.getSigla().equals("MCR")) {
		continue;
	    }

	    if (degree.getSigla().equals("MEEst")) {
		continue;
	    }

	    if (degree.getSigla().equals("MMM")) {
		continue;
	    }

	    if (degree.getSigla().equals("MEFarm")) {
		continue;
	    }

	    if (matchesExecutionYear(degree, chooseDegreeBean.getExecutionYear())) {
		result.add(degree);
	    }
	}

	return result;
    }

    private boolean matchesExecutionYear(Degree degree, ExecutionYear executionYear) {
	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
	    if (executionDegree.getDegree() == degree) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}