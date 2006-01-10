package net.sourceforge.fenixedu.dataConsistency;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.StringAppender;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.log4j.Logger;

public class CheckOverlappingRoomOccupations {

    private static final Logger logger = Logger.getLogger(CheckOverlappingRoomOccupations.class);

    private static ISuportePersistente persistentSupport;

    private static IPersistentObject persistentObject;

    private static DateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    private static int overlapCounter = 0;

    public static void main(String[] args) {
        try {
            persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentSupport.iniciarTransaccao();
            persistentObject = persistentSupport.getIPersistentObject();
            check();
            persistentSupport.confirmarTransaccao();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                persistentSupport.cancelarTransaccao();
            } catch (ExcepcaoPersistencia e) {
                e.printStackTrace();
            }
        }

        logger.info("Found a total of " + overlapCounter + " overlapping room occupations.");

        logger.info("Check complete.");
        System.exit(0);
    }

    private static void check() throws ExcepcaoPersistencia {
        final List<Room> rooms = (List<Room>) persistentObject.readAll(Room.class);
        for (final Room room : rooms) {
            checkRoom(room);
        }
    }

    private static void checkRoom(final Room room) {
        final Set<RoomOccupation> sortedRoomOccupations = sortedRoomOccupations(room);

        if (sortedRoomOccupations.size() > 1) {
            RoomOccupation roomOccupation1 = null;
            RoomOccupation roomOccupation2 = null;
            for (final Iterator iterator = sortedRoomOccupations.iterator(); iterator.hasNext();) {
                if (roomOccupation2 == null) {
                    roomOccupation2 = (RoomOccupation) iterator.next();
                } else {
                    roomOccupation2 = roomOccupation1;
                }
                roomOccupation1 = (RoomOccupation) iterator.next();

                checkRoomOccupations(roomOccupation2, roomOccupation1);
            }
        }
    }

    private static void checkRoomOccupations(final RoomOccupation roomOccupation1,
            final RoomOccupation roomOccupation2) {
        if (roomOccupation1.getDayOfWeek().getDiaSemana().equals(
                roomOccupation2.getDayOfWeek().getDiaSemana())) {
            if (roomOccupation1.getPeriod().intersectPeriods(roomOccupation2.getPeriod())) {
                if (roomOccupation1.getEndTimeDate().after(roomOccupation2.getStartTimeDate())) {
                    logger.fatal(roomOccupationComparationString(roomOccupation1, roomOccupation2));
                    overlapCounter++;
                }
            }
        }
    }

    private static String roomOccupationComparationString(final RoomOccupation roomOccupation1,
            final RoomOccupation roomOccupation2) {
        return StringAppender.append(roomOccupation1.getRoom().getNome(), " ", roomOccupation1
                .getDayOfWeek().getDiaSemana().toString(), "ª ", dateFormat.format(roomOccupation1
                .getPeriod().getStart()), "-", dateFormat.format(roomOccupation1.getPeriod().getEnd()),
                " ", timeFormat.format(roomOccupation1.getStartTimeDate()), "-", timeFormat
                        .format(roomOccupation1.getEndTimeDate()), " + ", dateFormat
                        .format(roomOccupation2.getPeriod().getStart()), "-", dateFormat
                        .format(roomOccupation2.getPeriod().getEnd()), " ", timeFormat
                        .format(roomOccupation2.getStartTimeDate()), "-", timeFormat
                        .format(roomOccupation2.getEndTimeDate()));
    }

    private static Set<RoomOccupation> sortedRoomOccupations(final Room room) {
        final Comparator comparator = roomOccupationComparator();
        final Set<RoomOccupation> sortedRoomOccupations = new TreeSet<RoomOccupation>(comparator);
        sortedRoomOccupations.addAll(room.getRoomOccupations());
        return sortedRoomOccupations;
    }

    private static Comparator roomOccupationComparator() {
        final ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("period.start"));
        comparatorChain.addComparator(new BeanComparator("dayOfWeek.diaSemana"));
        comparatorChain.addComparator(new BeanComparator("startTimeDate"));
        comparatorChain.addComparator(new BeanComparator("endTimeDate"));
        return comparatorChain;
    }

}
