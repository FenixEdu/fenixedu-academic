package ServidorAplicacao.Servico.masterDegree.administrativeOffice.thesis;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.IEmployee;
import Dominio.IMasterDegreeThesisDataVersion;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import Dominio.MasterDegreeThesisDataVersion;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.GuiderAlreadyChosenServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.RequiredGuidersServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.State;

/**
 * 
 * @author
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public class ChangeMasterDegreeThesisData implements IServico {

	private static ChangeMasterDegreeThesisData servico = new ChangeMasterDegreeThesisData();

	/**
	 * The singleton access method of this class.
	 **/
	public static ChangeMasterDegreeThesisData getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ChangeMasterDegreeThesisData() {
	}

	/**
	 * Returns The Service Name */
	public final String getNome() {
		return "ChangeMasterDegreeThesisData";
	}

	public void run(
		IUserView userView,
		InfoStudentCurricularPlan infoStudentCurricularPlan,
		String dissertationTitle,
		ArrayList infoTeacherGuiders,
		ArrayList infoTeacherAssistentGuiders,
		ArrayList infoExternalPersonExternalAssistentGuiders)
		throws FenixServiceException {

		try {

			if (infoTeacherGuiders.size() < 1)
				throw new RequiredGuidersServiceException("error.exception.masterDegree.noGuidersSelected");

			for (Iterator iter = infoTeacherGuiders.iterator(); iter.hasNext();) {
				InfoTeacher guider = (InfoTeacher) iter.next();
				
				for (Iterator iterator = infoTeacherAssistentGuiders.iterator(); iterator.hasNext();) {
					InfoTeacher assistentGuider = (InfoTeacher) iterator.next();
					if (assistentGuider.getIdInternal().equals(guider.getIdInternal())) {
						throw new GuiderAlreadyChosenServiceException("error.exception.masterDegree.guiderAlreadyChosen");
					}
				}
			}

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlan studentCurricularPlan = Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);

			IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion =
				sp.getIPersistentMasterDegreeThesisDataVersion().readActiveByStudentCurricularPlan(studentCurricularPlan);
			if (storedMasterDegreeThesisDataVersion == null)
				throw new NonExistingServiceException("error.exception.masterDegree.nonExistentMasterDegreeThesis");

			storedMasterDegreeThesisDataVersion.setCurrentState(new State(State.INACTIVE));
			sp.getIPersistentMasterDegreeThesisDataVersion().simpleLockWrite(storedMasterDegreeThesisDataVersion);

			IMasterDegreeThesisDataVersion masterDegreeThesisDataVersionWithChosenDissertationTitle =
				sp.getIPersistentMasterDegreeThesisDataVersion().readActiveByDissertationTitle(dissertationTitle);
			if (masterDegreeThesisDataVersionWithChosenDissertationTitle != null)
				if (!masterDegreeThesisDataVersionWithChosenDissertationTitle
					.getMasterDegreeThesis()
					.getStudentCurricularPlan()
					.equals(studentCurricularPlan))
					throw new ExistingServiceException("error.exception.masterDegree.dissertationTitleAlreadyChosen");

			IPessoa person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
			IEmployee employee = sp.getIPersistentEmployee().readByPerson(person.getIdInternal().intValue());

			IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion =
				new MasterDegreeThesisDataVersion(
					storedMasterDegreeThesisDataVersion.getMasterDegreeThesis(),
					employee,
					dissertationTitle,
					new Timestamp(new Date().getTime()),
					new State(State.ACTIVE));
			List guiders = Cloner.copyListInfoTeacher2ListITeacher(infoTeacherGuiders);
			List assistentGuiders = Cloner.copyListInfoTeacher2ListITeacher(infoTeacherAssistentGuiders);
			List externalAssistentGuiders = Cloner.copyListInfoExternalPerson2ListIExternalPerson(infoExternalPersonExternalAssistentGuiders);
			masterDegreeThesisDataVersion.setGuiders(guiders);
			masterDegreeThesisDataVersion.setAssistentGuiders(assistentGuiders);
			masterDegreeThesisDataVersion.setExternalAssistentGuiders(externalAssistentGuiders);
			sp.getIPersistentMasterDegreeThesisDataVersion().simpleLockWrite(masterDegreeThesisDataVersion);

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

	}
}