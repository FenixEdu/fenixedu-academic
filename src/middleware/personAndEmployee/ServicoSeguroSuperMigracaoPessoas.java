/*
 * Created on 25/Mar/2004
 *
 */
package middleware.personAndEmployee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ServidorAplicacao.Servico.exceptions.NotExecuteException;


/**
 * @author Tânia Pousão
 *
 */
public class ServicoSeguroSuperMigracaoPessoas
{
	public String readPathFile() throws NotExecuteException
	{

		File file = null;
		InputStream inputStreamFile = null;
		BufferedReader buffReader;
		String fileLine = null;
		String dataFilePath = null;
		final String keyDataFilePath = "load.data.infile.person = ";
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
}
