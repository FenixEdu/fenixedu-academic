package net.sourceforge.fenixedu.domain.protocols;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFileBean;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory.FilePermissionType;
import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolAction;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolActionType;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.commons.beanutils.BeanComparator;
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
        if(areDatesActive()) {
            setActive(Boolean.TRUE);
        } else {
            setActive(Boolean.FALSE);
        }
        if (protocolFactory.getFileBeans() != null) {
            writeFiles(protocolFactory.getFileBeans());
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

    private void writeFiles(List<ProtocolFileBean> files) {
        final VirtualPath filePath = getFilePath();
        for (ProtocolFileBean protocolFileBean : files) {
            writeFile(filePath, protocolFileBean.getInputStream(), protocolFileBean.getFileName(),
                    protocolFileBean.getFilePermissionType());
        }
    }

    private void writeFile(VirtualPath filePath, InputStream inputStream, String fileName,
            FilePermissionType filePermissionType) {
        final FileDescriptor fileDescriptor = FileManagerFactory.getFactoryInstance().getFileManager()
                .saveFile(filePath, fileName, false, null, fileName, inputStream);

        final ProtocolFile protocolFile = new ProtocolFile(fileName, fileName, fileDescriptor
                .getMimeType(), fileDescriptor.getChecksum(), fileDescriptor.getChecksumAlgorithm(),
                fileDescriptor.getSize(), fileDescriptor.getUniqueId(), getGroup(filePermissionType));
        getProtocolFiles().add(protocolFile);
    }

    protected VirtualPath getFilePath() {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode("ProtocolFiles", "Protocol Files"));
        filePath.addNode(new VirtualPathNode("Protocol" + getIdInternal(), getProtocolNumber()));
        return filePath;
    }

    private Group getGroup(FilePermissionType filePermissionType) {
        if (filePermissionType.equals(FilePermissionType.RESPONSIBLES_AND_SCIENTIFIC_COUNCIL)) {
            Group unionGroup = null;
            for (Person responsible : getResponsibles()) {
                PersonGroup personGroup = new PersonGroup(responsible);
                if (unionGroup == null) {
                    unionGroup = new GroupUnion(personGroup, personGroup);
                } else {
                    unionGroup = new GroupUnion(unionGroup, personGroup);
                }
            }
            final RoleGroup roleGroup = new RoleGroup(Role
                    .getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL));
            return new GroupUnion(unionGroup, roleGroup);
        } else if (filePermissionType.equals(FilePermissionType.IST_PEOPLE)) {
            return new InternalPersonGroup();
        }
        return null;
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
        setScientificAreas(protocolFactory.getScientificAreas());
        setActive(protocolFactory.getActive());
        for (ProtocolHistory protocolHistory : protocolFactory.getProtocolHistories()) {
            setProtocolHistory(protocolHistory);
        }
    }

    private void setProtocolHistory(ProtocolHistory protocolHistory) {
        for (ProtocolHistory originalProtocolHistory : getProtocolHistories()) {
            if (protocolHistory.getIdInternal().equals(originalProtocolHistory.getIdInternal())) {
                originalProtocolHistory.setBeginDate(protocolHistory.getBeginDate());
                originalProtocolHistory.setEndDate(protocolHistory.getEndDate());
            }
        }
    }

    public void addResponsible(ProtocolFactory protocolFactory) {
        if (protocolFactory.getIstResponsible()) {
            getResponsibles().add(protocolFactory.getResponsibleToAdd());
        } else {
            getPartnerResponsibles().add(protocolFactory.getResponsibleToAdd());
        }
    }

    public void removeResponsible(ProtocolFactory protocolFactory) {
        if (protocolFactory.getIstResponsible()) {
            getResponsibles().remove(protocolFactory.getResponsibleToRemove());
        } else {
            getPartnerResponsibles().remove(protocolFactory.getResponsibleToRemove());
        }
    }

    public void addUnit(ProtocolFactory protocolFactory) {
        if (protocolFactory.getInternalUnit()) {
            getUnits().add(protocolFactory.getUnitToAdd());
        } else {
            getPartners().add(protocolFactory.getUnitToAdd());
        }
    }

    public void removeUnit(ProtocolFactory protocolFactory) {
        if (protocolFactory.getInternalUnit()) {
            getUnits().remove(protocolFactory.getUnitToRemove());
        } else {
            getPartners().remove(protocolFactory.getUnitToRemove());
        }
    }

    public void addFile(ProtocolFactory protocolFactory) {
        writeFile(getFilePath(), protocolFactory.getInputStream(), protocolFactory.getFileName(),
                protocolFactory.getFilePermissionType());
    }

    public void deleteFile(ProtocolFactory protocolFactory) {
        for (ProtocolFile protocolFile : getProtocolFiles()) {
            if (protocolFile == protocolFactory.getFileToDelete()) {
                protocolFile.delete();
                break;
            }
        }
    }

    public ProtocolHistory getFirstProtocolHistory() {
        ProtocolHistory protocolHistoryToReturn = null;
        for (ProtocolHistory protocolHistory : getProtocolHistories()) {
            if (protocolHistory.getBeginDate() == null) {
                return protocolHistory;
            } else if (protocolHistoryToReturn == null
                    || protocolHistoryToReturn.getBeginDate().isAfter(protocolHistory.getBeginDate())) {
                protocolHistoryToReturn = protocolHistory;
            }
        }
        return protocolHistoryToReturn;
    }

    public ProtocolHistory getLastProtocolHistory() {
        ProtocolHistory protocolHistoryToReturn = null;
        for (ProtocolHistory protocolHistory : getProtocolHistories()) {
            if (protocolHistory.getEndDate() == null) {
                return protocolHistory;
            } else if (protocolHistoryToReturn == null
                    || protocolHistoryToReturn.getEndDate().isBefore(protocolHistory.getEndDate())) {
                protocolHistoryToReturn = protocolHistory;
            }
        }
        return protocolHistoryToReturn;
    }

    public List<ProtocolHistory> getOrderedProtocolHistories() {
        List<ProtocolHistory> protocolHistoryListToReturn = new ArrayList<ProtocolHistory>(
                getProtocolHistories());
        Collections.sort(protocolHistoryListToReturn, new BeanComparator("beginDate"));
        return protocolHistoryListToReturn;
    }

    public List<ProtocolHistory> getOrderedProtocolHistoriesMinusLast() {
        List<ProtocolHistory> protocolHistoryListToReturn = new ArrayList<ProtocolHistory>(
                getProtocolHistories());
        Collections.sort(protocolHistoryListToReturn, new BeanComparator("beginDate"));
        return getOrderedProtocolHistories().subList(0, protocolHistoryListToReturn.size() - 1);
    }

    public ProtocolHistory getActualProtocolHistory() {
        YearMonthDay today = new YearMonthDay();
        for (ProtocolHistory protocolHistory : getProtocolHistories()) {
            if (protocolHistory.getBeginDate() != null) {
                if (!today.isBefore(protocolHistory.getBeginDate())) {
                    if (protocolHistory.getEndDate() != null) {
                        if (!today.isAfter(protocolHistory.getEndDate())) {
                            return protocolHistory;
                        }
                    } else {
                        return protocolHistory;
                    }
                }
            }
        }
        return null;
    }

    public List<Person> getAllResponsibles() {
        List<Person> allResponsibles = new ArrayList<Person>();
        allResponsibles.addAll(getResponsibles());
        allResponsibles.addAll(getPartnerResponsibles());
        return allResponsibles;
    }

    public boolean isActive() {
        if (getActive()) {
            return true;
        } else {
            return areDatesActive();
        }
    }

    private boolean areDatesActive() {
        ProtocolHistory protocolHistory = getActualProtocolHistory();
        if (protocolHistory != null) {
            YearMonthDay today = new YearMonthDay();
            if (!today.isBefore(protocolHistory.getBeginDate())
                    && (protocolHistory.getEndDate() == null || !today.isAfter(protocolHistory
                            .getEndDate()))) {
                return true;
            }
        }
        return false;
    }

    public static void getExcelHeader(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle) {
        spreadsheet.newHeaderRow();
        spreadsheet.addHeader(bundle.getString("label.protocol.number"));
        spreadsheet.addHeader(bundle.getString("label.protocol.beginDate"));
        spreadsheet.addHeader(bundle.getString("label.protocol.endDate"));
        spreadsheet.addHeader(bundle.getString("label.protocol.signedDate"));
        spreadsheet.addHeader(bundle.getString("label.protocol.scientificAreas"), 9000);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources",
                LanguageUtils.getLocale());
        spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.STUDENTS_INTERCHANGE
                .toString()), spreadsheet.getExcelStyle().getVerticalHeaderStyle(), 500);
        spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.TEACHERS_INTERCHANGE
                .toString()), spreadsheet.getExcelStyle().getVerticalHeaderStyle(), 500);
        spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.INVESTIGATION_AND_DEVELOPMENT
                .toString()), spreadsheet.getExcelStyle().getVerticalHeaderStyle(), 500);
        spreadsheet.addHeader(
                resourceBundle.getString(ProtocolActionType.RENDERING_SERVICES.toString()), spreadsheet
                        .getExcelStyle().getVerticalHeaderStyle(), 500);
        spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.RENDERING_SERVICE_IST_TEACHER
                .toString()), spreadsheet.getExcelStyle().getVerticalHeaderStyle(), 500);
        spreadsheet.addHeader(resourceBundle
                .getString(ProtocolActionType.RENDERING_SERVICE_OTHER_INSTITUTION_TEACHER.toString()),
                spreadsheet.getExcelStyle().getVerticalHeaderStyle(), 500);
        spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.WORKSHOPS.toString()),
                spreadsheet.getExcelStyle().getVerticalHeaderStyle(), 500);
        spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.FORMATION_TRAINEE.toString()),
                spreadsheet.getExcelStyle().getVerticalHeaderStyle(), 500);
        spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.DOCUMENTATION.toString()),
                spreadsheet.getExcelStyle().getVerticalHeaderStyle(), 500);
        spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.TECHNICAL_COOPERATION
                .toString()), spreadsheet.getExcelStyle().getVerticalHeaderStyle(), 500);
        spreadsheet.addHeader(bundle.getString("label.protocol.otherActionTypes"), 10000);
        spreadsheet.addHeader(bundle.getString("label.protocol.observations"), 10000);
        spreadsheet.addHeader(bundle.getString("label.protocol.units"), 10000);
        spreadsheet.addHeader(bundle.getString("label.protocol.responsibles"), 10000);
        spreadsheet.addHeader(bundle.getString("label.protocol.externalUnits"), 10000);
        spreadsheet.addHeader(bundle.getString("label.protocol.externalResponsibles"), 10000);
    }

    public void getExcelRow(StyledExcelSpreadsheet spreadsheet) {
        spreadsheet.newRow();
        spreadsheet.addCell(getProtocolNumber());
        ProtocolHistory first = getFirstProtocolHistory();
        spreadsheet.addCell(first.getBeginDate() == null ? "" : first.getBeginDate().toString());
        ProtocolHistory last = getLastProtocolHistory();
        spreadsheet.addCell(last.getEndDate() == null ? "" : last.getEndDate().toString());
        spreadsheet.addCell(getSignedDate() == null ? "" : getSignedDate().toString());
        spreadsheet.addCell(getScientificAreas());
        spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.STUDENTS_INTERCHANGE) ? "X"
                : "", spreadsheet.getExcelStyle().getStringStyle());
        spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.TEACHERS_INTERCHANGE) ? "X"
                : "", spreadsheet.getExcelStyle().getStringStyle());
        spreadsheet.addCell(getProtocolAction().contains(
                ProtocolActionType.INVESTIGATION_AND_DEVELOPMENT) ? "X" : "", spreadsheet
                .getExcelStyle().getStringStyle());
        spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.RENDERING_SERVICES) ? "X"
                : "", spreadsheet.getExcelStyle().getStringStyle());
        spreadsheet.addCell(getProtocolAction().contains(
                ProtocolActionType.RENDERING_SERVICE_IST_TEACHER) ? "X" : "", spreadsheet
                .getExcelStyle().getStringStyle());
        spreadsheet.addCell(getProtocolAction().contains(
                ProtocolActionType.RENDERING_SERVICE_OTHER_INSTITUTION_TEACHER) ? "X" : "", spreadsheet
                .getExcelStyle().getStringStyle());
        spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.WORKSHOPS) ? "X" : "",
                spreadsheet.getExcelStyle().getStringStyle());
        spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.FORMATION_TRAINEE) ? "X"
                : "", spreadsheet.getExcelStyle().getStringStyle());
        spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.DOCUMENTATION) ? "X" : "",
                spreadsheet.getExcelStyle().getStringStyle());
        spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.TECHNICAL_COOPERATION) ? "X"
                : "", spreadsheet.getExcelStyle().getStringStyle());
        spreadsheet.addCell(getProtocolAction().getOtherTypes() == null ? "" : getProtocolAction()
                .getOtherTypes());
        spreadsheet.addCell(getObservations());
        spreadsheet.addCell(getCommaSeparatedPartyNames(getUnits()));
        spreadsheet.addCell(getCommaSeparatedPartyNames(getResponsibles()));
        spreadsheet.addCell(getCommaSeparatedPartyNames(getPartners()));
        spreadsheet.addCell(getCommaSeparatedPartyNames(getPartnerResponsibles()));
    }

    private String getCommaSeparatedPartyNames(List parties) {
        StringBuilder result = new StringBuilder();
        for (Party party : (List<Party>) parties) {
            if (result.length() != 0) {
                result.append(", ");
            }
            result.append(party.getName());
        }
        return result.toString();
    }
}