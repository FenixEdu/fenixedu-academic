package net.sourceforge.fenixedu.domain.space;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class OldRoom extends OldRoom_Base {

    public static final Comparator<OldRoom> OLD_ROOM_COMPARATOR_BY_NAME = new ComparatorChain();
    static {
	((ComparatorChain) OLD_ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("name", Collator.getInstance()));
	((ComparatorChain) OLD_ROOM_COMPARATOR_BY_NAME).addComparator(new BeanComparator("idInternal"));
    }
    
    public OldRoom() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    /** @deprecated */
    public void createRoomOccupationForWrittenEvaluations(OccupationPeriod period, Calendar startTime, Calendar endTime,
	    DiaSemana dayOfWeek, WrittenEvaluation writtenEvaluation) {
	
	boolean isFree = isFree(period, startTime, endTime, dayOfWeek, null, null, Boolean.TRUE, Boolean.TRUE);
	if (!isFree) {
	    throw new DomainException("error.roomOccupied");
	}
	new RoomOccupation(this, startTime, endTime, dayOfWeek, period, writtenEvaluation);	
    }

    /** @deprecated */
    public void delete() {
	if (canBeDeleted()) {	    
	    removeBuilding();
            removeRootDomainObject();
	    deleteDomainObject();
	} else {
	    String[] args = { "a sala", "as aulas" };
	    throw new DomainException("errors.invalid.delete.with.objects", args);
	}
    }

    /** @deprecated */
    public boolean isFree(OccupationPeriod period, Calendar startTime, Calendar endTime,
	    DiaSemana dayOfWeek, Integer frequency, Integer week, Boolean dailyFrequencyMarkSaturday,
	    Boolean dailyFrequencyMarkSunday) {
	
	for (final RoomOccupation roomOccupation : getRoomOccupations()) {
	    if (roomOccupation.roomOccupationForDateAndTime(period, startTime, endTime, dayOfWeek, frequency, week, this,
		    dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday)) {
		return false;
	    }
	}
	
	return true;
    }

    /** @deprecated */
    public Set<RoomOccupation> findOccupationSet(OccupationPeriod period, Calendar startTime,
	    Calendar endTime, DiaSemana dayOfWeek, Integer frequency, Integer week, Boolean dailyFrequencyMarkSaturday,
	    Boolean dailyFrequencyMarkSunday) {
	
	final Set<RoomOccupation> roomOccupations = new HashSet<RoomOccupation>();
	for (final RoomOccupation roomOccupation : getRoomOccupations()) {
	    if (roomOccupation.roomOccupationForDateAndTime(period, startTime, endTime, dayOfWeek, frequency, week, this, 
		    dailyFrequencyMarkSaturday, dailyFrequencyMarkSunday)) {
		roomOccupations.add(roomOccupation);
	    }
	}
	return roomOccupations;
    }

    /** @deprecated */
    private boolean canBeDeleted() {
	return getAssociatedLessons().isEmpty() && getAssociatedSummaries().isEmpty()
		&& getRoomOccupations().isEmpty() && getWrittenEvaluationEnrolments().isEmpty();
    }

    public List<Lesson> findLessonsForExecutionPeriod(final ExecutionPeriod executionPeriod) {
	final List<Lesson> lessons = new ArrayList<Lesson>();
	for (final RoomOccupation roomOccupation : getRoomOccupations()) {
	    final Lesson lesson = roomOccupation.getLesson();
	    if (lesson != null && lesson.getExecutionPeriod() == executionPeriod) {
		lessons.add(lesson);
	    }
	}
	return lessons;
    }

    public static OldRoom findOldRoomByName(final String name) {
	for (final OldRoom oldRoom : OldRoom.getOldRooms()) {
	    if (oldRoom.getName().equalsIgnoreCase(name)) {
		return oldRoom;
	    }
	}
	return null;
    }

    public static Set<OldRoom> findOldRoomsBySpecifiedArguments(String nome, String edificio,
	    Integer piso, Integer tipo, Integer capacidadeNormal, Integer capacidadeExame)
	    throws ExcepcaoPersistencia {
	
	final Set<OldRoom> oldRooms = OldRoom.getOldRooms();
	final Set<OldRoom> result = new HashSet<OldRoom>();
	for (OldRoom room : oldRooms) {
	    boolean isAcceptable = true;
	    if (nome != null && !room.getName().equalsIgnoreCase(nome)) {
		continue;
	    }
	    if (edificio != null && !room.getBuilding().getName().equalsIgnoreCase(edificio)) {
		continue;
	    }
	    if (piso != null && !room.getPiso().equals(piso)) {
		continue;
	    }
	    if (tipo != null && !room.getTipo().getTipo().equals(tipo)) {
		continue;
	    }
	    if (capacidadeNormal != null
		    && room.getCapacidadeNormal().intValue() < capacidadeNormal.intValue()) {
		continue;
	    }
	    if (capacidadeExame != null
		    && room.getCapacidadeExame().intValue() < capacidadeExame.intValue()) {
		continue;
	    }
	    if (isAcceptable) {
		result.add(room);
	    }
	}
	return result;
    }

    public static Set<OldRoom> findOldRoomsOfAnyOtherType(final TipoSala tipoSala) {
	final Set<OldRoom> oldRooms = new HashSet<OldRoom>();
	for (final OldRoom oldRoom : OldRoom.getOldRooms()) {
	    if (!oldRoom.getTipo().equals(tipoSala)) {
		oldRooms.add(oldRoom);
	    }
	}
	return oldRooms;
    }

    public static Set<OldRoom> findOldRoomsByBuildingNames(final Collection<String> buildingNames) {
	final Set<OldRoom> oldRooms = new HashSet<OldRoom>();
	for (final OldRoom oldRoom : OldRoom.getOldRooms()) {
	    if (buildingNames.contains(oldRoom.getBuilding().getName())) {
		oldRooms.add(oldRoom);
	    }
	}
	return oldRooms;
    }

    public static Set<OldRoom> findOldRoomsWithNormalCapacity(final Integer normalCapacity) {
	final Set<OldRoom> oldRooms = new HashSet<OldRoom>();
	for (final OldRoom oldRoom : OldRoom.getOldRooms()) {
	    if (oldRoom.getCapacidadeNormal().intValue() >= normalCapacity.intValue()) {
		oldRooms.add(oldRoom);
	    }
	}
	return oldRooms;
    }

    @Deprecated
    public String getNome() {
	return super.getName();
    }

    @Deprecated
    public void setNome(String name) {
	super.setName(name);
    }

    public static Set<OldRoom> getOldRooms() {
	final Set<OldRoom> oldRooms = new HashSet<OldRoom>();
	for (final Space space : RootDomainObject.getInstance().getSpacesSet()) {
	    if (space instanceof OldRoom) {
		OldRoom oldRoom = (OldRoom) space;
		oldRooms.add(oldRoom);
	    }
	}
	return oldRooms;
    }
    
    public String getBuildingName() {
	return (hasBuilding()) ? getBuilding().getName() : "";
    }
}