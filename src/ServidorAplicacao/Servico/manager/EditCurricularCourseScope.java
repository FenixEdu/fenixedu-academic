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
import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
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
	

	public List run(Integer oldCurricularCourseScopeId, InfoCurricularCourseScope newInfoCurricularCourseScope, Integer curricularCourseId, Integer degreeCPId, Integer curricularSemesterId) throws FenixServiceException {
	
		IPersistentCurricularCourseScope persistentCurricularCourseScope = null;
		ICurricularCourseScope oldCurricularCourseScope = null;
		
		try {
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			IPersistentBranch persistentBranch = ps.getIPersistentBranch();
			IPersistentCurricularSemester persistentCurricularSemester = ps.getIPersistentCurricularSemester();
//			IPersistentCurricularYear persistentCurricularYear =ps.getIPersistentCurricularYear();
			
			persistentCurricularCourseScope = ps.getIPersistentCurricularCourseScope();
			IPersistentCurricularCourse persistentCurricularCourse = ps.getIPersistentCurricularCourse();
			IPersistentDegreeCurricularPlan persistentDegreeCP = ps.getIPersistentDegreeCurricularPlan();
			oldCurricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope.readByOId(new CurricularCourseScope(oldCurricularCourseScopeId), false);
			ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(curricularCourseId), false);
			
			
			
//			ICurricularSemester curricularSemester = new CurricularSemester();
//			curricularSemester.setIdInternal(curricularSemesterId);			
			ICurricularSemester newCurricularSemester = (ICurricularSemester) persistentCurricularSemester.readByOId(new CurricularSemester(curricularSemesterId), false);
			System.out.println("222222222222222222222222222222"+curricularSemesterId);
			System.out.println("222222222222222222222222222222"+newCurricularSemester);
			
			IDegreeCurricularPlan degreeCP = (IDegreeCurricularPlan) persistentDegreeCP.readByOId(new DegreeCurricularPlan(degreeCPId), false);

			String newBranchCode = newInfoCurricularCourseScope.getInfoBranch().getCode();
//			System.out.println("SERVICO NOVO CODIGO DO BRANCH"+newBranchCode);
////	VER S O BRANCH EO SEMESTER EXISTEM

			IBranch newBranch = (IBranch) persistentBranch.readBranchByDegreeCurricularPlanAndCode(degreeCP,newBranchCode);
	
			if(newBranch == null){
				newBranch = new Branch();
				newBranch.setCode(newBranchCode);
				
				
				///soh por agora->vai passar a aparecer tb no form ou entao eh so pa escolher dos que ha ->ULTIMSA HIPOTESE MAIS PROVAVEL
				newBranch.setName(newBranchCode);
				persistentBranch.simpleLockWrite(newBranch);
				}	
	
			System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDd"+ newCurricularSemester+"DDDDD"+newBranch+"DDDDD"+curricularCourse);
				
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
				System.out.println("AINDA TA AQUI!!!!!!!!!1SERVICO"+newCurricularSemester.getInternalID());
				
				persistentCurricularCourseScope.simpleLockWrite(oldCurricularCourseScope);
				return null;
			}
		
			List errors = new ArrayList(2);
			
			errors.add(0, newCurricularSemester.getSemester());
			errors.add(1, newBranchCode);	
			
			return errors;
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}

