package ServidorPersistenteJDBC;

import java.util.ArrayList;
import java.util.Date;

import Dominio.Feriado;
import Dominio.Horario;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public interface IFeriadoPersistente {
  public boolean alterarFeriado(Feriado feriado);
  public boolean apagarFeriado(int codigoInterno);  
  public boolean diaFeriado(Date dia);
  public boolean escreverFeriado(Feriado feriado);  
  public boolean escreverFeriados(ArrayList listaFeriados);
  public Horario horarioFeriado(Date dia);
  public Horario horarioFeriado(int numMecanografico, Date dia);
  public Feriado lerFeriado(int codigoInterno);  
  public Feriado lerFeriado(Date dia);
	public Feriado lerFeriado(String string, Date date);
  public ArrayList lerTodosCalendarios();
}
