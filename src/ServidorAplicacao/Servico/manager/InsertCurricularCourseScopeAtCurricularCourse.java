/*
 * Created on 21/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.List;

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
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
	

	public List run(InfoCurricularCourseScope infoCurricularCourseScope) throws FenixServiceException {

		IPersistentCurricularCourseScope persistentCurricularCourseScope = null;
	
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			
				IPersistentCurricularSemester persistentCurricularSemester = persistentSuport.getIPersistentCurricularSemester();
				ICurricularSemester semester = new CurricularSemester();
				semester.setIdInternal(infoCurricularCourseScope.getInfoCurricularSemester().getIdInternal());
				ICurricularSemester curricularSemester = (ICurricularSemester) persistentCurricularSemester.readByOId(semester, false);
			
				IPersistentCurricularCourse persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
				ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(infoCurricularCourseScope.getInfoCurricularCourse().getIdInternal()), false);
			
				IPersistentBranch persistentBranch = persistentSuport.getIPersistentBranch();
				IBranch temporaryBranch = new Branch();
				temporaryBranch.setIdInternal(infoCurricularCourseScope.getInfoBranch().getIdInternal());
				IBranch branch = (IBranch) persistentBranch.readByOId(temporaryBranch, false);
			
				persistentCurricularCourseScope = persistentSuport.getIPersistentCurricularCourseScope();
				ICurricularCourseScope curricularCourseScope = persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(curricularCourse, curricularSemester, branch);
//				check if the scope can be written in database
				if(curricularCourseScope == null) {
					curricularCourseScope = new CurricularCourseScope();
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
					persistentCurricularCourseScope.simpleLockWrite(curricularCourseScope);
					return null;
				}
			
//				if the scope already exists
				List resultErrors = new ArrayList(3);
				resultErrors.add(branch.getCode());
				resultErrors.add(curricularSemester.getCurricularYear().getYear());
				resultErrors.add(curricularSemester.getSemester());
				return resultErrors;
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}