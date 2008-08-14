package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;

public class InfoGroupingWithExportGrouping extends InfoGrouping {

    public void copyFromDomain(Grouping grouping) {
	super.copyFromDomain(grouping);
	if (grouping != null) {
	    final List<ExportGrouping> exportGroupings = grouping.getExportGroupings();
	    final List<InfoExportGrouping> infoExportGroupings = new ArrayList<InfoExportGrouping>(exportGroupings.size());
	    for (final ExportGrouping exportGrouping : exportGroupings) {
		infoExportGroupings.add(InfoExportGrouping.newInfoFromDomain(exportGrouping));
	    }
	    setInfoExportGroupings(infoExportGroupings);
	}
    }

    public static InfoGroupingWithExportGrouping newInfoFromDomain(Grouping groupProperties) {
	InfoGroupingWithExportGrouping infoGroupProperties = null;
	if (groupProperties != null) {
	    infoGroupProperties = new InfoGroupingWithExportGrouping();
	    infoGroupProperties.copyFromDomain(groupProperties);
	}

	return infoGroupProperties;
    }

}
