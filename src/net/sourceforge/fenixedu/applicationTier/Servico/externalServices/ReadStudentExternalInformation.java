/*
 * Created on 2:40:27 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.GetEnrolmentGrade;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalAdressInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalCitizenshipInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalDegreeBranchInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalDegreeCurricularPlanInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalEnrollmentInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalIdentificationInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalPersonInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoStudentExternalInformation;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz </a>
 * 
 * Created at 2:40:27 PM, Mar 11, 2005
 */
public class ReadStudentExternalInformation implements IService
{
    public Collection run(String username) throws ExcepcaoPersistencia, FenixServiceException
    {
        Collection result = new ArrayList();
        IPessoaPersistente persistentPerson = PersistenceSupportFactory.getDefaultPersistenceSupport()
                .getIPessoaPersistente();
        IPersistentStudent persistentStudent = PersistenceSupportFactory.getDefaultPersistenceSupport()
                .getIPersistentStudent();
        IPerson person = persistentPerson.lerPessoaPorUsername(username);
        Collection students = persistentStudent.readbyPerson(person);
        for (Iterator iter = students.iterator(); iter.hasNext();)
        {
            InfoStudentExternalInformation info = new InfoStudentExternalInformation();
            IStudent student = (IStudent) iter.next();

            info.setPerson(this.buildExternalPersonInfo(person));
            info.setDegree(this.buildExternalDegreeCurricularPlanInfo(student));
            info.setCourses(this.buildExternalEnrollmentsInfo(student));

            result.add(info);
        }

        return result;
    }

    /**
     * @param student
     * @return
     * @throws FenixServiceException 
     */
    private Collection buildExternalEnrollmentsInfo(IStudent student) throws FenixServiceException
    {
        Collection enrollments = new ArrayList();
        for (Iterator iter = student.getActiveStudentCurricularPlan().getEnrolments().iterator(); iter
                .hasNext();)
        {
            IEnrollment enrollment = (IEnrollment) iter.next();
            InfoExternalEnrollmentInfo info = InfoExternalEnrollmentInfo.newFromEnrollment(enrollment);
            
            GetEnrolmentGrade getEnrollmentGrade = new GetEnrolmentGrade();
            InfoEnrolmentEvaluation infoEnrollmentEvaluation =  getEnrollmentGrade.run(enrollment);
            info.setFinalGrade(infoEnrollmentEvaluation.getGrade());
            enrollments.add(info);
        }


        return enrollments;
    }

    /**
     * @param student
     * @return
     */
    private InfoExternalDegreeCurricularPlanInfo buildExternalDegreeCurricularPlanInfo(IStudent student)
    {
        InfoExternalDegreeCurricularPlanInfo info = new InfoExternalDegreeCurricularPlanInfo();
        IDegreeCurricularPlan degreeCurricularPlan = student.getActiveStudentCurricularPlan()
                .getDegreeCurricularPlan();

        info.setName(degreeCurricularPlan.getName());
        info.setCode(degreeCurricularPlan.getDegree().getIdInternal().toString());
        info.setBranch(this.buildExternalDegreeBranchInfo(student));


        return info;
    }

    /**
     * @param student
     * @return
     */
    private InfoExternalDegreeBranchInfo buildExternalDegreeBranchInfo(IStudent student)
    {
        InfoExternalDegreeBranchInfo info = new InfoExternalDegreeBranchInfo();
        if (student.getActiveStudentCurricularPlan().getBranch() != null)
        {
            info.setName(student.getActiveStudentCurricularPlan().getBranch().getName());
            info.setCode(student.getActiveStudentCurricularPlan().getBranch().getCode());
        }

        return info;
    }

    /**
     * @param infoPerson
     * @return
     */
    private InfoExternalPersonInfo buildExternalPersonInfo(IPerson person)
    {
        InfoExternalPersonInfo info = new InfoExternalPersonInfo();
        info.setAddress(this.buildInfoExternalAdressInfo(person));
        info.setBirthday(person.getNascimento().toString());
        info.setCelularPhone(person.getTelemovel());
        info.setCitizenship(this.builsExternalCitizenshipInfo(person));
        info.setEmail(person.getEmail());
        info.setFiscalNumber(person.getNumContribuinte());
        info.setIdentification(this.buildExternalIdentificationInfo(person));
        info.setName(person.getNome());
        info.setNationality(person.getNacionalidade());
        info.setPhone(person.getTelefone());
        info.setSex(person.getSexo().toString());

        return info;
    }

    /**
     * @param infoPerson
     * @return
     */
    private InfoExternalIdentificationInfo buildExternalIdentificationInfo(IPerson person)
    {
        InfoExternalIdentificationInfo info = new InfoExternalIdentificationInfo();
        info.setDocumentType(person.getTipoDocumentoIdentificacao().toString());
        info.setNumber(person.getNumeroDocumentoIdentificacao());

        return info;
    }

    /**
     * @param infoPerson
     * @return
     */
    private InfoExternalCitizenshipInfo builsExternalCitizenshipInfo(IPerson person)
    {
        InfoExternalCitizenshipInfo info = new InfoExternalCitizenshipInfo();
        info.setArea(person.getFreguesiaNaturalidade());
        info.setCounty(person.getConcelhoNaturalidade());

        return info;
    }

    /**
     * @param infoPerson
     * @return
     */
    private InfoExternalAdressInfo buildInfoExternalAdressInfo(IPerson person)
    {
        InfoExternalAdressInfo info = new InfoExternalAdressInfo();
        info.setPostalCode(person.getCodigoPostal());
        info.setStreet(person.getMorada());
        info.setTown(person.getLocalidade());

        return info;
    }
}