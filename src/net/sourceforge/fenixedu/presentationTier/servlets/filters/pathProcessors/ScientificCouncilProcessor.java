package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.ScientificCouncilSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class ScientificCouncilProcessor extends PathProcessor {

	public static final String PREFIX1 = "scientific";
	public static final String PREFIX2 = "cientifico";

	public ScientificCouncilProcessor(String uri) {
		super(uri);
	}

	public ScientificCouncilProcessor add(SectionProcessor processor) {
		addChild(processor);
		return this;
	}

	@Override
	public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
		return new ScientificCouncilContext(parentContext, getForwardURI());
	}

	@Override
	protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
		String current = provider.current();
		return current.equalsIgnoreCase(PREFIX1) || current.equalsIgnoreCase(PREFIX2);
	}

	@Override
	protected boolean forward(ProcessingContext context, PathElementsProvider provider) throws IOException, ServletException {
		if (provider.hasNext()) {
			return false;
		}
		else {
			ScientificCouncilContext ownContext = (ScientificCouncilContext) context;
			return doForward(context, new Object[] { "presentation", ownContext.getUnit().getIdInternal() } );
		}
	}

	public static class ScientificCouncilContext extends ProcessingContext implements SiteContext {

		private String contextURI;

		public ScientificCouncilContext(ProcessingContext parent, String contextUri) {
			super(parent);
			
			this.contextURI = contextUri;
		}

		public ScientificCouncilSite getSite() {
			return ScientificCouncilSite.getSite();
		}

		public Unit getUnit() {
			return getSite().getUnit();
		}
		
		public String getSiteBasePath() {
			return String.format(this.contextURI, "%s", getUnit().getIdInternal());
		}

	}

	public static String getScientificCouncilPath() {
		return "/" + PREFIX2;
	}
}
