package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExportGrouping;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExportGroupingsByGrouping extends Service {

    public List<InfoExportGrouping> run(Integer groupingOID) {
	final Grouping grouping = rootDomainObject.readGroupingByOID(groupingOID);
	final List<ExportGrouping> exportGroupings = grouping.getExportGroupings();

	final List<InfoExportGrouping> infoExportGroupings = new ArrayList<InfoExportGrouping>(exportGroupings.size());
	for (final ExportGrouping exportGrouping : exportGroupings) {
	    final InfoExportGrouping infoExportGrouping = new InfoExportGrouping();
	    infoExportGrouping.setIdInternal(exportGrouping.getIdInternal());
	    infoExportGrouping.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(exportGrouping.getExecutionCourse()));
	    infoExportGroupings.add(infoExportGrouping);
	}
	return infoExportGroupings;
    }
}