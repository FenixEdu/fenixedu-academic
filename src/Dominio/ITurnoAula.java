/*
 * ITurnoAula.java
 *
 * Created on 22 de Outubro de 2002, 9:15
 */

package Dominio;

import java.io.Serializable;

/**
 *
 * @author  tfc130
 */
public interface ITurnoAula extends Serializable,IDomainObject {
  public ITurno getTurno();
  public IAula getAula();  

  public void setTurno(ITurno turno);
  public void setAula(IAula aula);  
}
