package ServidorAplicacao.Servico;

/**
 * @author jorge
 */
public class ItemView {
  protected String _nome;
  protected String _informacao;
  protected boolean _urgente;
    
  public ItemView(String nome, String informacao, boolean urgente) {
    _nome = nome;
    _informacao = informacao;
    _urgente = urgente;
  }
    
  public String getNome() {
    return _nome;
  }

  public String getInformacao() {
    return _informacao;
  }
    
  public boolean getUrgente() {
    return _urgente;
  }
}
