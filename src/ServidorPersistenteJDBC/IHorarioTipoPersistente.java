package ServidorPersistenteJDBC;

import java.util.ArrayList;

import Dominio.HorarioTipo;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public interface IHorarioTipoPersistente {
  public boolean alterarHorarioTipo(HorarioTipo horarioTipo);
  public boolean apagarHorarioTipo(String sigla);
  public boolean associarHorarioTipoRegime(int chaveHorarioTipo, int chaveRegime);
  public boolean escreverHorarioTipo(HorarioTipo horarioTipo);
  public HorarioTipo lerHorarioTipo(String sigla);
  public HorarioTipo lerHorarioTipo(int codigoInterno);
  public ArrayList lerHorariosTipo();
  public ArrayList lerHorariosTipoSemTurnos();
  public ArrayList lerHorarioTipoRegime(int chaveHorarioTipo);
  public ArrayList lerRegimes(int chaveHorario);  
  
  public ArrayList lerTodosHorariosTipoComRegime(int chaveRegime);
}

