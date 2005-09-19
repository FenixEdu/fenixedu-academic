package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IExportGrouping;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExportGroupingsByGrouping implements IService {

    public List<InfoExportGrouping> run(Integer groupingOID) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentGrouping persistentGrouping = persistentSupport.getIPersistentGrouping();

        final IGrouping grouping = (IGrouping) persistentGrouping.readByOID(Grouping.class, groupingOID);
        final List<IExportGrouping> exportGroupings = grouping.getExportGroupings();

        final List<InfoExportGrouping> infoExportGroupings = new ArrayList<InfoExportGrouping>(exportGroupings.size());
        for (final IExportGrouping exportGrouping : exportGroupings) {
            final InfoExportGrouping infoExportGrouping = new InfoExportGrouping();
            infoExportGrouping.setIdInternal(exportGrouping.getIdInternal());
            infoExportGrouping.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(exportGrouping.getExecutionCourse()));
            infoExportGroupings.add(infoExportGrouping);
        }
        return infoExportGroupings;
    }
}