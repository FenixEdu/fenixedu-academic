/*
 * Created on 26/Ago/2003
 *

 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.ISiteComponent;
import DataBeans.InfoShift;
import DataBeans.InfoSiteGroupsByShift;
import DataBeans.InfoSiteShift;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.InfoStudentGroup;
import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */
public class ReadStudentGroups implements IServico {

	private static ReadStudentGroups _servico = new ReadStudentGroups();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadStudentGroups getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadStudentGroups() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadStudentGroups";
	}

	public ISiteComponent run(Integer groupPropertiesCode, Integer shiftCode) throws FenixServiceException {

		InfoSiteGroupsByShift infoSiteGroupsByShift = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudentGroupAttend persistentStudentGroupAttend = sp.getIPersistentStudentGroupAttend();

			IGroupProperties groupProperties =
				(IGroupProperties) sp.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode), false);
			ITurno shift = (ITurno) sp.getITurnoPersistente().readByOId(new Turno(shiftCode), false);

			List allStudentsGroupByShift =
				sp.getIPersistentStudentGroup().readAllStudentGroupByGroupPropertiesAndShift(groupProperties, shift);

			infoSiteGroupsByShift = new InfoSiteGroupsByShift();
			InfoShift infoShift = Cloner.copyIShift2InfoShift(shift);
			InfoSiteShift infoSiteShift = new InfoSiteShift();
			infoSiteShift.setInfoShift(infoShift);
			if (groupProperties.getGroupMaximumNumber() != null) {

				int vagas = groupProperties.getGroupMaximumNumber().intValue() - allStudentsGroupByShift.size();
				if (vagas >= 0)
					infoSiteShift.setNrOfGroups(new Integer(vagas));
				else
					infoSiteShift.setNrOfGroups(new Integer(0));
			} else
				infoSiteShift.setNrOfGroups("Sem limite");
			infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);

			if (allStudentsGroupByShift.size() != 0) {

				List infoStudentGroupList = new ArrayList(allStudentsGroupByShift.size());
				Iterator iter = allStudentsGroupByShift.iterator();
				InfoSiteStudentGroup infoSiteStudentGroup = null;
				while (iter.hasNext()) {
					IStudentGroup studentGroup = (IStudentGroup) iter.next();
					List allSGAttend = persistentStudentGroupAttend.readAllByStudentGroup(studentGroup);
					InfoStudentGroup infoStudentGroup = Cloner.copyIStudentGroup2InfoStudentGroup(studentGroup);

					infoSiteStudentGroup = new InfoSiteStudentGroup();
					infoSiteStudentGroup.setInfoStudentGroup(infoStudentGroup);
					if (groupProperties.getMaximumCapacity() != null) {

						int vagas = groupProperties.getMaximumCapacity().intValue() - allSGAttend.size();
						if (vagas >= 0)
							infoSiteStudentGroup.setNrOfElements(new Integer(vagas));
						else
							infoSiteStudentGroup.setNrOfElements(new Integer(0));
					} else
						infoSiteStudentGroup.setNrOfElements("Sem limite");
				
					infoStudentGroupList.add(infoSiteStudentGroup);

				}
				
				Collections.sort(infoStudentGroupList, new BeanComparator("infoStudentGroup.groupNumber"));
				infoSiteGroupsByShift.setInfoSiteStudentGroupsList(infoStudentGroupList);

			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException("error.impossibleReadStudentGroups");
		}

		return infoSiteGroupsByShift;
	}
}