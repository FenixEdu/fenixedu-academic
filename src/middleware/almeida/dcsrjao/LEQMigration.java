package middleware.almeida.dcsrjao;


/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LEQMigration{


	private LEQMigration() {
	}

	public static void main(String[] args) {
		LoadAlmeidaLEQCurricularCourses loadAlmeidaLEQCurricularCourses = new LoadAlmeidaLEQCurricularCourses();
		loadAlmeidaLEQCurricularCourses.run();
		// TODO DAVID-RICARDO: Adicionar flag de Activo/Inactivo no CurricularCourseScope.
	}

}