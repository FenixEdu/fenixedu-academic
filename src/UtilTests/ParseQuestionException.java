/*
 * Created on 4/Mar/2004
 *
 */

package UtilTests;

/**
 * @author Susana Fernandes
 */

public class ParseQuestionException extends Exception
{
	public ParseQuestionException()
	{
	}

	public ParseQuestionException(String element)
	{
		super("O elemento <" + element + "> ainda não é suportado pelo sistema.");
	}

	public ParseQuestionException(String element, String attribute)
	{
		super("O atributo \""+attribute+"\" do elemento <" + element + "> ainda não é suportado pelo sistema.");
	}
	
	public String toString()
	{
		String result = ": " + this.getMessage();
		return result;
	}
}