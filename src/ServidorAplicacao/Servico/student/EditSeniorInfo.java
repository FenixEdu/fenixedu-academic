/*
 * Created on Jan 4, 2005
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.student.InfoSenior;
import Dominio.student.ISenior;
import Dominio.student.Senior;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.student.IPersistentSenior;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 *
 */
public class EditSeniorInfo implements IService {

    public void run(InfoSenior changedInfoSenior) throws ExcepcaoPersistencia, FenixServiceException {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentSenior persistentSenior = sp.getIPersistentSenior();
            
            ISenior seniorToEdit = (ISenior) persistentSenior.readByOID(Senior.class, changedInfoSenior.getIdInternal());
            if (seniorToEdit == null) {
                throw new NonExistingServiceException("Object doesn't exist!");
            }
            
            persistentSenior.simpleLockWrite(seniorToEdit);
            copyProperties(changedInfoSenior, seniorToEdit);
    }

    /**
     * @param changedInfoSenior
     * @param seniorToEdit
     */
    protected void copyProperties(InfoSenior changedInfoSenior, ISenior seniorToEdit) {
        seniorToEdit.setExpectedDegreeTermination(changedInfoSenior.getExpectedDegreeTermination());
        seniorToEdit.setExpectedDegreeAverageGrade(changedInfoSenior.getExpectedDegreeAverageGrade());
        seniorToEdit.setSpecialtyField(changedInfoSenior.getSpecialtyField());
        seniorToEdit.setProfessionalInterests(changedInfoSenior.getProfessionalInterests());
        seniorToEdit.setLanguageSkills(changedInfoSenior.getLanguageSkills());
        seniorToEdit.setInformaticsSkills(changedInfoSenior.getInformaticsSkills());
        seniorToEdit.setExtracurricularActivities(changedInfoSenior.getExtracurricularActivities());
        seniorToEdit.setProfessionalExperience(changedInfoSenior.getProfessionalExperience());
        seniorToEdit.setLastModificationDate(Calendar.getInstance().getTime());
    }
    
}
