/**
 * Nov 30, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.ProfessorshipDTO;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShowTeacherCreditsDispatchAction extends FenixDispatchAction {

    protected void getAllTeacherCredits(HttpServletRequest request, ExecutionPeriod executionPeriod,
            Teacher teacher) throws ParseException {

        request.setAttribute("teacher", teacher);
        request.setAttribute("teacherCategory", teacher.getCategoryForCreditsByPeriod(executionPeriod));
        request.setAttribute("executionPeriod", executionPeriod);        

        setTeachingServicesAndSupportLessons(request, teacher, executionPeriod);

        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);        
        if (teacherService != null) {            
            setMasterDegreeServices(request, teacherService);
            setAdviseServices(request, teacherService);
            setInstitutionWorkTimes(request, teacherService);           
            request.setAttribute("otherServices", teacherService.getOtherServices());
            request.setAttribute("teacherServiceNotes", teacherService.getTeacherServiceNotes());            
        }

        List<TeacherServiceExemption> serviceExemptions = teacher
                .getServiceExemptionsWithoutMedicalSituations(executionPeriod.getBeginDateYearMonthDay(),
                        executionPeriod.getEndDateYearMonthDay());

        if (!serviceExemptions.isEmpty()) {
            Iterator orderedServiceExemptions = new OrderedIterator(serviceExemptions.iterator(),
                    new BeanComparator("start"));
            request.setAttribute("serviceExemptions", orderedServiceExemptions);
        }

        List<PersonFunction> personFuntions = teacher.getPersonFuntions(YearMonthDay
                .fromDateFields(executionPeriod.getBeginDate()), YearMonthDay
                .fromDateFields(executionPeriod.getEndDate()));

        if (!personFuntions.isEmpty()) {
            Iterator orderedPersonFuntions = new OrderedIterator(personFuntions.iterator(),
                    new BeanComparator("beginDate"));
            request.setAttribute("personFunctions", orderedPersonFuntions);
        }
            
        double managementCredits = teacher.getManagementFunctionsCredits(executionPeriod);
        double serviceExemptionCredits = teacher.getServiceExemptionCredits(executionPeriod);        
        int mandatoryLessonHours = teacher.getMandatoryLessonHours(executionPeriod);
        
        CreditLineDTO creditLineDTO = new CreditLineDTO(executionPeriod, teacherService,
                managementCredits, serviceExemptionCredits, mandatoryLessonHours, teacher);

        request.setAttribute("creditLineDTO", creditLineDTO);
    } 

    private void setMasterDegreeServices(HttpServletRequest request, TeacherService teacherService) {
        if (!teacherService.getMasterDegreeServices().isEmpty()) {
            request.setAttribute("masterDegreeServices", teacherService.getMasterDegreeServices());
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

            List<InstitutionWorkTime> institutionWorkingTimes = new ArrayList(teacherService
                    .getInstitutionWorkTimes());           
            Collections.sort(institutionWorkingTimes, comparatorChain);
            request.setAttribute("institutionWorkTimeList", institutionWorkingTimes);
        }
    }

    private void setAdviseServices(HttpServletRequest request, TeacherService teacherService) {
        if (!teacherService.getTeacherAdviseServices().isEmpty()) {
            List<TeacherAdviseService> tfcAdvises = (List<TeacherAdviseService>) CollectionUtils.select(
                    teacherService.getTeacherAdviseServices(), new Predicate() {
                        public boolean evaluate(Object arg0) {
                            TeacherAdviseService teacherAdviseService = (TeacherAdviseService) arg0;
                            return teacherAdviseService.getAdvise().getAdviseType().equals(
                                    AdviseType.FINAL_WORK_DEGREE);
                        }
                    });
            Collections.sort(tfcAdvises, new BeanComparator("advise.student.number"));                        
            request.setAttribute("adviseServices", tfcAdvises);
        }
    }
    
    protected void showLinks(HttpServletRequest request, ExecutionPeriod executionPeriod, RoleType roleType) {
        boolean showLinks = true;
        try {            
            executionPeriod.checkValidCreditsPeriod(roleType);   
        } catch (DomainException e) {
            showLinks = false;
        }
        request.setAttribute("showLinks", showLinks);
    }          
}
