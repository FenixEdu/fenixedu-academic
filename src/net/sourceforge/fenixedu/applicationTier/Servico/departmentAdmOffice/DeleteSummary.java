/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 * 21/Jul/2003 fenix-head ServidorAplicacao.Servico.teacher
 *  
 */
public class DeleteSummary implements IService {

    /**
     *  
     */
    public DeleteSummary() {
    }

    public boolean run(Integer executionCourseId, Integer summaryId) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();

            ISummary summary = (ISummary) persistentSummary.readByOID(Summary.class, summaryId, true);
            if (summary != null) {
                if(summary.getExecutionCourse() != null)
                    summary.getExecutionCourse().getAssociatedSummaries().remove(summary);
                if(summary.getShift() != null)
                    summary.getShift().getAssociatedSummaries().remove(summary);
                if(summary.getRoom() != null)
                	summary.getRoom().getAssociatedSummaries().remove(summary);                
                if(summary.getProfessorship() != null)
                	summary.getProfessorship().getAssociatedSummaries().remove(summary);
                if(summary.getTeacher() != null)
                	summary.getTeacher().getAssociatedSummaries().remove(summary);
                                         
                persistentSummary.deleteByOID(Summary.class, summary.getIdInternal());
            }
            return true;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}