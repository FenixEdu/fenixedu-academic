package ServidorAplicacao.Servico;

import java.util.List;

/**
 * This is the view class that contains information about the seccao
 * domain object.
 *
 * @author Joao Pereira 
 **/

public class SeccaoView {
  protected String _nome;
  protected int _ordem;
  protected String _sitio;
  protected String _seccaoSuperior;
  protected List _seccoesInferiores;
  protected List _itens;

  /**
   * @param seccoesInferiores e' uma lista com os nomes das
   * sub-seccoes. It can have a null value if the list is empty.
   **/
  public SeccaoView(String nome, int ordem, String sitio,
		    String seccaoSuperior, List seccoesInferiores,
		    List itens) {
    _nome = nome;
    _ordem = ordem;
    _sitio = sitio;
    _seccaoSuperior = seccaoSuperior;
    _seccoesInferiores = seccoesInferiores;
    _itens = itens;
  }

  public String getNome() {
    return _nome;
  }
  
  public int getOrdem() {
    return _ordem;
  }

  public String getSitio() {
    return _sitio;
  }

  public String getSeccaoSuperior() {
    return _seccaoSuperior;
  }

  /**
   * The list of sub-seccoes of a seccao only contains the names of
   * the sub-seccoes. This list can be null, if the seccao has no
   * sub-seccoes.
   **/
  public List getSeccoesInferiores() {
    return _seccoesInferiores;
  }

  public List getItens() {
    return _itens;
  }
}
