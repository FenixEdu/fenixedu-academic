package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.equivalences;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoNotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ManageNotNeedToEnrollDispathAction extends FenixDispatchAction {

    private static final ComparatorChain curricularCourseComparator = new ComparatorChain();
    static {
        curricularCourseComparator.addComparator(new BeanComparator("name", Collator.getInstance()));
        curricularCourseComparator.addComparator(new BeanComparator("code", Collator.getInstance()));
    }
    private static final ComparatorChain enrolmentCurricularCourseComparator = new ComparatorChain();
    static {
        enrolmentCurricularCourseComparator.addComparator(new BeanComparator("infoCurricularCourse.name", Collator.getInstance()));
        enrolmentCurricularCourseComparator.addComparator(new BeanComparator("infoCurricularCourse.code", Collator.getInstance()));
    }
    
    public ActionForward prepareNotNeedToEnroll(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final String insert = (String) request.getParameter("insert");
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

        final Object args[] = { studentNumber, DegreeType.DEGREE };
        InfoStudentCurricularPlan infoSCP = readStudentCurricularPlan(getUserView(request), args);
        
        final List<InfoNotNeedToEnrollInCurricularCourse> infoNotNeedToEnrollCurricularCourses = infoSCP
		.getInfoNotNeedToEnrollCurricularCourses();
	Collections.sort(infoNotNeedToEnrollCurricularCourses, enrolmentCurricularCourseComparator);
        
        final List<InfoCurricularCourse> curricularCourses = infoSCP.getInfoDegreeCurricularPlan().getCurricularCourses();
        Collections.sort(curricularCourses, curricularCourseComparator);

        request.setAttribute("infoStudentCurricularPlan", infoSCP);
        request.setAttribute("infoNotNeedToEnrollCurricularCourses", infoNotNeedToEnrollCurricularCourses);
        request.setAttribute("infoDegreeCurricularPlanCurricularCourses", curricularCourses);
        notNeedToEnrollForm.set("studentNumber", studentNumber.toString());

        return mapping.findForward("showNotNeedToEnroll");
    }

    private InfoStudentCurricularPlan readStudentCurricularPlan(
            IUserView userView, Object[] args) throws FenixServiceException, FenixFilterException {
        InfoStudentCurricularPlan infoSCP = 
            (InfoStudentCurricularPlan) ServiceManagerServiceFactory
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

        DynaActionForm notNeedToEnrollForm = (DynaActionForm) form;
        Integer[] curricularCoursesID = (Integer[]) notNeedToEnrollForm.get("curricularCoursesID");
        Integer studentCurricularPlanID = new Integer((String) notNeedToEnrollForm
                .get("studentCurricularPlanID"));

        Object[] args = { studentCurricularPlanID, curricularCoursesID };
        ServiceManagerServiceFactory.executeService(getUserView(request),
                "InsertNotNeedToEnrollInCurricularCourses", args);

        request.setAttribute("insert", "insert");

        return mapping.findForward("insertNotNeedToEnroll");
    }

    public ActionForward deleteNotNeedToEnroll(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final IUserView userView = getUserView(request);

        final Object[] args = { Integer.valueOf((String) request.getParameter("notNeedToEnrollID")) };
        ServiceManagerServiceFactory.executeService(userView, "DeleteNotNeedToEnrollInCurricularCourse", args);
        

        return prepareNotNeedToEnroll(mapping, form, request, response);
    }
}
