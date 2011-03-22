package net.sourceforge.fenixedu.dataTransferObject.protocol;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.ProtocolFile;
import net.sourceforge.fenixedu.domain.protocols.ProtocolHistory;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolAction;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolActionType;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.YearMonthDay;

public class ProtocolFactory implements Serializable, FactoryExecutor {

    public static enum EditProtocolAction {
	EDIT_PROTOCOL_DATA, ADD_RESPONSIBLE, REMOVE_RESPONSIBLE, ADD_UNIT, REMOVE_UNIT, DELETE_FILE
    }

    public static enum FilePermissionType {
	IST_PEOPLE, RESPONSIBLES_AND_SCIENTIFIC_COUNCIL
    }

    private EditProtocolAction editProtocolAction;

    private Protocol protocol;

    private String protocolNumber;

    private YearMonthDay signedDate;

    private Boolean renewable;

    private Boolean active;

    private String scientificAreas;

    private String observations;

    private ProtocolAction protocolAction;

    private YearMonthDay beginDate;

    private YearMonthDay endDate;

    private String otherActionTypes;

    private Boolean istResponsible;

    private Boolean internalUnit;

    private Boolean istResponsibleIsPerson;

    private Boolean functionByPerson;

    private Teacher responsible;

    private Function responsibleFunction;

    private PersonName partnerResponsible;

    private String responsibleName;

    private String responsibleFunctionName;

    private UnitName unitObject;

    private String unitName;

    private Person responsibleToAdd;

    private Person responsibleToRemove;

    private Function responsibleFunctionToRemove;

    private Unit unitToAdd;

    private Unit unitToRemove;

    private List<ProtocolActionType> actionTypes;

    private List<Person> responsibles;

    private List<Function> responsibleFunctions;

    private Unit responsibleFunctionUnit;

    private List<Person> partnerResponsibles;

    private List<Unit> partnerUnits;

    private List<Unit> units;

    private List<ProtocolFile> protocolFiles;

    private ProtocolFile fileToDelete;

    private List<ProtocolHistory> protocolHistories;

    private transient InputStream inputStream;

    private String fileName;

    private FilePermissionType filePermissionType;

    private List<ProtocolFileBean> fileBeans;

    private Country country;

    public ProtocolFactory(Protocol protocol) {
	setProtocol(protocol);
	setProtocolNumber(protocol.getProtocolNumber());
	setSignedDate(protocol.getSignedDate());
	setObservations(protocol.getObservations());
	setRenewable(protocol.getRenewable());
	setActive(protocol.getActive());
	setProtocolHistories(new ArrayList<ProtocolHistory>(protocol.getProtocolHistories()));
	setProtocolAction(protocol.getProtocolAction());
	setOtherActionTypes(protocol.getProtocolAction().getOtherTypes());
	setActionTypes(protocol.getProtocolAction().getProtocolActionTypes());
	setScientificAreas(protocol.getScientificAreas());
	setResponsiblesList(new ArrayList<Person>(protocol.getResponsibles()));
	setResponsibleFunctions(new ArrayList<Function>(protocol.getResponsibleFunctions()));
	setPartnerResponsiblesList(new ArrayList<Person>(protocol.getPartnerResponsibles()));
	setUnitsList(new ArrayList<Unit>(protocol.getUnits()));
	setPartnersList(new ArrayList<Unit>(protocol.getPartners()));
	setIstResponsible(true);
	setInternalUnit(true);
	setIstResponsibleIsPerson(true);
	setFunctionByPerson(true);
	setFilePermissionType(FilePermissionType.RESPONSIBLES_AND_SCIENTIFIC_COUNCIL);
    }

    private void setPartnersList(List<Unit> partners) {
	if (partners != null) {
	    setPartnerUnits(partners);
	}
    }

    private void setUnitsList(List<Unit> units) {
	if (units != null) {
	    setUnits(units);
	}
    }

    private void setPartnerResponsiblesList(List<Person> partnerResponsibles) {
	if (partnerResponsibles != null) {
	    setPartnerResponsibles(partnerResponsibles);
	}
    }

