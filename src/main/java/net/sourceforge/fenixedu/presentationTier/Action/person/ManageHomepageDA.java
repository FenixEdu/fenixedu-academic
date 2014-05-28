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
/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.net.MalformedURLException;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.GetHomepage;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SubmitHomepage;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
import net.sourceforge.fenixedu.presentationTier.Action.person.PersonApplication.HomepageApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.RequestUtils;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@StrutsFunctionality(app = HomepageApp.class, path = "manage", titleKey = "title.manage.homepage")
@Mapping(module = "person", path = "/manageHomepage", formBean = "homepageForm")
@Forwards({ @Forward(name = "addInstitutionSection", path = "/commons/sites/addInstitutionSection.jsp"),
        @Forward(name = "organizeItems", path = "/commons/sites/organizeItems.jsp"),
        @Forward(name = "organizeFiles", path = "/commons/sites/organizeFiles.jsp"),
        @Forward(name = "edit-fileItem-name", path = "/commons/sites/editFileItemDisplayName.jsp"),
        @Forward(name = "editSectionPermissions", path = "/commons/sites/editSectionPermissions.jsp"),
        @Forward(name = "confirmSectionDelete", path = "/commons/sites/confirmSectionDelete.jsp"),
        @Forward(name = "editItemPermissions", path = "/commons/sites/editItemPermissions.jsp"),
        @Forward(name = "createSection", path = "/commons/sites/createSection.jsp"),
        @Forward(name = "section", path = "/commons/sites/section.jsp"),
        @Forward(name = "editSection", path = "/commons/sites/editSection.jsp"),
        @Forward(name = "uploadFile", path = "/commons/sites/uploadFile.jsp"),
        @Forward(name = "sectionsManagement", path = "/commons/sites/sectionsManagement.jsp"),
        @Forward(name = "createItem", path = "/commons/sites/createItem.jsp"),
        @Forward(name = "show-homepage-options", path = "/person/homepageOptions.jsp"),
        @Forward(name = "editItem", path = "/commons/sites/editItem.jsp"),
        @Forward(name = "editFile", path = "/commons/sites/editFile.jsp") })
public class ManageHomepageDA extends SiteManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Homepage homepage = getSite(request);
        if (homepage != null) {
            request.setAttribute("homepage", homepage);
            request.setAttribute("site", homepage);
            request.setAttribute("siteActionName", "/manageHomepage.do");
            request.setAttribute("siteContextParam", "homepageID");
            request.setAttribute("siteContextParamValue", homepage.getExternalId());
        }

        return super.execute(mapping, actionForm, request, response);
    }

    @EntryPoint
    public ActionForward options(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person person = getUserView(request).getPerson();
        final Homepage homepage = getSite(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        if (homepage != null) {
            dynaActionForm.set("activated", booleanString(homepage.getActivated()));
            dynaActionForm.set("showUnit", booleanString(homepage.getShowUnit()));
            dynaActionForm.set("showCategory", booleanString(homepage.getShowCategory()));
            dynaActionForm.set("showPhoto", booleanString(homepage.getShowPhoto()));
            dynaActionForm.set("showResearchUnitHomepage", booleanString(homepage.getShowResearchUnitHomepage()));
            dynaActionForm.set("showCurrentExecutionCourses", booleanString(homepage.getShowCurrentExecutionCourses()));
            dynaActionForm.set("showActiveStudentCurricularPlans", booleanString(homepage.getShowActiveStudentCurricularPlans()));
            dynaActionForm.set("showAlumniDegrees", booleanString(homepage.getShowAlumniDegrees()));
            dynaActionForm.set("researchUnitHomepage", homepage.getResearchUnitHomepage());
            dynaActionForm.set("researchUnit",
                    homepage.getResearchUnit() != null ? homepage.getResearchUnit().getContent() : null);
            dynaActionForm.set("showCurrentAttendingExecutionCourses",
                    booleanString(homepage.getShowCurrentAttendingExecutionCourses()));
        }

        SortedSet<Attends> personAttendsSortedByExecutionCourseName =
                new TreeSet<Attends>(Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
        personAttendsSortedByExecutionCourseName.addAll(person.getCurrentAttends());

        request.setAttribute("personAttends", personAttendsSortedByExecutionCourseName);

        return mapping.findForward("show-homepage-options");
    }

    private Object booleanString(Boolean values) {
        if (values == null) {
            return Boolean.FALSE.toString();
        } else {
            return values.toString();
        }
    }

    public ActionForward changeHomepageOptions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;

        final String activated = (String) dynaActionForm.get("activated");
        final String showUnit = (String) dynaActionForm.get("showUnit");
        final String showCategory = (String) dynaActionForm.get("showCategory");
        final String showPhoto = (String) dynaActionForm.get("showPhoto");
        final String showResearchUnitHomepage = (String) dynaActionForm.get("showResearchUnitHomepage");
        final String showCurrentExecutionCourses = (String) dynaActionForm.get("showCurrentExecutionCourses");
        final String showActiveStudentCurricularPlans = (String) dynaActionForm.get("showActiveStudentCurricularPlans");
        final String showAlumniDegrees = (String) dynaActionForm.get("showAlumniDegrees");
        final String researchUnitHomepage = (String) dynaActionForm.get("researchUnitHomepage");
        final String researchUnit = (String) dynaActionForm.get("researchUnit");

        final MultiLanguageString researchUnitMultiLanguageString;
        if (researchUnit != null && researchUnit.length() > 0) {
            researchUnitMultiLanguageString = new MultiLanguageString(researchUnit);
        } else {
            researchUnitMultiLanguageString = null;
        }
        final String showCurrentAttendingExecutionCourses = (String) dynaActionForm.get("showCurrentAttendingExecutionCourses");

        SubmitHomepage.run(getUserView(request).getPerson(), Boolean.valueOf(activated), Boolean.valueOf(showUnit),
                Boolean.valueOf(showCategory), Boolean.valueOf(showPhoto), Boolean.valueOf(showResearchUnitHomepage),
                Boolean.valueOf(showCurrentExecutionCourses), Boolean.valueOf(showActiveStudentCurricularPlans),
                Boolean.valueOf(showAlumniDegrees), researchUnitHomepage, researchUnitMultiLanguageString,
                Boolean.valueOf(showCurrentAttendingExecutionCourses));

        return options(mapping, actionForm, request, response);
    }

    @Override
    protected Homepage getSite(HttpServletRequest request) {
        try {
            return GetHomepage.run(getUserView(request).getPerson(), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        return getUserView(request).getPerson().getName();
    }

    @Override
    protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
        String userUId = getUserView(request).getPerson().getUser().getUsername();
        try {
            return RequestUtils.absoluteURL(request, "/homepage/" + userUId).toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}