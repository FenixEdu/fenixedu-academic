/*
 * Created on 21/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoCurricularCourseScope;
import Dominio.Branch;
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
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
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
	

	public List run(Integer oldCurricularCourseScopeId, InfoCurricularCourseScope newInfoCurricularCourseScope) throws FenixServiceException {

		IPersistentCurricularCourseScope persistentCurricularCourseScope = null;
		ICurricularCourseScope oldCurricularCourseScope = null;
		
		try {
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			IPersistentBranch persistentBranch = ps.getIPersistentBranch();
			IPersistentCurricularSemester persistentCurricularSemester = ps.getIPersistentCurricularSemester();
			persistentCurricularCourseScope = ps.getIPersistentCurricularCourseScope();			
			
			ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setIdInternal(oldCurricularCourseScopeId);
			
			oldCurricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope.readByOId(curricularCourseScope, false);
			ICurricularCourse curricularCourse = oldCurricularCourseScope.getCurricularCourse();	
				
						
			Integer branchId = newInfoCurricularCourseScope.getInfoBranch().getIdInternal();
		
			IBranch branch = new Branch();
			branch.setIdInternal(branchId);
			IBranch newBranch = (IBranch) persistentBranch.readByOId(branch, false);
	
			Integer curricularSemesterId = newInfoCurricularCourseScope.getInfoCurricularSemester().getIdInternal();
		
			ICurricularSemester curricularSemester = new CurricularSemester();
			curricularSemester.setIdInternal(curricularSemesterId);
			ICurricularSemester newCurricularSemester = (ICurricularSemester) persistentCurricularSemester.readByOId(curricularSemester, false);
		
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
		
			List errors = new ArrayList(3);
			
			errors.add(0,newCurricularSemester.getCurricularYear().getYear());	
			errors.add(1, newCurricularSemester.getSemester());
			errors.add(2, newBranch.getCode());	
			
			return errors;
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}