    private void setResponsiblesList(List<Person> responsibles) {
	if (responsibles != null) {
	    setResponsibles(responsibles);
	}
    }

    private void setActionTypes(EnumSet<ProtocolActionType> protocolActionTypes) {
	if (protocolActionTypes != null) {
	    ArrayList<ProtocolActionType> actionsTypesList = new ArrayList<ProtocolActionType>();
	    for (ProtocolActionType actionType : protocolActionTypes) {
		actionsTypesList.add(actionType);
	    }
	    setActionTypes(actionsTypesList);
	}
    }

    public ProtocolFactory() {
	setIstResponsible(true);
	setIstResponsibleIsPerson(true);
	setFunctionByPerson(true);
	setInternalUnit(true);
	setFilePermissionType(FilePermissionType.RESPONSIBLES_AND_SCIENTIFIC_COUNCIL);
    }

    public Object execute() {
	if (getProtocol() == null) {
	    return new Protocol(this);
	} else {
	    switch (getEditProtocolAction()) {
	    case EDIT_PROTOCOL_DATA:
		getProtocol().editData(this);
		break;
	    case ADD_RESPONSIBLE:
		getProtocol().addResponsible(this);
		break;
	    case REMOVE_RESPONSIBLE:
		getProtocol().removeResponsible(this);
		break;
	    case ADD_UNIT:
		getProtocol().addUnit(this);
		break;
	    case REMOVE_UNIT:
		getProtocol().removeUnit(this);
		break;
	    case DELETE_FILE:
		getProtocol().deleteFile(this);
		break;
	    default:
		return null;
	    }
	    return getProtocol();
	}
    }

    public Boolean getActive() {
	return active;
    }

    public void setActive(Boolean active) {
	this.active = active;
    }

    public String getObservations() {
	return observations;
    }

    public void setObservations(String observations) {
	this.observations = observations;
    }

    public List<Person> getPartnerResponsibles() {
	return partnerResponsibles;
    }

    public void setPartnerResponsibles(List<Person> partnerResponsibles) {
	this.partnerResponsibles = partnerResponsibles;
    }

    public List<Unit> getPartnerUnits() {
	return partnerUnits;
    }

    public void setPartnerUnits(List<Unit> partnerUnits) {
	this.partnerUnits = partnerUnits;
    }

    public Unit getResponsibleFunctionUnit() {
	return responsibleFunctionUnit;
    }

    public void setResponsibleFunctionUnit(Unit responsibleFunctionUnit) {
	this.responsibleFunctionUnit = responsibleFunctionUnit;
    }

    public Protocol getProtocol() {
	return protocol;
    }

    public void setProtocol(Protocol protocol) {
	if (protocol != null) {
	    this.protocol = protocol;
	} else {
	    this.protocol = null;
	}
    }

    public ProtocolAction getProtocolAction() {
	return protocolAction;
    }

    public void setProtocolAction(ProtocolAction protocolAction) {
	this.protocolAction = protocolAction;
    }

    public String getProtocolNumber() {
	return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
	this.protocolNumber = protocolNumber;
    }

    public Boolean getRenewable() {
	return renewable;
    }

    public void setRenewable(Boolean renewable) {
	this.renewable = renewable;
    }

    public List<Person> getResponsibles() {
	return responsibles;
    }

    public void setResponsibles(List<Person> responsibles) {
	this.responsibles = responsibles;
    }

    public List<Function> getResponsibleFunctions() {
	return responsibleFunctions;
    }

    public void setResponsibleFunctions(List<Function> responsibleFunctions) {
	this.responsibleFunctions = responsibleFunctions;
    }

    public String getScientificAreas() {
	return scientificAreas;
    }

    public void setScientificAreas(String scientificAreas) {
	this.scientificAreas = scientificAreas;
    }

    public YearMonthDay getSignedDate() {
	return signedDate;
    }

    public void setSignedDate(YearMonthDay signedDate) {
	this.signedDate = signedDate;
    }

    public List<Unit> getUnits() {
	return units;
    }

    public void setUnits(List<Unit> units) {
	this.units = units;
    }

    public YearMonthDay getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(YearMonthDay beginDate) {
	this.beginDate = beginDate;
    }

