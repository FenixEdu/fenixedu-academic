package ServidorAplicacao.Servico;

/**
 * The authentication service.
 *
 * @author Joao Pereira
 * @version
 **/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.ICandidateSituation;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import Dominio.IRole;
import ServidorAplicacao.ICandidateView;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.State;

public class Autenticacao implements IServico {
	public static final String EXTRANET = "extranet";
	public static final String INTRANET = "intranet"; 
	private static Autenticacao _servico = new Autenticacao();

	/**
	 * The singleton access method of this class.
	 **/
	public static Autenticacao getService() {
		return _servico;
	}

	/**
	 * The ctor of this class.
	 **/
	private Autenticacao() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "Autenticacao";
	}

	public IUserView run(String utilizador, String password, String application)
		throws FenixServiceException, ExcepcaoAutenticacao {
		IPessoa pessoa = null;

		try {
			//pessoa = SuportePersistenteOJB.getInstance().getIPessoaPersistente().readByUtilizador(utilizador);
			pessoa =
				SuportePersistenteOJB
					.getInstance()
					.getIPessoaPersistente()
					.lerPessoaPorUsername(utilizador);
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace(System.out);
			throw new FenixServiceException(ex.getMessage());
		}
		if (pessoa != null
			&& PasswordEncryptor.areEquals(pessoa.getPassword(), password)) {
			Collection roles = new ArrayList();
			if (pessoa.getPersonRoles() != null) {
				Iterator rolesIterator = pessoa.getPersonRoles().iterator();
				while (rolesIterator.hasNext()) {
					IRole role = (IRole) rolesIterator.next();
					InfoRole infoRole = Cloner.copyIRole2InfoRole(role);
					roles.add(infoRole);
				}
			}
			UserView userView = new UserView(pessoa.getUsername(), roles);
			
			// Check if the Person is a Candidate
			
			if (userView.hasRoleType(RoleType.MASTER_DEGREE_CANDIDATE)){
				List masterDegreeCandidates = null; 
				try {
					masterDegreeCandidates = SuportePersistenteOJB.getInstance().getIPersistentMasterDegreeCandidate().
											readMasterDegreeCandidatesByUsername(userView.getUtilizador());
				} catch(ExcepcaoPersistencia ex) {
					ex.printStackTrace(System.out);
					throw new FenixServiceException(ex.getMessage());
				}
				
				// Create a list with the active situations of the Candidate
				ICandidateView candidateView = null;
				List situations = new ArrayList();
				
				Iterator iterator = masterDegreeCandidates.iterator();
				while(iterator.hasNext()){
					IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) iterator.next();
					Iterator situationIter = masterDegreeCandidate.getSituations().iterator();
					while(situationIter.hasNext()){
						ICandidateSituation candidateSituation = (ICandidateSituation) situationIter.next();
						if (candidateSituation.getValidation().equals(new State(State.ACTIVE)))
							situations.add(Cloner.copyICandidateSituation2InfoCandidateSituation(candidateSituation));						
					}
				}
				candidateView = new CandidateView(situations);
				
				userView.setCandidateView(candidateView);
				
			} else userView.setCandidateView(null);
			
			
			return filterUserView(userView, application);
		} else
			throw new ExcepcaoAutenticacao("Autenticacao incorrecta");
	}

	/**
	 * @param userView
	 * @return IUserView filtered by application type.
	 */
	private IUserView filterUserView(UserView userView, String application) {
		Collection rolesIntranet = new ArrayList();
		InfoRole masterDegreeAdministrativeOffice = new InfoRole();
		masterDegreeAdministrativeOffice.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
		
		rolesIntranet.add(masterDegreeAdministrativeOffice);


		InfoRole managementAssiduousness = new InfoRole();
		managementAssiduousness.setRoleType(RoleType.MANAGEMENT_ASSIDUOUSNESS);		
		rolesIntranet.add(managementAssiduousness);

		if (application.equalsIgnoreCase(Autenticacao.INTRANET)){
			Collection roles = CollectionUtils.intersection(userView.getRoles(), rolesIntranet);
			userView.setRoles(roles);	
		}else if (application.equals("") || application.equalsIgnoreCase(Autenticacao.EXTRANET)){
			userView.getRoles().removeAll(rolesIntranet);
		}
		return userView;
	}
}