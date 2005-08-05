/**
 * Jul 26, 2005
 */

package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.equivalences;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoNotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageNotNeedToEnrollDispathAction extends DispatchAction {

    public ActionForward prepareNotNeedToEnroll(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String insert = (String) request.getParameter("insert");
        if (insert != null) {
            request.setAttribute("insert", insert);
        }

        Integer studentNumber = null;
        DynaActionForm notNeedToEnrollForm = (DynaActionForm) form;
        if (notNeedToEnrollForm.get("studentNumber") != null) {
            studentNumber = new Integer((String) notNeedToEnrollForm.get("studentNumber"));
        } else {
            studentNumber = new Integer((String) request.getAttribute("studentNumer"));
        }

        Object args[] = { studentNumber, DegreeType.DEGREE };

        InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan infoSCP = readStudentCurricularPlan(
                userView, args);

        request.setAttribute("infoStudentCurricularPlan", infoSCP);
        notNeedToEnrollForm.set("studentNumber", studentNumber.toString());

        return mapping.findForward("showNotNeedToEnroll");
    }

    /**
     * @param userView
     * @param args
     * @return
     * @throws FenixServiceException
     * @throws FenixFilterException
     */
    private InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan readStudentCurricularPlan(
            IUserView userView, Object[] args) throws FenixServiceException, FenixFilterException {
        InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan infoSCP = 
            (InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan) ServiceManagerServiceFactory
                .executeService(userView, "ReadActiveStudentCurricularPlanByNumberAndType", args);

        final List infoCurricularCourses = infoSCP.getInfoDegreeCurricularPlan().getCurricularCourses();
        final List infoCurricularCourseToRemove = (List) CollectionUtils.collect(infoSCP
                .getInfoNotNeedToEnrollCurricularCourses(), new Transformer() {
            public Object transform(Object arg0) {
                InfoNotNeedToEnrollInCurricularCourse infoNotNeedToEnrollInCurricularCourse = (InfoNotNeedToEnrollInCurricularCourse) arg0;
                return infoNotNeedToEnrollInCurricularCourse.getInfoCurricularCourse();
            }
        });

        infoCurricularCourses.removeAll(infoCurricularCourseToRemove);
        return infoSCP;
    }

    public ActionForward insertNotNeedToEnroll(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        DynaActionForm notNeedToEnrollForm = (DynaActionForm) form;
        Integer[] curricularCoursesID = (Integer[]) notNeedToEnrollForm.get("curricularCoursesID");
        Integer studentCurricularPlanID = new Integer((String) notNeedToEnrollForm
                .get("studentCurricularPlanID"));

        Object[] args = { studentCurricularPlanID, curricularCoursesID };
        ServiceManagerServiceFactory.executeService(userView,
                "InsertNotNeedToEnrollInCurricularCourses", args);

        request.setAttribute("insert", "insert");

        return mapping.findForward("insertNotNeedToEnroll");
    }

    public ActionForward deleteNotNeedToEnroll(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer notNeedToEnrollID = new Integer((String) request.getParameter("notNeedToEnrollID"));
        Object[] args = { notNeedToEnrollID };
        ServiceManagerServiceFactory.executeService(userView, "DeleteNotNeedToEnrollInCurricularCourse",
                args);
        
        Integer studentNumber = new Integer((String) request.getParameter("studentNumber"));
       
        Object[] args1 = { studentNumber, DegreeType.DEGREE };
        InfoStudentCurricularPlanWithEquivalencesAndInfoDegreeCurricularPlan infoSCP = readStudentCurricularPlan(
                userView, args1);

        request.setAttribute("infoStudentCurricularPlan", infoSCP);
        

        return mapping.findForward("showNotNeedToEnroll");
    }
}
