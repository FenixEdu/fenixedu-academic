package net.sourceforge.fenixedu.domain.protocols;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory;
import net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFactory.FilePermissionType;
import net.sourceforge.fenixedu.domain.ManagementGroups;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolAction;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolActionType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.YearMonthDay;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;
import pt.utl.ist.fenix.tools.util.Strings;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

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
	setProtocolAction(new ProtocolAction(protocolFactory.getActionTypes(), protocolFactory.getOtherActionTypes()));
	setObservations(protocolFactory.getObservations());
	setScientificAreas(protocolFactory.getScientificAreas());
	setResponsables(protocolFactory.getResponsibles());
	setResponsibleFunctions(protocolFactory.getResponsibleFunctions());
	setPartnerResponsibles(protocolFactory.getPartnerResponsibles());
	setUnits(protocolFactory.getUnits());
	setPartnerUnits(protocolFactory.getPartnerUnits());
	if (areDatesActive()) {
	    setActive(Boolean.TRUE);
	} else {
	    setActive(Boolean.FALSE);
	}
    }

    public Protocol(String protocolNumber, YearMonthDay signedDate, Boolean renewable, Boolean active, String scientificAreas,
	    String observations, ProtocolHistory protocolHistory, ProtocolAction protocolAction, Person responsible,
	    List<Unit> unitList, List<Unit> partnersList) {
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
	ProtocolHistory protocolHistory = new ProtocolHistory(protocolFactory.getBeginDate(), protocolFactory.getEndDate());
	getProtocolHistories().add(protocolHistory);
    }

    private void writeFile(VirtualPath filePath, InputStream fileInputStream, String fileName,
	    FilePermissionType filePermissionType) throws FileNotFoundException {
	final ProtocolFile protocolFile = new ProtocolFile(fileName, fileInputStream, getGroup(filePermissionType));
	getProtocolFiles().add(protocolFile);
    }

    protected VirtualPath getFilePath() {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode("ProtocolFiles", "Protocol Files"));
	filePath.addNode(new VirtualPathNode("Protocol" + getIdInternal(), getProtocolNumber()));
	return filePath;
    }

    public Group getGroup(FilePermissionType filePermissionType) {
	if (filePermissionType.equals(FilePermissionType.RESPONSIBLES_AND_SCIENTIFIC_COUNCIL)) {
	    Group unionGroup = null;
	    for (Person responsible : getResponsibles()) {
		PersonGroup personGroup = new PersonGroup(responsible);
		if (unionGroup == null) {
		    unionGroup = new GroupUnion(personGroup);
		} else {
		    unionGroup = new GroupUnion(unionGroup, personGroup);
		}
	    }

	    final RoleGroup roleGroup = new RoleGroup(Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL));
	    unionGroup = unionGroup != null ? new GroupUnion(unionGroup, roleGroup) : roleGroup;

	    ManagementGroups managementGroups = null;
	    if (!RootDomainObject.getInstance().getManagementGroups().isEmpty()) {
		managementGroups = RootDomainObject.getInstance().getManagementGroups().iterator().next();

		Group managersGroup = managementGroups.getProtocolManagers();
		if (managersGroup != null) {
		    unionGroup = new GroupUnion(unionGroup, managersGroup);
		}
	    }

	    return unionGroup;

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

    private void setResponsibleFunctions(List<Function> responsibleFunctions) {
	if (responsibleFunctions != null) {
	    getResponsibleFunctions().addAll(responsibleFunctions);
	}
    }

    private void setPartnerResponsibles(List<Person> partnerResponsibles) {
	if (partnerResponsibles != null) {
	    getPartnerResponsibles().addAll(partnerResponsibles);
	}
    }

    private void setUnits(List<Unit> units) {
	if (units != null) {
	    getUnits().addAll(units);
	}
    }

    private void setPartnerUnits(List<Unit> partnerUnits) {
	if (partnerUnits != null) {
	    getPartners().addAll(partnerUnits);
	}
    }

    public void editData(ProtocolFactory protocolFactory) {
	setProtocolNumber(protocolFactory.getProtocolNumber());
	setSignedDate(protocolFactory.getSignedDate());
	setProtocolAction(new ProtocolAction(protocolFactory.getActionTypes(), protocolFactory.getOtherActionTypes()));
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
	    if (protocolFactory.getIstResponsibleIsPerson()) {
		getResponsibles().add(protocolFactory.getResponsibleToAdd());
	    } else {
		getResponsibleFunctions().add(protocolFactory.getResponsibleFunction());
	    }
	} else {
	    getPartnerResponsibles().add(protocolFactory.getResponsibleToAdd());
	}
    }

    public void removeResponsible(ProtocolFactory protocolFactory) {
	if (protocolFactory.getIstResponsible()) {
	    if (protocolFactory.getResponsibleToRemove() != null) {
		getResponsibles().remove(protocolFactory.getResponsibleToRemove());
	    } else {
		getResponsibleFunctions().remove(protocolFactory.getResponsibleFunctionToRemove());
	    }
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

    public void addFile(InputStream fileInputStream, String fileName, FilePermissionType filePermissionType)
	    throws FileNotFoundException {
	writeFile(getFilePath(), fileInputStream, fileName, filePermissionType);
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
	List<ProtocolHistory> protocolHistoryListToReturn = new ArrayList<ProtocolHistory>(getProtocolHistories());
	Collections.sort(protocolHistoryListToReturn, new BeanComparator("beginDate"));
	return protocolHistoryListToReturn;
    }

    public List<ProtocolHistory> getOrderedProtocolHistoriesMinusLast() {
	List<ProtocolHistory> protocolHistoryListToReturn = new ArrayList<ProtocolHistory>(getProtocolHistories());
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

    public String getAllResponsibles() {
	StringBuilder result = new StringBuilder();
	for (Person person : getResponsibles()) {
	    if (result.length() != 0) {
		result.append(", ");
	    }
	    result.append(person.getName());
	}
	for (Function funtion : getResponsibleFunctions()) {
	    if (result.length() != 0) {
		result.append(", ");
	    }
	    result.append(funtion.getName());
	}
	for (Person person : getPartnerResponsibles()) {
	    if (result.length() != 0) {
		result.append(", ");
	    }
	    result.append(person.getName());
	}
	return result.toString();
    }

    public boolean isActive() {
	if (!getActive()) {
	    return false;
	} else {
	    return areDatesActive();
	}
    }

    private boolean areDatesActive() {
	YearMonthDay today = new YearMonthDay();
	for (ProtocolHistory protocolHistory : getProtocolHistories()) {
	    if (protocolHistory.getBeginDate() != null) {
		if (!today.isBefore(protocolHistory.getBeginDate())) {
		    return protocolHistory.getEndDate() != null ? !today.isAfter(protocolHistory.getEndDate()) : true;
		}
	    } else if (protocolHistory.getEndDate() != null) {
		if (!today.isAfter(protocolHistory.getEndDate())) {
		    return true;
		}
	    } else {
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
	ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.STUDENTS_INTERCHANGE.toString()), spreadsheet
		.getExcelStyle().getVerticalHeaderStyle(), 500);
	spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.TEACHERS_INTERCHANGE.toString()), spreadsheet
		.getExcelStyle().getVerticalHeaderStyle(), 500);
	spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.INVESTIGATION_AND_DEVELOPMENT.toString()), spreadsheet
		.getExcelStyle().getVerticalHeaderStyle(), 500);
	spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.RENDERING_SERVICES.toString()), spreadsheet
		.getExcelStyle().getVerticalHeaderStyle(), 500);
	spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.RENDERING_SERVICE_IST_TEACHER.toString()), spreadsheet
		.getExcelStyle().getVerticalHeaderStyle(), 500);
	spreadsheet.addHeader(
		resourceBundle.getString(ProtocolActionType.RENDERING_SERVICE_OTHER_INSTITUTION_TEACHER.toString()), spreadsheet
			.getExcelStyle().getVerticalHeaderStyle(), 500);
	spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.WORKSHOPS.toString()), spreadsheet.getExcelStyle()
		.getVerticalHeaderStyle(), 500);
	spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.FORMATION_TRAINEE.toString()), spreadsheet
		.getExcelStyle().getVerticalHeaderStyle(), 500);
	spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.DOCUMENTATION.toString()), spreadsheet.getExcelStyle()
		.getVerticalHeaderStyle(), 500);
	spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.TECHNICAL_COOPERATION.toString()), spreadsheet
		.getExcelStyle().getVerticalHeaderStyle(), 500);
	spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.POST_GRADUATION.toString()), spreadsheet
		.getExcelStyle().getVerticalHeaderStyle(), 500);
	spreadsheet.addHeader(resourceBundle.getString(ProtocolActionType.DOUBLE_DEGREE.toString()), spreadsheet.getExcelStyle()
		.getVerticalHeaderStyle(), 500);
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
	spreadsheet.addCell(getScientificAreas() == null ? "" : getScientificAreas());
	spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.STUDENTS_INTERCHANGE) ? "X" : "", spreadsheet
		.getExcelStyle().getStringStyle());
	spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.TEACHERS_INTERCHANGE) ? "X" : "", spreadsheet
		.getExcelStyle().getStringStyle());
	spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.INVESTIGATION_AND_DEVELOPMENT) ? "X" : "",
		spreadsheet.getExcelStyle().getStringStyle());
	spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.RENDERING_SERVICES) ? "X" : "", spreadsheet
		.getExcelStyle().getStringStyle());
	spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.RENDERING_SERVICE_IST_TEACHER) ? "X" : "",
		spreadsheet.getExcelStyle().getStringStyle());
	spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.RENDERING_SERVICE_OTHER_INSTITUTION_TEACHER) ? "X"
		: "", spreadsheet.getExcelStyle().getStringStyle());
	spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.WORKSHOPS) ? "X" : "", spreadsheet.getExcelStyle()
		.getStringStyle());
	spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.FORMATION_TRAINEE) ? "X" : "", spreadsheet
		.getExcelStyle().getStringStyle());
	spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.DOCUMENTATION) ? "X" : "", spreadsheet
		.getExcelStyle().getStringStyle());
	spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.TECHNICAL_COOPERATION) ? "X" : "", spreadsheet
		.getExcelStyle().getStringStyle());
	spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.POST_GRADUATION) ? "X" : "", spreadsheet
		.getExcelStyle().getStringStyle());
	spreadsheet.addCell(getProtocolAction().contains(ProtocolActionType.DOUBLE_DEGREE) ? "X" : "", spreadsheet
		.getExcelStyle().getStringStyle());
	spreadsheet.addCell(getProtocolAction().getOtherTypes(), true);
	spreadsheet.addCell(getObservations(), true);
	spreadsheet.addCell(getCommaSeparatedPartyNames(getUnits()));
	spreadsheet.addCell(getCommaSeparatedResponsiblesNames());
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

    private String getCommaSeparatedResponsiblesNames() {
	StringBuilder result = new StringBuilder();
	for (Person person : getResponsibles()) {
	    if (result.length() != 0) {
		result.append(", ");
	    }
	    result.append(person.getName());
	}
	for (Function funtion : getResponsibleFunctions()) {
	    if (result.length() != 0) {
		result.append(", ");
	    }
	    result.append(funtion.getName());
	}
	return result.toString();
    }

    public void delete() {
	for (Person partnerResponsible : new ArrayList<Person>(getPartnerResponsibles())) {
	    removePartnerResponsibles(partnerResponsible);
	}
	for (Unit partner : new ArrayList<Unit>(getPartners())) {
	    removePartners(partner);
	}
	for (; !getProtocolFiles().isEmpty(); getProtocolFiles().get(0).delete())
	    ;
	for (; !getProtocolHistories().isEmpty(); getProtocolHistories().get(0).delete())
	    ;
	for (Function responsibleFunction : new ArrayList<Function>(getResponsibleFunctions())) {
	    removeResponsibleFunctions(responsibleFunction);
	}
	for (Person responsible : new ArrayList<Person>(getResponsibles())) {
	    removeResponsibles(responsible);
	}
	for (Unit unit : new ArrayList<Unit>(getUnits())) {
	    removeUnits(unit);
	}
	removeRootDomainObject();
	deleteDomainObject();
    }

    public String getProtocolActionString() {
	return getProtocolAction().toString();
    }

    @SuppressWarnings("unchecked")
    public String generateResponsiblesJSON() {
	try {
	    Unit ISTUnit = RootDomainObject.getInstance().getInstitutionUnit();

	    JSONArray jsonArray = new JSONArray();

	    boolean hasISTUnit = getUnits().size() == 0;

	    for (Unit unit : getUnits()) {
		if (unit.equals(ISTUnit) || unit.getOID() == 107374258258l) {
		    if (unit.getOID() == 107374258258l)
			System.out.println("WARNING: Adding IST unit in place of UTL!");
		    hasISTUnit = true;
		    break;
		}
	    }

	    if (hasISTUnit)
		jsonArray.add(getISTUnit());

	    for (Unit unit : getUnits()) {

		if (unit.equals(ISTUnit) || unit.getOID() == 107374258258l)
		    continue;

		JSONObject object = new JSONObject();

		object.put("type", "INTERNAL");

		Integer costCenterCode = unit.getCostCenterCode();

		if (costCenterCode == null)
		    continue;

		object.put("costCenter", costCenterCode);

		if (hasISTUnit) {

		    object.put("functions", "");

		    object.put("people", new JSONArray());

		} else {

		    List<String> functions = new ArrayList<String>();

		    for (Function function : getResponsibleFunctions()) {
			functions.add(function.getName());
		    }

		    object.put("functions", new Strings(functions).exportAsString());

		    JSONArray istPeople = new JSONArray();

		    for (Person person : getResponsibles()) {

			// String userName = person.getUsername();

			// if (userName.startsWith("INA"))
			// continue;

			istPeople.add(person.getUsername());
		    }

		    object.put("people", istPeople);

		}

		jsonArray.add(object);

	    }

	    for (Unit unit : getPartners()) {

		JSONObject object = new JSONObject();

		object.put("type", "EXTERNAL");

		object.put("unitName", unit.getPartyName().exportAsString());
		object.put("acronym", unit.getAcronym());
		object.put("unitCountry", unit.getCountry() == null ? "" : unit.getCountry().getThreeLetterCode());

		Collection<Person> people = locatePartnerResponsiblesFromUnit(getPartnerResponsibles(), unit);

		JSONArray peopleArray = new JSONArray();

		for (Person person : people) {
		    peopleArray.add(person.getPartyName().exportAsString());
		}

		object.put("people", peopleArray);

		jsonArray.add(object);

	    }

	    String json = jsonArray.toJSONString();

	    // System.out.println("Sending json string for protocol " +
	    // getProtocolNumber() + ": \n" + json);

	    return json;
	} catch (Exception e) {
	    e.printStackTrace();
	    throw e;
	}
    }

    @SuppressWarnings("unchecked")
    private JSONObject getISTUnit() {
	JSONObject obj = new JSONObject();

	obj.put("type", "INTERNAL");

	obj.put("costCenter", Integer.valueOf(-1));

	List<String> functions = new ArrayList<String>();

	for (Function function : getResponsibleFunctions()) {
	    functions.add(function.getName());
	}

	obj.put("functions", new Strings(functions).exportAsString());

	JSONArray istPeople = new JSONArray();

	for (Person person : getResponsibles()) {
	    if (!person.getUsername().startsWith("INA"))
		istPeople.add(person.getUsername());
	}

	obj.put("people", istPeople);

	return obj;
    }

    @SuppressWarnings("unchecked")
    public String getFilesJSON() {
	JSONArray files = new JSONArray();

	for (ProtocolFile file : getProtocolFiles()) {
	    try {
		JSONObject obj = new JSONObject();

		obj.put("isPublic", file.getPermittedGroup() instanceof InternalPersonGroup);
		obj.put("fileName", file.getDisplayName());
		obj.put("contents", new String(Base64.encodeBase64(file.getContents())));

		files.add(obj);
	    } catch (Exception e) {
		System.out.println("Could not fetch file: " + file.getDisplayName());
	    }
	}

	String json = files.toJSONString();

	return json;
    }

    private Collection<Person> locatePartnerResponsiblesFromUnit(Collection<Person> people, final Unit unit) {
	return Collections2.filter(people, new Predicate<Person>() {

	    @Override
	    public boolean apply(Person person) {
		return unit.equals(person.getExternalContract().getInstitutionUnit());
	    }
	});
    }
}
