package ServidorPersistenteJDBC;

import java.sql.Timestamp;
import java.util.ArrayList;

import Dominio.PeriodoFerias;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public interface IPeriodoFeriasPersistente {
  public boolean alterarPeriodoFerias(PeriodoFerias periodoFerias);
  public boolean apagarPeriodoFerias(int codigoInterno);
  public boolean escreverPeriodoFerias(PeriodoFerias periodoFerias);
  public ArrayList historicoFeriasPorFuncionario(int numFuncionario);
  public PeriodoFerias lerPeriodoFerias(int codigoInterno);
  public ArrayList lerFuncionariosComFerias(Timestamp dataFerias);
  public ArrayList lerFeriasPorTipo(String tipoFerias);
}
