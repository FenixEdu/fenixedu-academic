/*
 * Created on Jul 22, 2004
 *
 */
package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoObject;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrollmentContext;
import ServidorApresentacao.Action.commons.TransactionalDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author João Mota
 *  
 */
public class OptionalCurricularCoursesEnrollmentDispatchAction extends TransactionalDispatchAction {

    public ActionForward showOptionalCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        IUserView userView = SessionUtils.getUserView(request);
        Object[] args1 = { userView.getUtilizador() };
        Integer studentId = null;
        try {
            if (studentId == null) {
                InfoStudent infoStudent = (InfoStudent) ServiceUtils.executeService(userView,
                        "ReadStudentByUsername", args1);
                studentId = infoStudent.getIdInternal();
            }
            DynaValidatorForm enrollmentForm = (DynaValidatorForm) form;
            Object[] args2 = { studentId };
            InfoStudentEnrollmentContext infoStudentEnrollmentContext = (InfoStudentEnrollmentContext) ServiceUtils
                    .executeService(userView, "ShowOptionalCoursesForEnrollment", args2);

            Collections.sort(infoStudentEnrollmentContext.getStudentCurrentSemesterInfoEnrollments(),
                    new BeanComparator("infoCurricularCourse.name"));

            Integer[] enrolledInArray = buildArrayForForm(infoStudentEnrollmentContext
                    .getStudentCurrentSemesterInfoEnrollments());
            enrollmentForm.set("enrolledCurricularCoursesBefore", enrolledInArray);
            enrollmentForm.set("enrolledCurricularCoursesAfter", enrolledInArray);

            request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrollmentContext);

            request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrollmentContext);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return mapping.findForward("showOptionalCourses");
    }

    private Integer[] buildArrayForForm(List listToTransform) {
        List newList = new ArrayList();
        newList = (List) CollectionUtils.collect(listToTransform, new Transformer() {
            public Object transform(Object arg0) {
                InfoObject infoObject = (InfoObject) arg0;
                return infoObject.getIdInternal();
            }
        });
        Integer[] array = new Integer[newList.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = (Integer) newList.get(i);
        }
        return array;
    }
}