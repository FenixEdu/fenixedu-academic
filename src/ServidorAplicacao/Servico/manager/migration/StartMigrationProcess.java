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

	public void run(String password, String method, String flag) throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
//			persistentSuport.iniciarTransaccao();
			IPessoa person = persistentSuport.getIPessoaPersistente().lerPessoaPorUsername("L45438");
//			persistentSuport.confirmarTransaccao();

			if (person != null && person.getPassword().equals(password))
			{
				List fileNames = getFileNames(Boolean.valueOf(flag).booleanValue());

				String fileName1 = (String) fileNames.get(0);
				String fileName2 = (String) fileNames.get(1);
				
				if (method.equals("pastCurriculum"))
				{
//					String args[] = { flag, "true", fileName1 };
//					CreateAndUpdateAllStudentsPastEnrolments.main(args);
					CreateUpdateDeleteEnrollmentsInPastStudentCurricularPlans instance =
						new CreateUpdateDeleteEnrollmentsInPastStudentCurricularPlans();
					instance.run(Boolean.valueOf(flag), Boolean.TRUE, fileName1);
				} else if (method.equals("thisSemester"))
				{
//					String args[] = { flag, "true", fileName2 };
//					UpdateStudentEnrolments.main(args);
					CreateUpdateDeleteEnrollmentsInCurrentStudentCurricularPlans instance =
						new CreateUpdateDeleteEnrollmentsInCurrentStudentCurricularPlans();
					instance.run(Boolean.valueOf(flag), Boolean.TRUE, fileName2);
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
	private List getFileNames(boolean recordsToAdd) throws IOException
	{
		Properties properties = new Properties();
		properties.load(StartMigrationProcess.class.getResourceAsStream("/migrationLog.properties"));
		String fileNameTemp1 = properties.getProperty("update.past.enrollments.log.file.name");
		String fileNameTemp2 = properties.getProperty("update.current.enrollments.log.file.name");
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String action = null;
		if (recordsToAdd)
		{
			action = "recordsToAdd";
		} else
		{
			action = "recordsToRemove";
		}

		String concat = "_" + year + "_" + month + "_" + day + "_" + hour + "_" + minute + "_" + action + ".txt";
		String fileName1 = fileNameTemp1 + concat;
		String fileName2 = fileNameTemp2 + concat;
		
		List result = new ArrayList();
		result.add(0, fileName1);
		result.add(1, fileName2);
		
		return result;
	}

}