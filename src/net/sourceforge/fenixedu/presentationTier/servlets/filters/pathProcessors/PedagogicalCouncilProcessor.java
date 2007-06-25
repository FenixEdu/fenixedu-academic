package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.PedagogicalCouncilSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class PedagogicalCouncilProcessor extends PathProcessor {

	public static final String PREFIX1 = "pedagogical";
	public static final String PREFIX2 = "pedagogico";

	public PedagogicalCouncilProcessor(String uri) {
		super(uri);
	}

	public PedagogicalCouncilProcessor add(SectionProcessor processor) {
		addChild(processor);
		return this;
	}

	@Override
	public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
		return new PedagogicalContext(parentContext, getForwardURI());
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
			PedagogicalContext ownContext = (PedagogicalContext) context;
			return doForward(context, new Object[] { "presentation", ownContext.getUnit().getIdInternal() } );
		}
	}

	public static class PedagogicalContext extends ProcessingContext implements SiteContext {

		private String contextURI;

		public PedagogicalContext(ProcessingContext parent, String contextUri) {
			super(parent);
			
			this.contextURI = contextUri;
		}

		public PedagogicalCouncilSite getSite() {
			return PedagogicalCouncilSite.getSite();
		}

		public Unit getUnit() {
			return getSite().getUnit();
		}
		
		public String getSiteBasePath() {
			return String.format(this.contextURI, "%s", getUnit().getIdInternal());
		}

	}

	public static String getPedagogicalCouncilPath() {
		return "/" + PREFIX2;
	}
}
