package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.researchActivity;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;

import org.apache.commons.beanutils.BeanComparator;

public class EditScientificJournalDA extends EditResearchActivityDA {

    
    
    @Override
    protected List getObjects() {
	List<ScientificJournal> scientificJournals = rootDomainObject.getScientificJournals();
	Collections.sort(scientificJournals, new BeanComparator("name", Collator.getInstance()));
        return scientificJournals;
    }

}
