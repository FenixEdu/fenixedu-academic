/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1 modified by Fernanda Quitério
 */
public class InsertExecutionCourseAtExecutionPeriod implements IService {

    public InsertExecutionCourseAtExecutionPeriod() {
    }

    public void run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {
        IExecutionCourse executionCourse = new ExecutionCourse();
        IExecutionPeriod executionPeriod = null;
        IExecutionCourse existentExecutionCourse = null;
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

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
                    .readByExecutionCourseInitialsAndExecutionPeriod(infoExecutionCourse.getSigla(),
                            executionPeriod);

            if (existentExecutionCourse != null) {
                throw new ExistingPersistentException();
            }

            IPersistentSite persistentSite = persistentSuport.getIPersistentSite();

            persistentExecutionCourse.simpleLockWrite(executionCourse);
            executionCourse.setNome(infoExecutionCourse.getNome());
            executionCourse.setExecutionPeriod(executionPeriod);
            executionCourse.setSigla(infoExecutionCourse.getSigla());
            executionCourse.setLabHours(infoExecutionCourse.getLabHours());
            executionCourse.setPraticalHours(infoExecutionCourse.getPraticalHours());
            executionCourse.setTheoPratHours(infoExecutionCourse.getTheoPratHours());
            executionCourse.setTheoreticalHours(infoExecutionCourse.getTheoreticalHours());
            executionCourse.setComment(infoExecutionCourse.getComment());

            ISite site = new Site();
            persistentSite.simpleLockWrite(site);
            site.setExecutionCourse(executionCourse);

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