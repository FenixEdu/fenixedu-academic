/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.groupManagement.WriteExecutionCourseUserGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.messaging.WriteMailingList;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1 modified by Fernanda Quitério
 */
public class InsertExecutionCourseAtExecutionPeriod implements IService {

    public void run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {
        IExecutionCourse executionCourse = DomainFactory.makeExecutionCourse();
        IExecutionPeriod executionPeriod = null;
        IExecutionCourse existentExecutionCourse = null;
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentExecutionPeriod persistentExecutionPeriod = persistentSuport
                    .getIPersistentExecutionPeriod();
            executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(
                    ExecutionPeriod.class, infoExecutionCourse.getInfoExecutionPeriod().getIdInternal());

            if (executionPeriod == null) {
                throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
            }

            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();

            existentExecutionCourse = persistentExecutionCourse
                    .readByExecutionCourseInitialsAndExecutionPeriodId(infoExecutionCourse.getSigla(),
                            executionPeriod.getIdInternal());

            if (existentExecutionCourse != null) {
                throw new ExistingPersistentException();
            }

            executionCourse.setNome(infoExecutionCourse.getNome());
            executionCourse.setExecutionPeriod(executionPeriod);
            executionCourse.setSigla(infoExecutionCourse.getSigla());
            executionCourse.setLabHours(infoExecutionCourse.getLabHours());
            executionCourse.setPraticalHours(infoExecutionCourse.getPraticalHours());
            executionCourse.setTheoPratHours(infoExecutionCourse.getTheoPratHours());
            executionCourse.setTheoreticalHours(infoExecutionCourse.getTheoreticalHours());
            executionCourse.setComment(infoExecutionCourse.getComment());

            executionCourse.createSite();            
            
        } catch (ExistingPersistentException existingException) {
            throw new ExistingServiceException("A disciplina execução com sigla "
                    + existentExecutionCourse.getSigla() + " e período execução "
                    + executionPeriod.getName() + "-" + executionPeriod.getExecutionYear().getYear(),
                    existingException);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }                
	}	
}