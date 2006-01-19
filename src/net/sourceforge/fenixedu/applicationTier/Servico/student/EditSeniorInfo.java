/*
 * Created on Jan 4, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoSenior;
import net.sourceforge.fenixedu.domain.student.Senior;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentSenior;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 *
 */
public class EditSeniorInfo implements IService {

    public void run(InfoSenior changedInfoSenior) throws ExcepcaoPersistencia, FenixServiceException {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSenior persistentSenior = sp.getIPersistentSenior();
            
            Senior seniorToEdit = (Senior) persistentSenior.readByOID(Senior.class, changedInfoSenior.getIdInternal());
            if (seniorToEdit == null) {
                throw new NonExistingServiceException("Object doesn't exist!");
            }

            copyProperties(changedInfoSenior, seniorToEdit);
    }

    /**
     * @param changedInfoSenior
     * @param seniorToEdit
     */
    protected void copyProperties(InfoSenior changedInfoSenior, Senior seniorToEdit) {
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
