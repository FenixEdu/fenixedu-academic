/*
 * Created on Dec 22, 2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.gesdis.ReadCourseInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
@Mapping(module = "gep", path = "/readCourseInformation", scope = "request")
@Forwards(value = { @Forward(name = "show", path = "/gep/courses/viewCourseInformation.jsp") })
public class ReadCourseInformationAction extends FenixAction {
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
        IUserView userView = UserView.getUser();

        TeacherAdministrationSiteView teacherAdministrationSiteView =
                ReadCourseInformation.runReadCourseInformation(new Integer(request.getParameter("executionCourseId")));
        InfoSiteCourseInformation infoSiteCourseInformation =
                (InfoSiteCourseInformation) teacherAdministrationSiteView.getComponent();
        request.setAttribute("infoSiteCourseInformation", infoSiteCourseInformation);

        if (request.getParameter("executionDegreeId") != null) {

            InfoExecutionDegree infoExecutionDegree =
                    ReadExecutionDegreeByOID.run(new Integer(request.getParameter("executionDegreeId")));
            request.setAttribute("infoExecutionDegree", infoExecutionDegree);
        }

        return mapping.findForward("show");
    }
}