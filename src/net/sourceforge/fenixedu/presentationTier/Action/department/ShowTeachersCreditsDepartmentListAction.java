/**
 * Dec 20, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.dataTransferObject.credits.TeacherWithCreditsDTO;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShowTeachersCreditsDepartmentListAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException,
            FenixServiceException, ParseException {

        DynaActionForm dynaActionForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer executionPeriodID = (Integer) dynaActionForm.get("executionPeriodId");

        ExecutionPeriod executionPeriod = null;
        if (executionPeriodID == null) {
            executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        } else {
            executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);            
        }

        dynaActionForm.set("executionPeriodId", executionPeriod.getIdInternal());
        Collection<Category> categories = rootDomainObject.getCategorys();

        List<Category> monitorCategories = (List<Category>) CollectionUtils.select(categories,
                new Predicate() {
                    public boolean evaluate(Object object) {
                        Category category = (Category) object;
                        return category.getCode().equals("MNL") || category.getCode().equals("MNT");
                    }
                });

        List<TeacherWithCreditsDTO> teachersCredits = new ArrayList<TeacherWithCreditsDTO>();
        for (Department department : userView.getPerson().getManageableDepartmentCredits()) {
            
            List<Teacher> teachers = department.getAllTeachers(executionPeriod.getBeginDateYearMonthDay(),
                    executionPeriod.getEndDateYearMonthDay());
                    
            for (Teacher teacher : teachers) {
                double managementCredits = teacher.getManagementFunctionsCredits(executionPeriod);
                double serviceExemptionsCredits = teacher.getServiceExemptionCredits(executionPeriod);
                int mandatoryLessonHours = 0;                
                Category category = teacher.getCategoryForCreditsByPeriod(executionPeriod);
                if (category != null && !monitorCategories.contains(category)) {
                    mandatoryLessonHours = teacher.getMandatoryLessonHours(executionPeriod);
                }
                TeacherService teacherService = teacher
                        .getTeacherServiceByExecutionPeriod(executionPeriod);
                CreditLineDTO creditLineDTO = new CreditLineDTO(executionPeriod, teacherService,
                        managementCredits, serviceExemptionsCredits, mandatoryLessonHours, teacher);
                TeacherWithCreditsDTO teacherWithCreditsDTO = new TeacherWithCreditsDTO(teacher,
                        category, creditLineDTO);
                teachersCredits.add(teacherWithCreditsDTO);
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
            orderedIterator = new OrderedIterator(iterator, new BeanComparator("teacher.teacherNumber"));
        }
        return orderedIterator;
    }

    private void readAndSaveAllExecutionPeriods(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {
        List<InfoExecutionPeriod> notClosedExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
        Object[] args = {};

        notClosedExecutionPeriods = (List<InfoExecutionPeriod>) ServiceManagerServiceFactory.executeService(
                null, "ReadNotClosedExecutionPeriods", args);

        List<LabelValueBean> executionPeriods = getNotClosedExecutionPeriods(notClosedExecutionPeriods);
        request.setAttribute("executionPeriods", executionPeriods);
    }

    private List<LabelValueBean> getNotClosedExecutionPeriods(
            List<InfoExecutionPeriod> allExecutionPeriods) {
        List<LabelValueBean> executionPeriods = new ArrayList<LabelValueBean>();
        for (InfoExecutionPeriod infoExecutionPeriod : allExecutionPeriods) {
            LabelValueBean labelValueBean = new LabelValueBean();
            labelValueBean.setLabel(infoExecutionPeriod.getInfoExecutionYear().getYear() + " - "
                    + infoExecutionPeriod.getSemester() + "º Semestre");
            labelValueBean.setValue(infoExecutionPeriod.getIdInternal().toString());
            executionPeriods.add(labelValueBean);
        }
        Collections.sort(executionPeriods, new BeanComparator("label"));
        return executionPeriods;
    }
}
