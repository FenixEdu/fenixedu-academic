/*
 * CreateExam.java
 *
 * Created on 2003/03/26
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o CriarAula.
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class DefineExamComment implements IServico {

    private static DefineExamComment _servico = new DefineExamComment();

    /**
     * The singleton access method of this class.
     */
    public static DefineExamComment getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private DefineExamComment() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "DefineExamComment";
    }

    public Boolean run(InfoExecutionCourse infoExecutionCourse, String comment)
            throws FenixServiceException {

        Boolean result = new Boolean(false);

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionCourse
                            .getInfoExecutionPeriod());

            IExecutionCourse executionCourse = executionCourseDAO
                    .readByExecutionCourseInitialsAndExecutionPeriod(infoExecutionCourse.getSigla(),
                            executionPeriod);

            // TODO: Temporary solution to lock object for write. In the future
            // we'll use readByUnique()
            executionCourse = (IExecutionCourse) executionCourseDAO.readByOID(ExecutionCourse.class,
                    executionCourse.getIdInternal(), true);
            executionCourse.setComment(comment);

            result = new Boolean(true);

        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException(ex.getMessage());
        }

        return result;
    }

}