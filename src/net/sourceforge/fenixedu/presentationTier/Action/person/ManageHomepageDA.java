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
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.RequestUtils;

public class ManageHomepageDA extends SiteManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Homepage homepage = getSite(request);
        if (homepage != null) {
            request.setAttribute("homepage", homepage);
        }
        
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        Homepage homepage = (Homepage) request.getAttribute("homepage");
        if (homepage == null) {
            return null;
        }
        
        try {
            return RequestUtils.absoluteURL(request, "/homepage/" + homepage.getPerson().getUser().getUserUId()).toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }
    
    public ActionForward options(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final Person person = getUserView(request).getPerson();
    	final Homepage homepage = getSite(request);

        final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
    	if (homepage != null) {
            dynaActionForm.set("activated", booleanString(homepage.getActivated()));
            dynaActionForm.set("showUnit", booleanString(homepage.getShowUnit()));
            dynaActionForm.set("showCategory", booleanString(homepage.getShowCategory()));
            dynaActionForm.set("showPhoto", booleanString(homepage.getShowPhoto()));
            dynaActionForm.set("showEmail", booleanString(homepage.getShowEmail()));
            dynaActionForm.set("showTelephone", booleanString(homepage.getShowTelephone()));
            dynaActionForm.set("showWorkTelephone", booleanString(homepage.getShowWorkTelephone()));
            dynaActionForm.set("showMobileTelephone", booleanString(homepage.getShowMobileTelephone()));
            dynaActionForm.set("showAlternativeHomepage", booleanString(homepage.getShowAlternativeHomepage()));
            dynaActionForm.set("showResearchUnitHomepage", booleanString(homepage.getShowResearchUnitHomepage()));
            dynaActionForm.set("showCurrentExecutionCourses", booleanString(homepage.getShowCurrentExecutionCourses()));
            dynaActionForm.set("showActiveStudentCurricularPlans", booleanString(homepage.getShowActiveStudentCurricularPlans()));
            dynaActionForm.set("showAlumniDegrees", booleanString(homepage.getShowAlumniDegrees()));
            dynaActionForm.set("researchUnitHomepage", homepage.getResearchUnitHomepage());
            dynaActionForm.set("researchUnit", homepage.getResearchUnit() != null ? homepage.getResearchUnit().getContent() : null);
            dynaActionForm.set("showCurrentAttendingExecutionCourses", booleanString(homepage.getShowCurrentAttendingExecutionCourses()));
            dynaActionForm.set("showPublications", booleanString(homepage.getShowPublications()));
            dynaActionForm.set("showPatents", booleanString(homepage.getShowPatents()));
            dynaActionForm.set("showInterests", booleanString(homepage.getShowInterests()));
            dynaActionForm.set("showParticipations", booleanString(homepage.getShowParticipations()));
    	}
        
        SortedSet<Attends> personAttendsSortedByExecutionCourseName = new TreeSet<Attends>(
                Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
        personAttendsSortedByExecutionCourseName.addAll(person.getCurrentAttends());

        request.setAttribute("personAttends", personAttendsSortedByExecutionCourseName);
        request.setAttribute("hasPhoto", person.getPersonalPhoto() != null);

        return mapping.findForward("show-homepage-options");
    }

    private Object booleanString(Boolean values) {
        if (values == null) {
            return Boolean.FALSE.toString();
        }
        else {
            return values.toString();
        }
    }

    public ActionForward changeHomepageOptions(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        	final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
    
        final String activated = (String) dynaActionForm.get("activated");
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
        	final String showPublications = (String) dynaActionForm.get("showPublications");
        	final String showPatents = (String) dynaActionForm.get("showPatents");
        	final String showInterests = (String) dynaActionForm.get("showInterests");
        	final String showParticipations = (String) dynaActionForm.get("showParticipations");
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
        			Boolean.valueOf(activated),
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
        			Boolean.valueOf(showCurrentAttendingExecutionCourses),
        			Boolean.valueOf(showPublications),
        		    Boolean.valueOf(showPatents),
        		    Boolean.valueOf(showInterests),
        		    Boolean.valueOf(showParticipations)
        		                		};
        	executeService(request, "SubmitHomepage", args);

        return options(mapping, actionForm, request, response);
    }

    @Override
    protected Homepage getSite(HttpServletRequest request) {
        try {
            return (Homepage) ServiceUtils.executeService(getUserView(request), "GetHomepage", getUserView(request).getPerson(), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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