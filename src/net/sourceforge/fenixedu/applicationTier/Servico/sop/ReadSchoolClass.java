package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author João Mota
 *  
 */

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ITurmaPersistente persistentSchoolClass = sp.getITurmaPersistente();
            ISchoolClass schoolClass = (ISchoolClass) persistentSchoolClass.readByOID(SchoolClass.class, infoSchoolClass
                    .getIdInternal());
            if (schoolClass != null) {
                result = Cloner.copyClass2InfoClass(schoolClass);
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return result;
    }

}