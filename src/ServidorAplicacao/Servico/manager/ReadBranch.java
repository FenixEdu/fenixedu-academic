/*
 * Created on 18/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoBranch;
import DataBeans.util.Cloner;
import Dominio.Branch;
import Dominio.IBranch;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadBranch implements IServico {

    private static ReadBranch service = new ReadBranch();

    /**
     * The singleton access method of this class.
     */
    public static ReadBranch getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ReadBranch() {
    }

    /**
     * Service name
     */
    public final String getNome() {
        return "ReadBranch";
    }

    /**
     * Executes the service. Returns the current infoBranch.
     */
    public InfoBranch run(Integer idInternal) throws FenixServiceException {
        ISuportePersistente sp;
        IBranch branch = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            branch = (IBranch) sp.getIPersistentBranch().readByOID(
                    Branch.class, idInternal);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (branch == null) {
            throw new NonExistingServiceException();
        }

        InfoBranch infoBranch = Cloner.copyIBranch2InfoBranch(branch);
        return infoBranch;
    }
}