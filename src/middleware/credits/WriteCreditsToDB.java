/*
 * Created on 12/Mar/2004
 *  
 */
package middleware.credits;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import middleware.middlewareDomain.MWDegreeTranslation;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import Util.DegreeCurricularPlanState;
/**
 * @author Tânia Pousão
 *  
 */
public class WriteCreditsToDB
{
	private static String file = null;
	private static String separator;
	private static Hashtable structure;
	private static List order;
	private static List list;
	public WriteCreditsToDB(String[] args) throws NotExecuteException
	{
		//The file was given by GEP and contains many lines with the next information:
		//degree code, curricular year, semester, course code, ECTs credits and finally national
		// credits
		String path;
		try
		{
			path = readPathFile();
		} catch (NotExecuteException e)
		{
			throw new NotExecuteException("error.ficheiro.naoEncontrado");
		}
		file = path.concat("creditos.txt");//args[0]);
		System.out.println("File: " + file);
		separator = new String("\t");
		//Inicialize a hash table with columns contain in file
		structure = new Hashtable();
		structure.put("degreeCode", new Object()); //String
		structure.put("curricularYear", new Object()); //int
		structure.put("curricularSemester", new Object()); //int
		structure.put("courseCode", new Object()); //String
		structure.put("ectsCredits", new Object()); //Double
		structure.put("credits", new Object()); //Double
		// Inicialize a Collection with the order of the data in the file
		order = new ArrayList();
		order.add("degreeCode");
		order.add("curricularYear");
		order.add("curricularSemester");
		order.add("courseCode");
		order.add("ectsCredits");
		order.add("credits");
	}
	/**
	 * @return
	 */
	private String readPathFile() throws NotExecuteException
	{
		File file = null;
		InputStream inputStreamFile = null;
		BufferedReader buffReader;
		String fileLine = null;
		String dataFilePath = null;
		final String keyDataFilePath = "load.data.infile.credits = ";
		try
		{
			//The file was given by GEP and contains many lines with the next information:
			//degree code, curricular year, semester, course code, ECTs credits and finally national
			// credits
			file = new File("middleware.properties");
			inputStreamFile = new FileInputStream(file);
			buffReader = new BufferedReader(new InputStreamReader(inputStreamFile, "8859_1"), new Long(
					file.length()).intValue());
		} catch (IOException e)
		{
			throw new NotExecuteException("error.ficheiro.naoEncontrado");
		}
		do
		{
			//read file line by line
			//load.data.infile.credits = "E:/Projectos/_docsFenix/GEP/"
			try
			{
				fileLine = buffReader.readLine();
				if (fileLine != null && fileLine.startsWith(keyDataFilePath))
				{
					String[] result = fileLine.split(keyDataFilePath);
					if (result.length > 0)
					{
						dataFilePath = result[result.length - 1];
					}
				}
			} catch (IOException e)
			{
				throw new NotExecuteException("error.ficheiro.impossivelLer");
			}
		} while ((fileLine != null));
		try
		{
			buffReader.close();
			inputStreamFile.close();
		} catch (Exception e)
		{
			throw new NotExecuteException("error.ficheiro.impossivelFechar");
		}
		if (dataFilePath == null || dataFilePath.length() <= 0)
		{
			throw new NotExecuteException("error.ficheiro.naoEncontrado");
		}
		return dataFilePath;
	}
	/**
	 * Read all data in the file and fill a list This list is useful for update da data base
	 */
	public static void main(String[] args) throws Exception
	{
		try
		{
			new WriteCreditsToDB(args);
		} catch (NotExecuteException e)
		{
			e.printStackTrace();
			return;
		}
		ReadCreditsFromFile readDataFile = new ReadCreditsFromFile();
		//read all data in file and return a collection
		list = readDataFile.readFile(file, separator, structure, order);
		System.out.println("Read " + list.size() + " courses!!");
		Iterator iterator = list.iterator();
		String degreeCode = null;
		Integer curricularYear = null;
		Integer curricularSemester = null;
		String courseCode = null;
		Double credits = null;
		Double ectsCredits = null;
		int coursesUpdated = 0;
		int line = 0;
		try
		{
			//open linking for data base
			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.clearCache();
			broker.beginTransaction();
			while (iterator.hasNext())
			{
				Hashtable temporaryInstance = (Hashtable) iterator.next();
				line++;
				Criteria criteria = new Criteria();
				Query query = null;
				degreeCode = (String) temporaryInstance.get("degreeCode");
				courseCode = (String) temporaryInstance.get("courseCode");
				curricularYear = new Integer((String) temporaryInstance.get("curricularYear"));
				curricularSemester = new Integer((String) temporaryInstance.get("curricularSemester"));
				try
				{
					credits = new Double((String) temporaryInstance.get("credits"));
				} catch (NumberFormatException e1)
				{
					credits = null;
				}
				try
				{
					ectsCredits = new Double((String) temporaryInstance.get("ectsCredits"));
				} catch (NumberFormatException e2)
				{
					ectsCredits = null;
				}
				//Read degree
				ICurso degree = getDegree(broker, degreeCode, criteria, query);
				if (degree == null)
				{
					throw new Exception("Degree not finded: " + degreeCode);
				}
				System.out.println("DEGREE: " + degree.getNome());
				List degreeCurricularPlans = degree.getDegreeCurricularPlans();
				Iterator iteratorPlan = degreeCurricularPlans.iterator();
				while (iteratorPlan.hasNext())
				{
					IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iteratorPlan
							.next();
					if (degreeCurricularPlan.getState().equals(DegreeCurricularPlanState.ACTIVE_OBJ))
					{
						System.out.println("DEGREE_CURRICULAR_PLAN: " + degreeCurricularPlan.getName()
								+ "-" + degreeCurricularPlan.getIdInternal());
						//Read curricular Course by code and degree curricular plan
						ICurricularCourse curricularCourse = getCurricularCourse(broker, courseCode,
								degreeCurricularPlan.getIdInternal(), criteria, query);
						if (curricularCourse != null)
						{
							System.out.println("CURRICULAR COURSE: " + curricularCourse.getName());
							if (verifyCurricularCourseScopes(curricularCourse, curricularYear,
									curricularSemester))
							{
								System.out.println("CURRICULAR COURSE " + curricularCourse.getName()
										+ "...UPDATED");
								curricularCourse.setCredits(credits);
								curricularCourse.setEctsCredits(ectsCredits);
								broker.store(curricularCourse);
								coursesUpdated++;
							}
						}
					}
				}
			}
			broker.commitTransaction();
			broker.clearCache();
			broker.close();
		} catch (Exception e)
		{
			System.out.println("\nError updating credits in line " + line + "\n");
			e.printStackTrace(System.out);
		}
		System.out.println("Courses updated : " + coursesUpdated);
		System.out.println("  Done !");
	}
	/**
	 * @param broker
	 * @param degreeCode
	 * @param criteria
	 * @return
	 */
	private static ICurso getDegree(PersistenceBroker broker, String degreeCode, Criteria criteria,
			Query query)
	{
		criteria = new Criteria();
		criteria.addEqualTo("degreeCode", degreeCode);
		query = new QueryByCriteria(MWDegreeTranslation.class, criteria);
		List resultDegree = (List) broker.getCollectionByQuery(query);
		if (resultDegree.size() <= 0 || resultDegree.size() > 1)
		{
			return null;
		}
		return ((MWDegreeTranslation) resultDegree.get(0)).getDegree();
	}
	/**
	 * @param broker
	 * @param degreeCode
	 * @param keyDegreeCurricularPlan
	 * @param criteria
	 * @return
	 */
	private static ICurricularCourse getCurricularCourse(PersistenceBroker broker, String courseCode,
			Integer keyDegreeCurricularPlan, Criteria criteria, Query query)
	{
		criteria.addEqualTo("degreeCurricularPlan.idInternal", keyDegreeCurricularPlan);
		criteria.addEqualTo("code", courseCode);
		query = new QueryByCriteria(CurricularCourse.class, criteria);
		List resultCourse = (List) broker.getCollectionByQuery(query);
		if (resultCourse.size() <= 0)
		{
			return null;
		}
		if (resultCourse.size() > 1)
		{
			System.out.println("More than one curricular course finded with code '" + courseCode + "'");
		}
		return (ICurricularCourse) resultCourse.get(0);
	}
	/**
	 * @param curricularCourse
	 * @param curricularYear
	 * @param curricularSemester
	 * @return
	 */
	private static boolean verifyCurricularCourseScopes(ICurricularCourse curricularCourse,
			Integer curricularYear, Integer curricularSemester)
	{
		boolean result = false;
		List scopes = curricularCourse.getScopes();
		if (scopes != null && scopes.size() >= 0)
		{
			ListIterator iterator = scopes.listIterator();
			while (iterator.hasNext())
			{
				ICurricularCourseScope scope = (ICurricularCourseScope) iterator.next();
				if (scope.getCurricularSemester() != null
						&& scope.getCurricularSemester().getCurricularYear() != null
						&& scope.getCurricularSemester().getCurricularYear().getYear().equals(
								curricularYear)
						&& scope.getCurricularSemester().getSemester().equals(curricularSemester))
				{
					result = true;
					return result;
				}
			}
		} else
		{
			System.out.println("Curricular course " + curricularCourse.getCode() + " without scopes!");
		}
		return result;
	}
}