package ServidorAplicacao.Servico.student;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

/**
 * @author João Mota
 * 
 */
public class ReadShiftsByTypeFromExecutionCourse implements IService {

    public List run(InfoExecutionCourse infoExecutionCourse, TipoAula tipoAula) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
        final ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();

        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, infoExecutionCourse.getIdInternal());
        final List shifts = persistentShift.readByExecutionCourseAndType(executionCourse, tipoAula.getTipo());

        return (List) CollectionUtils.collect(shifts, new Transformer() {
            public Object transform(Object arg0) {
                final ITurno shift = (ITurno) arg0;
                return InfoShift.newInfoFromDomain(shift);
            }});
    }

}