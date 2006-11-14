package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public class Room extends Room_Base {

    public final static Comparator<Room> ROOM_COMPARATOR_BY_PRESENTATION_NAME = new ComparatorChain();
    static {
	((ComparatorChain) ROOM_COMPARATOR_BY_PRESENTATION_NAME).addComparator(new BeanComparator(
		"spaceInformation.presentationName", Collator.getInstance()));
	((ComparatorChain) ROOM_COMPARATOR_BY_PRESENTATION_NAME).addComparator(new BeanComparator(
		"idInternal"));
    }

    public static abstract class RoomFactory implements Serializable, FactoryExecutor {
	private String blueprintNumber;

	private String identification;

	private String description;

	private BigDecimal area;

	private Boolean heightQuality;

	private Boolean illuminationQuality;

	private Boolean distanceFromSanitaryInstalationsQuality;

	private Boolean securityQuality;

	private Boolean ageQuality;

	private String observations;

	private YearMonthDay begin;

	private YearMonthDay end;

	private DomainReference<RoomClassification> roomClassificationReference;

	public YearMonthDay getBegin() {
	    return begin;
	}

	public void setBegin(YearMonthDay begin) {
	    this.begin = begin;
	}

	public YearMonthDay getEnd() {
	    return end;
	}

	public void setEnd(YearMonthDay end) {
	    this.end = end;
	}

	public Boolean getAgeQuality() {
	    return ageQuality;
	}

	public void setAgeQuality(Boolean ageQuality) {
	    this.ageQuality = ageQuality;
	}

	public BigDecimal getArea() {
	    return area;
	}

	public void setArea(BigDecimal area) {
	    this.area = area;
	}

	public String getBlueprintNumber() {
	    return blueprintNumber;
	}

	public void setBlueprintNumber(String blueprintNumber) {
	    this.blueprintNumber = blueprintNumber;
	}

	public String getDescription() {
	    return description;
	}

	public void setDescription(String description) {
	    this.description = description;
	}

	public Boolean getDistanceFromSanitaryInstalationsQuality() {
	    return distanceFromSanitaryInstalationsQuality;
	}

	public void setDistanceFromSanitaryInstalationsQuality(
		Boolean distanceFromSanitaryInstalationsQuality) {
	    this.distanceFromSanitaryInstalationsQuality = distanceFromSanitaryInstalationsQuality;
	}

	public Boolean getHeightQuality() {
	    return heightQuality;
	}

	public void setHeightQuality(Boolean heightQuality) {
	    this.heightQuality = heightQuality;
	}

	public String getIdentification() {
	    return identification;
	}

	public void setIdentification(String identification) {
	    this.identification = identification;
	}

	public Boolean getIlluminationQuality() {
	    return illuminationQuality;
	}

	public void setIlluminationQuality(Boolean illuminationQuality) {
	    this.illuminationQuality = illuminationQuality;
	}

	public String getObservations() {
	    return observations;
	}

	public void setObservations(String observations) {
	    this.observations = observations;
	}

	public Boolean getSecurityQuality() {
	    return securityQuality;
	}

	public void setSecurityQuality(Boolean securityQuality) {
	    this.securityQuality = securityQuality;
	}

	public RoomClassification getRoomClassification() {
	    return (this.roomClassificationReference != null) ? this.roomClassificationReference
		    .getObject() : null;
	}

	public void setRoomClassification(RoomClassification roomClassification) {
	    this.roomClassificationReference = (roomClassification != null) ? new DomainReference<RoomClassification>(
		    roomClassification)
		    : null;
	}
    }

    public static class RoomFactoryCreator extends RoomFactory {
	private DomainReference<Space> surroundingSpaceReference;

	public Space getSurroundingSpace() {
	    return surroundingSpaceReference == null ? null : surroundingSpaceReference.getObject();
	}

	public void setSurroundingSpace(Space surroundingSpace) {
	    if (surroundingSpace != null) {
		this.surroundingSpaceReference = new DomainReference<Space>(surroundingSpace);
	    }
	}

	public Room execute() {
	    return new Room(getSurroundingSpace(), getBlueprintNumber(), getIdentification(),
		    getDescription(), getRoomClassification(), getArea(), getHeightQuality(),
		    getIlluminationQuality(), getDistanceFromSanitaryInstalationsQuality(),
		    getSecurityQuality(), getAgeQuality(), getObservations(), getBegin(), getEnd());
	}
    }

    public static class RoomFactoryEditor extends RoomFactory {
	private DomainReference<Room> roomReference;

	public Room getSpace() {
	    return roomReference == null ? null : roomReference.getObject();
	}

	public void setSpace(Room room) {
	    if (room != null) {
		this.roomReference = new DomainReference<Room>(room);
	    }
	}

	public RoomInformation execute() {
	    return new RoomInformation(getSpace(), getBlueprintNumber(), getIdentification(),
		    getDescription(), getRoomClassification(), getArea(), getHeightQuality(),
		    getIlluminationQuality(), getDistanceFromSanitaryInstalationsQuality(),
		    getSecurityQuality(), getAgeQuality(), getObservations(), getBegin(), getEnd());
	}
    }

    protected Room() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }

    public Room(Space suroundingSpace, String blueprintNumber, String identification,
	    String description, RoomClassification roomClassification, BigDecimal area,
	    Boolean heightQuality, Boolean illuminationQuality,
	    Boolean distanceFromSanitaryInstalationsQuality, Boolean securityQuality,
	    Boolean ageQuality, String observations, YearMonthDay begin, YearMonthDay end) {

	this();

	if (suroundingSpace == null) {
	    throw new DomainException("error.surrounding.space");
	}
	setSuroundingSpace(suroundingSpace);
	new RoomInformation(this, blueprintNumber, identification, description, roomClassification,
		area, heightQuality, illuminationQuality, distanceFromSanitaryInstalationsQuality,
		securityQuality, ageQuality, observations, begin, end);
    }
    
    @Override
    public RoomInformation getSpaceInformation() {
	return (RoomInformation) super.getSpaceInformation();
    }

    @Override
    public RoomInformation getSpaceInformation(final YearMonthDay when) {
	return (RoomInformation) super.getSpaceInformation(when);
    }

    @Checked("SpacePredicates.checkPermissionsToManageSpace")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted room", parameters = {})
    public void delete() {
	super.delete();
    }
}
