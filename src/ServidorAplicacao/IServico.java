package ServidorAplicacao;

/**
 * The interface of the service classes.The services classes must also
 * define a static public method called getService that returns an
 * instance of the service.
 **/

public interface IServico {
  /**
   * Returns the name of the service
   **/
  String getNome();
}
