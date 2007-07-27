package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.UnitSiteProcessor.UnitSiteContext;

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
		String current = provider.current();
	        
		ResearchUnitContext ownContext = (ResearchUnitContext) context;
		
		int i;
        for (i = 0; ; i++) {
            current = provider.peek(i);
            
            if (! ownContext.addUnit(current)) {
                break;
            }
        }

        while (i > 1) {
            provider.next();
            i--;
        }
        
        return ownContext.getUnit() != null && ownContext.getUnit().hasSite();	
    }

	@Override
	protected boolean forward(ProcessingContext context, PathElementsProvider provider)
			throws IOException, ServletException {
		if (provider.hasNext()) {
            return false;
        }
        else {
        	ResearchUnitContext ownContext = (ResearchUnitContext) context;
            return doForward(context, new Object[] { "frontPage", ownContext.getUnit().getSite().getIdInternal()  });
        }
	}

	@Override
	public ProcessingContext getProcessingContext(ProcessingContext parentContext) {
		return new ResearchUnitContext(parentContext, getForwardURI());
	}

	public static class ResearchUnitContext extends UnitSiteContext {

		public ResearchUnitContext(ProcessingContext parent, String contextURI) {
			super(parent, contextURI);
		}

		@Override
		protected boolean isTypeAccepted(Unit unit, PartyTypeEnum type) {
			return type != null && type == PartyTypeEnum.RESEARCH_UNIT; 
		}

		@Override
		protected Integer getContextIdInternal() {
			return getSite().getIdInternal();
		}
	}
	
	public static String getResearchUnitPath(ResearchUnit unit) {
        return UnitSiteProcessor.getUnitSitePath(unit, ResearchProcessor.PREFIX1);
    }
}
