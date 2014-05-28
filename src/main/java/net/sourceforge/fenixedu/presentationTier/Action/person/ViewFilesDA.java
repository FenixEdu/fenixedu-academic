/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import net.sourceforge.fenixedu.domain.ScientificCouncilSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.MessagingApplication.MessagingFilesApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@StrutsFunctionality(app = MessagingFilesApp.class, path = "view", titleKey = "label.files.view")
@Mapping(module = "messaging", path = "/viewFiles")
@Forwards(value = { @Forward(name = "uploadFile", path = "/commons/unitFiles/uploadFile.jsp"),
        @Forward(name = "manageFiles", path = "/commons/unitFiles/manageFiles.jsp"),
        @Forward(name = "showSources", path = "/messaging/files/showSources.jsp"),
        @Forward(name = "editFile", path = "/commons/unitFiles/editFile.jsp") })
public class ViewFilesDA extends UnitFunctionalities {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute("functionalityAction", "viewFiles");
        request.setAttribute("module", "messaging");
        return super.execute(mapping, form, request, response);
    }

    @EntryPoint
    public ActionForward showSources(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        List<PersonFileSource> result = new ArrayList<PersonFileSource>();

        MultiLanguageString departmentsName =
                new MultiLanguageString().with(MultiLanguageString.pt, "Departamentos").with(MultiLanguageString.en, "Departments");
        PersonFileSourceGroupBean departmentsGroup = new PersonFileSourceGroupBean(departmentsName);

        SortedSet<Department> departments = new TreeSet<Department>(Department.COMPARATOR_BY_NAME);
        departments.addAll(Bennu.getInstance().getDepartmentsSet());
        for (Department department : departments) {
            departmentsGroup.add(new PersonFileSourceBean(department.getDepartmentUnit()));
        }

        MultiLanguageString researchUnitsName =
                new MultiLanguageString().with(MultiLanguageString.pt, "Unidades de Investigação").with(MultiLanguageString.en, "Research Units");
        PersonFileSourceGroupBean researchUnitsGroup = new PersonFileSourceGroupBean(researchUnitsName);

        SortedSet<Unit> researchUnits = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
        researchUnits.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.RESEARCH_UNIT));
        for (Unit unit : researchUnits) {
            researchUnitsGroup.add(new PersonFileSourceBean(unit));
        }

        MultiLanguageString scientificAreaName =
                new MultiLanguageString().with(MultiLanguageString.pt, "Áreas Ciêntificas").with(MultiLanguageString.en, "Scientific Areas");
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
