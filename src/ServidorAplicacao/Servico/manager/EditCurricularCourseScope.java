/*
 * Created on 21/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoCurricularCourseScope;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class EditCurricularCourseScope implements IServico {

	private static EditCurricularCourseScope service = new EditCurricularCourseScope();

	public static EditCurricularCourseScope getService() {
		return service;
	}

	private EditCurricularCourseScope() {
	}

	public final String getNome() {
		return "EditCurricularCourseScope";
	}
	

	public List run(Integer oldCurricularCourseScopeId, InfoCurricularCourseScope newInfoCurricularCourseScope, Integer curricularCourseId, Integer degreeCPId) throws FenixServiceException {
	
		IPersistentCurricularCourseScope persistentCurricularCourseScope = null;
		ICurricularCourseScope oldCurricularCourseScope = null;
		
		try {
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			IPersistentBranch persistentBranch = ps.getIPersistentBranch();
			IPersistentCurricularSemester persistentCurricularSemester = ps.getIPersistentCurricularSemester();
			IPersistentCurricularYear persistentCurricularYear =ps.getIPersistentCurricularYear();
			
			persistentCurricularCourseScope = ps.getIPersistentCurricularCourseScope();
			IPersistentCurricularCourse persistentCurricularCourse = ps.getIPersistentCurricularCourse();
			IPersistentDegreeCurricularPlan persistentDegreeCP = ps.getIPersistentDegreeCurricularPlan();
			oldCurricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope.readByOId(new CurricularCourseScope(oldCurricularCourseScopeId), false);
			ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(curricularCourseId), false);
			
			
			//arguments to read a curricular course scope
			//curricular semester
			Integer newSemester = newInfoCurricularCourseScope.getInfoCurricularSemester().getSemester();
			Integer newYear = newInfoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear();
			ICurricularYear newCurricularYear = persistentCurricularYear.readCurricularYearByYear(newYear); 			
			ICurricularSemester newCurricularSemester = (ICurricularSemester) persistentCurricularSemester.readCurricularSemesterBySemesterAndCurricularYear(newSemester, newCurricularYear);
			
			//branch
			IDegreeCurricularPlan degreeCP = (IDegreeCurricularPlan) persistentDegreeCP.readByOId(new DegreeCurricularPlan(degreeCPId), false);
			String newBranchCode = newInfoCurricularCourseScope.getInfoBranch().getCode();
			IBranch newBranch = (IBranch) persistentBranch.readBranchByDegreeCurricularPlanAndCode(degreeCP,newBranchCode);
	
			ICurricularCourseScope newCurricularCourseScope = 
			        persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(curricularCourse, newCurricularSemester, newBranch );
		
			if(newCurricularCourseScope == null) {
				newCurricularCourseScope = new CurricularCourseScope();
				oldCurricularCourseScope.setCredits(newInfoCurricularCourseScope.getCredits());
				oldCurricularCourseScope.setTheoreticalHours(newInfoCurricularCourseScope.getTheoreticalHours());
				oldCurricularCourseScope.setPraticalHours(newInfoCurricularCourseScope.getPraticalHours());
				oldCurricularCourseScope.setTheoPratHours(newInfoCurricularCourseScope.getTheoPratHours());
				oldCurricularCourseScope.setLabHours(newInfoCurricularCourseScope.getLabHours());
				oldCurricularCourseScope.setMaxIncrementNac(newInfoCurricularCourseScope.getMaxIncrementNac());
				oldCurricularCourseScope.setMinIncrementNac(newInfoCurricularCourseScope.getMinIncrementNac());
				oldCurricularCourseScope.setWeigth(newInfoCurricularCourseScope.getWeigth());
				
				oldCurricularCourseScope.setBranch(newBranch);
				//it already includes the curricular year
				oldCurricularCourseScope.setCurricularSemester(newCurricularSemester);
				
				persistentCurricularCourseScope.simpleLockWrite(oldCurricularCourseScope);
				return null;
			}
		
			List errors = new ArrayList(2);
			
			errors.add(0, newSemester);
			errors.add(1, newBranchCode);	
			
			return errors;
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}

