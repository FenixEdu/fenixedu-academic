/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.administrativeOfficeServices;


import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.ExecutionDegreeBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.IngressionInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.OriginInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.candidacy.RegisteredCandidacySituation;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.student.PersonalIngressionData;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - �ngela Almeida (argelina@ist.utl.pt)
 * 
 */
public class CreateStudent {

    @Checked("AcademicPredicates.CREATE_REGISTRATION")
    @Atomic
    public static Registration run(PersonBean personBean, ExecutionDegreeBean executionDegreeBean,
            PrecedentDegreeInformationBean precedentDegreeInformationBean, IngressionInformationBean ingressionInformationBean,
            OriginInformationBean originInformationBean) {

        // get or update person
        Person person = getPerson(personBean);

        // create candidacy
        StudentCandidacy studentCandidacy =
                StudentCandidacy.createStudentCandidacy(executionDegreeBean.getExecutionDegree(), person);

        new RegisteredCandidacySituation(studentCandidacy, ingressionInformationBean.getRegistrationAgreement(),
                executionDegreeBean.getCycleType(), ingressionInformationBean.getIngression(),
                ingressionInformationBean.getEntryPhase(), personBean.getStudentNumber());

        // create registration
        Registration registration = studentCandidacy.getRegistration();
        if (registration == null) {
            /*
             * 26/08/2009 - Due to curriculum validation we must support creation of students, if necessary, with a custom student
             * number (for students that are not in the system).
             */
            // registration = new Registration(person,
            // executionDegreeBean.getDegreeCurricularPlan(), studentCandidacy,
            // ingressionInformationBean.getRegistrationAgreement(),
            // executionDegreeBean.getCycleType(), executionDegreeBean
            // .getExecutionYear());
            registration =
                    Registration.createRegistrationWithCustomStudentNumber(person, executionDegreeBean.getDegreeCurricularPlan(),
                            studentCandidacy, ingressionInformationBean.getRegistrationAgreement(),
                            executionDegreeBean.getCycleType(), executionDegreeBean.getExecutionYear(),
                            personBean.getStudentNumber());
        }
        registration.setHomologationDate(ingressionInformationBean.getHomologationDate());
        registration.setStudiesStartDate(ingressionInformationBean.getStudiesStartDate());

        PersonalIngressionData personalIngressionData =
                registration.getStudent().getPersonalIngressionDataByExecutionYear(
                        executionDegreeBean.getExecutionDegree().getExecutionYear());

        if (personalIngressionData == null) {
            personalIngressionData =
                    new PersonalIngressionData(originInformationBean, personBean, registration.getStudent(), executionDegreeBean
                            .getExecutionDegree().getExecutionYear());
        } else {
            personalIngressionData.edit(originInformationBean, personBean);
        }
        PrecedentDegreeInformation precedentDegreeInformation = studentCandidacy.getPrecedentDegreeInformation();
        precedentDegreeInformation.edit(personalIngressionData, registration, precedentDegreeInformationBean, studentCandidacy);

        // create qualification
        new Qualification(person, precedentDegreeInformation);

        // add roles
        //	person.addPersonRoleByRoleType(RoleType.STUDENT);
        //	person.addPersonRoleByRoleType(RoleType.PERSON);

        return registration;
    }

    private static Person getPerson(PersonBean personBean) {
        Person person = null;
        if (personBean.getPerson() != null) {
            person = personBean.getPerson();
            person.edit(personBean);
        } else {
            // create person
            person = new Person(personBean);
        }
        return person;
    }

}