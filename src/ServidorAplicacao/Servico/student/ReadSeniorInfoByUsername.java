/*
 * Created on Dec 22, 2004
 *
 */
package ServidorAplicacao.Servico.student;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.student.InfoSenior;
import Dominio.IPerson;
import Dominio.IStudent;
import Dominio.student.ISenior;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.student.IPersistentSenior;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 *  
 */
public class ReadSeniorInfoByUsername implements IService {

    public ReadSeniorInfoByUsername() {
        super();
    }

    public InfoSenior run(IUserView userView) throws FenixServiceException {
        try {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

            IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();
            IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());

            IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
            IStudent student = persistentStudent.readByUsername(userView.getUtilizador());

            IPersistentSenior persistentSenior = persistentSupport.getIPersistentSenior();
            ISenior senior = persistentSenior.readByStudent(student);

            InfoSenior readInfoSenior = null;
            if(senior == null)
                return readInfoSenior;
            
            readInfoSenior = new InfoSenior();
            readInfoSenior.setIdInternal(senior.getIdInternal());
            readInfoSenior.setName(person.getNome());
            readInfoSenior.setAddress(person.getMorada());
            readInfoSenior.setAreaCode(person.getCodigoPostal());
            readInfoSenior.setAreaCodeArea(person.getLocalidadeCodigoPostal());
            readInfoSenior.setPhone(person.getTelefone());
            readInfoSenior.setMobilePhone(person.getTelemovel());
            readInfoSenior.setEmail(person.getEmail());
            readInfoSenior.setAvailablePhoto(person.getAvailablePhoto());
            readInfoSenior.setPersonID(person.getIdInternal());
            readInfoSenior.setExpectedDegreeTermination(senior.getExpectedDegreeTermination());
            readInfoSenior.setExpectedDegreeAverageGrade(senior.getExpectedDegreeAverageGrade());
            readInfoSenior.setSpecialtyField(senior.getSpecialtyField());
            readInfoSenior.setProfessionalInterests(senior.getProfessionalInterests());
            readInfoSenior.setLanguageSkills(senior.getLanguageSkills());
            readInfoSenior.setInformaticsSkills(senior.getInformaticsSkills());
            readInfoSenior.setExtracurricularActivities(senior.getExtracurricularActivities());
            readInfoSenior.setProfessionalExperience(senior.getProfessionalExperience());
            readInfoSenior.setLastModificationDate(senior.getLastModificationDate());

            return readInfoSenior;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (Exception e) {
            if (e instanceof FenixServiceException) {
                throw (FenixServiceException) e;
            }
            throw new FenixServiceException(e);
        }
    }
}