/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.DFACandidacyBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExecutionYearsForCandidacyProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final DFACandidacyBean candidacyBean = ((DFACandidacyBean) source);
		return candidacyBean.getDegreeCurricularPlan() == null ? Collections.EMPTY_SET : candidacyBean.getDegreeCurricularPlan()
				.getCandidacyPeriodsExecutionYears();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
