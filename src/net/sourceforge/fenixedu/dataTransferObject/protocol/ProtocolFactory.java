package net.sourceforge.fenixedu.dataTransferObject.protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.research.result.OpenFileBean;
import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.PersonName;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.ProtocolHistory;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolAction;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolActionType;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.YearMonthDay;

public class ProtocolFactory implements Serializable, FactoryExecutor {

    public static enum EditProtocolAction {
        EDIT_PROTOCOL_DATA, EDIT_PROTOCOL_RESPONSIBLES, EDIT_PROTOCOL_UNITS, EDIT_PROTOCOL_FILES
    }

    EditProtocolAction editProtocolAction;

    DomainReference<Protocol> protocol;

    String protocolNumber;

    YearMonthDay signedDate;

    Boolean renewable;

    Boolean active;

    String scientificAreas;

    String observations;

    ProtocolAction protocolAction;

    YearMonthDay beginDate;

    YearMonthDay endDate;

    String otherActionTypes;

    Integer[] filesToDelete;

    Boolean istResponsible;

    Boolean internalUnit;

    DomainReference<PersonName> responsible;

    String responsibleName;

    DomainReference<UnitName> unitObject;

    String unitName;

    DomainReference<Person> responsibleToAdd;

    DomainReference<Unit> unitToAdd;

    List<ProtocolActionType> actionTypes;

    List<ProtocolHistory> protocolHistories;//só pode editar o ultimo (actual) ou criar novo caso n exista

    DomainListReference<Person> responsibles;

    DomainListReference<Person> partnerResponsibles;

    DomainListReference<Unit> partnerUnits;

    DomainListReference<Unit> units;

    List<OpenFileBean> files;

    public ProtocolFactory(Protocol protocol) {
        setProtocol(protocol);
        setProtocolNumber(protocol.getProtocolNumber());
        setSignedDate(protocol.getSignedDate());
        setObservations(protocol.getObservations());
        setRenewable(protocol.getRenewable());
        setActive(protocol.getActive());
        //setProtocolHistories(protocol.getProtocolHistories());
        setProtocolAction(protocol.getProtocolAction());
        setOtherActionTypes(protocol.getProtocolAction().getOtherTypes());
        setActionTypes(protocol.getProtocolAction().getProcotolActionTypes());
        setResponsiblesList(protocol.getResponsibles());
        setPartnerResponsiblesList(protocol.getPartnerResponsibles());
        setUnitsList(protocol.getUnits());
        setPartnersList(protocol.getPartners());
        setIstResponsible(true);
        setInternalUnit(true);
    }

    private void setPartnersList(List<Unit> partners) {
        if (partners != null) {
            setPartnerUnits(new DomainListReference<Unit>(partners));
        }
    }

    private void setUnitsList(List<Unit> units) {
        if (units != null) {
            setUnits(new DomainListReference<Unit>(units));
        }
    }

    private void setPartnerResponsiblesList(List<Person> partnerResponsibles) {
        if (partnerResponsibles != null) {
            setPartnerResponsibles(new DomainListReference<Person>(partnerResponsibles));
        }
    }

    private void setResponsiblesList(List<Person> responsibles) {
        if (responsibles != null) {
            setResponsibles(new DomainListReference<Person>(responsibles));
        }
    }

    private void setActionTypes(EnumSet<ProtocolActionType> procotolActionTypes) {
        if (procotolActionTypes != null) {
            ArrayList<ProtocolActionType> actionsTypesList = new ArrayList<ProtocolActionType>();
            for (ProtocolActionType actionType : procotolActionTypes) {
                actionsTypesList.add(actionType);
            }
            setActionTypes(actionsTypesList);
        }
    }

    public ProtocolFactory() {
        setIstResponsible(true);
        setInternalUnit(true);
    }

    public Object execute() {
        if (getProtocol() == null) {
            return new Protocol(this);
        } else {
            if (getEditProtocolAction().equals(EditProtocolAction.EDIT_PROTOCOL_DATA)) {
                getProtocol().editData(this);
                return getProtocol();
            } else if (getEditProtocolAction().equals(EditProtocolAction.EDIT_PROTOCOL_RESPONSIBLES)) {
                getProtocol().editResponsibles(this);
                return getProtocol();
            } else if (getEditProtocolAction().equals(EditProtocolAction.EDIT_PROTOCOL_UNITS)) {
                getProtocol().editUnits(this);
                return getProtocol();
            } else if (getEditProtocolAction().equals(EditProtocolAction.EDIT_PROTOCOL_FILES)) {
                //getProtocol().editFiles(this);
                //return getProtocol();
            }
            //editar, se já existir um ficheiro com o mesmo nome e tiver inputstream, apaga e poe o novo
            //tem de ter link para apagar, provavelmente vai ter de ser protocolFileBean, com id do file
        }
        return null;
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
        this.partnerResponsibles = new DomainListReference<Person>(partnerResponsibles);
    }

    public DomainListReference<Unit> getPartnerUnits() {
        return partnerUnits;
    }

    public void setPartnerUnits(DomainListReference<Unit> partnerUnits) {
        this.partnerUnits = partnerUnits;
    }

    public Protocol getProtocol() {
        return protocol != null ? protocol.getObject() : null;
    }

    public void setProtocol(Protocol protocol) {
        if (protocol != null) {
            this.protocol = new DomainReference<Protocol>(protocol);
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

    public List<ProtocolHistory> getProtocolHistories() {
        return protocolHistories;
    }

    public void setProtocolHistories(List<ProtocolHistory> protocolHistories) {
        this.protocolHistories = protocolHistories;
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
        this.responsibles = new DomainListReference<Person>(responsibles);
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

    public DomainListReference<Unit> getUnits() {
        return units;
    }

    public void setUnits(DomainListReference<Unit> units) {
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

    public List<OpenFileBean> getFiles() {
        return files;
    }

    public void setFiles(List<OpenFileBean> files) {
        this.files = files;
    }

    public Integer[] getFilesToDelete() {
        return filesToDelete;
    }

    public void setFilesToDelete(Integer[] filesToDelete) {
        this.filesToDelete = filesToDelete;
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

    public PersonName getResponsible() {
        return responsible != null ? responsible.getObject() : null;
    }

    public void setResponsible(PersonName responsible) {
        this.responsible = (responsible != null) ? new DomainReference<PersonName>(responsible) : null;
    }

    public UnitName getUnitObject() {
        return unitObject != null ? unitObject.getObject() : null;
    }

    public void setUnitObject(UnitName unitObject) {
        this.unitObject = unitObject != null ? new DomainReference<UnitName>(unitObject) : null;
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

    public void addPartnerResponsible(Person person) {
        if (getPartnerResponsibles() != null) {
            getPartnerResponsibles().add(person);
        } else {
            setPartnerResponsibles(new DomainListReference<Person>());
            getPartnerResponsibles().add(person);
        }
    }

    public void resetSearches() {
        setResponsible(null);
        setResponsibleName(null);
        setUnitObject(null);
        setUnitName(null);
    }

    public Person getResponsibleToAdd() {
        return responsibleToAdd != null ? responsibleToAdd.getObject() : null;
    }

    public void setResponsibleToAdd(Person responsibleToAdd) {
        this.responsibleToAdd = responsibleToAdd != null ? new DomainReference<Person>(responsibleToAdd) : null;
    }

    public Unit getUnitToAdd() {
        return unitToAdd != null ? unitToAdd.getObject() : null;
    }

    public void setUnitToAdd(Unit unitToAdd) {
        this.unitToAdd = unitToAdd != null ? new DomainReference<Unit>(unitToAdd) : null;
    }

}
