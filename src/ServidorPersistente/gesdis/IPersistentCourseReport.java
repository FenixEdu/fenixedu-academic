/*
 * Created on 11/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorPersistente.gesdis;

import Dominio.IExecutionCourse;
import Dominio.gesdis.ICourseReport;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public interface IPersistentCourseReport extends IPersistentObject {

	public ICourseReport readCourseReportByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia;
}
