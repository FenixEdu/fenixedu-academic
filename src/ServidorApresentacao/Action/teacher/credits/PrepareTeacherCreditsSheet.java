/*
 * Created on Dec 5, 2003 by jpvl
 *
 */
package ServidorApresentacao.Action.teacher.credits;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class PrepareTeacherCreditsSheet extends Action {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        Object args[] = { userView.getUtilizador() };
        InfoTeacher infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView,
                "ReadTeacherByUsername", args);
        request.setAttribute("infoTeacher", infoTeacher);

        args[0] = infoTeacher.getIdInternal();
        List infoCredits = (List) ServiceUtils.executeService(userView, "ReadTeacherCredits", args);

        request.setAttribute("infoCredits", infoCredits);

        BeanComparator descriptionComparator = new BeanComparator("infoExecutionPeriod.description");
        Collections.sort(infoCredits, descriptionComparator);
        return mapping.findForward("successfull-prepare");
    }
}