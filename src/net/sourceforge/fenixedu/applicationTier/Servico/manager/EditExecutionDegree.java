/*
 * Created on 18/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ICampus;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.persistenceTier.places.campus.IPersistentCampus;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class EditExecutionDegree implements IService {

    public void run(InfoExecutionDegree infoExecutionDegree) throws FenixServiceException {

        IPersistentExecutionDegree persistentExecutionDegree = null;
        IExecutionYear executionYear = null;

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentExecutionDegree = persistentSuport.getIPersistentExecutionDegree();

            IExecutionDegree oldExecutionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                    ExecutionDegree.class, infoExecutionDegree.getIdInternal());

            IPersistentCampus campusDAO = persistentSuport.getIPersistentCampus();

            ICampus campus = (ICampus) campusDAO.readByOID(Campus.class, infoExecutionDegree
                    .getInfoCampus().getIdInternal());
            if (campus == null) {
                throw new NonExistingServiceException("message.nonExistingCampus", null);
            }

            if (oldExecutionDegree == null) {
                throw new NonExistingServiceException("message.nonExistingExecutionDegree", null);
            }
            IPersistentExecutionYear persistentExecutionYear = persistentSuport
                    .getIPersistentExecutionYear();

            executionYear = (IExecutionYear) persistentExecutionYear.readByOID(ExecutionYear.class,
                    infoExecutionDegree.getInfoExecutionYear().getIdInternal());

            if (executionYear == null) {
                throw new NonExistingServiceException("message.non.existing.execution.year", null);
            }
            persistentExecutionDegree.simpleLockWrite(oldExecutionDegree);
            oldExecutionDegree.setExecutionYear(executionYear);

            oldExecutionDegree.setTemporaryExamMap(infoExecutionDegree.getTemporaryExamMap());

            oldExecutionDegree.setCampus(campus);

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}