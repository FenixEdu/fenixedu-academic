/*
 * IFrequenta.java
 *
 * Created on 20 de Outubro de 2002, 15:17
 */

package Dominio;

/**
 *
 * @author  tfc130
 */
public interface IFrequenta {
  public IStudent getAluno();
  public IDisciplinaExecucao getDisciplinaExecucao();

  public void setAluno(IStudent aluno);
  public void setDisciplinaExecucao(IDisciplinaExecucao disciplinaExecucao);
}
