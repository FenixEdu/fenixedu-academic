/*
 * Created on 24/Jul/2003
 */

package ServidorAplicacao.strategy.groupEnrolment.strategys;

import java.util.ArrayList;
import java.util.List;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */

public class AtomicGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {

	public AtomicGroupEnrolmentStrategy() {
	}

	public boolean enrolmentPolicyNewGroup(IGroupProperties groupProperties, int numberOfStudentsToEnrole, ITurno shift) {
		boolean result = false;
		if (checkNumberOfGroups(groupProperties, shift)) {
			Integer maximumCapacity = groupProperties.getMaximumCapacity();
			Integer minimumCapacity = groupProperties.getMinimumCapacity();
			
			if(maximumCapacity == null && minimumCapacity == null)
				return true;
		
			if(minimumCapacity !=null && maximumCapacity ==null)
				if (numberOfStudentsToEnrole>=minimumCapacity.intValue())
					return true;
					
			if(maximumCapacity !=null && minimumCapacity ==null)
				if (numberOfStudentsToEnrole <= maximumCapacity.intValue())
					return true;
			
			if(maximumCapacity !=null && minimumCapacity !=null)
				if (numberOfStudentsToEnrole >= minimumCapacity.intValue()&& numberOfStudentsToEnrole <= maximumCapacity.intValue())
					result = true;
				
		}
		return result;

	}

	public boolean checkNumberOfGroupElements(IGroupProperties groupProperties, IStudentGroup studentGroup)
		throws ExcepcaoPersistencia {

		boolean result = false;
		Integer minimumCapacity = groupProperties.getMinimumCapacity();

		if (minimumCapacity == null)
			result = true;
		else {

			List allStudentGroupAttend = new ArrayList();
			try {

				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSuport.getIPersistentStudentGroupAttend();

				allStudentGroupAttend = (List) persistentStudentGroupAttend.readAllByStudentGroup(studentGroup);

			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			int numberOfGroupElements = allStudentGroupAttend.size();

			//se o nr de elementos do grupo for maior que o nr minimo,entao podemos desinscrever o aluno,caso contrario nao
			if (numberOfGroupElements > minimumCapacity.intValue())
				result = true;

		}
		return result;
	}
}

