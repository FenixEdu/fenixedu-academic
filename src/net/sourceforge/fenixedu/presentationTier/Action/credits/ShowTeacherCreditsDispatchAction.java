/**
 * Nov 30, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.ProfessorshipDTO;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShowTeacherCreditsDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("method", "showTeacherCredits");
        return mapping.findForward("search-teacher-form");
    }

    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm teacherCreditsForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);
        ExecutionPeriod executionPeriod = (ExecutionPeriod) ServiceUtils.executeService(userView,
                "ReadDomainExecutionPeriodByOID", new Object[] { (Integer) teacherCreditsForm
                        .get("executionPeriodId") });

        Integer teacherID = (Integer) teacherCreditsForm.get("teacherId");
        Teacher teacher = null;
        if (teacherID == null || teacherID == 0) {
            Integer teacherNumber = Integer.valueOf(teacherCreditsForm.getString("teacherNumber"));
            List<Department> manageableDepartments = userView.getPerson()
                    .getManageableDepartmentCredits();
            for (Department department : manageableDepartments) {
                teacher = department.getTeacherByPeriod(teacherNumber, executionPeriod.getBeginDate(),
                        executionPeriod.getEndDate());
                if (teacher != null) {
                    break;
                }
            }
            if (teacher == null) {
                request.setAttribute("teacherNotFound", "teacherNotFound");
                return mapping.findForward("teacher-not-found");
            }
        } else {
            teacher = (Teacher) ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "ReadDomainTeacherByOID", new Object[] { teacherID });
        }
        
        request.setAttribute("teacher", teacher);
        request.setAttribute("executionPeriod", executionPeriod);
        setTeachingServicesAndSupportLessons(request, teacher, executionPeriod);

        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService != null) {
            setMasterDegreeServices(request, teacherService);
            setAdviseServices(request, teacherService);
            setInstitutionWorkTimes(request, teacherService);
            request.setAttribute("otherServices", teacherService.getOtherServices());
        }

        List<TeacherServiceExemption> serviceExemptions = teacher.getServiceExemptionSituations(
                executionPeriod.getBeginDate(), executionPeriod.getEndDate());
        if (!serviceExemptions.isEmpty()) {
            Iterator orderedServiceExemptions = new OrderedIterator(serviceExemptions.iterator(),
                    new BeanComparator("start"));
            request.setAttribute("serviceExemptions", orderedServiceExemptions);
        }
        List<PersonFunction> personFuntions = teacher.getPersonFuntions(executionPeriod.getBeginDate(),
                executionPeriod.getEndDate());
        if (!personFuntions.isEmpty()) {
            Iterator orderedPersonFuntions = new OrderedIterator(personFuntions.iterator(),
                    new BeanComparator("beginDate"));
            request.setAttribute("personFunctions", orderedPersonFuntions);
        }

        List<Category> categories = (List<Category>) ServiceUtils.executeService(userView,
                "ReadAllDomainObjects", new Object[] { Category.class });
        List<Category> monitorCategories = (List<Category>) CollectionUtils.select(categories, new Predicate(){
            public boolean evaluate(Object object) {
                Category category = (Category) object;
                return category.getCode().equals("MNL") || category.getCode().equals("MNT");
            }});
        double managementCredits = teacher.getManagementFunctionsCredits(executionPeriod);
        double serviceExemptionCredits = teacher.getServiceExemptionCredits(executionPeriod);
        int mandatoryLessonHours = 0;
        Category category = teacher.getCategoryForCreditsByPeriod(executionPeriod);
        if(!monitorCategories.contains(category)){
            mandatoryLessonHours = teacher.getMandatoryLessonHours(executionPeriod);
        }
        CreditLineDTO creditLineDTO = new CreditLineDTO(executionPeriod, teacherService,
                managementCredits, serviceExemptionCredits, mandatoryLessonHours);
        request.setAttribute("creditLineDTO", creditLineDTO);
        return mapping.findForward("show-teacher-credits");
    }

    /**
     * @param request
     * @param teacherService
     */
    private void setMasterDegreeServices(HttpServletRequest request, TeacherService teacherService) {
        if(!teacherService.getMasterDegreeServices().isEmpty()){
            request.setAttribute("masterDegreeServices",teacherService.getMasterDegreeServices());
        }        
    }

    private void setTeachingServicesAndSupportLessons(HttpServletRequest request, Teacher teacher,
            ExecutionPeriod executionPeriod) {
        List<Professorship> professorships = teacher
                .getDegreeProfessorshipsByExecutionPeriod(executionPeriod);

        List<ProfessorshipDTO> professorshipDTOs = (List<ProfessorshipDTO>) CollectionUtils.collect(
                professorships, new Transformer() {
                    public Object transform(Object arg0) {
                        Professorship professorship = (Professorship) arg0;
                        return new ProfessorshipDTO(professorship);
                    }
                });

        if (!professorshipDTOs.isEmpty()) {
            BeanComparator comparator = new BeanComparator("professorship.executionCourse.nome");
            Iterator orderedProfessorshipsIter = new OrderedIterator(professorshipDTOs.iterator(),
                    comparator);
            request.setAttribute("professorshipDTOs", orderedProfessorshipsIter);
        }
    }

    private void setInstitutionWorkTimes(HttpServletRequest request, TeacherService teacherService) {
        if (!teacherService.getInstitutionWorkTimes().isEmpty()) {
            ComparatorChain comparatorChain = new ComparatorChain();
            BeanComparator weekDayComparator = new BeanComparator("weekDay");
            BeanComparator startTimeComparator = new BeanComparator("startTime");
            comparatorChain.addComparator(weekDayComparator);
            comparatorChain.addComparator(startTimeComparator);

            Iterator institutionWorkTimeIterator = new OrderedIterator(teacherService
                    .getInstitutionWorkTimes().iterator(), comparatorChain);
            request.setAttribute("institutionWorkTimeList", institutionWorkTimeIterator);
        }
    }

    private void setAdviseServices(HttpServletRequest request, TeacherService teacherService) {
        if (!teacherService.getTeacherAdviseServices().isEmpty()) {
            List<TeacherAdviseService> tfcAdvises = (List<TeacherAdviseService>) CollectionUtils
                    .select(teacherService.getTeacherAdviseServices(), new Predicate() {
                        public boolean evaluate(Object arg0) {
                            TeacherAdviseService teacherAdviseService = (TeacherAdviseService) arg0;
                            return teacherAdviseService.getAdvise().getAdviseType().equals(
                                    AdviseType.FINAL_WORK_DEGREE);
                        }
                    });
            BeanComparator comparator = new BeanComparator("advise.student.number");
            Iterator orderedAdviseServicesIter = new OrderedIterator(tfcAdvises.iterator(), comparator);
            request.setAttribute("adviseServices", orderedAdviseServicesIter);
        }
    }
}
