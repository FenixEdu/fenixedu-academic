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
import java.util.List;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
/**
 * @author Tânia Pousão
 *  
 */
public class ReadCreditsFromFile
{
	private List order;
	private ArrayList list;
	private BufferedReader buffReader;
	public ReadCreditsFromFile()
	{
	}
	public List readFile(String fileName, String separator, Hashtable structure, List order)
			throws NotExecuteException
	{
		this.order = order;
		this.list = new ArrayList();
		this.buffReader = null;
		File file = null;
		InputStream inputStreamFile = null;
		String fileLine = null;
		Hashtable instance = null;
		try
		{
			//The file was given by GEP and contains many lines with the next
			// information:
			//degree code, curricular year, semester, course code, ECTs
			// credits and finally national credits
			file = new File(fileName);
			inputStreamFile = new FileInputStream(file);
			buffReader = new BufferedReader(new InputStreamReader(inputStreamFile, "8859_1"), new Long(
					file.length()).intValue());
		} catch (IOException e)
		{
			throw new NotExecuteException("error.ficheiro.naoEncontrado");
		}
		//first line with header
		try
		{
			fileLine = buffReader.readLine();
		} catch (IOException e)
		{
			throw new NotExecuteException("error.ficheiro.impossivelLer");
		}
		int i = 2;
		do
		{
			//read file line by line
			try
			{
				fileLine = buffReader.readLine();
			} catch (IOException e)
			{
				throw new NotExecuteException("error.ficheiro.impossivelLer");
			}
			try
			{
				if ((fileLine != null) && (fileLine.length() > 10))
				{
					// buil a instance and add it to the list
					instance = new Hashtable();
					instance = createInstance(fileLine, separator);
					list.add(instance);
				}
			} catch (Exception e)
			{
				//e.printStackTrace();
				System.out.println("Error in line " + i);
				//throw new NotExecuteException("error.ficheiro.impossivelLer");
			}
			i++;
		} while ((fileLine != null));
		try
		{
			buffReader.close();
			inputStreamFile.close();
		} catch (Exception e)
		{
			throw new NotExecuteException("error.ficheiro.impossivelFechar");
		}
		return list;
	}
	private Hashtable createInstance(String line, String separator) throws NotExecuteException
	{
		Hashtable instance;
		try
		{
			//With string tokenizer
			// 			StringTokenizer stringTokenizer;
			//			if (separator != null)
			//			{
			//				stringTokenizer = new StringTokenizer(line, separator);
			//			} else
			//			{
			//				stringTokenizer = new StringTokenizer(line);
			//			}
			//			instance = new Hashtable();
			//			Iterator iterator = order.iterator();
			//			String field = null;
			//			String data = null;
			//			while (iterator.hasNext())
			//			{
			//				field = (String) iterator.next();
			//				data = stringTokenizer.nextToken().trim();
			//				instance.put(field, data);
			//			}
			instance = new Hashtable();
			String field = null;
			String data = null;
			String[] tokens = line.split(separator);
			for (int i = 0; i < tokens.length; i++)
			{
				field = (String) order.get(i);
				data = tokens[i];
				instance.put(field, data.replace(',', '.'));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new NotExecuteException("error.ficheiro.impossivelLer");
		}
		return instance;
	}
}