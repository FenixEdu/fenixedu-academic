package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace.ActiveForEducationWithNormalCapacityPredicate;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace.AllocatableSpacePredicate;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace.AllocatableSpaceTransformer;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace.IsFreeIntervalPredicate;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace.IsFreePredicate;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

/**
 * @author Ana e Ricardo
 */
public class ReadAvailableRoomsForExam {

    private static AllocatableSpacePredicate getSearchPredicate(final Integer normalCapacity, final Boolean withLabs) {
        return normalCapacity != null ? new ActiveForEducationWithNormalCapacityPredicate(normalCapacity) : withLabs
                .booleanValue() ? AllocatableSpace.ACTIVE_FOR_EDUCATION_PREDICATE : AllocatableSpace.ACTIVE_FOR_EDUCATION_EXCEPT_LABS_PREDICATE;
    }

    public static <T> List<T> findRooms(final Integer normalCapacity, final Boolean withLabs,
            final AllocatableSpaceTransformer<T> transformer, final AllocatableSpacePredicate isFreePredicate) {

        final AllocatableSpacePredicate searchPredicate = getSearchPredicate(normalCapacity, withLabs);
        final AllocatableSpacePredicate hasIdentificationPredicate = new AllocatableSpacePredicate() {
            @Override
            public boolean eval(final AllocatableSpace space) {
                return space.containsIdentification();
            }
        };

        return AllocatableSpace.findAllocatableSpacesByPredicates(transformer, searchPredicate, hasIdentificationPredicate,
                isFreePredicate);
    }

    public static List<InfoRoom> run(YearMonthDay startDate, YearMonthDay endDate, HourMinuteSecond startTimeHMS,
            HourMinuteSecond endTimeHMS, DiaSemana dayOfWeek, Integer normalCapacity, FrequencyType frequency, Boolean withLabs) {
        final AllocatableSpacePredicate isFreePredicate =
                new IsFreePredicate(startDate, endDate, startTimeHMS, endTimeHMS, dayOfWeek, frequency, Boolean.TRUE,
                        Boolean.TRUE);

        final AllocatableSpaceTransformer<InfoRoom> transformer = new AllocatableSpaceTransformer<InfoRoom>() {
            @Override
            public InfoRoom transform(final AllocatableSpace space) {
                return InfoRoom.newInfoFromDomain(space);
            }
        };

        return findRooms(normalCapacity, withLabs, transformer, isFreePredicate);
    }

    public static List<AllocatableSpace> findAllocatableSpace(final Integer normalCapacity, final Boolean withLabs,
            final Interval... intervals) {
        final AllocatableSpacePredicate isFreePredicate = new IsFreeIntervalPredicate(intervals);
        return findRooms(normalCapacity, withLabs, AllocatableSpace.NO_TRANSFORMER, isFreePredicate);
    }

}