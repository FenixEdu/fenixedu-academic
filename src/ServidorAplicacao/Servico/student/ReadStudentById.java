/*
 * Created on 28/Ago/2003, 7:57:10
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.student;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentWithInfoPerson;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 28/Ago/2003, 7:57:10
 * 
 */
public class ReadStudentById implements IServico
{
	private static ReadStudentById _servico= new ReadStudentById();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadStudentById getService()
	{
		return _servico;
	}
	/**
	 * The actor of this class.
	 **/
	private ReadStudentById()
	{
	}
	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome()
	{
		return "student.ReadStudentById";
	}
	public Object run(Integer id)
	{
		InfoStudent infoStudent= null;
		try
		{
			ISuportePersistente sp= SuportePersistenteOJB.getInstance();
			IStudent student= (IStudent) sp.getIPersistentStudent().readByOID(Student.class, id);
			if (student != null)
			{
			    //CLONER
			    //infoStudent= Cloner.copyIStudent2InfoStudent(student);
			    infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
			}
		}
		catch (ExcepcaoPersistencia ex)
		{
			ex.printStackTrace();
		}
		return infoStudent;
	}
}
