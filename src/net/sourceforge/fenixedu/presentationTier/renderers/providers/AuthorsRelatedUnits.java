package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class AuthorsRelatedUnits implements DataProvider {

    public Converter getConverter() {
	return null;
    }

    public Object provide(Object source, Object currentValue) {
	ResultUnitAssociationCreationBean bean = (ResultUnitAssociationCreationBean) source;
	ResearchResult result = bean.getResult();
	return extractRelatedUnits(result, result.getOrderedAuthorsResultParticipations());
    }

    private Collection<Unit> extractRelatedUnits(ResearchResult result,
	    List<ResultParticipation> orderedAuthorsResultParticipations) {
	Set<Unit> units = new HashSet<Unit>();
	for (ResultParticipation participation : orderedAuthorsResultParticipations) {
	    Person person = participation.getPerson();
	    for (ResearchUnit unit : person.getWorkingResearchUnits()) {
		if (!result.isAssociatedWithUnit(unit)) {
		    units.add(unit);
		}
	    }
	    if (person.hasEmployee() && !result.isAssociatedWithUnit(person.getEmployee().getCurrentWorkingPlace())) {
		units.add(person.getEmployee().getCurrentWorkingPlace());
	    }

	    if (person.hasTeacher()) {
		for (ExecutionCourse course : person.getTeacher().getCurrentExecutionCourses()) {
		    for (Degree degree : course.getDegreesSortedByDegreeName()) {
			if (degree.getUnit() != null && !result.isAssociatedWithUnit(degree.getUnit())) {
			    units.add(degree.getUnit());
			}
		    }
		    for (CurricularCourse curricularCourse : course.getAssociatedCurricularCourses()) {

			CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
			CompetenceCourseGroupUnit groupUnit = (competenceCourse != null) ? competenceCourse
				.getCompetenceCourseGroupUnit() : null;

			if (groupUnit != null) {
			    Collection<? extends Party> scientificAreaUnits = groupUnit
				    .getParentParties(ScientificAreaUnit.class);
			    for (Party scientificAreaUnit : scientificAreaUnits) {
				if (!result.isAssociatedWithUnit((ScientificAreaUnit) scientificAreaUnit)) {
				    units.add((ScientificAreaUnit) scientificAreaUnit);
				}
			    }
			}
		    }

		}
	    }
	}
	return units;
    }

}
