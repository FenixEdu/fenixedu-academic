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
	

	public List run(Integer oldCurricularCourseScopeId, InfoCurricularCourseScope newInfoCurricularCourseScope, Integer curricularCourseId) throws FenixServiceException {

		IPersistentCurricularCourseScope persistentCurricularCourseScope = null;
		ICurricularCourseScope oldCurricularCourseScope = null;
		
		try {
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			IPersistentBranch persistentBranch = ps.getIPersistentBranch();

			IPersistentCurricularSemester persistentCurricularSemester = ps.getIPersistentCurricularSemester();
			
			persistentCurricularCourseScope = ps.getIPersistentCurricularCourseScope();
			IPersistentCurricularCourse persistentCurricularCourse = ps.getIPersistentCurricularCourse();
//			IPersistentDegreeCurricularPlan persistentDegreeCP = ps.getIPersistentDegreeCurricularPlan();
			oldCurricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope.readByOId(new CurricularCourseScope(oldCurricularCourseScopeId), false);
			ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(curricularCourseId), false);
			
			
			
//			ICurricularSemester newCurricularSemester = (ICurricularSemester) persistentCurricularSemester.readByOId(new CurricularSemester(curricularSemesterId), false);
//			IDegreeCurricularPlan degreeCP = (IDegreeCurricularPlan) persistentDegreeCP.readByOId(new DegreeCurricularPlan(degreeCPId), false);
//			String newBranchCode = newInfoCurricularCourseScope.getInfoBranch().getCode();
			
			Integer branchId = newInfoCurricularCourseScope.getInfoBranch().getIdInternal();
//			
//			IBranch branch = new Branch();
//			branch.setIdInternal(branchId);
			IBranch newBranch = (IBranch) persistentBranch.readByOId(new Branch(branchId), false);
	
	//ha 2 getId tem que ser o do IDomainObject
			Integer curricularSemesterId = newInfoCurricularCourseScope.getInfoCurricularSemester().getIdInternal();
			
			
			//so da com o construtor como ta
//			ICurricularSemester curricularSemester = new CurricularSemester();
//			curricularSemester.setIdInternal(curricularSemesterId);
			ICurricularSemester newCurricularSemester = (ICurricularSemester) persistentCurricularSemester.readByOId(new CurricularSemester(curricularSemesterId), false);
			System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDd"+ new CurricularSemester(curricularSemesterId)+"DDDDD"+newBranch+"DDDDD"+curricularCourse);
				
			ICurricularCourseScope newCurricularCourseScope = 
			        persistentCurricularCourseScope.readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(curricularCourse, newCurricularSemester, newBranch );
System.out.println("AAAAAAAAAAAAAAAAAAaAAAAAAAAAAAnewCurricularCourseScope"+newCurricularCourseScope);
		
		
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
				System.out.println("2222222222222222CurricularCourseScopeQUE ESCREVEU NA BD"+oldCurricularCourseScope);
				
				persistentCurricularCourseScope.simpleLockWrite(oldCurricularCourseScope);
				return null;
			}
		
			List errors = new ArrayList(2);
			
			errors.add(0, newCurricularSemester.getSemester());
			errors.add(1, newBranch.getCode());	
			
			return errors;
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}

