package middleware.studentMigration.enrollments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import middleware.persistentMiddlewareSupport.PersistanceBrokerForMigrationProcess;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.ExcepcaoAutenticacao;
import ServidorAplicacao.Servico.manager.migration.withBroker.CreateNewStudents;
import ServidorAplicacao.Servico.manager.migration.withBroker.CreateUpdateEnrollmentsInCurrentStudentCurricularPlans;
import ServidorAplicacao.Servico.manager.migration.withBroker.CreateUpdateEnrollmentsInPastStudentCurricularPlans;
import ServidorAplicacao.Servico.manager.migration.withBroker.DeleteEnrollmentsInCurrentStudentCurricularPlans;
import ServidorAplicacao.Servico.manager.migration.withBroker.DeleteEnrollmentsInPastStudentCurricularPlans;

/**
 * @author David Santos in Jan 29, 2004
 */

public class RunMigrationProcess
{
	public static void main(String args[]) throws Throwable
	{
		PersistanceBrokerForMigrationProcess persistentSuport = new PersistanceBrokerForMigrationProcess();

		try
		{
			String curriculum = args[0];
			String method = args[1];
			String password = args[2];
//			String password = "1d0258c2440a8d19e716292b231e3190"; // real
//			String password = "1a1dc91c907325c69271ddf0c944bc72"; // pass
			
			persistentSuport.beginTransaction();
			RunMigrationProcess.run(password, method, curriculum, persistentSuport);
			persistentSuport.commitTransaction();
		} catch (Throwable e)
		{
			e.printStackTrace(System.out);
			persistentSuport.abortTransaction();
			persistentSuport.close();
			throw new Throwable(e);
		}

		persistentSuport.close();
	}

	/**
	 * @param password
	 * @param method
	 * @param curriculum
	 * @param persistentSuport
	 * @throws Throwable
	 */
	public static void run(String password, String method, String curriculum, PersistanceBrokerForMigrationProcess persistentSuport)
		throws Throwable
	{
		IPessoa person = persistentSuport.readPersonByUsername("L45438");

		boolean areTheesRecordsToBeWriten = false;
		if (method.equals("write"))
		{
			areTheesRecordsToBeWriten = true;
		}

		if (person != null && person.getPassword().equals(password))
		{
			List fileNames = RunMigrationProcess.getFileNames(areTheesRecordsToBeWriten);

			String fileName1 = (String) fileNames.get(0);
			String fileName2 = (String) fileNames.get(1);
			String fileName3 = (String) fileNames.get(2);

			if (curriculum.equals("past"))
			{
				if (areTheesRecordsToBeWriten)
				{
					CreateUpdateEnrollmentsInPastStudentCurricularPlans instance =
						new CreateUpdateEnrollmentsInPastStudentCurricularPlans(persistentSuport);
					instance.run(Boolean.TRUE, fileName1);
				} else
				{
					DeleteEnrollmentsInPastStudentCurricularPlans instance =
						new DeleteEnrollmentsInPastStudentCurricularPlans(persistentSuport);
					instance.run(Boolean.TRUE, fileName1);
				}
			} else if (curriculum.equals("current"))
			{
				if (areTheesRecordsToBeWriten)
				{
					CreateUpdateEnrollmentsInCurrentStudentCurricularPlans instance =
						new CreateUpdateEnrollmentsInCurrentStudentCurricularPlans(persistentSuport);
					instance.run(Boolean.TRUE, fileName2);
				} else
				{
					DeleteEnrollmentsInCurrentStudentCurricularPlans instance =
						new DeleteEnrollmentsInCurrentStudentCurricularPlans(persistentSuport);
					instance.run(Boolean.TRUE, fileName2);
				}
			} else if (curriculum.equals("students"))
			{
				CreateNewStudents instance = new CreateNewStudents(persistentSuport);
				instance.run(Boolean.TRUE, fileName3);
			}
		} else
		{
			throw new ExcepcaoAutenticacao("Autenticacao incorrecta");
		}
	}

	/**
	 * @param recordsToAdd
	 * @return List of file names
	 * @throws IOException
	 */
	private static List getFileNames(boolean areTheesRecordsToBeWriten) throws IOException
	{
		Properties properties = new Properties();
		properties.load(RunMigrationProcess.class.getResourceAsStream("/migrationLog.properties"));
		String fileNameTemp1 = properties.getProperty("update.past.curriculums.log.file.name");
		String fileNameTemp2 = properties.getProperty("update.current.curriculums.log.file.name");
		String fileNameTemp3 = properties.getProperty("create.new.students.log.file.name");

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		String action = null;
		if (areTheesRecordsToBeWriten)
		{
			action = "-writen-records";
		} else
		{
			action = "-deleted-records";
		}

//		String concat = "_" + year + "_" + month + "_" + day + "_" + hour + "_" + minute + "_" + action + ".txt";
		String concat =
			action + "-" + zeroPaddIt(year) + zeroPaddIt(month) + zeroPaddIt(day) + zeroPaddIt(hour) + zeroPaddIt(minute) + ".txt";
		String fileName1 = fileNameTemp1 + concat;
		String fileName2 = fileNameTemp2 + concat;
		String fileName3 = fileNameTemp3 + concat;

		List result = new ArrayList();
		result.add(0, fileName1);
		result.add(1, fileName2);
		result.add(2, fileName3);

		return result;
	}

	/**
	 * @param value
	 * @return padded value
	 */
	private static String zeroPaddIt(int value)
	{
		if (value < 10)
		{
			return "0" + value;
		} else
		{
			return "" + value;
		}
	}
}