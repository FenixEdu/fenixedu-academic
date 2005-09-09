package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExportGrouping;
import net.sourceforge.fenixedu.domain.IGrouping;

public class InfoGroupingWithExportGrouping extends InfoGrouping {

    public void copyFromDomain(IGrouping grouping) {
        super.copyFromDomain(grouping);
        if (grouping != null) {
            final List<IExportGrouping> exportGroupings = grouping.getExportGroupings();
            final List<InfoExportGrouping> infoExportGroupings = new ArrayList<InfoExportGrouping>(
                    exportGroupings.size());
            for (final IExportGrouping exportGrouping : exportGroupings) {
                infoExportGroupings.add(InfoExportGrouping.newInfoFromDomain(exportGrouping));
            }
            setInfoExportGroupings(infoExportGroupings);
        }
    }

    public static InfoGroupingWithExportGrouping newInfoFromDomain(IGrouping groupProperties) {
        InfoGroupingWithExportGrouping infoGroupProperties = null;
        if (groupProperties != null) {
            infoGroupProperties = new InfoGroupingWithExportGrouping();
            infoGroupProperties.copyFromDomain(groupProperties);
        }

        return infoGroupProperties;
    }

}
