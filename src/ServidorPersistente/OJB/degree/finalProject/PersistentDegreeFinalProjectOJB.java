/*
 * Created on Nov 11, 2003 by jpvl
 *
 */
package ServidorPersistente.OJB.degree.finalProject;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ITeacher;
import Dominio.degree.finalProject.DegreeFinalProjectOrientation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.degree.finalProject.IPersistentDegreeFinalProjectOrientation;

/**
 * @author jpvl
 */
public class PersistentDegreeFinalProjectOJB extends ObjectFenixOJB implements IPersistentDegreeFinalProjectOrientation
{

	/* (non-Javadoc)
	 * @see ServidorPersistente.degree.finalProject.IPersistentDegreeFinalProjectOrientation#readByTeacher(Dominio.ITeacher)
	 */
	public List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
		return queryList(DegreeFinalProjectOrientation.class, criteria);
	}}
