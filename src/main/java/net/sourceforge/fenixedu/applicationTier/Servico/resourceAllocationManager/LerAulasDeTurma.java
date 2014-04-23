package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstanceAggregation;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LerAulasDeTurma {

    @Atomic
    public static List<InfoLessonInstanceAggregation> run(InfoClass infoClass) {
        final SchoolClass schoolClass = FenixFramework.getDomainObject(infoClass.getExternalId());

        final Collection<Shift> shiftList = schoolClass.getAssociatedShiftsSet();

        final List<InfoLessonInstanceAggregation> infoLessonList = new ArrayList<InfoLessonInstanceAggregation>();
        for (final Shift shift : shiftList) {
            infoLessonList.addAll(InfoLessonInstanceAggregation.getAggregations(shift));
        }

        return infoLessonList;
    }

}