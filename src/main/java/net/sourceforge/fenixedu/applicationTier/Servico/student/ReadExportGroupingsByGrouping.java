package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExportGrouping;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadExportGroupingsByGrouping {

    @Service
    public static List<InfoExportGrouping> run(Integer groupingOID) {
        final Grouping grouping = AbstractDomainObject.fromExternalId(groupingOID);
        final List<ExportGrouping> exportGroupings = grouping.getExportGroupings();

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