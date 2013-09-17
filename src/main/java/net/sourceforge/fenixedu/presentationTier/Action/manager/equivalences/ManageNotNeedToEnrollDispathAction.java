package net.sourceforge.fenixedu.presentationTier.Action.manager.equivalences;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.equivalences.DeleteNotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.equivalences.InsertNotNeedToEnrollInCurricularCourses;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoNotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.student.ChooseStudentCurricularPlanBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixframework.FenixFramework;

public class ManageNotNeedToEnrollDispathAction extends FenixDispatchAction {

    private static final ComparatorChain curricularCourseComparator = new ComparatorChain();
    static {
        curricularCourseComparator.addComparator(new BeanComparator("name", Collator.getInstance()));
        curricularCourseComparator.addComparator(new BeanComparator("code", Collator.getInstance()));
    }

    private static final ComparatorChain enrolmentCurricularCourseComparator = new ComparatorChain();
    static {
        enrolmentCurricularCourseComparator
                .addComparator(new BeanComparator("infoCurricularCourse.name", Collator.getInstance()));
        enrolmentCurricularCourseComparator
                .addComparator(new BeanComparator("infoCurricularCourse.code", Collator.getInstance()));
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ChooseStudentCurricularPlanBean bean = getRenderedObject();
        request.setAttribute("chooseSCPBean", bean == null ? new ChooseStudentCurricularPlanBean() : bean);

        if (bean != null && bean.getStudentCurricularPlan() != null) {
            if (bean.getStudentCurricularPlan().isBoxStructure()) {
                addActionMessage(request, "error.ManageNotNeedToEnrollDispathAction.cannot.manage.studentCurricularPlan");
            } else {
                return showNotNeedToEnroll(mapping, form, request, response);
            }
        }

        return mapping.findForward("showNotNeedToEnroll");
    }

    public ActionForward showNotNeedToEnroll(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ChooseStudentCurricularPlanBean bean = getRenderedObject();

        InfoStudentCurricularPlan infoSCP = readStudentCurricularPlan(bean.getStudentCurricularPlan());

        final List<InfoNotNeedToEnrollInCurricularCourse> infoNotNeedToEnrollCurricularCourses =
                infoSCP.getInfoNotNeedToEnrollCurricularCourses();
        Collections.sort(infoNotNeedToEnrollCurricularCourses, enrolmentCurricularCourseComparator);

        final List<InfoCurricularCourse> curricularCourses = infoSCP.getInfoDegreeCurricularPlan().getCurricularCourses();
        Collections.sort(curricularCourses, curricularCourseComparator);

        request.setAttribute("infoStudentCurricularPlan", infoSCP);
        request.setAttribute("infoNotNeedToEnrollCurricularCourses", infoNotNeedToEnrollCurricularCourses);
        request.setAttribute("infoDegreeCurricularPlanCurricularCourses", curricularCourses);

        request.setAttribute("chooseSCPBean", bean);

        return mapping.findForward("showNotNeedToEnroll");
    }

    public ActionForward prepareNotNeedToEnroll(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String insert = request.getParameter("insert");
        if (insert != null) {
            request.setAttribute("insert", insert);
        }

        Integer studentNumber = null;
        DynaActionForm notNeedToEnrollForm = (DynaActionForm) form;

        String scpID = getStringFromRequest(request, "scpID");
        if (scpID == null) {
            scpID =
                    notNeedToEnrollForm.get("studentCurricularPlanID").equals("") ? null : (String) notNeedToEnrollForm
                            .get("studentCurricularPlanID");
        }

        StudentCurricularPlan studentCurricularPlan = FenixFramework.getDomainObject(scpID);
        InfoStudentCurricularPlan infoSCP = InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);

        final List<InfoNotNeedToEnrollInCurricularCourse> infoNotNeedToEnrollCurricularCourses =
                infoSCP.getInfoNotNeedToEnrollCurricularCourses();
        Collections.sort(infoNotNeedToEnrollCurricularCourses, enrolmentCurricularCourseComparator);

        final List<InfoCurricularCourse> curricularCourses = infoSCP.getInfoDegreeCurricularPlan().getCurricularCourses();
        Collections.sort(curricularCourses, curricularCourseComparator);

        request.setAttribute("infoStudentCurricularPlan", infoSCP);
        request.setAttribute("infoNotNeedToEnrollCurricularCourses", infoNotNeedToEnrollCurricularCourses);
        request.setAttribute("infoDegreeCurricularPlanCurricularCourses", curricularCourses);
        notNeedToEnrollForm.set("studentCurricularPlanID", scpID);

        ChooseStudentCurricularPlanBean bean = getRenderedObject();
        request.setAttribute("chooseSCPBean", bean == null ? new ChooseStudentCurricularPlanBean(studentCurricularPlan) : bean);

        return mapping.findForward("showNotNeedToEnroll");
    }

    private InfoStudentCurricularPlan readStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan)
            throws FenixServiceException {

        InfoStudentCurricularPlan infoSCP = InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);

        final List infoCurricularCourses = infoSCP.getInfoDegreeCurricularPlan().getCurricularCourses();
        final List infoCurricularCourseToRemove =
                (List) CollectionUtils.collect(infoSCP.getInfoNotNeedToEnrollCurricularCourses(), new Transformer() {
                    @Override
                    public Object transform(Object arg0) {
                        InfoNotNeedToEnrollInCurricularCourse infoNotNeedToEnrollInCurricularCourse =
                                (InfoNotNeedToEnrollInCurricularCourse) arg0;
                        return infoNotNeedToEnrollInCurricularCourse.getInfoCurricularCourse();
                    }
                });

        infoCurricularCourses.removeAll(infoCurricularCourseToRemove);
        return infoSCP;
    }

    public ActionForward insertNotNeedToEnroll(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm notNeedToEnrollForm = (DynaActionForm) form;
        String[] curricularCoursesID = (String[]) notNeedToEnrollForm.get("curricularCoursesID");
        String studentCurricularPlanID = (String) notNeedToEnrollForm.get("studentCurricularPlanID");

        InsertNotNeedToEnrollInCurricularCourses.runInsertNotNeedToEnrollInCurricularCourses(studentCurricularPlanID,
                curricularCoursesID);

        request.setAttribute("insert", "insert");
        request.setAttribute("scpID", studentCurricularPlanID);

        return mapping.findForward("insertNotNeedToEnroll");
    }

    public ActionForward deleteNotNeedToEnroll(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DeleteNotNeedToEnrollInCurricularCourse.runDeleteNotNeedToEnrollInCurricularCourse(request
                .getParameter("notNeedToEnrollID"));

        return prepareNotNeedToEnroll(mapping, form, request, response);
    }
}
