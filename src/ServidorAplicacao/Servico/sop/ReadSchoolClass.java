package ServidorAplicacao.Servico.sop;

/**
 * @author João Mota
 *  
 */

import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadSchoolClass implements IServico {

    private static ReadSchoolClass _servico = new ReadSchoolClass();

    /**
     * The singleton access method of this class.
     */
    public static ReadSchoolClass getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadSchoolClass() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadSchoolClass";
    }

    public InfoClass run(InfoClass infoSchoolClass) throws FenixServiceException {

        InfoClass result = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ITurmaPersistente persistentSchoolClass = sp.getITurmaPersistente();
            ITurma schoolClass = (ITurma) persistentSchoolClass.readByOID(
                    Turma.class, infoSchoolClass.getIdInternal());
            if (schoolClass != null) {
                result = Cloner.copyClass2InfoClass(schoolClass);
            }

        } catch (ExcepcaoPersistencia ex) {
           throw new FenixServiceException(ex);
        }
        return result;
    }

}