/*
 * Created on 2003/08/01
 *
 *
 */
package ServidorAplicacao.Servico.sop;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ITurmaPersistente classDAO = sp.getITurmaPersistente();
            ITurma turma = (ITurma) classDAO.readByOID(Turma.class, oid);
            if (turma != null) {
                result = Cloner.copyClass2InfoClass(turma);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}