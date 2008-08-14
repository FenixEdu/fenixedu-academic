package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

public class CardGenerationProblem extends CardGenerationProblem_Base {

    public static class CardGenerationProblemDeleter implements FactoryExecutor {

	private final CardGenerationProblem cardGenerationProblem;

	public CardGenerationProblemDeleter(final CardGenerationProblem cardGenerationProblem) {
	    this.cardGenerationProblem = cardGenerationProblem;
	}

	public Object execute() {
	    if (cardGenerationProblem != null) {
		cardGenerationProblem.delete();
	    }
	    return null;
	}
    }

    public CardGenerationProblem(final CardGenerationBatch cardGenerationBatch, final String descriptionKey, final String arg,
	    final Person person) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCardGenerationBatch(cardGenerationBatch);
	setDescriptionKey(descriptionKey);
	setArg(arg);
	setPerson(person);
    }

    public void delete() {
	removePerson();
	removeCardGenerationBatch();
	removeRootDomainObject();
	deleteDomainObject();
    }

}
