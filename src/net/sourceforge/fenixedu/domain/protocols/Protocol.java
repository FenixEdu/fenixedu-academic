package net.sourceforge.fenixedu.domain.protocols;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.dataTransferObject.research.result.OpenFileBean;
import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolAction;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class Protocol extends Protocol_Base {

    public Protocol() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Protocol(ProtocolFactory protocolFactory) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setProtocolNumber(protocolFactory.getProtocolNumber());
        setSignedDate(protocolFactory.getSignedDate());
        setDates(protocolFactory);
        setProtocolAction(new ProtocolAction(protocolFactory.getActionTypes(), protocolFactory
                .getOtherActionTypes()));
        setObservations(protocolFactory.getObservations());
        setScientificAreas(protocolFactory.getScientificAreas());
        setResponsables(protocolFactory.getResponsibles());
        setPartnerResponsibles(protocolFactory.getPartnerResponsibles());
        setUnits(protocolFactory.getUnits());
        setPartnerUnits(protocolFactory.getPartnerUnits());
        if (protocolFactory.getFiles() != null) {
            writeFiles(protocolFactory.getFiles());
        }
    }

    public Protocol(String protocolNumber, YearMonthDay signedDate, Boolean renewable, Boolean active,
            String scientificAreas, String observations, ProtocolHistory protocolHistory,
            ProtocolAction protocolAction, Person responsible, List<Unit> unitList,
            List<Unit> partnersList) {
        setRootDomainObject(RootDomainObject.getInstance());
        setProtocolNumber(protocolNumber);
        setRenewable(renewable);
        setActive(active);
        setSignedDate(signedDate);
        addProtocolHistories(protocolHistory);
        setProtocolAction(protocolAction);
        setObservations(observations);
        setScientificAreas(scientificAreas);
        if (responsible != null) {
            getResponsibles().add(responsible);
        }
        if (unitList != null && unitList.size() != 0) {
            getUnits().addAll(unitList);
        }
        if (partnersList != null && partnersList.size() != 0) {
            getPartners().addAll(partnersList);
        }
    }

    private void setDates(ProtocolFactory protocolFactory) {
        ProtocolHistory protocolHistory = new ProtocolHistory(protocolFactory.getBeginDate(),
                protocolFactory.getEndDate());
        getProtocolHistories().add(protocolHistory);
    }

    private void writeFiles(List<OpenFileBean> files) {
        final VirtualPath filePath = getFilePath();
        for (OpenFileBean openFileBean : files) {
            writeFile(filePath, openFileBean);
        }
    }

    private void writeFile(VirtualPath filePath, OpenFileBean openFileBean) {
        final Group group = getGroup();
        final FileDescriptor fileDescriptor = FileManagerFactory.getFactoryInstance().getFileManager()
                .saveFile(filePath, openFileBean.getFileName(), false, null, openFileBean.getFileName(),
                        openFileBean.getInputStream());

        final ProtocolFile protocolFile = new ProtocolFile(openFileBean.getFileName(), openFileBean
                .getFileName(), fileDescriptor.getMimeType(), fileDescriptor.getChecksum(),
                fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor
                        .getUniqueId(), group);
        getProtocolFiles().add(protocolFile);
    }

    protected VirtualPath getFilePath() {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode("ProtocolFiles", "Protocol Files"));
        filePath.addNode(new VirtualPathNode("Protocol" + getIdInternal(), getProtocolNumber()));
        return filePath;
    }

    private Group getGroup() {
        // final PersonGroup personGroup = new PersonGroup((Person) party);
        return new RoleGroup(Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL));
        // return new GroupUnion(personGroup, roleGroup);
    }

    private void setResponsables(List<Person> responsibles) {
        if (responsibles != null) {
            getResponsibles().addAll(responsibles);
        }
    }

    private void setPartnerResponsibles(List<Person> partnerResponsibles) {
        if (partnerResponsibles != null) {
            getPartnerResponsibles().addAll(partnerResponsibles);
        }
    }

    private void setUnits(DomainListReference<Unit> units) {
        if (units != null) {
            getUnits().addAll(units);
        }
    }

    private void setPartnerUnits(DomainListReference<Unit> partnerUnits) {
        if (partnerUnits != null) {
            getPartners().addAll(partnerUnits);
        }
    }

    public void editData(ProtocolFactory protocolFactory) {
        setProtocolNumber(protocolFactory.getProtocolNumber());
        setSignedDate(protocolFactory.getSignedDate());
        setProtocolAction(new ProtocolAction(protocolFactory.getActionTypes(), protocolFactory
                .getOtherActionTypes()));
        setObservations(protocolFactory.getObservations());
    }

    public void editResponsibles(ProtocolFactory protocolFactory) {
        if (protocolFactory.getIstResponsible()) {
            getResponsibles().add(protocolFactory.getResponsibleToAdd());
        } else {
            getPartnerResponsibles().add(protocolFactory.getResponsibleToAdd());
        }
    }

    public void editUnits(ProtocolFactory protocolFactory) {
        if (protocolFactory.getInternalUnit()) {
            getUnits().add(protocolFactory.getUnitToAdd());
        } else {
            getPartners().add(protocolFactory.getUnitToAdd());
        }
    }
}
