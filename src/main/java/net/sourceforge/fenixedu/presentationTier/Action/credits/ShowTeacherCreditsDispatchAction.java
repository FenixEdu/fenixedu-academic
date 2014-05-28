/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Nov 30, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.credits.ReadAllTeacherCredits;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShowTeacherCreditsDispatchAction extends FenixDispatchAction {

    protected void getAllTeacherCredits(HttpServletRequest request, ExecutionSemester executionSemester, Teacher teacher)
            throws ParseException {
        getRequestAllTeacherCredits(request, executionSemester, teacher);
        CreditLineDTO creditLineDTO = ReadAllTeacherCredits.readCreditLineDTO(executionSemester, teacher);
        request.setAttribute("creditLineDTO", creditLineDTO);
    }

    private void setMasterDegreeServices(HttpServletRequest request, TeacherService teacherService) {
        if (!teacherService.getMasterDegreeServices().isEmpty()) {
            request.setAttribute("masterDegreeServices", teacherService.getMasterDegreeServices());
        }
    }

    private void setTeachingServicesAndSupportLessons(HttpServletRequest request, Teacher teacher,
            ExecutionSemester executionSemester) {

        List<Professorship> professorships = teacher.getDegreeProfessorshipsByExecutionPeriod(executionSemester);

        if (!professorships.isEmpty()) {
            Collections.sort(professorships, new BeanComparator("executionCourse.nome"));
            request.setAttribute("professorships", professorships);
        }
    }

    private void setInstitutionWorkTimes(HttpServletRequest request, TeacherService teacherService) {
        if (!teacherService.getInstitutionWorkTimes().isEmpty()) {
            ComparatorChain comparatorChain = new ComparatorChain();
            BeanComparator weekDayComparator = new BeanComparator("weekDay");
            BeanComparator startTimeComparator = new BeanComparator("startTime");
            comparatorChain.addComparator(weekDayComparator);
            comparatorChain.addComparator(startTimeComparator);

            List<InstitutionWorkTime> institutionWorkingTimes = new ArrayList(teacherService.getInstitutionWorkTimes());
            Collections.sort(institutionWorkingTimes, comparatorChain);
            request.setAttribute("institutionWorkTimeList", institutionWorkingTimes);
        }
    }

    private void setAdviseServices(HttpServletRequest request, TeacherService teacherService) {
        if (!teacherService.getTeacherAdviseServices().isEmpty()) {
            List<TeacherAdviseService> tfcAdvises =
                    (List<TeacherAdviseService>) CollectionUtils.select(teacherService.getTeacherAdviseServices(),
                            new Predicate() {
                                @Override
                                public boolean evaluate(Object arg0) {
                                    TeacherAdviseService teacherAdviseService = (TeacherAdviseService) arg0;
                                    return teacherAdviseService.getAdvise().getAdviseType().equals(AdviseType.FINAL_WORK_DEGREE);
                                }
                            });
            Collections.sort(tfcAdvises, new BeanComparator("advise.student.number"));
            request.setAttribute("adviseServices", tfcAdvises);
        }
    }

    protected void showLinks(HttpServletRequest request, ExecutionSemester executionSemester, RoleType roleType) {
        boolean showLinks = true;
        try {
            executionSemester.checkValidCreditsPeriod(roleType);
        } catch (DomainException e) {
            showLinks = false;
        }
        request.setAttribute("showLinks", showLinks);
    }

    protected void getRequestAllTeacherCredits(HttpServletRequest request, ExecutionSemester executionSemester, Teacher teacher)
            throws ParseException {
        request.setAttribute("teacher", teacher);

        ProfessionalCategory categoryByPeriod = teacher.getCategoryByPeriod(executionSemester);
        String professionalCategory = categoryByPeriod != null ? categoryByPeriod.getName().getContent() : null;

        request.setAttribute("teacherCategory", professionalCategory);
        request.setAttribute("executionPeriod", executionSemester);

        setTeachingServicesAndSupportLessons(request, teacher, executionSemester);

        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
        if (teacherService != null) {
            setMasterDegreeServices(request, teacherService);
            setAdviseServices(request, teacherService);
            setInstitutionWorkTimes(request, teacherService);
            request.setAttribute("otherServices", teacherService.getOtherServices());
            request.setAttribute("teacherServiceNotes", teacherService.getTeacherServiceNotes());
        }

        Set<PersonContractSituation> serviceExemptions = teacher.getValidTeacherServiceExemptions(executionSemester);

        if (!serviceExemptions.isEmpty()) {
            request.setAttribute("serviceExemptions", serviceExemptions);
        }

        List<PersonFunction> personFuntions =
                teacher.getPersonFuntions(executionSemester.getBeginDateYearMonthDay(),
                        executionSemester.getEndDateYearMonthDay());

        if (!personFuntions.isEmpty()) {
            Iterator<PersonFunction> orderedPersonFuntions =
                    new OrderedIterator<PersonFunction>(personFuntions.iterator(), new BeanComparator("beginDate"));
            request.setAttribute("personFunctions", orderedPersonFuntions);
        }

        Collection<ThesisEvaluationParticipant> thesisEvaluationParticipants =
                teacher.getPerson().getThesisEvaluationParticipants(executionSemester);
        Collection<ThesisEvaluationParticipant> teacherThesisEvaluationParticipants =
                new ArrayList<ThesisEvaluationParticipant>();
        for (ThesisEvaluationParticipant participant : thesisEvaluationParticipants) {
            if (participant.getCreditsDistribution() > 0) {
                teacherThesisEvaluationParticipants.add(participant);
            }
        }

        if (!teacherThesisEvaluationParticipants.isEmpty()) {
            request.setAttribute("teacherThesisEvaluationParticipants", teacherThesisEvaluationParticipants);
        }
    }

    protected CreditLineDTO simulateCalcCreditLine(Teacher teacher, ExecutionSemester executionSemester) throws ParseException {

        double managementCredits = teacher.getManagementFunctionsCredits(executionSemester);
        double serviceExemptionCredits = teacher.getServiceExemptionCredits(executionSemester);
        double thesesCredits = teacher.getThesesCredits(executionSemester);
        double mandatoryLessonHours = teacher.getMandatoryLessonHours(executionSemester);
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
        return new CreditLineDTO(executionSemester, teacherService, managementCredits, serviceExemptionCredits,
                mandatoryLessonHours, teacher, thesesCredits);

    }

}
