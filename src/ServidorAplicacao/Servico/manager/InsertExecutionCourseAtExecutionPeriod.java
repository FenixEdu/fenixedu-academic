/*
 * Created on 24/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import Dominio.ExecutionCourse;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ISite;
import Dominio.Site;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1 modified by Fernanda Quitério
 */
public class InsertExecutionCourseAtExecutionPeriod implements IService {

    public InsertExecutionCourseAtExecutionPeriod() {
    }

    public void run(InfoExecutionCourse infoExecutionCourse)
            throws FenixServiceException {
        IExecutionCourse executionCourse = new ExecutionCourse();
        IExecutionPeriod executionPeriod = null;
        IExecutionCourse existentExecutionCourse = null;
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();

            IPersistentExecutionPeriod persistentExecutionPeriod = persistentSuport
                    .getIPersistentExecutionPeriod();
            executionPeriod = (IExecutionPeriod) persistentExecutionPeriod
                    .readByOId(new ExecutionPeriod(infoExecutionCourse
                            .getInfoExecutionPeriod().getIdInternal()), false);

            if (executionPeriod == null) { throw new NonExistingServiceException(
                    "message.nonExistingExecutionPeriod", null); }

            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();

            existentExecutionCourse = persistentExecutionCourse
                    .readByExecutionCourseInitialsAndExecutionPeriod(
                            infoExecutionCourse.getSigla(), executionPeriod);

            if (existentExecutionCourse != null) { throw new ExistingPersistentException(); }

            IPersistentSite persistentSite = persistentSuport
                    .getIPersistentSite();

            persistentExecutionCourse.simpleLockWrite(executionCourse);
            executionCourse.setNome(infoExecutionCourse.getNome());
            executionCourse.setExecutionPeriod(executionPeriod);
            executionCourse.setSigla(infoExecutionCourse.getSigla());
            executionCourse.setLabHours(infoExecutionCourse.getLabHours());
            executionCourse.setPraticalHours(infoExecutionCourse
                    .getPraticalHours());
            executionCourse.setTheoPratHours(infoExecutionCourse
                    .getTheoPratHours());
            executionCourse.setTheoreticalHours(infoExecutionCourse
                    .getTheoreticalHours());
            executionCourse.setComment(infoExecutionCourse.getComment());

            ISite site = new Site();
            persistentSite.simpleLockWrite(site);
            site.setExecutionCourse(executionCourse);

        } catch (ExistingPersistentException existingException) {
            throw new ExistingServiceException(
                    "A disciplina execução com sigla "
                            + existentExecutionCourse.getSigla()
                            + " e período execução "
                            + executionPeriod.getName() + "-"
                            + executionPeriod.getExecutionYear().getYear(),
                    existingException);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}