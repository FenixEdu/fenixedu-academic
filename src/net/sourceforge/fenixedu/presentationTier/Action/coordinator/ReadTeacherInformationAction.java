/*
 * Created on Nov 15, 2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PublicationArea;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ReadTeacherInformationAction extends FenixAction {
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        Object[] args = { request.getParameter("username"), request.getParameter("executionYear") };
        SiteView siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTeacherInformation",
                args);
        InfoSiteTeacherInformation infoSiteTeacherInformation = (InfoSiteTeacherInformation) siteView
                .getComponent();
        request.setAttribute("infoSiteTeacherInformation", infoSiteTeacherInformation);
        
        /* 
         * This code is to replace PublicationTeacher by ResultTeacher.
         * Sergio Patricio & Luis Santos
        */
        /*
        List<ResultTeacher> teacherResults = getUserView(request).getPerson().getTeacher()
                .getTeacherResults();
        List<Result> didaticResults = new ArrayList<Result>();
        List<Result> cientificResults = new ArrayList<Result>();
        for (ResultTeacher resultTeacher : teacherResults) {
            if (resultTeacher.getPublicationArea().equals(PublicationArea.DIDATIC))
                didaticResults.add(resultTeacher.getResult());
            else
                // PublicationArea.CIENTIFIC
                cientificResults.add(resultTeacher.getResult());
        }
        request.setAttribute("didaticResults",didaticResults);
        request.setAttribute("cientificResults",cientificResults);*/
        
        /*END modification*/
        
        return mapping.findForward("show");
    }
}