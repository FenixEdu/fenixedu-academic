package ServidorAplicacao.Servico;

import java.util.Set;

import ServidorAplicacao.IUserView;

/**
 * @author jorge
 */
public class UserView implements IUserView {
  private String _utilizador;
  private Set _privilegios;

  public UserView(String utilizador, Set privilegios) {
    setUtilizador(utilizador);
    setPrivilegios(privilegios);
  }

  public String getUtilizador() {
    return _utilizador;
  }

  public Set getPrivilegios() {
    return _privilegios;
  }
  
  public void setUtilizador(String utilizador) {
    _utilizador = utilizador;
  }
  
  public void setPrivilegios(Set privilegios) {
    _privilegios = privilegios;
  }
}