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

	public void run(Integer oldCurricularCourseScopeId, InfoCurricularCourseScope newInfoCurricularCourseScope) throws FenixServiceException {

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

			Integer branchId = newInfoCurricularCourseScope.getInfoBranch().getIdInternal();

			IBranch branch = new Branch();
			branch.setIdInternal(branchId);
			IBranch newBranch = (IBranch) persistentBranch.readByOId(branch, false);

			Integer curricularSemesterId = newInfoCurricularCourseScope.getInfoCurricularSemester().getIdInternal();

			ICurricularSemester curricularSemester = new CurricularSemester();
			curricularSemester.setIdInternal(curricularSemesterId);
			ICurricularSemester newCurricularSemester = (ICurricularSemester) persistentCurricularSemester.readByOId(curricularSemester, false);

			if (oldCurricularCourseScope != null) {

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

				try {
					persistentCurricularCourseScope.lockWrite(oldCurricularCourseScope);
				} catch (ExistingPersistentException ex) {
					throw new ExistingServiceException(
						"O âmbito do "
							+ newCurricularSemester.getCurricularYear().getYear()
							+ "º ano/ "
							+ newCurricularSemester.getSemester()
							+ "º semestre, do ramo"
							+ newBranch.getCode(),
						ex);
				}

			}else
			throw new NonExistingServiceException();

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}
