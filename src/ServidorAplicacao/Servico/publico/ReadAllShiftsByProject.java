/*
 * Created on 2/Ago/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadAllShiftsByProject implements IServico {

	private static ReadAllShiftsByProject _servico =
		new ReadAllShiftsByProject();

	/**
	 * The actor of this class.
	 **/
	private ReadAllShiftsByProject() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ReadAllShiftsByProject";
	}

	/**
	 * Returns the _servico.
	 * @return ReadAllShiftsByProject
	 */
	public static ReadAllShiftsByProject getService() {
		return _servico;
	}

	public List run(Integer groupPropertiesCode)
		throws ExcepcaoInexistente, FenixServiceException {

		List infoShiftsList = null;
		List shiftsInternalCodeList = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IGroupProperties groupProperties =
				(IGroupProperties) sp
					.getIPersistentGroupProperties()
					.readByOId(
					new GroupProperties(groupPropertiesCode),
					false);
			List allStudentsGroup =
				sp
					.getIPersistentStudentGroup()
					.readAllStudentGroupByGroupProperties(
					groupProperties);
			
			
			ITurno shift = null;	
			shiftsInternalCodeList = new ArrayList();
			Iterator iterator = allStudentsGroup.iterator();

			while (iterator.hasNext()) 
			{
				shift = ((IStudentGroup) iterator.next()).getShift();
				if(!shiftsInternalCodeList.contains(shift.getIdInternal())&& shift!=null)
				{
					if(shift !=null)
					{
						shiftsInternalCodeList.add(shift.getIdInternal());
					}
					else
					{
						shiftsInternalCodeList.add(shift.getIdInternal());
					}
				}
					

			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException("error.impossibleReadAllShiftsByProject");
		}

		return infoShiftsList;
	}

}
