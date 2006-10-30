package net.sourceforge.fenixedu.domain.space;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

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

	private String classification;

	private BigDecimal area;

	private Boolean heightQuality;

	private Boolean illuminationQuality;

	private Boolean distanceFromSanitaryInstalationsQuality;

	private Boolean securityQuality;

	private Boolean ageQuality;

	private String observations;

	private YearMonthDay begin;

	private YearMonthDay end;

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

	public String getClassification() {
	    return classification;
	}

	public void setClassification(String classification) {
	    this.classification = classification;
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
	    return RoomClassification.findRoomClassificationByPresentationCode(getClassification());
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
	    return new Room(this);
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
	    return new RoomInformation(getSpace(), this);
	}
    }

    protected Room() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }

    public Room(RoomFactoryCreator roomFactoryCreator) {
	this();	
	
	final Space suroundingSpace = roomFactoryCreator.getSurroundingSpace();
	if (suroundingSpace == null) {
	    throw new DomainException("error.surrounding.space");
	}
	checkIfLoggedPersonHasPermissionsToManageSpace(AccessControl.getUserView().getPerson(), suroundingSpace);	
	setSuroundingSpace(suroundingSpace);
	new RoomInformation(this, roomFactoryCreator);
    }
    
    @Override
    public RoomInformation getSpaceInformation() {
	return (RoomInformation) super.getSpaceInformation();
    }

    @Override
    public RoomInformation getSpaceInformation(final YearMonthDay when) {
	return (RoomInformation) super.getSpaceInformation(when);
    }

}
