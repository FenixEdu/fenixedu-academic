/*
 * Created on 26/Ago/2003
 *

 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteAllGroups;
import DataBeans.InfoSiteGroupsByShift;
import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */
public class ReadAllShiftsAndGroupsByProject implements IServico {

	private static ReadAllShiftsAndGroupsByProject _servico = new ReadAllShiftsAndGroupsByProject();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadAllShiftsAndGroupsByProject getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadAllShiftsAndGroupsByProject() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadAllShiftsAndGroupsByProject";
	}

	public ISiteComponent run(Integer groupPropertiesCode) throws FenixServiceException {

		InfoSiteAllGroups infoSiteAllGroups = null;
		List infoSiteGroupsByShiftList = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IGroupProperties groupProperties =
				(IGroupProperties) sp.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode), false);
			List allStudentsGroup = sp.getIPersistentStudentGroup().readAllStudentGroupByGroupProperties(groupProperties);
			if (allStudentsGroup.size() != 0) {
				
				infoSiteAllGroups = new InfoSiteAllGroups();
				ITurno shift = null;
				List shiftsInternalList = new ArrayList();
				Iterator iterator = allStudentsGroup.iterator();

				while (iterator.hasNext()) {
					shift = ((IStudentGroup) iterator.next()).getShift();
					if (!shiftsInternalList.contains(shift))
						shiftsInternalList.add(shift);
				}

				Iterator iterator2 = shiftsInternalList.iterator();
				List studentGroupsList = null;
				InfoSiteGroupsByShift infoSiteGroupsByShift = null;
				shift = null;
				infoSiteGroupsByShiftList = new ArrayList(shiftsInternalList.size());
				while (iterator2.hasNext()) {
					shift = (ITurno) iterator2.next();
					studentGroupsList =
						sp.getIPersistentStudentGroup().readAllStudentGroupByGroupPropertiesAndShift(groupProperties, shift);

					List infoStudentGroupList = new ArrayList(studentGroupsList.size());
					Iterator iter = studentGroupsList.iterator();
					while (iter.hasNext())
						infoStudentGroupList.add(Cloner.copyIStudentGroup2InfoStudentGroup((IStudentGroup) iter.next()));

					infoSiteGroupsByShift = new InfoSiteGroupsByShift();
					infoSiteGroupsByShift.setInfoStudentGroupsList(infoStudentGroupList);
					infoSiteGroupsByShift.setInfoShift(Cloner.copyIShift2InfoShift(shift));
					infoSiteGroupsByShiftList.add(infoSiteGroupsByShift);

				}
				infoSiteAllGroups.setInfoSiteGroupsByShiftList(infoSiteGroupsByShiftList);
			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException("error.impossibleReadAllShiftsByProject");
		}

		return infoSiteAllGroups;
	}
}