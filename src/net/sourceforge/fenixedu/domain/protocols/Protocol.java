package net.sourceforge.fenixedu.domain.protocols;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.dataTransferObject.protocol.UnitSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.OpenFileBean;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolAction;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class Protocol extends Protocol_Base {
    
    public  Protocol() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Protocol(ProtocolFactory protocolFactory) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setProtocolNumber(protocolFactory.getProtocolNumber());
        setSignedDate(protocolFactory.getSignedDate());
        setDates(protocolFactory);
        setProtocolAction(new ProtocolAction(protocolFactory.getActionTypes(),protocolFactory.getOtherActionTypes()));
        setObservations(protocolFactory.getObservations());
        setResponsables(protocolFactory.getResponsibles());
        setPartnerResponsibles(protocolFactory.getPartnerResponsibles());
        setUnits(protocolFactory.getUnits());
        setPartnerUnits(protocolFactory.getPartnerUnits());
        writeFiles(protocolFactory.getFiles());
    }
    
    private void setDates(ProtocolFactory protocolFactory) {
        ProtocolHistory protocolHistory = new ProtocolHistory(protocolFactory.getBeginDate(),protocolFactory.getEndDate());
        getProtocolHistories().add(protocolHistory);
    }

    private void writeFiles(List<OpenFileBean> files){
        final VirtualPath filePath = getFilePath();
        for (OpenFileBean openFileBean : files) {
            writeFile(filePath,openFileBean);
        }
    }
    
    private void writeFile(VirtualPath filePath, OpenFileBean openFileBean){        
        final Group group = getGroup();
        final FileDescriptor fileDescriptor = FileManagerFactory.getFactoryInstance().getFileManager().saveFile(
                filePath, openFileBean.getFileName(), false, null, openFileBean.getFileName(), openFileBean.getInputStream());

        final ProtocolFile protocolFile = new ProtocolFile(openFileBean.getFileName(), openFileBean.getFileName(), fileDescriptor
                .getMimeType(), fileDescriptor.getChecksum(), fileDescriptor
                .getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor.getUniqueId(),
                group);
        getProtocolFiles().add(protocolFile);
    }
    
    protected VirtualPath getFilePath() {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode("ProtocolFiles", "Protocol Files"));
        filePath.addNode(new VirtualPathNode("Protocol" + getIdInternal(), getProtocolNumber()));
        return filePath;
    }
    
    private Group getGroup() {
        //final PersonGroup personGroup = new PersonGroup((Person) party);
        return new RoleGroup(Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL));
        //return new GroupUnion(personGroup, roleGroup);
    }
    
    private void setResponsables(List<ChoosePersonBean> responsibles) {
        if(responsibles != null){
            for (ChoosePersonBean personBean : responsibles) {
                if(personBean.getPerson() != null){
                    getResponsibles().add(personBean.getPerson());
                }
            }
        }        
    }
    
    private void setPartnerResponsibles(List<ChoosePersonBean> partnerResponsibles) {
        if(partnerResponsibles != null){
            for (ChoosePersonBean personBean : partnerResponsibles) {
                if(personBean.getPerson() != null){
                    getPartnerResponsibles().add(personBean.getPerson());
                }
            }
        }        
    }
    
    private void setUnits(List<UnitSearchBean> units) {
        if(units != null){
            for (UnitSearchBean unitBean : units) {
                if(unitBean.getUnit() != null){
                    getUnits().add(unitBean.getUnit());
                }
            }
        }        
    }
    
    private void setPartnerUnits(List<UnitSearchBean> partnerUnits) {
        if(partnerUnits != null){
            for (UnitSearchBean unitBean : partnerUnits) {
                if(unitBean.getUnit() != null){
                    getPartners().add(unitBean.getUnit());
                }
            }
        }        
    }
}
