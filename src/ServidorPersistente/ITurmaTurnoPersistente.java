/*
 * ITurmaTurnoPersistente.java
 *
 * Created on 19 de Outubro de 2002, 15:21
 */

package ServidorPersistente;

/**
 *
 * @author  tfc130
 */
import java.util.List;

import Dominio.ICursoExecucao;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;

public interface ITurmaTurnoPersistente extends IPersistentObject {
    public ITurmaTurno readByTurmaAndTurno(ITurma turma, ITurno turno)
               throws ExcepcaoPersistencia;
   
    public void delete(ITurmaTurno turmaTurno) throws ExcepcaoPersistencia;
   
    public List readByClass(ITurma group) throws ExcepcaoPersistencia;
	public List readClassesWithShift(ITurno turno) throws ExcepcaoPersistencia;
	public List readByShift(ITurno group) throws ExcepcaoPersistencia;
	public List readByShiftAndExecutionDegree(ITurno turno, ICursoExecucao execucao) throws ExcepcaoPersistencia;
}
