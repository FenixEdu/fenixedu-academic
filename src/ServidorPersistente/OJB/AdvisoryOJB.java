package ServidorPersistente.OJB;

import java.util.Iterator;
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
 * 
 * @author Luis Cruz Package ServidorPersistente.OJB
 */
public class AdvisoryOJB extends ObjectFenixOJB implements IPersistentAdvisory
{

	/**
	 * Constructor for ExecutionYearOJB.
	 */
	public AdvisoryOJB()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentAdvisory#write(Dominio.IAdvisory, Util.AdvisoryRecipients)
	 */
	public void write(IAdvisory advisory, AdvisoryRecipients advisoryRecipients)
		throws ExcepcaoPersistencia
	{
		simpleLockWrite(advisory);

		Criteria criteria = new Criteria();
		if (advisoryRecipients.equals(AdvisoryRecipients.STUDENTS))
		{
			criteria.addEqualTo("personRoles.roleType", RoleType.getEnum(RoleType.STUDENT_TYPE));
		}
		if (advisoryRecipients.equals(AdvisoryRecipients.TEACHERS))
		{
			criteria.addEqualTo("personRoles.roleType", RoleType.getEnum(RoleType.TEACHER_TYPE));
		}
		if (advisoryRecipients.equals(AdvisoryRecipients.EMPLOYEES))
		{
			criteria.addEqualTo("personRoles.roleType", RoleType.getEnum(RoleType.EMPLOYEE_TYPE));
			criteria.addNotEqualTo("personRoles.roleType", RoleType.getEnum(RoleType.TEACHER_TYPE));
		}

		int numberOfRecipients = count(Pessoa.class, criteria);
		for (int i = 0; i < numberOfRecipients; i++)
		{
			IPessoa person =
				(IPessoa) readSpan(Pessoa.class, criteria, new Integer(1), new Integer(i + 1)).get(0);
			write(advisory, person);
			if ((numberOfRecipients % 500) == 0) {
				System.out.println("Processed 500... clearing the cache to make room for more. :o)");
				SuportePersistenteOJB.getInstance().clearCache();
			}
		}
	}

	public void write(IAdvisory advisory, List group) throws ExcepcaoPersistencia
	{
		lockWrite(advisory);

		Iterator it = group.iterator();
		while (it.hasNext())
		{
			IPessoa person = (IPessoa) it.next();
			lockWrite(person);
			person.getAdvisories().add(advisory);
		}
	}

	public void write(IAdvisory advisory, IPessoa person) throws ExcepcaoPersistencia
	{
		simpleLockWrite(person);
		person.getAdvisories().add(advisory);
	}

}