    public YearMonthDay getEndDate() {
	return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
	this.endDate = endDate;
    }

    public List<ProtocolActionType> getActionTypes() {
	return actionTypes;
    }

    public void setActionTypes(List<ProtocolActionType> actionTypes) {
	this.actionTypes = actionTypes;
    }

    public String getOtherActionTypes() {
	return otherActionTypes;
    }

    public void setOtherActionTypes(String otherActionTypes) {
	this.otherActionTypes = otherActionTypes;
    }

    public List<ProtocolFile> getProtocolFiles() {
	return protocolFiles;
    }

    public void setProtocolFiles(List<ProtocolFile> protocolFiles) {
	this.protocolFiles = protocolFiles;
    }

    public EditProtocolAction getEditProtocolAction() {
	return editProtocolAction;
    }

    public void setEditProtocolAction(EditProtocolAction editProtocolAction) {
	this.editProtocolAction = editProtocolAction;
    }

    public Boolean getIstResponsible() {
	return istResponsible;
    }

    public void setIstResponsible(Boolean istResponsible) {
	this.istResponsible = istResponsible;
    }

    public PersonName getPartnerResponsible() {
	return partnerResponsible;
    }

    public void setPartnerResponsible(PersonName responsible) {
	this.partnerResponsible = responsible;
    }

    public Teacher getResponsible() {
	return responsible;
    }

    public void setResponsible(Teacher responsible) {
	this.responsible = responsible;
    }

    public Function getResponsibleFunction() {
	return responsibleFunction;
    }

    public void setResponsibleFunction(Function responsibleFunction) {
	this.responsibleFunction = responsibleFunction;
    }

    public UnitName getUnitObject() {
	return unitObject;
    }

    public void setUnitObject(UnitName unitObject) {
	this.unitObject = unitObject;
    }

    public Boolean getInternalUnit() {
	return internalUnit;
    }

    public void setInternalUnit(Boolean internalUnit) {
	this.internalUnit = internalUnit;
    }

    public String getResponsibleName() {
	return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
	this.responsibleName = responsibleName;
    }

    public String getUnitName() {
	return unitName;
    }

    public void setUnitName(String unitName) {
	this.unitName = unitName;
    }

    public boolean addISTResponsible() {
	if (getResponsibles() == null) {
	    setResponsibles(new ArrayList<Person>());
	}
	if (getResponsibles().contains(getResponsible().getPerson())) {
	    return false;
	} else {
	    return getResponsibles().add(getResponsible().getPerson());
	}
    }

    public boolean addISTResponsibleFunction() {
	if (getResponsibleFunctions() == null) {
	    setResponsibleFunctions(new ArrayList<Function>());
	}
	if (getResponsibleFunctions().contains(getResponsibleFunction())) {
	    return false;
	} else {
	    return getResponsibleFunctions().add(getResponsibleFunction());
	}
    }

    public boolean addPartnerResponsible() {
	if (getPartnerResponsibles() == null) {
	    setPartnerResponsibles(new ArrayList<Person>());
	}
	if (getPartnerResponsibles().contains(getPartnerResponsible().getPerson())) {
	    return false;
	} else {
	    return getPartnerResponsibles().add(getPartnerResponsible().getPerson());
	}
    }

    public void addPartnerResponsible(Person responsible) {
	if (getPartnerResponsibles() == null) {
	    setPartnerResponsibles(new ArrayList<Person>());
	}
	getPartnerResponsibles().add(responsible);
    }

    public boolean addISTUnit() {
	if (getUnits() == null) {
	    setUnits(new ArrayList<Unit>());
	}
	if (getUnits().contains(getUnitObject().getUnit())) {
	    return false;
	} else {
	    return getUnits().add(getUnitObject().getUnit());
	}
    }

    public boolean addPartnerUnit() {
	if (getPartnerUnits() == null) {
	    setPartnerUnits(new ArrayList<Unit>());
	}
	if (getPartnerUnits().contains(getUnitObject().getUnit())) {
	    return false;
	} else {
	    return getPartnerUnits().add(getUnitObject().getUnit());
	}
    }

