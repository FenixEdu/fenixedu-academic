/*
 * ApagarAula.java
 *
 * Created on 27 de Outubro de 2002, 14:30
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Service DeleteExam.
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.notAuthorizedPersistentDeleteException;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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