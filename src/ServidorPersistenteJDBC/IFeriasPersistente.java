package ServidorPersistenteJDBC;

import java.util.ArrayList;

import Dominio.Ferias;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public interface IFeriasPersistente {
  public boolean alterarFerias(Ferias ferias);
  public boolean apagarFerias(int codigoInterno);
  public boolean escreverFerias(Ferias ferias);
  public ArrayList HistoricoFeriasPorFuncionario(int numFuncionario);
  public Ferias lerFerias(int codigoInterno);
  public Ferias lerFeriasAnoPorFuncionario(int ano, int numMecanografico);
  public ArrayList lerFeriasPorAno(int ano);
}
