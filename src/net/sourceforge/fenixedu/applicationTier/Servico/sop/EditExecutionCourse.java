/*
 *
 * Created on 2003/08/22
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

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