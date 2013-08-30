package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstanceAggregation;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class LerAulasDeTurma {

    @Service
    public static List<InfoLessonInstanceAggregation> run(InfoClass infoClass) {
        final SchoolClass schoolClass = AbstractDomainObject.fromExternalId(infoClass.getExternalId());

        final List<Shift> shiftList = schoolClass.getAssociatedShifts();

        final List<InfoLessonInstanceAggregation> infoLessonList = new ArrayList<InfoLessonInstanceAggregation>();
        for (final Shift shift : shiftList) {
            infoLessonList.addAll(InfoLessonInstanceAggregation.getAggregations(shift));
        }

        return infoLessonList;
    }

}