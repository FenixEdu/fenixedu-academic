/*
 * Created on Dec 22, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoSenior;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.student.ISenior;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentSenior;

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