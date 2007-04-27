package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

public class ResearchProcessor extends PathProcessor {

	public static final String PREFIX1 = "research";

	public static final String PREFIX2 = "investigacao";

	public ResearchProcessor() {
		super();
	}

	public ResearchProcessor add(ResearchUnitProcessor processor) {
		addChild(processor);
		return this;
	}

	@Override
	public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
		return new ResearchContext(parentContext);
	}

	@Override
	protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
		String current = provider.current();

		return current.equalsIgnoreCase(PREFIX1) || current.equalsIgnoreCase(PREFIX2);
	}

	@Override
	protected boolean forward(ProcessingContext context, PathElementsProvider provider)
			throws IOException, ServletException {
		return false;
	}

	public static class ResearchContext extends ProcessingContext {

		public ResearchContext(ProcessingContext parent) {
			super(parent);
		}

	}

}
