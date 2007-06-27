package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.RoomSubdivision.RoomSubdivisionFactoryEditor;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.injectionCode.FenixDomainObjectActionLogAnnotation;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class RoomSubdivisionInformation extends RoomSubdivisionInformation_Base {
    
    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Created roomSubdivision information", parameters = { "identification", 
	    "roomSubdivision", "begin", "end" })
    public RoomSubdivisionInformation(String identification, RoomSubdivision roomSubdivision, YearMonthDay begin, YearMonthDay end) {
        super();
        super.setSpace(roomSubdivision);
        setIdentification(identification);
        setFirstTimeInterval(begin, end);
    }
 
    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Edited roomSubdivision information", parameters = { "identification", "begin", "end" })
    public void editRoomSubdivisionCharacteristics(String identification, YearMonthDay begin, YearMonthDay end) {	
	editTimeInterval(begin, end);	
	setIdentification(identification);	
    }
    
    @Checked("SpacePredicates.checkIfLoggedPersonHasPermissionsToManageSpaceInformation")
    @FenixDomainObjectActionLogAnnotation(actionName = "Deleted roomSubdivision information", parameters = {})
    public void delete() {	
	super.delete();	
    }
 
    @Override
    public void setIdentification(String identification) {
        if(StringUtils.isEmpty(identification)) {
            throw new DomainException("error.RoomSubdivisionInformation.empty.identification"); 
        }
	super.setIdentification(identification);
    }
    
    @Override
    public void setSpace(final Space space) {
	throw new DomainException("error.incompatible.space");
    }

    public void setSpace(final RoomSubdivision roomSubdivision) {
	throw new DomainException("error.cannot.change.roomSubdivision");
    }
    
    @Override
    public String getPresentationName() {
	return getIdentification();
    }

    @Override
    public FactoryExecutor getSpaceFactoryEditor() {
	final RoomSubdivisionFactoryEditor roomFactoryEditor = new RoomSubdivisionFactoryEditor();	
	roomFactoryEditor.setIdentification(getIdentification());
	roomFactoryEditor.setSpace((RoomSubdivision) getSpace());
	roomFactoryEditor.setBegin(getNextPossibleValidFromDate());
	return roomFactoryEditor;
    }    
}
