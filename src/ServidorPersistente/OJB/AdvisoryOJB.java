package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IAdvisory;
import Dominio.IPessoa;
import Dominio.Pessoa;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentAdvisory;
import Util.AdvisoryRecipients;
import Util.RoleType;

/**
 * Created on 2003/09/06
 * @author Luis Cruz
 * Package  ServidorPersistente.OJB
 */
public class AdvisoryOJB
	extends ObjectFenixOJB
	implements IPersistentAdvisory {

	/**
	 * Constructor for ExecutionYearOJB.
	 */
	public AdvisoryOJB() {
		super();
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IPersistentAdvisory#write(Dominio.IAdvisory, Util.AdvisoryRecipients)
	 */
	public void write(
		IAdvisory advisory,
		AdvisoryRecipients advisoryRecipients)
		throws ExcepcaoPersistencia {
		lockWrite(advisory);

		Criteria criteria = new Criteria();
		if (advisoryRecipients.equals(AdvisoryRecipients.STUDENTS)) {
			criteria.addEqualTo("personRoles.roleType", RoleType.getEnum(RoleType.STUDENT_TYPE));
		}
		if (advisoryRecipients.equals(AdvisoryRecipients.TEACHERS)) {
			criteria.addEqualTo("personRoles.roleType", RoleType.getEnum(RoleType.TEACHER_TYPE));
		}
		if (advisoryRecipients.equals(AdvisoryRecipients.EMPLOYEES)) {
			criteria.addEqualTo("personRoles.roleType", RoleType.getEnum(RoleType.EMPLOYEE_TYPE));
			criteria.addNotEqualTo("personRoles.roleType", RoleType.getEnum(RoleType.TEACHER_TYPE));
		}
		List recipients = queryList(Pessoa.class, criteria, true);
		for (int i = 0; i < recipients.size(); i++) {
			IPessoa person = (IPessoa) recipients.get(i); 
			lockWrite(person);
			person.getAdvisories().add(advisory);
		}
	}
}
