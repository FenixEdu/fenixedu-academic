/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoBranch;
import DataBeans.util.Cloner;
import Dominio.IBranch;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetBranchListByCandidateID implements IServico {

	private static GetBranchListByCandidateID servico = new GetBranchListByCandidateID();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static GetBranchListByCandidateID getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private GetBranchListByCandidateID() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "GetBranchListByCandidateID";
	}

	public List run(Integer candidateID) throws FenixServiceException {

		ISuportePersistente sp = null;
		List result = null;
		
		try {
			sp = SuportePersistenteOJB.getInstance();
			
			IMasterDegreeCandidate mdcTemp = new MasterDegreeCandidate();
			mdcTemp.setIdInternal(candidateID);
			
			IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate().readByOId(mdcTemp, false);
			result = sp.getIPersistentBranch().readByExecutionDegree(masterDegreeCandidate.getExecutionDegree());
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error",ex);
			throw newEx;
		} 
		List branchList = new ArrayList();
		if (result == null){
			InfoBranch infoBranch = new InfoBranch();
			infoBranch.setName("Tronco Comum");
			branchList.add(infoBranch);
			return branchList;
		}
		
		Iterator iterator = result.iterator();
		
		while(iterator.hasNext()){
			IBranch branch = (IBranch) iterator.next();
			InfoBranch infoBranch = Cloner.copyIBranch2InfoBranch(branch);
			
			if ((infoBranch.getName() == null) || (infoBranch.getName().length() == 0)){
				
				// FIXME
				infoBranch.setName("Tronco Comum");
			}
			branchList.add(infoBranch);
		}

		return branchList;
	}
}