    public void addISTUnit(Unit unit) {
	if (getUnits() == null) {
	    setUnits(new ArrayList<Unit>());
	}
	getUnits().add(unit);
    }

    public void addPartnerUnit(Unit unit) {
	if (getPartnerUnits() == null) {
	    setPartnerUnits(new ArrayList<Unit>());
	}
	getPartnerUnits().add(unit);
    }

    public void addFile() {
	if (getFileBeans() == null) {
	    setFileBeans(new ArrayList<ProtocolFileBean>());
	}
	getFileBeans().add(new ProtocolFileBean(getInputStream(), getFileName(), getFilePermissionType()));
    }

    public void removeFile(String fileName) {
	for (ProtocolFileBean protocolFileBean : getFileBeans()) {
	    if (protocolFileBean.getFileName().equals(fileName)) {
		getFileBeans().remove(protocolFileBean);
		break;
	    }
	}
    }

    public void resetSearches() {
	setPartnerResponsible(null);
	setResponsible(null);
	setResponsibleName(null);
	setUnitObject(null);
	setUnitName(null);
    }

    public void resetFile() {
	setFileName(null);
	setInputStream(null);
    }

    public ProtocolHistory getActualProtocolHistory() {
	ProtocolHistory actualProtocolHistory = getProtocol().getActualProtocolHistory();
	return actualProtocolHistory != null ? actualProtocolHistory : getProtocol().getLastProtocolHistory();
    }

    public Person getResponsibleToAdd() {
	return responsibleToAdd;
    }

    public void setResponsibleToAdd(Person responsibleToAdd) {
	this.responsibleToAdd = responsibleToAdd;
    }

    public Person getResponsibleToRemove() {
	return responsibleToRemove;
    }

    public void setResponsibleToRemove(Person responsibleToRemove) {
	this.responsibleToRemove = responsibleToRemove;
    }

    public Function getResponsibleFunctionToRemove() {
	return responsibleFunctionToRemove;
    }

    public void setResponsibleFunctionToRemove(Function responsibleFunctionToRemove) {
	this.responsibleFunctionToRemove = responsibleFunctionToRemove;
    }

    public Unit getUnitToAdd() {
	return unitToAdd;
    }

    public void setUnitToAdd(Unit unitToAdd) {
	this.unitToAdd = unitToAdd;
    }

    public Unit getUnitToRemove() {
	return unitToRemove;
    }

    public void setUnitToRemove(Unit unitToRemove) {
	this.unitToRemove = unitToRemove;
    }

    public ProtocolFile getFileToDelete() {
	return fileToDelete;
    }

    public void setFileToDelete(ProtocolFile fileToDelete) {
	this.fileToDelete = fileToDelete;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public InputStream getInputStream() {
	return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
    }

    public FilePermissionType getFilePermissionType() {
	return filePermissionType;
    }

    public void setFilePermissionType(FilePermissionType filePermissionType) {
	this.filePermissionType = filePermissionType;
    }

    public List<ProtocolFileBean> getFileBeans() {
	return fileBeans;
    }

    public void setFileBeans(List<ProtocolFileBean> fileBeans) {
	this.fileBeans = fileBeans;
    }

    public Country getCountry() {
	return country;
    }

    public void setCountry(Country country) {
	this.country = country;
    }

    public List<ProtocolHistory> getProtocolHistories() {
	return protocolHistories;
    }

    public void setProtocolHistories(List<ProtocolHistory> protocolHistories) {
	this.protocolHistories = protocolHistories;
    }

    public Boolean getIstResponsibleIsPerson() {
	return istResponsibleIsPerson;
    }

    public void setIstResponsibleIsPerson(Boolean istResponsibleIsPerson) {
	this.istResponsibleIsPerson = istResponsibleIsPerson;
    }

    public String getResponsibleFunctionName() {
	return responsibleFunctionName;
    }

    public void setResponsibleFunctionName(String responsibleFunctionName) {
	this.responsibleFunctionName = responsibleFunctionName;
    }

    public Boolean getFunctionByPerson() {
	return functionByPerson;
    }

    public void setFunctionByPerson(Boolean functionByPerson) {
	this.functionByPerson = functionByPerson;
    }
}
