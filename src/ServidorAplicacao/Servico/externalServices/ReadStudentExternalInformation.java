/*
 * Created on 2:40:27 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package ServidorAplicacao.Servico.externalServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.externalServices.InfoExternalAdressInfo;
import DataBeans.externalServices.InfoExternalCitizenshipInfo;
import DataBeans.externalServices.InfoExternalDegreeBranchInfo;
import DataBeans.externalServices.InfoExternalDegreeCurricularPlanInfo;
import DataBeans.externalServices.InfoExternalEnrollmentInfo;
import DataBeans.externalServices.InfoExternalIdentificationInfo;
import DataBeans.externalServices.InfoExternalPersonInfo;
import DataBeans.externalServices.InfoStudentExternalInformation;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrollment;
import Dominio.IPessoa;
import Dominio.IStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz </a>
 * 
 * Created at 2:40:27 PM, Mar 11, 2005
 */
public class ReadStudentExternalInformation implements IService
{
    public Collection run(String username) throws ExcepcaoPersistencia
    {
        Collection result = new ArrayList();
        IPessoaPersistente persistentPerson = SuportePersistenteOJB.getInstance().getIPessoaPersistente();
        IPersistentStudent persistentStudent = SuportePersistenteOJB.getInstance().getIPersistentStudent();
        IPessoa person = persistentPerson.lerPessoaPorUsername(username);
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
     */
    private Collection buildExternalEnrollmentsInfo(IStudent student)
    {
        Collection enrollments = new ArrayList();
        for (Iterator iter = student.getActiveStudentCurricularPlan().getEnrolments().iterator(); iter.hasNext();)
        {
            IEnrollment enrollment = (IEnrollment) iter.next();
            InfoExternalEnrollmentInfo info = InfoExternalEnrollmentInfo.newFromEnrollment(enrollment);
            
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
        IDegreeCurricularPlan degreeCurricularPlan =  student.getActiveStudentCurricularPlan().getDegreeCurricularPlan();
        
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
        info.setName(student.getActiveStudentCurricularPlan().getBranch().getName());
        info.setCode(student.getActiveStudentCurricularPlan().getBranch().getCode());
        
        return info;
    }

    /**
     * @param infoPerson
     * @return
     */
    private InfoExternalPersonInfo buildExternalPersonInfo(IPessoa person)
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
    private InfoExternalIdentificationInfo buildExternalIdentificationInfo(IPessoa person)
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
    private InfoExternalCitizenshipInfo builsExternalCitizenshipInfo(IPessoa person)
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
    private InfoExternalAdressInfo buildInfoExternalAdressInfo(IPessoa person)
    {
        InfoExternalAdressInfo info = new InfoExternalAdressInfo();
        info.setPostalCode(person.getCodigoPostal());
        info.setStreet(person.getMorada());
        info.setTown(person.getLocalidade());
        
        return info;
    }
}