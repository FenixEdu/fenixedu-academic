package ServidorAplicacao.Servico;

/**
 * @author jorge
 */
public class ExcepcaoInexistente extends Exception {

  /**
   * Constructor for ExcepcaoInexistente.
   */
  public ExcepcaoInexistente() {
    super();
  }

  /**
   * Constructor for ExcepcaoInexistente.
   * @param message
   */
  public ExcepcaoInexistente(String message) {
    super(message);
  }

  /**
   * Constructor for ExcepcaoInexistente.
   * @param message
   * @param cause
   */
  public ExcepcaoInexistente(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructor for ExcepcaoInexistente.
   * @param cause
   */
  public ExcepcaoInexistente(Throwable cause) {
    super(cause);
  }

}
