/*
 * ApagarAula.java
 *
 * Created on 27 de Outubro de 2002, 14:30
 */

package ServidorAplicacao.Servico.sop;

/**
 * Service DeleteExam.
 * 
 * @author tfc130
 */
import DataBeans.InfoViewExamByDayAndShift;
import Dominio.Exam;
import Dominio.IExam;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.notAuthorizedPersistentDeleteException;

public class DeleteExam implements IServico {

    private static DeleteExam _servico = new DeleteExam();

    /**
     * The singleton access method of this class.
     */
    public static DeleteExam getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private DeleteExam() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "DeleteExam";
    }

    public Object run(InfoViewExamByDayAndShift infoViewExam) throws FenixServiceException {

        boolean result = false;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExam examToDelete = (IExam) sp.getIPersistentExam().readByOID(Exam.class,
                    infoViewExam.getInfoExam().getIdInternal());
            sp.getIPersistentExam().simpleLockWrite(examToDelete);

            sp.getIPersistentExam().delete(examToDelete);
            result = true;
        } catch (notAuthorizedPersistentDeleteException ex) {
            throw new notAuthorizedServiceDeleteException(ex);
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException("Error deleting exam");
        }

        return new Boolean(result);

    }

}