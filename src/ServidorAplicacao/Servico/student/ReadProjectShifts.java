/*
 * Created on 28/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteProjectShifts;
import DataBeans.InfoSiteShift;
import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */
public class ReadProjectShifts implements IServico {

	private static ReadProjectShifts service = new ReadProjectShifts();

	/**
		* The singleton access method of this class.
		*/
	public static ReadProjectShifts getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private ReadProjectShifts() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "ReadProjectShifts";
	}

	/**
	 * Executes the service.
	 */
	public ISiteComponent run(Integer groupPropertiesCode) throws FenixServiceException {

		InfoSiteProjectShifts infoSiteProjectShifts = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ITurnoPersistente persistentShift = sp.getITurnoPersistente();
			IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();

			IGroupProperties groupProperties =
				(IGroupProperties) sp.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode), false);

			List allShifts =
				persistentShift.readByExecutionCourseAndType(
					groupProperties.getExecutionCourse(),
					groupProperties.getShiftType().getTipo());

			List allStudentsGroup = sp.getIPersistentStudentGroup().readAllStudentGroupByGroupProperties(groupProperties);

			if (allStudentsGroup.size() != 0) {
				infoSiteProjectShifts = new InfoSiteProjectShifts();

				Iterator iterator = allStudentsGroup.iterator();
				while (iterator.hasNext()) {
					ITurno shift = ((IStudentGroup) iterator.next()).getShift();
					if (!allShifts.contains(shift)) {
						allShifts.add(shift);

					}
				}
			}

			if (allShifts.size() != 0) {
				Iterator iter = allShifts.iterator();
				List infoSiteShifts = new ArrayList();
				InfoSiteShift infoSiteShift = null;

				while (iter.hasNext()) {
					ITurno shift = (ITurno) iter.next();
					List allStudentGroups =
						persistentStudentGroup.readAllStudentGroupByGroupPropertiesAndShift(groupProperties, shift);

					infoSiteShift = new InfoSiteShift();
					infoSiteShift.setInfoShift(Cloner.copyIShift2InfoShift(shift));
					if (groupProperties.getGroupMaximumNumber() != null) {

						int vagas = groupProperties.getGroupMaximumNumber().intValue() - allStudentGroups.size();
						if (vagas >= 0)
							infoSiteShift.setNrOfGroups(new Integer(vagas));
						else
							infoSiteShift.setNrOfGroups(new Integer(0));
					} else
						infoSiteShift.setNrOfGroups("Sem limite");
					infoSiteShifts.add(infoSiteShift);
					/* Sort the list of shifts */
					ComparatorChain chainComparator = new ComparatorChain();
					chainComparator.addComparator(new BeanComparator("infoShift.tipo"));
					chainComparator.addComparator(new BeanComparator("infoShift.nome"));
					Collections.sort(infoSiteShifts, chainComparator);
				}

				infoSiteProjectShifts = new InfoSiteProjectShifts();
				infoSiteProjectShifts.setInfoSiteShifts(infoSiteShifts);

			}

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException("error.impossibleReadProjectShifts");
		}

		return infoSiteProjectShifts;
	}
}
