package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoSenior;
import net.sourceforge.fenixedu.domain.student.Senior;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditSeniorInfo extends Service {

    public void run(InfoSenior changedInfoSenior) throws ExcepcaoPersistencia, FenixServiceException {
        Senior seniorToEdit = rootDomainObject.readSeniorByOID(changedInfoSenior.getIdInternal());
        if (seniorToEdit == null) {
            throw new NonExistingServiceException("Object doesn't exist!");
        }

        copyProperties(changedInfoSenior, seniorToEdit);
    }

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
