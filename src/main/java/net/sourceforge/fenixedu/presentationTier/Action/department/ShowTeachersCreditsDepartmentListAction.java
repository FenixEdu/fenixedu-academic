/**
 * Dec 20, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.dataTransferObject.credits.TeacherWithCreditsDTO;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherCredits;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Ricardo Rodrigues
 * 
 */

@Mapping(module = "departmentAdmOffice", path = "/prepareListDepartmentTeachersCredits", attribute = "executionPeriodForm",
        formBean = "executionPeriodForm", scope = "session")
@Forwards(value = { @Forward(name = "show-teachers-credits-list", path = "show-teachers-credits-list") })
public class ShowTeachersCreditsDepartmentListAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws NumberFormatException,  FenixServiceException, ParseException {

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        IUserView userView = UserView.getUser();

        Integer executionPeriodID = (Integer) dynaActionForm.get("executionPeriodId");

        ExecutionSemester executionSemester = null;
        if (executionPeriodID == null) {
            executionSemester = ExecutionSemester.readLastExecutionSemesterForCredits();
        } else {
            executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);
        }

        dynaActionForm.set("executionPeriodId", executionSemester.getExternalId());

        List<TeacherWithCreditsDTO> teachersCredits = new ArrayList<TeacherWithCreditsDTO>();
        for (Department department : userView.getPerson().getManageableDepartmentCredits()) {
            List<Teacher> teachers =
                    department.getAllTeachers(executionSemester.getBeginDateYearMonthDay(),
                            executionSemester.getEndDateYearMonthDay());
            for (Teacher teacher : teachers) {
                TeacherCredits teacherCredits = TeacherCredits.readTeacherCredits(executionSemester, teacher);
                if (teacherCredits == null || teacherCredits.getTeacherCreditsState().isOpenState()) {
                    double managementCredits = teacher.getManagementFunctionsCredits(executionSemester);
                    double serviceExemptionsCredits = teacher.getServiceExemptionCredits(executionSemester);
                    double thesesCredits = teacher.getThesesCredits(executionSemester);
                    ProfessionalCategory categoryByPeriod = teacher.getCategoryByPeriod(executionSemester);
                    String category = categoryByPeriod != null ? categoryByPeriod.getName().getContent() : null;
                    double mandatoryLessonHours = teacher.getMandatoryLessonHours(executionSemester);
                    TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
                    CreditLineDTO creditLineDTO =
                            new CreditLineDTO(executionSemester, teacherService, managementCredits, serviceExemptionsCredits,
                                    mandatoryLessonHours, teacher, thesesCredits);
                    TeacherWithCreditsDTO teacherWithCreditsDTO = new TeacherWithCreditsDTO(teacher, category, creditLineDTO);
                    teachersCredits.add(teacherWithCreditsDTO);
                } else if (teacherCredits.getTeacherCreditsState().isCloseState()) {
                    CreditLineDTO creditLineDTO = new CreditLineDTO(executionSemester, teacherCredits);
                    String categopry =
                            teacherCredits.getProfessionalCategory() != null ? teacherCredits.getProfessionalCategory().getName()
                                    .getContent() : null;
                    TeacherWithCreditsDTO teacherWithCreditsDTO = new TeacherWithCreditsDTO(teacher, categopry, creditLineDTO);
                    teachersCredits.add(teacherWithCreditsDTO);
                }

            }
        }
        String sortBy = request.getParameter("sortBy");
        request.setAttribute("teachersCreditsListSize", teachersCredits.size());
        Iterator orderedTeacherCredits = orderList(sortBy, teachersCredits.iterator());
        request.setAttribute("departmentsList", userView.getPerson().getManageableDepartmentCredits());
        request.setAttribute("teachersCreditsList", orderedTeacherCredits);

        readAndSaveAllExecutionPeriods(request);
        return mapping.findForward("show-teachers-credits-list");
    }

    private Iterator orderList(String sortBy, Iterator<TeacherWithCreditsDTO> iterator) {
        Iterator orderedIterator = null;
        if (sortBy == null || sortBy.length() == 0 || sortBy.equals("name")) {
            orderedIterator = new OrderedIterator(iterator, new BeanComparator("teacher.person.name"));
        } else {
            orderedIterator = new OrderedIterator(iterator, new BeanComparator("teacher.person.istUsername"));
        }
        return orderedIterator;
    }

    private void readAndSaveAllExecutionPeriods(HttpServletRequest request) throws  FenixServiceException {
        List<InfoExecutionPeriod> notClosedExecutionPeriods = new ArrayList<InfoExecutionPeriod>();

        notClosedExecutionPeriods = ReadNotClosedExecutionPeriods.run();

        List<LabelValueBean> executionPeriods = getNotClosedExecutionPeriods(notClosedExecutionPeriods);
        request.setAttribute("executionPeriods", executionPeriods);
    }

    private List<LabelValueBean> getNotClosedExecutionPeriods(List<InfoExecutionPeriod> allExecutionPeriods) {
        List<LabelValueBean> executionPeriods = new ArrayList<LabelValueBean>();
        ExecutionSemester lastExecutionSemester = ExecutionSemester.readLastExecutionSemesterForCredits();

        for (InfoExecutionPeriod infoExecutionPeriod : allExecutionPeriods) {
            if (infoExecutionPeriod.getInfoExecutionYear().getExecutionYear()
                    .isBeforeOrEquals(lastExecutionSemester.getExecutionYear())) {
                LabelValueBean labelValueBean = new LabelValueBean();
                labelValueBean.setLabel(infoExecutionPeriod.getInfoExecutionYear().getYear() + " - "
                        + infoExecutionPeriod.getSemester() + "º Semestre");
                labelValueBean.setValue(infoExecutionPeriod.getExternalId().toString());
                executionPeriods.add(labelValueBean);
            }
        }
        Collections.sort(executionPeriods, new BeanComparator("label"));
        return executionPeriods;
    }
}