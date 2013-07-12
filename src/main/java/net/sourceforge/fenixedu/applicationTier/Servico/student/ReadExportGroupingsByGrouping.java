package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExportGrouping;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadExportGroupingsByGrouping {

    @Atomic
    public static List<InfoExportGrouping> run(String groupingOID) {
        final Grouping grouping = FenixFramework.getDomainObject(groupingOID);
        final Collection<ExportGrouping> exportGroupings = grouping.getExportGroupings();

        final List<InfoExportGrouping> infoExportGroupings = new ArrayList<InfoExportGrouping>(exportGroupings.size());
        for (final ExportGrouping exportGrouping : exportGroupings) {
            final InfoExportGrouping infoExportGrouping = new InfoExportGrouping();
            infoExportGrouping.setExternalId(exportGrouping.getExternalId());
            infoExportGrouping.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(exportGrouping.getExecutionCourse()));
            infoExportGroupings.add(infoExportGrouping);
        }
        return infoExportGroupings;
    }
}