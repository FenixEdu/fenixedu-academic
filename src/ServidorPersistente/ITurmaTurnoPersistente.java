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

import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import ServidorPersistente.exceptions.ExistingPersistentException;

public interface ITurmaTurnoPersistente extends IPersistentObject {
    public ITurmaTurno readByTurmaAndTurno(ITurma turma, ITurno turno)
               throws ExcepcaoPersistencia;
    public void lockWrite(ITurmaTurno turmaTurno) throws ExcepcaoPersistencia, ExistingPersistentException;
    public void delete(ITurmaTurno turmaTurno) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
    public List readByClass(ITurma group) throws ExcepcaoPersistencia;
	public List readClassesWithShift(ITurno turno) throws ExcepcaoPersistencia;    
}
