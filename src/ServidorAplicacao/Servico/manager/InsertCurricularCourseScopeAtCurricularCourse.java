/*
 * Created on 21/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoCurricularCourseScope;
import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.CurricularSemester;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */

public class InsertCurricularCourseScopeAtCurricularCourse implements IServico {

	private static InsertCurricularCourseScopeAtCurricularCourse service = new InsertCurricularCourseScopeAtCurricularCourse();

	public static InsertCurricularCourseScopeAtCurricularCourse getService() {
		return service;
	}

	private InsertCurricularCourseScopeAtCurricularCourse() {
	}

	public final String getNome() {
		return "InsertCurricularCourseScopeAtCurricularCourse";
	}
	

	public void run(InfoCurricularCourseScope infoCurricularCourseScope) throws FenixServiceException {
	
		IBranch branch = null;
		ICurricularSemester curricularSemester = null;
		
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			
				IPersistentCurricularSemester persistentCurricularSemester = persistentSuport.getIPersistentCurricularSemester();
				ICurricularSemester semester = new CurricularSemester();
				semester.setIdInternal(infoCurricularCourseScope.getInfoCurricularSemester().getIdInternal());
				curricularSemester = (ICurricularSemester) persistentCurricularSemester.readByOId(semester, false);
			
				IPersistentCurricularCourse persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
				ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(infoCurricularCourseScope.getInfoCurricularCourse().getIdInternal()), false);
			
				IPersistentBranch persistentBranch = persistentSuport.getIPersistentBranch();
				IBranch temporaryBranch = new Branch();
				temporaryBranch.setIdInternal(infoCurricularCourseScope.getInfoBranch().getIdInternal());
				branch = (IBranch) persistentBranch.readByOId(temporaryBranch, false);
			
				IPersistentCurricularCourseScope persistentCurricularCourseScope = persistentSuport.getIPersistentCurricularCourseScope();
				
				ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
				curricularCourseScope.setBranch(branch);
				curricularCourseScope.setCredits(infoCurricularCourseScope.getCredits());
				curricularCourseScope.setCurricularCourse(curricularCourse);
				curricularCourseScope.setCurricularSemester(curricularSemester);
				curricularCourseScope.setLabHours(infoCurricularCourseScope.getLabHours());
				curricularCourseScope.setMaxIncrementNac(infoCurricularCourseScope.getMaxIncrementNac());
				curricularCourseScope.setMinIncrementNac(infoCurricularCourseScope.getMinIncrementNac());
				curricularCourseScope.setPraticalHours(infoCurricularCourseScope.getPraticalHours());
				curricularCourseScope.setTheoPratHours(infoCurricularCourseScope.getTheoPratHours());
				curricularCourseScope.setTheoreticalHours(infoCurricularCourseScope.getTheoreticalHours());
				curricularCourseScope.setWeigth(infoCurricularCourseScope.getWeigth());
					
				persistentCurricularCourseScope.lockWrite(curricularCourseScope);
					
		} catch (ExistingPersistentException existingException) {
			throw new ExistingServiceException("O âmbito com ramo " + branch.getCode() + ", do " + curricularSemester.getCurricularYear().getYear() + "º ano, " + curricularSemester.getSemester() + "º semestre", existingException); 
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}