/*
 * ITurnoAlunoPersistente.java
 *
 * Created on 21 de Outubro de 2002, 19:01
 */

package ServidorPersistente;

/**
 *
 * @author  tfc130
 */
import java.util.List;

import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Util.TipoAula;


public interface ITurnoAlunoPersistente extends IPersistentObject {
    public ITurnoAluno readByTurnoAndAluno(ITurno turno, IStudent aluno)
               throws ExcepcaoPersistencia;
    public void lockWrite(ITurnoAluno turnoAluno) throws ExcepcaoPersistencia;
    public void delete(ITurnoAluno turnoAluno) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
    public List readByTurno(String nomeTurno) throws ExcepcaoPersistencia;
	public List readByTurno(ITurno shift) throws ExcepcaoPersistencia;    
	public ITurno readByStudentIdAndShiftType(Integer id, TipoAula shiftType, String nameExecutionCourse)
				throws ExcepcaoPersistencia;
}
