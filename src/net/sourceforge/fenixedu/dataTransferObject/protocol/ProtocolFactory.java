package net.sourceforge.fenixedu.dataTransferObject.protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.OpenFileBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.protocols.Protocol;
import net.sourceforge.fenixedu.domain.protocols.ProtocolHistory;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolAction;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolActionType;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.YearMonthDay;

public class ProtocolFactory implements Serializable, FactoryExecutor {

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

    List<ProtocolActionType> actionTypes;

    List<ProtocolHistory> protocolHistories;//só pode editar o ultimo (actual) ou criar novo caso n exista

    List<ChoosePersonBean> responsibles;

    List<ChoosePersonBean> partnerResponsibles;

    List<UnitSearchBean> partnerUnits;

    List<UnitSearchBean> units;

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
    }

    private void setPartnersList(List<Unit> partners) {
        if (partners != null) {
            ArrayList<UnitSearchBean> partnersList = new ArrayList<UnitSearchBean>();
            for (Unit partner : partners) {
                partnersList.add(new UnitSearchBean(partner));
            }
            setPartnerUnits(partnersList);
        }
    }

    private void setUnitsList(List<Unit> units) {
        if (units != null) {
            ArrayList<UnitSearchBean> unitsList = new ArrayList<UnitSearchBean>();
            for (Unit unit : units) {
                unitsList.add(new UnitSearchBean(unit));
            }
            setUnits(unitsList);
        }
    }

    private void setPartnerResponsiblesList(List<Person> partnerResponsibles) {
        if (partnerResponsibles != null) {
            ArrayList<ChoosePersonBean> persons = new ArrayList<ChoosePersonBean>();
            for (Person person : partnerResponsibles) {
                persons.add(new ChoosePersonBean(person));
            }
            setPartnerResponsibles(persons);
        }
    }

    private void setResponsiblesList(List<Person> responsibles) {
        if (responsibles != null) {
            ArrayList<ChoosePersonBean> persons = new ArrayList<ChoosePersonBean>();
            for (Person person : responsibles) {
                persons.add(new ChoosePersonBean(person));
            }
            setResponsibles(persons);
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
        addResponsible();
        addPartnerResponsible();
        addUnit();
        addPartnerUnit();
        addFile();
    }

    public Object execute() {
        if (getProtocol() == null) {
            return new Protocol(this);
        } else {
            //editar, se já existir um ficheiro com o mesmo nome e tiver inputstream, apaga e poe o novo
            //tem de ter link para apagar, provavelmente vai ter de ser protocolFileBean, com id do file
        }
        return null;
    }

    public void addResponsible() {
        if (getResponsibles() == null) {
            setResponsibles(new ArrayList<ChoosePersonBean>());
        }
        getResponsibles().add(new ChoosePersonBean());
    }

    public void addPartnerResponsible() {
        if (getPartnerResponsibles() == null) {
            setPartnerResponsibles(new ArrayList<ChoosePersonBean>());
        }
        getPartnerResponsibles().add(new ChoosePersonBean());
    }

    public void addPartnerUnit() {
        if (getPartnerUnits() == null) {
            setPartnerUnits(new ArrayList<UnitSearchBean>());
        }
        getPartnerUnits().add(new UnitSearchBean());
    }

    public void addUnit() {
        if (getUnits() == null) {
            setUnits(new ArrayList<UnitSearchBean>());
        }
        getUnits().add(new UnitSearchBean());
    }

    public void addFile() {
        if (getFiles() == null) {
            setFiles(new ArrayList<OpenFileBean>());
        }
        getFiles().add(new OpenFileBean());
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

    public List<ChoosePersonBean> getPartnerResponsibles() {
        return partnerResponsibles;
    }

    public void setPartnerResponsibles(List<ChoosePersonBean> partnerResponsibles) {
        this.partnerResponsibles = partnerResponsibles;
    }

    public List<UnitSearchBean> getPartnerUnits() {
        return partnerUnits;
    }

    public void setPartnerUnits(List<UnitSearchBean> partnerUnits) {
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

    public List<ChoosePersonBean> getResponsibles() {
        return responsibles;
    }

    public void setResponsibles(List<ChoosePersonBean> responsibles) {
        this.responsibles = responsibles;
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

    public List<UnitSearchBean> getUnits() {
        return units;
    }

    public void setUnits(List<UnitSearchBean> units) {
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

}
