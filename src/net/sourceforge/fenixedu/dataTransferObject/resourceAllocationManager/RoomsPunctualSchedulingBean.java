package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.Pair;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class RoomsPunctualSchedulingBean implements Serializable {

	private Boolean markSaturday;

	private Boolean markSunday;

	private PunctualRoomsOccupationRequest roomsReserveRequestReference;

	private Integer roomsReserveRequestIdentification;

	private MultiLanguageString smallDescription;

	private MultiLanguageString completeDescription;

	private YearMonthDay begin;

	private YearMonthDay end;

	private Partial beginTime;

	private Partial endTime;

	private PeriodType periodType;

	private FrequencyType frequency;

	private List<AllocatableSpace> roomsReferences;

	private AllocatableSpace selectedRoomReference;

	private GenericEvent genericEventReference;

	private Boolean ganttDiagramAvailable;

	private String emailsTo;

	private transient Locale locale = Language.getLocale();

	public RoomsPunctualSchedulingBean() {
		setGanttDiagramAvailable(Boolean.TRUE);
	}

	public RoomsPunctualSchedulingBean(GenericEvent genericEvent) {
		if (genericEvent.getPunctualRoomsOccupationRequest() != null) {
			setRoomsReserveRequestIdentification(genericEvent.getPunctualRoomsOccupationRequest().getIdentification());
		}
		setRooms(genericEvent.getAssociatedRooms());
		setBegin(genericEvent.getBeginDate());
		setEnd(genericEvent.getEndDate());
		setBeginTime(new Partial(genericEvent.getStartTimeDateHourMinuteSecond()));
		setEndTime(new Partial(genericEvent.getEndTimeDateHourMinuteSecond()));
		setFrequency(genericEvent.getFrequency());
		setSmallDescription(genericEvent.getTitle());
		setCompleteDescription(genericEvent.getDescription());
		setGenericEvent(genericEvent);
		setGanttDiagramAvailable(Boolean.TRUE);
		setMarkSaturday(genericEvent.getDailyFrequencyMarkSaturday());
		setMarkSunday(genericEvent.getDailyFrequencyMarkSunday());
	}

	public void editFrequencyTypeWithoutDailyFrequency(YearMonthDay begin, Partial beginTime, YearMonthDay end, Partial endTime,
			FrequencyType frequency) {

		setBegin(begin);
		setEnd(end);
		setBeginTime(beginTime);
		setEndTime(endTime);
		setFrequency(frequency);
		setMarkSaturday(null);
		setMarkSunday(null);
	}

	public void editFrequencyTypeWithDailyFrequency(YearMonthDay begin, Partial beginTime, YearMonthDay end, Partial endTime,
			FrequencyType frequency, Boolean markSaturday, Boolean markSunday) {

		setBegin(begin);
		setEnd(end);
		setBeginTime(beginTime);
		setEndTime(endTime);
		setFrequency(frequency);
		setMarkSaturday(markSaturday);
		setMarkSunday(markSunday);
	}

	public void editContinuousType(YearMonthDay begin, Partial beginTime, YearMonthDay end, Partial endTime) {
		setBegin(begin);
		setEnd(end);
		setBeginTime(beginTime);
		setEndTime(endTime);
		setFrequency(null);
		setMarkSaturday(null);
		setMarkSunday(null);
	}

	public void editDailyType(YearMonthDay begin, Partial beginTime, Partial endTime) {
		setBegin(begin);
		setEnd(begin);
		setBeginTime(beginTime);
		setEndTime(endTime);
		setFrequency(null);
		setMarkSaturday(null);
		setMarkSunday(null);
	}

	public enum PeriodType {
		DAILY_TYPE, WITH_FREQUENCY, CONTINUOUS;

		public String getName() {
			return name();
		}
	}

	public Pair<Integer, Integer> getTotalAvailableRoomSpace() {
		Integer availableSpaceForExam = Integer.valueOf(0), availableNormalSpace = Integer.valueOf(0);
		for (AllocatableSpace room : getRooms()) {
			Integer capacidadeExame = room.getCapacidadeExame();
			Integer capacidadeNormal = room.getCapacidadeNormal();
			if (capacidadeNormal != null) {
				availableNormalSpace += capacidadeNormal;
			}
			if (capacidadeExame != null) {
				availableSpaceForExam += capacidadeExame;
			}
		}
		return new Pair<Integer, Integer>(availableNormalSpace, availableSpaceForExam);
	}

	public String getPresentationBeginTime() {
		return getBeginTime().toString("HH:mm");
	}

	public String getPresentationEndTime() {
		return getEndTime().toString("HH:mm");
	}

	public String getPresentationBeginDate() {
		return getBegin().toString("dd MMMM yyyy", locale) + " (" + getBegin().toDateTimeAtMidnight().toString("E", locale) + ")";
	}

	public String getPresentationEndDate() {
		return getEnd().toString("dd MMMM yyyy", locale) + " (" + getEnd().toDateTimeAtMidnight().toString("E", locale) + ")";
	}

	public YearMonthDay getBegin() {
		return begin;
	}

	public void setBegin(YearMonthDay begin) {
		this.begin = begin;
	}

	public Partial getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Partial beginTime) {
		this.beginTime = beginTime;
	}

	public YearMonthDay getEnd() {
		return end;
	}

	public void setEnd(YearMonthDay end) {
		this.end = end;
	}

	public Partial getEndTime() {
		return endTime;
	}

	public void setEndTime(Partial endTime) {
		this.endTime = endTime;
	}

	public FrequencyType getFrequency() {
		return frequency;
	}

	public void setFrequency(FrequencyType frequency) {
		this.frequency = frequency;
	}

	public PeriodType getPeriodType() {
		return periodType;
	}

	public void setPeriodType(PeriodType periodType) {
		this.periodType = periodType;
	}

	public List<AllocatableSpace> getRooms() {
		List<AllocatableSpace> result = new ArrayList<AllocatableSpace>();
		if (roomsReferences == null) {
			return result;
		}
		for (AllocatableSpace domainReference : roomsReferences) {
			result.add(domainReference);
		}
		return result;
	}

	public void setRooms(List<AllocatableSpace> rooms) {
		if (roomsReferences == null) {
			roomsReferences = new ArrayList<AllocatableSpace>();
		}
		for (AllocatableSpace room : rooms) {
			roomsReferences.add(room);
		}
	}

	public void addNewRoom(AllocatableSpace space) {
		if (space != null) {
			if (roomsReferences == null) {
				roomsReferences = new ArrayList<AllocatableSpace>();
			}
			for (AllocatableSpace domainReference : roomsReferences) {
				if (domainReference.equals(space)) {
					return;
				}
			}
			roomsReferences.add(space);
		}
	}

	public void removeRoom(AllocatableSpace oldRoom) {
		if (oldRoom != null) {
			if (roomsReferences == null) {
				roomsReferences = new ArrayList<AllocatableSpace>();
			}
			roomsReferences.remove(oldRoom);
		}
	}

	public AllocatableSpace getSelectedRoom() {
		return this.selectedRoomReference;
	}

	public void setSelectedRoom(AllocatableSpace selectedRoom) {
		this.selectedRoomReference = selectedRoom;
	}

	public GenericEvent getGenericEvent() {
		return this.genericEventReference;
	}

	public void setGenericEvent(GenericEvent genericEvent) {
		this.genericEventReference = genericEvent;
	}

	public PunctualRoomsOccupationRequest getRoomsReserveRequest() {
		return this.roomsReserveRequestReference;
	}

	public void setRoomsReserveRequest(PunctualRoomsOccupationRequest request) {
		this.roomsReserveRequestReference = request;
	}

	public MultiLanguageString getCompleteDescription() {
		return completeDescription;
	}

	public void setCompleteDescription(MultiLanguageString completeDescription) {
		this.completeDescription = completeDescription;
	}

	public MultiLanguageString getSmallDescription() {
		return smallDescription;
	}

	public void setSmallDescription(MultiLanguageString smallDescription) {
		this.smallDescription = smallDescription;
	}

	public Boolean getGanttDiagramAvailable() {
		return ganttDiagramAvailable;
	}

	public void setGanttDiagramAvailable(Boolean ganttDiagramAvailable) {
		this.ganttDiagramAvailable = ganttDiagramAvailable;
	}

	public Integer getRoomsReserveRequestIdentification() {
		return roomsReserveRequestIdentification;
	}

	public void setRoomsReserveRequestIdentification(Integer roomsReserveIdentification) {
		this.roomsReserveRequestIdentification = roomsReserveIdentification;
	}

	public Boolean getMarkSaturday() {
		return markSaturday;
	}

	public void setMarkSaturday(Boolean markSaturday) {
		this.markSaturday = markSaturday;
	}

	public Boolean getMarkSunday() {
		return markSunday;
	}

	public void setMarkSunday(Boolean markSunday) {
		this.markSunday = markSunday;
	}

	public void setEmailsTo(String emailsTo) {
		this.emailsTo = emailsTo;
	}

	public String getEmailsTo() {
		return emailsTo;
	}
}
