package ServidorAplicacao.Servico;

/**
 * The authentication service.
 *
 * @author Joao Pereira
 * @version
 **/

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import Dominio.IPessoa;
import Dominio.IPrivilegio;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.NotExecutedException;
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
      throws NotExecutedException, ExcepcaoAutenticacao {
    IPessoa pessoa = null;
    
    try {
      //pessoa = SuportePersistenteOJB.getInstance().getIPessoaPersistente().readByUtilizador(utilizador);
      pessoa = SuportePersistenteOJB.getInstance().getIPessoaPersistente().lerPessoaPorUsername(utilizador);
    } catch (ExcepcaoPersistencia ex) {
      throw new NotExecutedException(ex.getMessage());
    }

    if (pessoa != null && pessoa.getPassword().equals(password)) {
      // Criar UserView
      Set privilegios = new HashSet();
      Iterator iterador = pessoa.getPrivilegios().iterator();
      
      while (iterador.hasNext())
        privilegios.add(((IPrivilegio)iterador.next()).getServico());
   
      //return new UserView(pessoa.getUtilizador(), privilegios);
      return new UserView(pessoa.getUsername(), privilegios);
    } else    
      throw new ExcepcaoAutenticacao("Autenticacao incorrecta");
  }
}