/*
 * Created on 6/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.transformers;

import org.apache.commons.collections.Transformer;

import Dominio.IExecutionCourse;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorPersistente.middleware.MigrationShift;
import ServidorPersistente.middleware.Utils.LessonTypeUtils;

/**
 * @author jpvl
 */
public class TransformerMigrationShift2Shift implements Transformer {

	/* (non-Javadoc)
	 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
	 */
	public Object transform(Object obj) {
		ITurno shift = null;
		if (obj != null) {

			MigrationShift migrationShift = (MigrationShift) obj;

			if (migrationShift.getShift() == null) {
				shift = new Turno();
				TransformerMigrationExecutionCourse2ExecutionCourse transf =
					new TransformerMigrationExecutionCourse2ExecutionCourse();
				shift.setNome(migrationShift.getShiftName());
				shift.setLotacao(migrationShift.getCapacity());
				shift.setTipo(
					LessonTypeUtils.convertLessonType(
						migrationShift.getType()));
				IExecutionCourse executionCourse =
					(IExecutionCourse) transf.transform(
						migrationShift.getMigrationExecutionCourse());
				shift.setDisciplinaExecucao(executionCourse);
				migrationShift.setShift(shift);
			} else {
				shift = migrationShift.getShift();
			}
		}
		return shift;
	}
}
