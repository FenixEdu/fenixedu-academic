package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author João Mota
 */
public class ReadExecutionCourse implements IServico {

    private static ReadExecutionCourse _servico = new ReadExecutionCourse();

    /**
     * The actor of this class.
     */

    private ReadExecutionCourse() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ReadExecutionCourse";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCourse
     */
    public static ReadExecutionCourse getService() {
        return _servico;
    }

    public Object run(InfoExecutionPeriod infoExecutionPeriod, String code) throws ExcepcaoInexistente,
            FenixServiceException {

        IExecutionCourse iExecCourse = null;
        InfoExecutionCourse infoExecCourse = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
//            IExecutionPeriod executionPeriod = Cloner
//                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
            iExecCourse = executionCourseDAO.readByExecutionCourseInitialsAndExecutionPeriodId(code,
                    infoExecutionPeriod.getIdInternal());
            if (iExecCourse != null)
                infoExecCourse = (InfoExecutionCourse) Cloner.get(iExecCourse);
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoExecCourse;
    }

}