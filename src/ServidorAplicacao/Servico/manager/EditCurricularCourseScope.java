/*
 * Created on 21/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoCurricularCourseScope;
import Dominio.Branch;
import Dominio.CurricularCourseScope;
import Dominio.CurricularSemester;
import Dominio.IBranch;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

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

	public void run(InfoCurricularCourseScope newInfoCurricularCourseScope) throws FenixServiceException {

		ICurricularCourseScope oldCurricularCourseScope = null;
		ICurricularSemester newCurricularSemester = null;
		IBranch newBranch = null;

		try {
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			IPersistentBranch persistentBranch = ps.getIPersistentBranch();
			IPersistentCurricularSemester persistentCurricularSemester = ps.getIPersistentCurricularSemester();
			IPersistentCurricularCourseScope persistentCurricularCourseScope = ps.getIPersistentCurricularCourseScope();
			
			Integer branchId = newInfoCurricularCourseScope.getInfoBranch().getIdInternal();
			IBranch branch = new Branch();
			branch.setIdInternal(branchId);
			newBranch = (IBranch) persistentBranch.readByOId(branch, false);
			
			if(newBranch == null)
				throw new NonExistingServiceException("message.non.existing.branch", null);

			Integer curricularSemesterId = newInfoCurricularCourseScope.getInfoCurricularSemester().getIdInternal();
			ICurricularSemester curricularSemester = new CurricularSemester();
			curricularSemester.setIdInternal(curricularSemesterId);
			newCurricularSemester = (ICurricularSemester) persistentCurricularSemester.readByOId(curricularSemester, false);

			if(newCurricularSemester == null)
				throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
				
			ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setIdInternal(newInfoCurricularCourseScope.getIdInternal());
			oldCurricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope.readByOId(curricularCourseScope, true);
			
			if(oldCurricularCourseScope == null)
				throw new NonExistingServiceException("message.non.existing.curricular.course.scope", null);

			oldCurricularCourseScope.setBranch(newBranch);
			//it already includes the curricular year
			oldCurricularCourseScope.setCurricularSemester(newCurricularSemester);
			oldCurricularCourseScope.setBeginDate(newInfoCurricularCourseScope.getBeginDate());
			oldCurricularCourseScope.setEndDate(null);
			
		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException("O âmbito com ramo "
			+ newBranch.getCode()
			+ ", do "
			+ newCurricularSemester.getCurricularYear().getYear()
			+ "º ano, "
			+ newCurricularSemester.getSemester()
			+ "º semestre");
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}
