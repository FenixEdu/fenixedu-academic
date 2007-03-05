package net.sourceforge.fenixedu.presentationTier.renderers.providers.researcher.publication;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.MethodUtils;

public class JournalIssuesForScienficalJournalProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Object provide(Object source, Object currentValue) {
	ScientificJournal scientificJournal;
	try {
	    scientificJournal = (ScientificJournal) MethodUtils.invokeMethod(source, "getScientificJournal", null);
	}catch(Exception e) {
		throw new RuntimeException(e);
	}
	
	return scientificJournal==null ? Collections.EMPTY_LIST : scientificJournal.getJournalIssues();
    }

}
