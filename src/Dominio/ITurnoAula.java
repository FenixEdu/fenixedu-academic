/*
 * ITurnoAula.java
 *
 * Created on 22 de Outubro de 2002, 9:15
 */

package Dominio;

/**
 *
 * @author  tfc130
 */
public interface ITurnoAula {
  public ITurno getTurno();
  public IAula getAula();  

  public void setTurno(ITurno turno);
  public void setAula(IAula aula);  
}
