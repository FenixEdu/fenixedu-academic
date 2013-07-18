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
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ReadTeacherInformationAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);

        SiteView siteView =
                ReadTeacherInformation.runReadTeacherInformation(request.getParameter("username"),
                        request.getParameter("executionYear"));
        InfoSiteTeacherInformation infoSiteTeacherInformation = (InfoSiteTeacherInformation) siteView.getComponent();
        request.setAttribute("infoSiteTeacherInformation", infoSiteTeacherInformation);

        return mapping.findForward("show");
    }
}