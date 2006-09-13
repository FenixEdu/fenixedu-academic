/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class SituationTypesForCandidacySelection implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        
        Collection<CandidacySituationType> result = new ArrayList<CandidacySituationType>();
        result.add(CandidacySituationType.ADMITTED);
        result.add(CandidacySituationType.SUBSTITUTE);
        result.add(CandidacySituationType.NOT_ADMITTED);
        
        return result;
    }

    public Converter getConverter() {
        return new EnumConverter();
    }

}
