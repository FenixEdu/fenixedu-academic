/*
 * ITurnoAulaPersistente.java
 *
 * Created on 22 de Outubro de 2002, 9:17
 */

package ServidorPersistente;

/**
 *
 * @author  tfc130
 */
import java.util.Calendar;
import java.util.List;

import Dominio.IAula;
import Dominio.ISala;
import Dominio.ITurno;
import Dominio.ITurnoAula;
import Util.DiaSemana;


public interface ITurnoAulaPersistente extends IPersistentObject{
    public ITurnoAula readByShiftAndLesson(ITurno shift, IAula lesson)
               throws ExcepcaoPersistencia;
    
    public void delete(ITurnoAula turnoAula) throws ExcepcaoPersistencia;
   
	
	
	public List readLessonsByStudent(String username) throws ExcepcaoPersistencia;
	
	
	public List readByShift(ITurno shift) throws ExcepcaoPersistencia;
	public void delete(ITurno shift, DiaSemana diaSemana, Calendar inicio, Calendar fim,
					   ISala sala) throws ExcepcaoPersistencia;
					   
	// FIXME: readByCriteria Legacy : readByShift does almost the same thing
	public List readLessonsByShift(ITurno shift) throws ExcepcaoPersistencia;					   

}
