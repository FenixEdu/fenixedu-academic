/*
 * Created on 21/Jul/2003
 *
 * 
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IExecutionCourse;
import Dominio.ISummary;
import Util.TipoAula;

/**
 * @author João Mota
 *
 * 21/Jul/2003
 * fenix-head
 * ServidorPersistente.OJB
 * 
 */
public interface IPersistentSummary extends IPersistentObject{
	public  List readByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia;		
	public  List readByExecutionCourseAndType(
		IExecutionCourse executionCourse,
		TipoAula summaryType)
		throws ExcepcaoPersistencia;
	public abstract void delete (ISummary summary) throws ExcepcaoPersistencia;	
}