/*
 * Created on 6/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.transformers;

import org.apache.commons.collections.Transformer;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorPersistente.middleware.MigrationExecutionCourse;
import ServidorPersistente.middleware.constants.Constants;

/**
 * @author jpvl
 */
public class TransformerMigrationExecutionCourse2ExecutionCourse
	implements Transformer {

	/* (non-Javadoc)
	 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
	 */
	public Object transform(Object obj) {
		MigrationExecutionCourse migrationExecutionCourse =
			(MigrationExecutionCourse) obj;
		IExecutionCourse executionCourse = null;
		if (migrationExecutionCourse.getExecutionCourse() == null) {
			executionCourse = new ExecutionCourse();

			executionCourse.setAssociatedCurricularCourses(
				executionCourse.getAssociatedCurricularCourses());

			executionCourse.setLabHours(
				new Double(migrationExecutionCourse.getLaboratoryHours()));
			executionCourse.setTheoreticalHours(
				new Double(migrationExecutionCourse.getTheoreticalHours()));
			executionCourse.setPraticalHours(
				new Double(migrationExecutionCourse.getPracticalHours()));
			executionCourse.setTheoPratHours(
				new Double(
					migrationExecutionCourse.getTheoreticalPraticalHours()));

			executionCourse.setSigla(migrationExecutionCourse.getInitials());
			executionCourse.setNome(migrationExecutionCourse.getName());
			executionCourse.setExecutionPeriod(Constants.executionPeriod);
			executionCourse.setAssociatedCurricularCourses(migrationExecutionCourse.getAssociatedCurricularCourses());
			migrationExecutionCourse.setExecutionCourse(executionCourse);
		} else{
			executionCourse = migrationExecutionCourse.getExecutionCourse();
		}
		
		return executionCourse;
	}

}
