package ServidorAplicacao.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IPessoa;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author David Santos in Mar 5, 2004
 */

public class ReadStudentsByPerson implements IService
{
	public ReadStudentsByPerson(){}

	public List run(InfoPerson infoPerson) throws FenixServiceException
	{
		List infoStudents = new ArrayList();
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPessoa person = Cloner.copyInfoPerson2IPerson(infoPerson);
			List students = persistentSuport.getIPersistentStudent().readbyPerson(person);
			Iterator iterator = students.iterator();
			while (iterator.hasNext())
			{
				IStudent student = (IStudent) iterator.next();
				InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(student);
				infoStudents.add(infoStudent);
			}
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		
		return infoStudents;
	}
}