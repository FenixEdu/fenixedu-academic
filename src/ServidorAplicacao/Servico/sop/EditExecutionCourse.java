/*
 *
 * Created on 2003/08/22
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditExecutionCourse implements IServico {

    private static EditExecutionCourse _servico = new EditExecutionCourse();

    /**
     * The singleton access method of this class.
     */
    public static EditExecutionCourse getService() {

        return _servico;
    }

    /**
     * The actor of this class.
     */
    private EditExecutionCourse() {

    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {

        return "EditExecutionCourse";
    }

    public InfoExecutionCourse run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            IExecutionCourse executionCourse = (IExecutionCourse) executionCourseDAO.readByOID(
                    ExecutionCourse.class, infoExecutionCourse.getIdInternal(), true);
            if (executionCourse == null) {
                throw new NonExistingServiceException();
            }
            executionCourse.setNome(infoExecutionCourse.getNome());
            executionCourse.setSigla(infoExecutionCourse.getSigla());
            executionCourse.setTheoreticalHours(infoExecutionCourse.getTheoreticalHours());
            executionCourse.setTheoPratHours(infoExecutionCourse.getTheoPratHours());
            executionCourse.setPraticalHours(infoExecutionCourse.getPraticalHours());
            executionCourse.setLabHours(infoExecutionCourse.getLabHours());
            executionCourse.setComment(infoExecutionCourse.getComment());

            //Cloner
            List curricularCourses = executionCourse.getAssociatedCurricularCourses();

            List infoCurricularCourses = new ArrayList();
            CollectionUtils.collect(curricularCourses, new Transformer() {
                public Object transform(Object input) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) input;

                    return Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                }
            }, infoCurricularCourses);

            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
            infoExecutionCourse.setAssociatedInfoCurricularCourses(infoCurricularCourses);

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }

        return infoExecutionCourse;
    }

}