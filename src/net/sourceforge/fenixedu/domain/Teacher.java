package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
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
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.domain.teacher.ITeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.teacher.ITeacherServiceExemption;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import net.sourceforge.fenixedu.util.PublicationArea;
import net.sourceforge.fenixedu.util.State;

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
        // NOTA : a linha seguinte contém um número explícito quando não deve
        // isto deve ser mudado! Mas esta mudança implica tornar explícito o
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

    public IDepartment getWorkingDepartment() {

        IEmployee employee = this.getPerson().getEmployee();
        if (employee != null) {
            return employee.getDepartmentWorkingPlace();
        }
        return null;
    }

    public IDepartment getMailingDepartment() {

        IEmployee employee = this.getPerson().getEmployee();
        if (employee != null) {
            return employee.getDepartmentMailingPlace();
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
                        boolean result = false;
                        for (Iterator iterator = attributedGroupByTeacher.getGroupStudents().iterator(); iterator
                                .hasNext();) {
                            IGroupStudent groupStudent = (IGroupStudent) iterator.next();
                            IProposal studentProposal = groupStudent
                                    .getFinalDegreeWorkProposalConfirmation();
                            if (studentProposal != null && studentProposal.equals(proposal)) {
                                result = true;
                            } else {
                                result = false;
                            }
                        }
                        if (result) {
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
                .getWorkingDepartment().readTeacherExpectationDefinitionPeriodByExecutionYear(
                        executionYear);

        if (teacherExpectationDefinitionPeriod.isPeriodOpen() == false) {
            throw new DomainException(
                    "error.exception.personalExpectation.definitionPeriodForExecutionYearAlreadyExpired");
        }

    }

    public List<ITeacherServiceExemption> getServiceExemptionSituations(Date beginDate, Date endDate) {

        List<ITeacherServiceExemption> serviceExemptions = new ArrayList<ITeacherServiceExemption>();
        for (ITeacherServiceExemption serviceExemption : this.getServiceExemptionSituations()) {
            if ((serviceExemption.getStart().before(beginDate) || serviceExemption.getStart().after(
                    beginDate))
                    && (serviceExemption.getEnd() == null || (serviceExemption.getEnd().before(endDate) || serviceExemption
                            .getEnd().after(endDate)))) {
                serviceExemptions.add(serviceExemption);
            }
        }
        return serviceExemptions;
    }

    public int getHoursByCategory(IExecutionPeriod executionPeriod) {

//        Date begin = executionPeriod.getBeginDate();
//        Date end = executionPeriod.getEndDate();
//
//        List<IContract> list = new ArrayList<IContract>();
//        for (IContract contract : this.getPerson().getEmployee().getContracts()) {
//            if (contract.belongsToPeriod(begin, end)) {
//                list.add(contract);
//            }
//        }       
        return 0;
    }

    public int getServiceExemptionCredits(IExecutionPeriod executionPeriod) {

        // Date begin = executionPeriod.getBeginDate();
        // Date end = executionPeriod.getEndDate();
        //
        // List<ITeacherServiceExemption> list = new
        // ArrayList<ITeacherServiceExemption>();
        // for (ITeacherServiceExemption serviceExemption :
        // this.getServiceExemptionSituations()) {
        // if (serviceExemption.belongsToPeriod(begin, end)) {
        // list.add(serviceExemption);
        // }
        // }
        return 0;
    }

    public int getManagementFunctionsCredits(IExecutionPeriod executionPeriod) {

        // Date begin = executionPeriod.getBeginDate();
        // Date end = executionPeriod.getEndDate();
        //
        // List<IPersonFunction> list = new ArrayList<IPersonFunction>();
        // for (IPersonFunction personFunction :
        // this.getPerson().getPersonFunctions()) {
        // if (personFunction.belongsToPeriod(begin, end)) {
        // list.add(personFunction);
        // }
        // }
        return 0;
    }
}
