/*
 * Created on 18/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ReadCareers;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
@Mapping(module = "teacher", path = "/readCareers", attribute = "readCareersForm", formBean = "readCareersForm",
        scope = "request")
@Forwards(value = { @Forward(name = "show-teaching-form", path = "view-teaching-careers"),
        @Forward(name = "show-professional-form", path = "view-professional-careers") })
public class ReadCareersAction extends FenixAction {

    /*
     * (non-Javadoc)
     * 
     * @seeorg.apache.struts.action.Action#execute(org.apache.struts.action.
     * ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String string = request.getParameter("careerType");
        CareerType careerType = null;
        User userView = Authenticate.getUser();

        if ((string != null)) {
            careerType = CareerType.valueOf(string);

            SiteView siteView = ReadCareers.run(careerType, userView.getUsername());

            request.setAttribute("siteView", siteView);
        }
        ActionForward actionForward = null;

        if (careerType.equals(CareerType.PROFESSIONAL)) {
            actionForward = mapping.findForward("show-professional-form");
        } else {
            actionForward = mapping.findForward("show-teaching-form");
        }
        return actionForward;
    }
}