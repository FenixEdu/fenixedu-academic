package ServidorAplicacao.Servico.manager.migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.ExcepcaoAutenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author David Santos
 * Jan 26, 2004
 */
public class StartMigrationProcess implements IService
{
	public StartMigrationProcess(){}

	public void run(String password, String method, String curriculum) throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPessoa person = persistentSuport.getIPessoaPersistente().lerPessoaPorUsername("L45438");

			boolean areTheesRecordsToBeWriten = false;
			if (method.equals("write"))
			{
				areTheesRecordsToBeWriten = true;
			}

			if (person != null && person.getPassword().equals(password))
			{
				List fileNames = getFileNames(areTheesRecordsToBeWriten);

				String fileName1 = (String) fileNames.get(0);
				String fileName2 = (String) fileNames.get(1);
				String fileName3 = (String) fileNames.get(2);

				if (curriculum.equals("past"))
				{
					if (areTheesRecordsToBeWriten)
					{
						CreateUpdateEnrollmentsInPastStudentCurricularPlans instance =
							new CreateUpdateEnrollmentsInPastStudentCurricularPlans();
						instance.run(Boolean.TRUE, fileName1);
					} else
					{
						DeleteEnrollmentsInPastStudentCurricularPlans instance = new DeleteEnrollmentsInPastStudentCurricularPlans();
						instance.run(Boolean.TRUE, fileName1);
					}
				} else if (curriculum.equals("current"))
				{
					if (areTheesRecordsToBeWriten)
					{
						CreateUpdateEnrollmentsInCurrentStudentCurricularPlans instance =
							new CreateUpdateEnrollmentsInCurrentStudentCurricularPlans();
						instance.run(Boolean.TRUE, fileName2);
					} else
					{
						DeleteEnrollmentsInCurrentStudentCurricularPlans instance =
							new DeleteEnrollmentsInCurrentStudentCurricularPlans();
						instance.run(Boolean.TRUE, fileName2);
					}
				} else if (curriculum.equals("students"))
				{
					CreateStudents instance = new CreateStudents();
					instance.run(Boolean.TRUE, fileName3);
				}
			} else
			{
				throw new ExcepcaoAutenticacao("Autenticacao incorrecta");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
	}

	/**
	 * @param recordsToAdd
	 * @return List of file names
	 * @throws IOException
	 */
	private List getFileNames(boolean areTheesRecordsToBeWriten) throws IOException
	{
		Properties properties = new Properties();
		properties.load(StartMigrationProcess.class.getResourceAsStream("/migrationLog.properties"));
		String fileNameTemp1 = properties.getProperty("update.past.enrollments.log.file.name");
		String fileNameTemp2 = properties.getProperty("update.current.enrollments.log.file.name");
		String fileNameTemp3 = properties.getProperty("update.students.log.file.name");

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		String action = null;
		if (areTheesRecordsToBeWriten)
		{
			action = "writenRecords";
		} else
		{
			action = "deletedRecords";
		}

		String concat = "_" + year + "_" + month + "_" + day + "_" + hour + "_" + minute + "_" + action + ".txt";
		String fileName1 = fileNameTemp1 + concat;
		String fileName2 = fileNameTemp2 + concat;
		String fileName3 = fileNameTemp3 + concat;

		List result = new ArrayList();
		result.add(0, fileName1);
		result.add(1, fileName2);
		result.add(1, fileName3);

		return result;
	}

}