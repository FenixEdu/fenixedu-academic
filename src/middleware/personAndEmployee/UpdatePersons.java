/*
 * Created on 25/Fev/2004
 *  
 */
package middleware.personAndEmployee;

import java.util.Collection;

import framework.factory.ServiceManagerServiceFactory;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;

/**
 * @author Tânia Pousão
 *  
 */
public class UpdatePersons
{

	private static String _delimitador = new String(";");

	public static void main(String[] args) throws Exception
	{

		String ficheiro = null;
		ficheiro = "E:/Projectos/_carregamentos/pessoa.dat";
//		if (args[0] != null)
//		{
//			ficheiro = args[0];
//		}

		Collection listaPessoas = null;
		try
		{
			listaPessoas = LeituraFicheiroPessoa.lerFicheiro(ficheiro, _delimitador);
		}
		catch (Exception nee)
		{
			nee.printStackTrace();
			throw new NotExecuteException(nee.getMessage());
		}
		System.out.println("Converting Persons " + listaPessoas.size() + " ... ");

		Object[] argsService = { listaPessoas };
		try
		{
			ServiceManagerServiceFactory.executeService(null, "UpdatePersons", argsService);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		System.out.println("  Done !");
	}
}
