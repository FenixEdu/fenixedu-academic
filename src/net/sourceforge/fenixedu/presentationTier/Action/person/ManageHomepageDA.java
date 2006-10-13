/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ManageHomepageDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
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

        return mapping.findForward("show-manage-homepage");
    }

    public ActionForward submitHomepage(ActionMapping mapping, ActionForm actionForm,
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
    			Boolean.valueOf(showCurrentAttendingExecutionCourses) };
    	executeService(request, "SubmitHomepage", args);

        return prepare(mapping, actionForm, request, response);
    }

}