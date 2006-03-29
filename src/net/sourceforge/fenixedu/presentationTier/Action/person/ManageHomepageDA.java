/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ManageHomepageDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final Person person = getUserView(request).getPerson();
    	final Homepage homepage = person.getHomepage();

    	if (homepage != null) {
    		final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;

            dynaActionForm.set("activated", homepage.getActivated().toString());
            if (homepage.getName() == null || homepage.getName().length() == 0) {
            	dynaActionForm.set("name", person.getName());
            } else {
            	dynaActionForm.set("name", homepage.getName());
            }
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
    	}

        return mapping.findForward("show-manage-homepage");
    }

    public ActionForward submitHomepage(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

    	final DynaActionForm dynaActionForm = (DynaActionForm) actionForm;

    	final String activated = (String) dynaActionForm.get("activated");
    	final String name = (String) dynaActionForm.get("name");
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

    	final Object[] args = {
    			getUserView(request).getPerson(),
    			Boolean.valueOf(activated),
    			name,
    			Boolean.valueOf(showUnit),
    			Boolean.valueOf(showCategory),
    			Boolean.valueOf(showPhoto),
    			Boolean.valueOf(showEmail),
    			Boolean.valueOf(showTelephone),
    			Boolean.valueOf(showWorkTelephone),
    			Boolean.valueOf(showMobileTelephone),
    			Boolean.valueOf(showAlternativeHomepage),
    			Boolean.valueOf(showResearchUnitHomepage),
    			Boolean.valueOf(showCurrentExecutionCourses) };
    	executeService(request, "SubmitHomepage", args);

        return prepare(mapping, actionForm, request, response);
    }

}