package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author LuisCruz & Sara Ribeiro
 */
public class ReadClassesByExecutionCourse implements IServico {
    private static ReadClassesByExecutionCourse serviceInstance = new ReadClassesByExecutionCourse();

    /**
     * The singleton access method of this class.
     */
    public static ReadClassesByExecutionCourse getService() {
        return serviceInstance;
    }

    /**
     * The actor of this class.
     */
    private ReadClassesByExecutionCourse() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadClassesByExecutionCourse";
    }

    public List run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {

        List infoClasses = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                    .readByOID(ExecutionCourse.class, infoExecutionCourse.getIdInternal());

            List classes = sp.getITurmaPersistente().readByExecutionCourse(executionCourse);

            infoClasses = (List) CollectionUtils.collect(classes, new Transformer() {
                public Object transform(Object arg0) {
                    return Cloner.copyClass2InfoClass((ISchoolClass) arg0);
                }
            });
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return infoClasses;
    }
}