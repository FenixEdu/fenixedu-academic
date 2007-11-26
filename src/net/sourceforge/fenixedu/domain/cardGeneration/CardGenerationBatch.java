package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

public class CardGenerationBatch extends CardGenerationBatch_Base {

    public static class CardGenerationBatchCreator implements FactoryExecutor {

	private final ExecutionYear executionYear;

	public CardGenerationBatchCreator(final ExecutionYear executionYear) {
	    this.executionYear = executionYear;
	}

	public Object execute() {
	    return new CardGenerationBatch(executionYear);
	}
	
    }

    public CardGenerationBatch(final ExecutionYear executionYear) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setExecutionYear(executionYear);
    }

}
