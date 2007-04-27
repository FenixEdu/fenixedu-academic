package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;

public class ResearchUnitProcessor extends PathProcessor {

	public ResearchUnitProcessor(String researchURI) {
		super(researchURI);
	}

	public ResearchUnitProcessor add(SectionProcessor processor) {
		addChild(processor);
		return this;
	}
	
	@Override
	protected boolean accepts(ProcessingContext context, PathElementsProvider provider) {
		String acronym = provider.current();
	        
	   ResearchUnitContext ownContext = (ResearchUnitContext) context;
	   ownContext.setAcronym(acronym);
	        
	   return ownContext.getResearchUnit() != null;
	}

	@Override
	protected boolean forward(ProcessingContext context, PathElementsProvider provider)
			throws IOException, ServletException {
		if (provider.hasNext()) {
            return false;
        }
        else {
        	ResearchUnitContext ownContext = (ResearchUnitContext) context;
            return doForward(context, new Object[] { ownContext.getResearchUnit().getSite().getIdInternal(), "frontPage" });
        }
	}

	@Override
	public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
		return new ResearchUnitContext(parentContext, getForwardURI());
	}

	public static class ResearchUnitContext extends ProcessingContext implements SiteContext {

		private String contextURI;

		private String acronym;

		private ResearchUnit researchUnit;

		public ResearchUnitContext(ProcessingContext parent, String contextURI) {
			super(parent);

			this.contextURI = contextURI;
		}

		public void setAcronym(String acronym) {
			this.acronym = acronym;
		}

		public String getAcronym() {
			return this.acronym;
		}

		public ResearchUnit getResearchUnit() {
			if (researchUnit != null) {
				return researchUnit;
			}
			if (acronym == null) {
				return null;
			}

			for (Party party : PartyType.readPartyTypeByType(PartyTypeEnum.RESEARCH_UNIT).getParties()) {
				ResearchUnit unit = (ResearchUnit) party;
				String acronym = unit.getAcronym();
				if (acronym != null && acronym.equalsIgnoreCase(this.acronym) && unit.hasSite()) {
					this.researchUnit = unit;
					break;
				}
			}
			return researchUnit;
		}

		public Site getSite() {
			return (researchUnit == null) ? null : researchUnit.getSite();
		}

		public String getSiteBasePath() {
			return String.format(this.contextURI, getResearchUnit().getIdInternal(), "%s");
		}

	}
}
