/*
 * Created on 2003/08/01
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

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

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadClassByOID implements IServico {

    private static ReadClassByOID service = new ReadClassByOID();

    /**
     * The singleton access method of this class.
     */
    public static ReadClassByOID getService() {
        return service;
    }

    /**
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadClassByOID";
    }

    public InfoClass run(Integer oid) throws FenixServiceException {

        InfoClass result = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ITurmaPersistente classDAO = sp.getITurmaPersistente();
            ISchoolClass turma = (ISchoolClass) classDAO.readByOID(SchoolClass.class, oid);
            if (turma != null) {
                result = Cloner.copyClass2InfoClass(turma);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}