/*
 * ITurmaTurno.java
 *
 * Created on 19 de Outubro de 2002, 14:39
 */

package Dominio;

/**
 * 
 * @author tfc130
 */
public interface ITurmaTurno extends IDomainObject {
    public ITurma getTurma();

    public ITurno getTurno();

    public void setTurma(ITurma turma);

    public void setTurno(ITurno turno);
}