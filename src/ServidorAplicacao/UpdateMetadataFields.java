package ServidorAplicacao;

import java.util.Iterator;
import java.util.List;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseMetadata;

public class UpdateMetadataFields
{

	public static void main(String[] args) throws Exception
	{
		String path = new String("/var/tomcat4/webapps/extra/");

		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		persistentSuport.iniciarTransaccao();

		Integer executionCourseId = new Integer(34882);
		IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);

		executionCourse =
			(IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOId(
				executionCourse,
				false);
		if (executionCourse == null)
			throw new InvalidArgumentsServiceException();
		System.out.println("EXECUTION_COURSE: " + executionCourse.getIdInternal());

		IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
		List metadataList = persistentMetadata.readByExecutionCourse(executionCourse);
		persistentSuport.confirmarTransaccao();
		Iterator it = metadataList.iterator();
		int i = 1;
		while (it.hasNext())
		{
			persistentSuport.iniciarTransaccao();
			ParseMetadata p = new ParseMetadata();
			System.out.println("vai no : " + i);
			IMetadata metadata = (IMetadata) it.next();
			persistentMetadata.lockWrite(metadata);
			metadata = p.parseMetadata(metadata.getMetadataFile(), metadata, path);
			i++;
			persistentSuport.confirmarTransaccao();
		}
		
	}
}
