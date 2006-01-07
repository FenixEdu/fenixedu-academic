/**
 * Dec 20, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.dataTransferObject.credits.TeacherWithCreditsDTO;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShowTeachersCreditsDepartmentListAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException,
            FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        Integer executionPeriodID = Integer.valueOf(request.getParameter("executionPeriodId"));
        ExecutionPeriod executionPeriod = (ExecutionPeriod) ServiceUtils.executeService(userView,
                "ReadDomainExecutionPeriodByOID", new Object[] { executionPeriodID });
        List<Category> categories = (List<Category>) ServiceUtils.executeService(userView,
                "ReadAllDomainObjects", new Object[] { Category.class });
        List<Category> monitorCategories = (List<Category>) CollectionUtils.select(categories, new Predicate(){
            public boolean evaluate(Object object) {
                Category category = (Category) object;
                return category.getCode().equals("MNL") || category.getCode().equals("MNT");
            }});
        
        List<TeacherWithCreditsDTO> teachersCredits = new ArrayList<TeacherWithCreditsDTO>();
        for (Department department : userView.getPerson().getManageableDepartmentCredits()) {
            List<Teacher> teachers = department.getTeachers(executionPeriod.getBeginDate(),
                    executionPeriod.getEndDate());
            for (Teacher teacher : teachers) {
                double managementCredits = teacher.getManagementFunctionsCredits(executionPeriod);
                double serviceExemptionsCredits = teacher.getServiceExemptionCredits(executionPeriod);
                int mandatoryLessonHours = 0;
                Category category = teacher.getCategoryByPeriod(executionPeriod.getBeginDate(),
                        executionPeriod.getEndDate());
                if(!monitorCategories.contains(category)){
                    mandatoryLessonHours = teacher.getHoursByCategory(executionPeriod.getBeginDate(),
                            executionPeriod.getEndDate());
                }
                TeacherService teacherService = teacher
                        .getTeacherServiceByExecutionPeriod(executionPeriod);
                CreditLineDTO creditLineDTO = new CreditLineDTO(executionPeriod, teacherService,
                        managementCredits, serviceExemptionsCredits, mandatoryLessonHours);
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
        request.setAttribute("executionPeriodId", executionPeriodID);
        return mapping.findForward("show-teachers-credits-list");
    }

    private Iterator orderList(String sortBy, Iterator<TeacherWithCreditsDTO> iterator) {
        Iterator orderedIterator = null;
        if (sortBy == null || sortBy.length() == 0 || sortBy.equals("name")) {
            orderedIterator = new OrderedIterator(iterator, new BeanComparator("teacher.person.nome"));
        } else {
            orderedIterator = new OrderedIterator(iterator, new BeanComparator("teacher.teacherNumber"));
        }
        return orderedIterator;
    }
}
