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

import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.IPessoa;
import Dominio.IRole;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class Autenticacao implements IServico {

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

	public IUserView run(String utilizador, String password)
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
			throw new FenixServiceException(ex.getMessage());
		}

		if (pessoa != null && pessoa.getPassword().equals(password)) {
			Collection roles = new ArrayList();
			Iterator rolesIterator = pessoa.getPersonRoles().iterator();
			while (rolesIterator.hasNext()) {
				IRole role = (IRole) rolesIterator.next();
				InfoRole infoRole = Cloner.copyIRole2InfoRole(role);
				roles.add(infoRole);
			}
			UserView userView = new UserView(pessoa.getUsername(), roles);
			return userView;
		} else
			throw new ExcepcaoAutenticacao("Autenticacao incorrecta");
	}
}