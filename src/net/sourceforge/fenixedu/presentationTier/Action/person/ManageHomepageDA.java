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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.RequestUtils;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Mapping(module = "person", path = "/manageHomepage", input = "/manageHomepage.do?method=prepare", attribute = "homepageForm", formBean = "homepageForm", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "addInstitutionSection", path = "homepage-add-institution-section", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "uploadScorm", path = "homepage-upload-scorm", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "organizeItems", path = "homepage-organizeItems", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "createScorm", path = "homepage-create-scorm", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "organizeFiles", path = "homepage-organizeFiles", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "edit-fileItem-name", path = "homepage-editFileItemName", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "editSectionPermissions", path = "homepage-editSectionPermissions", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "confirmSectionDelete", path = "homepage-confirmSectionDelete", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "editItemPermissions", path = "homepage-editItemPermissions", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "createSection", path = "homepage-createSection", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "section", path = "homepage-section", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "editSection", path = "homepage-editSection", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "uploadFile", path = "homepage-uploadFile", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "sectionsManagement", path = "homepage-sectionsManagement", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "createItem", path = "homepage-createItem", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "show-homepage-options", path = "/person/homepageOptions.jsp", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "editItem", path = "homepage-editItem", tileProperties = @Tile(title = "private.personal.homepage.options")),
	@Forward(name = "editFile", path = "homepage-editFile", tileProperties = @Tile(title = "private.personal.homepage.options")) })
public class ManageHomepageDA extends SiteManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Homepage homepage = getSite(request);
	if (homepage != null) {
	    request.setAttribute("homepage", homepage);
	}

	return super.execute(mapping, actionForm, request, response);
    }

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
	    dynaActionForm.set("researchUnit", homepage.getResearchUnit() != null ? homepage.getResearchUnit().getContent()
		    : null);
	    dynaActionForm.set("showCurrentAttendingExecutionCourses",
		    booleanString(homepage.getShowCurrentAttendingExecutionCourses()));
	    dynaActionForm.set("showPublications", booleanString(homepage.getShowPublications()));
	    dynaActionForm.set("showPatents", booleanString(homepage.getShowPatents()));
	    dynaActionForm.set("showInterests", booleanString(homepage.getShowInterests()));
	    dynaActionForm.set("showParticipations", booleanString(homepage.getShowParticipations()));
	    dynaActionForm.set("showPrizes", booleanString(homepage.getShowPrizes()));
	}

	SortedSet<Attends> personAttendsSortedByExecutionCourseName = new TreeSet<Attends>(
		Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
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
	final String showPublications = (String) dynaActionForm.get("showPublications");
	final String showPatents = (String) dynaActionForm.get("showPatents");
	final String showInterests = (String) dynaActionForm.get("showInterests");
	final String showParticipations = (String) dynaActionForm.get("showParticipations");
	final String showPrizes = (String) dynaActionForm.get("showPrizes");

	final MultiLanguageString researchUnitMultiLanguageString;
	if (researchUnit != null && researchUnit.length() > 0) {
	    researchUnitMultiLanguageString = new MultiLanguageString();
	    researchUnitMultiLanguageString.setContent(researchUnit);
	} else {
	    researchUnitMultiLanguageString = null;
	}
	final String showCurrentAttendingExecutionCourses = (String) dynaActionForm.get("showCurrentAttendingExecutionCourses");

	SubmitHomepage.run(getUserView(request).getPerson(), Boolean.valueOf(activated), Boolean.valueOf(showUnit),
		Boolean.valueOf(showCategory), Boolean.valueOf(showPhoto), Boolean.valueOf(showResearchUnitHomepage),
		Boolean.valueOf(showCurrentExecutionCourses), Boolean.valueOf(showActiveStudentCurricularPlans),
		Boolean.valueOf(showAlumniDegrees), researchUnitHomepage, researchUnitMultiLanguageString,
		Boolean.valueOf(showCurrentAttendingExecutionCourses), Boolean.valueOf(showPublications),
		Boolean.valueOf(showPatents), Boolean.valueOf(showInterests), Boolean.valueOf(showParticipations),
		Boolean.valueOf(showPrizes));

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
	String userUId = getUserView(request).getPerson().getUser().getUserUId();
	try {
	    return RequestUtils.absoluteURL(request, "/homepage/" + userUId).toString();
	} catch (MalformedURLException e) {
	    throw new RuntimeException(e);
	}
    }

}