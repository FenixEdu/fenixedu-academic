package ServidorPersistenteJDBC;

import java.util.ArrayList;

import Dominio.Paises;

public interface IPaisesPersistente {  
  /** ler pais atraves da chave pais */
  public Paises lerPaisesCodigoPais(int codigoPais);
  /** ler pais atraves de codigoInterno */  
  public Paises lerPaises(int codigoInterno);  
  public boolean escreverPaises(Paises paises);
  public boolean alterarPaises(Paises paises);
  public boolean apagarPaises(int codigoPais);
  /** ler pais atraves da do nomePais, devolve codigoPais */
  public int lerPaises(String paises);
  /** devolve um array com todos os paises */
  public ArrayList LerTodosPaises();
  /** devolve o codigoInterno do pais */
  public int lerChavePais(String paises);
    }

