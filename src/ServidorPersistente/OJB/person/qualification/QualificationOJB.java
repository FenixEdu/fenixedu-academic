package ServidorPersistente.OJB.person.qualification;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IPessoa;
import Dominio.Qualification;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author João Simas
 * @author Nuno Barbosa
 */
public class QualificationOJB extends ObjectFenixOJB implements IPersistentQualification
{

	public QualificationOJB()
	{
	}

	public List readQualificationsByPerson(IPessoa person) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("person.idInternal", person.getIdInternal());
		List result = (List) queryList(Qualification.class, criteria);
		return result;
	}

}
