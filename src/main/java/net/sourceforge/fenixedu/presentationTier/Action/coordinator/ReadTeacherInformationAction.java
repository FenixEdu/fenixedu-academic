/*
 * Created on Nov 15, 2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ReadTeacherInformation;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
@Mapping(module = "coordinator", path = "/readTeacherInformation", functionality = DegreeCoordinatorIndex.class)
@Forwards(@Forward(name = "show", path = "/teacher/information/viewTeacherInformation.jsp"))
public class ReadTeacherInformationAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);

        SiteView siteView =
                ReadTeacherInformation.runReadTeacherInformation(request.getParameter("username"),
                        request.getParameter("executionYear"));
        InfoSiteTeacherInformation infoSiteTeacherInformation = (InfoSiteTeacherInformation) siteView.getComponent();
        request.setAttribute("infoSiteTeacherInformation", infoSiteTeacherInformation);

        return mapping.findForward("show");
    }
}