/*
 * Created on 17/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.Branch;
import Dominio.IBranch;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DeleteBranches implements IServico {

    private static DeleteBranches service = new DeleteBranches();

    public static DeleteBranches getService() {
        return service;
    }

    private DeleteBranches() {
    }

    public final String getNome() {
        return "DeleteBranches";
    }

    // delete a set of branches
    public List run(List internalIds) throws FenixServiceException {

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentBranch persistentBranch = sp.getIPersistentBranch();
            IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();

            Iterator iter = internalIds.iterator();

            List undeletedCodes = new ArrayList();
            List studentCurricularPlans;
            Integer internalId;
            IBranch branch;

            while (iter.hasNext()) {
                internalId = (Integer) iter.next();
                branch = (IBranch) persistentBranch.readByOID(Branch.class,
                        internalId);
                if (branch != null) {
                    studentCurricularPlans = persistentStudentCurricularPlan
                            .readByBranch(branch);
                    if (studentCurricularPlans.isEmpty()) {
                        persistentBranch.delete(branch);
                    } else {
                        undeletedCodes.add(branch.getCode());
                    }
                }
            }

            return undeletedCodes;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}