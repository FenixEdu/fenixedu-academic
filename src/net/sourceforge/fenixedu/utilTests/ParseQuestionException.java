/*
 * Created on 4/Mar/2004
 *  
 */

package net.sourceforge.fenixedu.utilTests;

/**
 * @author Susana Fernandes
 */

public class ParseQuestionException extends Exception {

	public ParseQuestionException() {
	}

	public ParseQuestionException(final Throwable t) {
		super(t);
	}

	public ParseQuestionException(String errorMessage) {
		super(errorMessage);
	}

	public ParseQuestionException(String errorMessage, final Throwable t) {
		super(errorMessage, t);
	}

	public ParseQuestionException(String element, boolean isElement) {
		super("O elemento <" + element + "> ainda não é suportado pelo sistema.");
	}

	public ParseQuestionException(String element, String attribute) {
		super("O atributo \"" + attribute + "\" do elemento <" + element + "> ainda não é suportado pelo sistema.");
	}

	@Override
	public String toString() {
		String result = ": " + this.getMessage();
		return result;
	}
}