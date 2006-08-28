package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Tânia Pousão
 *  
 */
public class MarksListAction extends FenixDispatchAction {

    public ActionForward loadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer executionCourseCode = getFromRequest("objectCode", request);

        Integer evaluationCode = getFromRequest("evaluationCode", request);

        ISiteComponent commonComponent = new InfoSiteCommon();
        Object[] args = { executionCourseCode, commonComponent, new InfoEvaluation(), null,
                evaluationCode, null };

        try {
            TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) ServiceUtils
                    .executeService(userView, "TeacherAdministrationSiteComponentService", args);

            request.setAttribute("siteView", siteView);
            request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent())
                    .getExecutionCourse().getIdInternal());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("evaluationCode", evaluationCode);

        return mapping.findForward("loadMarks");

    }

    /**
     * @author Tânia Pousão
     *  
     */
    public ActionForward loadMarksOnline(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer executionCourseCode = getFromRequest("objectCode", request);

        Integer evaluationCode = getFromRequest("evaluationCode", request);

        Object[] args = { executionCourseCode, evaluationCode };

        TeacherAdministrationSiteView siteView = null;

        try {
            siteView = (TeacherAdministrationSiteView) ServiceUtils.executeService(userView,
                    "ReadStudentsAndMarksByEvaluation", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
        Collections.sort(infoSiteMarks.getInfoAttends(), new BeanComparator("aluno.number"));

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", executionCourseCode);
        request.setAttribute("evaluationCode", evaluationCode);

        return mapping.findForward("marksList");
    }

    /**
     * @author Fernanda Quitério
     *  
     */
    public ActionForward preparePublishMarks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        Integer evaluationCode = getFromRequest("evaluationCode", request);

        Integer infoExecutionCourseCode = getFromRequest("objectCode", request);

        ISiteComponent commonComponent = new InfoSiteCommon();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object[] args = { infoExecutionCourseCode, commonComponent, new InfoEvaluation(), null,
                evaluationCode, null };
        TeacherAdministrationSiteView siteView = null;
        try {
            siteView = (TeacherAdministrationSiteView) ServiceUtils.executeService(userView,
                    "TeacherAdministrationSiteComponentService", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", infoExecutionCourseCode);
        request.setAttribute("evaluationCode", evaluationCode);

        return mapping.findForward("preparePublishMarks");
    }

    /**
     * @author Fernanda Quitério
     *  
     */
    public ActionForward publishMarks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        Integer evaluationCode = getFromRequest("evaluationCode", request);

        Integer objectCode = getFromRequest("objectCode", request);

        DynaValidatorForm publishForm = (DynaValidatorForm) form;
        String publishmentMessage = (String) publishForm.get("publishmentMessage");
        Boolean sendSMS = (Boolean) publishForm.get("sendSMS");

        String announcementTitle = null;
        if (publishmentMessage != null && publishmentMessage.length() > 0) {
            MessageResources messages = getResources(request, "DEFAULT");
            announcementTitle = messages.getMessage("message.publishment");
        }

        Object[] args = { objectCode, evaluationCode, publishmentMessage, sendSMS, announcementTitle };
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        try {
            ServiceUtils.executeService(userView, "PublishMarks", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        request.setAttribute("objectCode", objectCode);

        return mapping.findForward("viewMarksOptions");
    }





    private Integer getFromRequest(String parameter, HttpServletRequest request) {
        Integer parameterCode = null;
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = request.getAttribute(parameter).toString();
        }
        if (parameterCodeString != null) {
            parameterCode = new Integer(parameterCodeString);
        }
        return parameterCode;

    }
}