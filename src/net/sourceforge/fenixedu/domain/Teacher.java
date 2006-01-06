package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.domain.credits.util.InfoCreditsBuilder;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.domain.organizationalStructure.IPersonFunction;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.domain.teacher.IAdvise;
import net.sourceforge.fenixedu.domain.teacher.ICategory;
import net.sourceforge.fenixedu.domain.teacher.ITeacherLegalRegimen;
import net.sourceforge.fenixedu.domain.teacher.ITeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.teacher.ITeacherService;
import net.sourceforge.fenixedu.domain.teacher.ITeacherServiceExemption;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.PublicationArea;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class Teacher extends Teacher_Base {

    /***************************************************************************
     * BUSINESS SERVICES *
     **************************************************************************/

    public void addToTeacherInformationSheet(IPublication publication, PublicationArea publicationArea) {
        new PublicationTeacher(publication, this, publicationArea);
    }

    public void removeFromTeacherInformationSheet(IPublication publication) {
        Iterator<IPublicationTeacher> iterator = getTeacherPublications().iterator();

        while (iterator.hasNext()) {
            IPublicationTeacher publicationTeacher = iterator.next();
            if (publicationTeacher.getPublication().equals(publication)) {
                iterator.remove();
                publicationTeacher.delete();
                return;
            }
        }
    }

    public Boolean canAddPublicationToTeacherInformationSheet(PublicationArea area) {
        // NOTA : a linha seguinte contém um número explícito quando não deve.
        // Isto deve ser mudado! Mas esta mudança implica tornar explícito o
        // conceito de Ficha de docente.
        return new Boolean(countPublicationsInArea(area) < 5);

    }

    public List responsibleFors() {
        List<IProfessorship> professorships = this.getProfessorships();
        List<IProfessorship> res = new ArrayList<IProfessorship>();

        for (IProfessorship professorship : professorships) {
            if (professorship.getResponsibleFor())
                res.add(professorship);
        }
        return res;
    }

    public IProfessorship responsibleFor(Integer executionCourseId) {
        List<IProfessorship> professorships = this.getProfessorships();

        for (IProfessorship professorship : professorships) {
            if (professorship.getResponsibleFor()
                    && professorship.getExecutionCourse().getIdInternal().equals(executionCourseId))
                return professorship;
        }
        return null;
    }

    public void updateResponsabilitiesFor(Integer executionYearId, List<Integer> executionCourses)
            throws MaxResponsibleForExceed, InvalidCategory {

        if (executionYearId == null || executionCourses == null)
            throw new NullPointerException();

        for (final IProfessorship professorship : this.getProfessorships()) {
            final IExecutionCourse executionCourse = professorship.getExecutionCourse();
            ResponsibleForValidator.getInstance().validateResponsibleForList(this, executionCourse,
                    professorship);
            if (executionCourse.getExecutionPeriod().getExecutionYear().getIdInternal().equals(
                    executionYearId)) {
                professorship.setResponsibleFor(executionCourses.contains(executionCourse
                        .getIdInternal()));
            }
        }
    }

    public IDepartment getCurrentWorkingDepartment() {

        IEmployee employee = this.getPerson().getEmployee();
        if (employee != null) {
            return employee.getCurrentDepartmentWorkingPlace();
        }
        return null;
    }

    public IDepartment getLastWorkingDepartment() {
        IEmployee employee = this.getPerson().getEmployee();
        if (employee != null) {
            return employee.getLastDepartmentWorkingPlace();
        }
        return null;
    }

    public ITeacherPersonalExpectation getTeacherPersonalExpectationByExecutionYear(
            IExecutionYear executionYear) {
        ITeacherPersonalExpectation result = null;

        List<ITeacherPersonalExpectation> teacherPersonalExpectations = this
                .getTeacherPersonalExpectations();

        for (ITeacherPersonalExpectation teacherPersonalExpectation : teacherPersonalExpectations) {
            if (teacherPersonalExpectation.getExecutionYear().equals(executionYear)) {
                result = teacherPersonalExpectation;
                break;
            }
        }

        return result;
    }

    public List<IProposal> getFinalDegreeWorksByExecutionYear(IExecutionYear executionYear) {
        List<IProposal> proposalList = new ArrayList<IProposal>();
        for (Iterator iter = getAssociatedProposalsByOrientator().iterator(); iter.hasNext();) {
            IProposal proposal = (IProposal) iter.next();
            if (proposal.getExecutionDegree().getExecutionYear().equals(executionYear)) {
                // if it was attributed by the coordinator the proposal is
                // efective
                if (proposal.getGroupAttributed() != null) {
                    proposalList.add(proposal);
                }
                // if not, we have to verify if the teacher has proposed it to
                // any student(s) and if that(those) student(s) has(have)
                // accepted it
                else {
                    IGroup attributedGroupByTeacher = proposal.getGroupAttributedByTeacher();
                    if (attributedGroupByTeacher != null) {
                        boolean toAdd = false;
                        for (Iterator iterator = attributedGroupByTeacher.getGroupStudents().iterator(); iterator
                                .hasNext();) {
                            IGroupStudent groupStudent = (IGroupStudent) iterator.next();
                            IProposal studentProposal = groupStudent
                                    .getFinalDegreeWorkProposalConfirmation();
                            if (studentProposal != null && studentProposal.equals(proposal)) {
                                toAdd = true;
                            } else {
                                toAdd = false;
                            }
                        }
                        if (toAdd) {
                            proposalList.add(proposal);
                        }
                    }
                }
            }
        }
        return proposalList;
    }

    public List<IExecutionCourse> getLecturedExecutionCoursesByExecutionYear(IExecutionYear executionYear) {
        List<IExecutionCourse> executionCourses = new ArrayList();
        for (Iterator iter = executionYear.getExecutionPeriods().iterator(); iter.hasNext();) {
            IExecutionPeriod executionPeriod = (IExecutionPeriod) iter.next();
            executionCourses.addAll(getLecturedExecutionCoursesByExecutionPeriod(executionPeriod));
        }
        return executionCourses;
    }

    public List<IExecutionCourse> getLecturedExecutionCoursesByExecutionPeriod(
            final IExecutionPeriod executionPeriod) {
        List<IExecutionCourse> executionCourses = new ArrayList<IExecutionCourse>();
        for (Iterator iter = getProfessorships().iterator(); iter.hasNext();) {
            IProfessorship professorship = (IProfessorship) iter.next();
            IExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod().equals(executionPeriod)) {
                executionCourses.add(executionCourse);
            }
        }
        return executionCourses;
    }

    public List<IExecutionCourse> getAllLecturedExecutionCourses() {
        List<IExecutionCourse> executionCourses = new ArrayList<IExecutionCourse>();

        for (IProfessorship professorship : this.getProfessorships()) {
            executionCourses.add(professorship.getExecutionCourse());
        }

        return executionCourses;
    }

    public Double getHoursLecturedOnExecutionCourse(IExecutionCourse executionCourse) {
        double returnValue = 0;

        for (IProfessorship professorShipEntry : executionCourse.getProfessorships()) {
            if (professorShipEntry.getTeacher() == this) {
                for (IShiftProfessorship shiftProfessorShiftEntry : professorShipEntry
                        .getAssociatedShiftProfessorship()) {
                    returnValue += shiftProfessorShiftEntry.getShift().hours();
                }
            }
        }

        return returnValue;
    }

    public ITeacherService getTeacherServiceByExecutionPeriod(final IExecutionPeriod executionPeriod) {
        return (ITeacherService) CollectionUtils.find(getTeacherServices(), new Predicate() {

            public boolean evaluate(Object arg0) {
                ITeacherService teacherService = (ITeacherService) arg0;
                return teacherService.getExecutionPeriod() == executionPeriod;
            }
        });
    }

    public IProfessorship getProfessorshipByExecutionCourse(final IExecutionCourse executionCourse) {
        return (IProfessorship) CollectionUtils.find(getProfessorships(), new Predicate() {
            public boolean evaluate(Object arg0) {
                IProfessorship professorship = (IProfessorship) arg0;
                return professorship.getExecutionCourse() == executionCourse;
            }
        });
    }

    public List<IProfessorship> getDegreeProfessorshipsByExecutionPeriod(
            final IExecutionPeriod executionPeriod) {
        return (List<IProfessorship>) CollectionUtils.select(getProfessorships(), new Predicate() {
            public boolean evaluate(Object arg0) {
                IProfessorship professorship = (IProfessorship) arg0;
                return professorship.getExecutionCourse().getExecutionPeriod() == executionPeriod
                        && !professorship.getExecutionCourse().isMasterDegreeOnly();
            }
        });
    }

    /***************************************************************************
     * OTHER METHODS *
     **************************************************************************/

    public String toString() {
        String result = "[Dominio.Teacher ";
        result += ", teacherNumber=" + getTeacherNumber();
        result += ", person=" + getPerson();
        result += ", category= " + getCategory();
        result += "]";
        return result;
    }

    public InfoCredits getExecutionPeriodCredits(IExecutionPeriod executionPeriod) {
        return InfoCreditsBuilder.build(this, executionPeriod);
    }

    /***************************************************************************
     * PRIVATE METHODS *
     **************************************************************************/

    private int countPublicationsInArea(PublicationArea area) {
        int count = 0;
        for (IPublicationTeacher publicationTeacher : getTeacherPublications()) {
            if (publicationTeacher.getPublicationArea().equals(area)) {
                count++;
            }
        }
        return count;
    }

    public List<IMasterDegreeThesisDataVersion> getGuidedMasterDegreeThesisByExecutionYear(
            IExecutionYear executionYear) {
        List<IMasterDegreeThesisDataVersion> guidedThesis = new ArrayList<IMasterDegreeThesisDataVersion>();

        for (IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion : this
                .getMasterDegreeThesisGuider()) {

            if (masterDegreeThesisDataVersion.getCurrentState().getState() == State.ACTIVE) {

                List<IExecutionDegree> executionDegrees = masterDegreeThesisDataVersion
                        .getMasterDegreeThesis().getStudentCurricularPlan().getDegreeCurricularPlan()
                        .getExecutionDegrees();

                for (IExecutionDegree executionDegree : executionDegrees) {
                    if (executionDegree.getExecutionYear().equals(executionYear)) {
                        guidedThesis.add(masterDegreeThesisDataVersion);
                    }
                }

            }
        }

        return guidedThesis;
    }

    public List<IMasterDegreeThesisDataVersion> getAllGuidedMasterDegreeThesis() {
        List<IMasterDegreeThesisDataVersion> guidedThesis = new ArrayList<IMasterDegreeThesisDataVersion>();

        for (IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion : this
                .getMasterDegreeThesisGuider()) {
            if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                guidedThesis.add(masterDegreeThesisDataVersion);
            }
        }

        return guidedThesis;
    }

    public void createTeacherPersonalExpectation(
            net.sourceforge.fenixedu.dataTransferObject.InfoTeacherPersonalExpectation infoTeacherPersonalExpectation,
            IExecutionYear executionYear) {

        checkIfCanCreatePersonalExpectation(executionYear);

        ITeacherPersonalExpectation teacherPersonalExpectation = new TeacherPersonalExpectation(
                infoTeacherPersonalExpectation, executionYear);

        addTeacherPersonalExpectations(teacherPersonalExpectation);

    }

    private void checkIfCanCreatePersonalExpectation(IExecutionYear executionYear) {
        ITeacherPersonalExpectation storedTeacherPersonalExpectation = getTeacherPersonalExpectationByExecutionYear(executionYear);

        if (storedTeacherPersonalExpectation != null) {
            throw new DomainException(
                    "error.exception.personalExpectation.expectationAlreadyExistsForExecutionYear");
        }

        ITeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod = this
                .getCurrentWorkingDepartment().readTeacherExpectationDefinitionPeriodByExecutionYear(
                        executionYear);

        if (teacherExpectationDefinitionPeriod.isPeriodOpen() == false) {
            throw new DomainException(
                    "error.exception.personalExpectation.definitionPeriodForExecutionYearAlreadyExpired");
        }

    }

    public List<ITeacherServiceExemption> getServiceExemptionSituations(Date beginDate, Date endDate) {

        List<ITeacherServiceExemption> serviceExemptions = new ArrayList<ITeacherServiceExemption>();
        for (ITeacherServiceExemption serviceExemption : this.getServiceExemptionSituations()) {
            if (serviceExemption.belongsToPeriod(beginDate, endDate)) {
                serviceExemptions.add(serviceExemption);
            }
        }
        return serviceExemptions;
    }

    public List<IPersonFunction> getPersonFuntions(Date beginDate, Date endDate) {

        List<IPersonFunction> personFuntions = new ArrayList<IPersonFunction>();
        for (IPersonFunction personFunction : getPerson().getPersonFunctions()) {
            if (personFunction.belongsToPeriod(beginDate, endDate)) {
                personFuntions.add(personFunction);
            }
        }
        return personFuntions;
    }

    public int getHoursByCategory(Date begin, Date end) {

        List<ITeacherLegalRegimen> list = new ArrayList<ITeacherLegalRegimen>();
        for (ITeacherLegalRegimen teacherLegalRegimen : this.getLegalRegimens()) {
            if (teacherLegalRegimen.belongsToPeriod(begin, end)) {
                list.add(teacherLegalRegimen);
            }
        }

        if (list.isEmpty()) {
            return 0;
        } else {
            Collections.sort(list, new BeanComparator("beginDate"));
            return list.get(list.size() -1).getLessonHours();
        }
    }

    public int getServiceExemptionCredits(IExecutionPeriod executionPeriod) {

        Date begin = executionPeriod.getBeginDate();
        Date end = executionPeriod.getEndDate();

        List<ITeacherServiceExemption> list = getServiceExemptionSituations(begin, end);

        if (list.isEmpty()) {
            return 0;
        } else {
            return calculateServiceExemptionsCredits(list, begin, end);
        }
    }

    public double getManagementFunctionsCredits(IExecutionPeriod executionPeriod) {

        Date begin = executionPeriod.getBeginDate();
        Date end = executionPeriod.getEndDate();

        List<IPersonFunction> list = new ArrayList<IPersonFunction>();
        for (IPersonFunction personFunction : this.getPerson().getPersonFunctions()) {
            if (personFunction.belongsToPeriod(begin, end)) {
                list.add(personFunction);
            }
        }

        double totalCredits = 0.0;
        if (list.size() > 1) {
            for (IPersonFunction function : list) {
                totalCredits = (function.getCredits() != null) ? totalCredits
                        + function.getCredits() : totalCredits;
            }
            return totalCredits;
        } else if (list.size() == 1) {
            Double credits = list.iterator().next().getCredits();
            if (credits != null) {
                return credits;
            }
        }
        return 0;
    }

    
    public ICategory getCategoryByPeriod(Date begin, Date end){
        List<ITeacherLegalRegimen> list = new ArrayList<ITeacherLegalRegimen>();
        for (ITeacherLegalRegimen teacherLegalRegimen : this.getLegalRegimens()) {
            if (teacherLegalRegimen.belongsToPeriod(begin, end)) {
                list.add(teacherLegalRegimen);
            }
        }
               
        if (list.isEmpty()) {
            return null;
        } else {
            Collections.sort(list,new BeanComparator("beginDate"));
            return list.get(list.size()-1).getCategory();
        }              
    }
    // /////////////////////////////////////////

    private ICategory getTeacherCategory(List<ITeacherLegalRegimen> list, Date begin, Date end) {
        Integer numberOfDaysInPeriod = null, maxDays = 0;
        ITeacherLegalRegimen teacherLegalRegimen = null;

        for (ITeacherLegalRegimen regimen : list) {

            if (regimen.getBeginDate().before(begin) || regimen.getBeginDate().equals(begin)) {
                numberOfDaysInPeriod = CalendarUtil.getNumberOfDaysBetweenDates(begin, regimen
                        .getEndDate());
                if (numberOfDaysInPeriod >= maxDays) {
                    teacherLegalRegimen = regimen;
                }
            }
            if (regimen.getBeginDate().after(begin) && regimen.getEndDate() != null
                    && regimen.getEndDate().before(end)) {
                numberOfDaysInPeriod = CalendarUtil.getNumberOfDaysBetweenDates(regimen.getBeginDate(),
                        regimen.getEndDate());
                if (numberOfDaysInPeriod >= maxDays) {
                    teacherLegalRegimen = regimen;
                }
            } else if (regimen.getBeginDate().after(begin)) {
                numberOfDaysInPeriod = CalendarUtil.getNumberOfDaysBetweenDates(regimen.getBeginDate(),
                        end);
                if (numberOfDaysInPeriod >= maxDays) {
                    teacherLegalRegimen = regimen;
                }
            }
        }
        return teacherLegalRegimen.getCategory();
    }

    private int calculateTeacherHours(List<ITeacherLegalRegimen> list, Date begin, Date end) {

        Integer numberOfDaysInPeriod = null, maxDays = 0;
        ITeacherLegalRegimen teacherLegalRegimen = null;

        for (ITeacherLegalRegimen regimen : list) {

            if (regimen.getBeginDate().before(begin) || regimen.getBeginDate().equals(begin)) {
                numberOfDaysInPeriod = CalendarUtil.getNumberOfDaysBetweenDates(begin, regimen
                        .getEndDate());
                if (numberOfDaysInPeriod >= maxDays) {
                    teacherLegalRegimen = regimen;
                }
            }
            if (regimen.getBeginDate().after(begin) && regimen.getEndDate() != null
                    && regimen.getEndDate().before(end)) {
                numberOfDaysInPeriod = CalendarUtil.getNumberOfDaysBetweenDates(regimen.getBeginDate(),
                        regimen.getEndDate());
                if (numberOfDaysInPeriod >= maxDays) {
                    teacherLegalRegimen = regimen;
                }
            } else if (regimen.getBeginDate().after(begin)) {
                numberOfDaysInPeriod = CalendarUtil.getNumberOfDaysBetweenDates(regimen.getBeginDate(),
                        end);
                if (numberOfDaysInPeriod >= maxDays) {
                    teacherLegalRegimen = regimen;
                }
            }
        }
        return teacherLegalRegimen.getLessonHours();
    }

    private int calculateServiceExemptionsCredits(List<ITeacherServiceExemption> list, Date begin,
            Date end) {

        Integer numberOfDaysInPeriod = null, maxDays = 0;
        ITeacherServiceExemption teacherServiceExemption = null;

        for (ITeacherServiceExemption serviceExemption : list) {

            if (serviceExemption.getStart().before(begin) || serviceExemption.getStart().equals(begin)) {
                Date endDate = (serviceExemption.getEnd() == null) ? end : serviceExemption.getEnd();
                numberOfDaysInPeriod = CalendarUtil.getNumberOfDaysBetweenDates(begin, endDate);
                if (numberOfDaysInPeriod >= maxDays) {
                    teacherServiceExemption = serviceExemption;
                }
            }
            if (serviceExemption.getStart().after(begin) && serviceExemption.getEnd() != null
                    && serviceExemption.getEnd().before(end)) {
                numberOfDaysInPeriod = CalendarUtil.getNumberOfDaysBetweenDates(serviceExemption
                        .getStart(), serviceExemption.getEnd());
                if (numberOfDaysInPeriod >= maxDays) {
                    teacherServiceExemption = serviceExemption;
                }
            } else if (serviceExemption.getStart().after(begin)) {
                numberOfDaysInPeriod = CalendarUtil.getNumberOfDaysBetweenDates(serviceExemption
                        .getStart(), end);
                if (numberOfDaysInPeriod >= maxDays) {
                    teacherServiceExemption = serviceExemption;
                }
            }
        }

        if (teacherServiceExemption.getEnd() != null) {
            return getHoursByCategory(teacherServiceExemption.getStart(), teacherServiceExemption
                    .getEnd());
        } else {
            return getHoursByCategory(teacherServiceExemption.getStart(), end);
        }
    }

    public List<net.sourceforge.fenixedu.domain.teacher.IAdvise> getAdvisesByAdviseTypeAndExecutionYear(
            net.sourceforge.fenixedu.domain.teacher.AdviseType adviseType, IExecutionYear executionYear) {

        List<IAdvise> result = new ArrayList<IAdvise>();
        Date executionYearStartDate = executionYear.getBeginDate();
        Date executionYearEndDate = executionYear.getEndDate();

        for (IAdvise advise : this.getAdvises()) {
            if ((advise.getAdviseType() == adviseType)) {
                Date adviseStartDate = advise.getStartExecutionPeriod().getBeginDate();
                Date adviseEndDate = advise.getEndExecutionPeriod().getEndDate();

                if (((executionYearStartDate.compareTo(adviseStartDate) < 0) && (executionYearEndDate
                        .compareTo(adviseStartDate) < 0))
                        || ((executionYearStartDate.compareTo(adviseEndDate) > 0) && (executionYearEndDate
                                .compareTo(adviseEndDate) > 0))) {
                    continue;
                }

                result.add(advise);

            }
        }

        return result;
    }

    public List<net.sourceforge.fenixedu.domain.teacher.IAdvise> getAdvisesByAdviseType(
            net.sourceforge.fenixedu.domain.teacher.AdviseType adviseType) {
        List<IAdvise> result = new ArrayList<IAdvise>();

        for (IAdvise advise : this.getAdvises()) {
            if (advise.getAdviseType() == adviseType) {
                result.add(advise);
            }
        }

        return result;
    }
}
