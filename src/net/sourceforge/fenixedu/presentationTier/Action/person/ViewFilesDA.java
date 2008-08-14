package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.PedagogicalCouncilSite;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCouncilSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ViewFilesDA extends UnitFunctionalities {

    public ActionForward showSources(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	List<PersonFileSource> result = new ArrayList<PersonFileSource>();

	MultiLanguageString departmentsName = MultiLanguageString.i18n().add("pt", "Departamentos").add("en", "Departments")
		.finish();
	PersonFileSourceGroupBean departmentsGroup = new PersonFileSourceGroupBean(departmentsName);

	SortedSet<Department> departments = new TreeSet<Department>(Department.COMPARATOR_BY_NAME);
	departments.addAll(RootDomainObject.getInstance().getDepartments());
	for (Department department : departments) {
	    departmentsGroup.add(new PersonFileSourceBean(department.getDepartmentUnit()));
	}

	MultiLanguageString researchUnitsName = MultiLanguageString.i18n().add("pt", "Unidades de Investigação").add("en",
		"Research Units").finish();
	PersonFileSourceGroupBean researchUnitsGroup = new PersonFileSourceGroupBean(researchUnitsName);

	SortedSet<Unit> researchUnits = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
	researchUnits.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.RESEARCH_UNIT));
	for (Unit unit : researchUnits) {
	    researchUnitsGroup.add(new PersonFileSourceBean(unit));
	}

	MultiLanguageString scientificAreaName = MultiLanguageString.i18n().add("pt", "Áreas Ciêntificas").add("en",
		"Scientific Areas").finish();
	PersonFileSourceGroupBean scientificAreaUnits = new PersonFileSourceGroupBean(scientificAreaName);

	SortedSet<Unit> scientificAreas = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
	scientificAreas.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.SCIENTIFIC_AREA));
	for (Unit unit : scientificAreas) {
	    scientificAreaUnits.add(new PersonFileSourceBean(unit));
	}

	PersonFileSourceBean pedagogicalCouncil = new PersonFileSourceBean(PedagogicalCouncilSite.getSite().getUnit());

	PersonFileSourceBean scientific = new PersonFileSourceBean(ScientificCouncilSite.getSite().getUnit());

	result.add(departmentsGroup);
	result.add(researchUnitsGroup);
	result.add(scientificAreaUnits);
	result.add(pedagogicalCouncil);
	result.add(scientific);

	Collections.sort(result, PersonFileSource.COMPARATOR);

	filterSources(result, getLoggedPerson(request));

	request.setAttribute("sources", result);

	return mapping.findForward("showSources");
    }

    private void filterSources(List<PersonFileSource> result, Person person) {
	Iterator<PersonFileSource> iterator = result.iterator();

	while (iterator.hasNext()) {
	    PersonFileSource source = iterator.next();

	    if (source.getCount() == 0 && !source.isAllowedToUpload(person)) {
		iterator.remove();
	    } else {
		filterSources(source.getChildren(), person);
	    }
	}
    }

    public ActionForward viewFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return manageFiles(mapping, form, request, response);
    }

}
