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

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.RequestUtils;

public class ManageHomepageDA extends SiteManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Homepage homepage = getUserView(request).getPerson().getHomepage();
        if (homepage != null) {
            request.setAttribute("homepage", homepage);
        }
        
        String homepagePath = RequestUtils.absoluteURL(request, "/homepage/" + getUserView(request).getPerson().getUser().getUserUId()).toString();
        request.setAttribute("directLinkContext", homepagePath);
        
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward activation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Person person = getUserView(request).getPerson();
        final Homepage homepage = person.getHomepage();
        
        final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        if (homepage != null) {
            dynaActionForm.set("activated", homepage.getActivated().toString());
        }
        
        return mapping.findForward("show-homepage-activation");
    }
    
    public ActionForward options(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final Person person = getUserView(request).getPerson();
    	final Homepage homepage = person.getHomepage();

        final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
    	if (homepage != null) {
            dynaActionForm.set("activated", homepage.getActivated().toString());
            dynaActionForm.set("showUnit", homepage.getShowUnit().toString());
            dynaActionForm.set("showCategory", homepage.getShowCategory().toString());
            dynaActionForm.set("showPhoto", homepage.getShowPhoto().toString());
            dynaActionForm.set("showEmail", homepage.getShowEmail().toString());
            dynaActionForm.set("showTelephone", homepage.getShowTelephone().toString());
            dynaActionForm.set("showWorkTelephone", homepage.getShowWorkTelephone().toString());
            dynaActionForm.set("showMobileTelephone", homepage.getShowMobileTelephone().toString());
            dynaActionForm.set("showAlternativeHomepage", homepage.getShowAlternativeHomepage().toString());
            dynaActionForm.set("showResearchUnitHomepage", homepage.getShowResearchUnitHomepage().toString());
            dynaActionForm.set("showCurrentExecutionCourses", homepage.getShowCurrentExecutionCourses().toString());
            dynaActionForm.set("showActiveStudentCurricularPlans", homepage.getShowActiveStudentCurricularPlans().toString());
            dynaActionForm.set("showAlumniDegrees", homepage.getShowAlumniDegrees().toString());
            dynaActionForm.set("researchUnitHomepage", homepage.getResearchUnitHomepage());
            dynaActionForm.set("researchUnit", homepage.getResearchUnit() != null ? homepage.getResearchUnit().getContent() : null);
            dynaActionForm.set("showCurrentAttendingExecutionCourses", homepage.getShowCurrentAttendingExecutionCourses().toString());
        }
        
        SortedSet<Attends> personAttendsSortedByExecutionCourseName = new TreeSet<Attends>(
                Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
        personAttendsSortedByExecutionCourseName.addAll(person.getCurrentAttends());
        request.setAttribute("personAttends", personAttendsSortedByExecutionCourseName);

        return mapping.findForward("show-homepage-options");
    }

    public ActionForward activateHomepage(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Homepage homepage = getUserView(request).getPerson().getHomepage();
        final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        
        final String activated = (String) dynaActionForm.get("activated");
        final Object[] args = {
                getUserView(request).getPerson(),
                Boolean.valueOf(activated),
                homepage != null ? homepage.getShowUnit() : false,
                homepage != null ? homepage.getShowCategory() : false,
                homepage != null ? homepage.getShowPhoto() : false,
                homepage != null ? homepage.getShowEmail() : false,
                homepage != null ? homepage.getShowTelephone() : false,
                homepage != null ? homepage.getShowWorkTelephone() : false,
                homepage != null ? homepage.getShowMobileTelephone() : false,
                homepage != null ? homepage.getShowAlternativeHomepage() : false,
                homepage != null ? homepage.getShowResearchUnitHomepage() : false,
                homepage != null ? homepage.getShowCurrentExecutionCourses() : false,
                homepage != null ? homepage.getShowActiveStudentCurricularPlans() : false,
                homepage != null ? homepage.getShowAlumniDegrees() : false,
                homepage != null ? homepage.getResearchUnitHomepage() : null,
                homepage != null ? homepage.getResearchUnit() : null,
                homepage != null ? homepage.getShowCurrentAttendingExecutionCourses() : false};
        executeService(request, "SubmitHomepage", args);

        return activation(mapping, actionForm, request, response);
    }
    
    public ActionForward changeHomepageOptions(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

    Homepage homepage = getUserView(request).getPerson().getHomepage();
    	final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;

    	final String showUnit = (String) dynaActionForm.get("showUnit");
    	final String showCategory = (String) dynaActionForm.get("showCategory");
    	final String showPhoto = (String) dynaActionForm.get("showPhoto");
    	final String showEmail = (String) dynaActionForm.get("showEmail");
    	final String showTelephone = (String) dynaActionForm.get("showTelephone");
    	final String showWorkTelephone = (String) dynaActionForm.get("showWorkTelephone");
    	final String showMobileTelephone = (String) dynaActionForm.get("showMobileTelephone");
    	final String showAlternativeHomepage = (String) dynaActionForm.get("showAlternativeHomepage");
    	final String showResearchUnitHomepage = (String) dynaActionForm.get("showResearchUnitHomepage");
    	final String showCurrentExecutionCourses = (String) dynaActionForm.get("showCurrentExecutionCourses");
    	final String showActiveStudentCurricularPlans = (String) dynaActionForm.get("showActiveStudentCurricularPlans");
    	final String showAlumniDegrees = (String) dynaActionForm.get("showAlumniDegrees");
    	final String researchUnitHomepage = (String) dynaActionForm.get("researchUnitHomepage");
    	final String researchUnit = (String) dynaActionForm.get("researchUnit");
    	final MultiLanguageString researchUnitMultiLanguageString;
    	if (researchUnit != null && researchUnit.length() > 0) {
    		researchUnitMultiLanguageString = new MultiLanguageString();
    		researchUnitMultiLanguageString.setContent(researchUnit);
    	} else {
    		researchUnitMultiLanguageString = null;
    	}
    	final String showCurrentAttendingExecutionCourses = (String) dynaActionForm.get("showCurrentAttendingExecutionCourses");

    	final Object[] args = {
    			getUserView(request).getPerson(),
    			homepage.getActivated(),
    			Boolean.valueOf(showUnit),
    			Boolean.valueOf(showCategory),
    			Boolean.valueOf(showPhoto),
    			Boolean.valueOf(showEmail),
    			Boolean.valueOf(showTelephone),
    			Boolean.valueOf(showWorkTelephone),
    			Boolean.valueOf(showMobileTelephone),
    			Boolean.valueOf(showAlternativeHomepage),
    			Boolean.valueOf(showResearchUnitHomepage),
    			Boolean.valueOf(showCurrentExecutionCourses),
    			Boolean.valueOf(showActiveStudentCurricularPlans),
    			Boolean.valueOf(showAlumniDegrees),
    			researchUnitHomepage,
    			researchUnitMultiLanguageString,
    			Boolean.valueOf(showCurrentAttendingExecutionCourses) };
    	executeService(request, "SubmitHomepage", args);

        return options(mapping, actionForm, request, response);
    }

    @Override
    protected Site getSite(HttpServletRequest request) {
        return getUserView(request).getPerson().getHomepage();
    }

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
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