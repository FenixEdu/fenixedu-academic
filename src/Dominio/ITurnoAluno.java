/*
 * ITurnoAluno.java
 *
 * Created on 21 de Outubro de 2002, 18:59
 */

package Dominio;

/**
 *
 * @author  tfc130
 */
public interface ITurnoAluno {
  public ITurno getTurno();
  public IStudent getAluno();  

  public void setTurno(ITurno turno);
  public void setAluno(IStudent aluno);  
}
