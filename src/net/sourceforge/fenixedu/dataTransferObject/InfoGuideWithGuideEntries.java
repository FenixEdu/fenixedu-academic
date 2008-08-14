/*
 * Created on 8/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoGuideWithGuideEntries extends InfoGuide {

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoGuide#copyFromDomain(
     * Dominio.Guide)
     */
    public void copyFromDomain(Guide guide) {
	super.copyFromDomain(guide);
	if (guide != null) {
	    setInfoGuideEntries(copyIGuideEntry2InfoGuide(guide.getGuideEntries()));
	}
    }

    private List copyIGuideEntry2InfoGuide(List guideEntries) {
	List infoGuideEntryList = null;

	infoGuideEntryList = (List) CollectionUtils.collect(guideEntries, new Transformer() {

	    public Object transform(Object arg0) {
		GuideEntry guideEntry = (GuideEntry) arg0;
		return InfoGuideEntry.newInfoFromDomain(guideEntry);
	    }
	});

	return infoGuideEntryList;
    }

    public static InfoGuide newInfoFromDomain(Guide guide) {
	InfoGuideWithGuideEntries infoGuide = new InfoGuideWithGuideEntries();
	if (guide != null) {
	    infoGuide = new InfoGuideWithGuideEntries();
	    infoGuide.copyFromDomain(guide);
	}

	return infoGuide;
    }
}