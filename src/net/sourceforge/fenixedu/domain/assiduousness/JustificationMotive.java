package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.InvalidGiafCodeException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class JustificationMotive extends JustificationMotive_Base {

    // construtors used in scripts
    public JustificationMotive(String acronym, String description, Boolean actualWorkTime, JustificationType justificationType,
	    DayType dayType, JustificationGroup justificationGroup, DateTime lastModifiedDate, Employee modifiedBy) {
	super();
	init(acronym, description, actualWorkTime, justificationType, dayType, justificationGroup, null, null, null,
		Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, lastModifiedDate, modifiedBy);
    }

    public JustificationMotive(String acronym, String description, DateTime lastModifiedDate, Employee modifiedBy) {
	super();
	init(acronym, description, false, null, null, null, null, null, null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
		Boolean.TRUE, lastModifiedDate, modifiedBy);
    }//

    private void init(String acronym, String description, Boolean actualWorkTime, JustificationType justificationType,
	    DayType dayType, JustificationGroup justificationGroup, String giafCodeOtherStatus, String giafCodeContractedStatus,
	    String giafCodeAdistStatus, Boolean discountBonus, Boolean discountA17Vacations, Boolean accumulate,
	    Boolean inExercise, DateTime lastModifiedDate, Employee modifiedBy) {
	setRootDomainObject(RootDomainObject.getInstance());
	setAcronym(acronym);
	setDescription(description);
	setJustificationType(justificationType);
	setDayType(dayType);
	setJustificationGroup(justificationGroup);
	setActualWorkTime(actualWorkTime);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
	setActive(Boolean.TRUE);
	setDiscountBonus(discountBonus);
	setDiscountA17Vacations(discountA17Vacations);
	setGiafCodeContractedStatus(giafCodeContractedStatus);
	setGiafCodeOtherStatus(giafCodeOtherStatus);
	setGiafCodeAdistStatus(giafCodeAdistStatus);
	setAccumulate(accumulate);
	setInExercise(inExercise);
    }

    public JustificationMotive(String acronym, String description, Boolean actualWorkTime, JustificationType justificationType,
	    DayType dayType, JustificationGroup justificationGroup, String giafCodeOtherStatus, String giafCodeContractedStatus,
	    String giafCodeAdistStatus, Boolean discountBonus, Boolean discountA17Vacations, Boolean accumulate,
	    Boolean inExercise, Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym)) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	if (justificationType != JustificationType.TIME) {
	    accumulate = Boolean.FALSE;
	}
	init(acronym, description, actualWorkTime, justificationType, dayType, justificationGroup, giafCodeOtherStatus,
		giafCodeContractedStatus, giafCodeAdistStatus, discountBonus, discountA17Vacations, accumulate, inExercise,
		new DateTime(), modifiedBy);
    }

    // construtor used for regularizations
    public JustificationMotive(String acronym, String description, Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym)) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	init(acronym, description, false, null, null, null, null, null, null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
		Boolean.FALSE, new DateTime(), modifiedBy);
    }// 

    private boolean alreadyExistsJustificationMotiveAcronym(String acronym) {
	return alreadyExistsJustificationMotiveAcronym(acronym, null);
    }

    private boolean alreadyExistsJustificationMotiveAcronym(String acronym, Integer id) {
	for (JustificationMotive justificationMotive : RootDomainObject.getInstance().getJustificationMotives()) {
	    if (justificationMotive.getAcronym().equalsIgnoreCase(acronym) && (id == null || !getIdInternal().equals(id))) {
		return true;
	    }
	}
	return false;
    }

    public void editJustificationMotive(String acronym, String description, Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym, getIdInternal())) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	setAcronym(acronym);
	setDescription(description);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
    }

    public void editJustificationMotive(String acronym, String description, Boolean active, String giafCodeOtherStatus,
	    String giafCodeContractedStatus, String giafCodeAdistStatus, Boolean discountBonus, Boolean discountA17Vacations,
	    Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym, getIdInternal())) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	setAcronym(acronym);
	setDescription(description);
	setActive(active);
	setGiafCodeContractedStatus(giafCodeContractedStatus);
	setGiafCodeOtherStatus(giafCodeOtherStatus);
	setGiafCodeAdistStatus(giafCodeAdistStatus);
	setDiscountBonus(discountBonus);
	setDiscountA17Vacations(discountA17Vacations);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);

    }

    public void editJustificationMotive(String acronym, String description, Boolean actualWorkTime,
	    JustificationType justificationType, DayType dayType, JustificationGroup justificationGroup, Boolean active,
	    String giafCodeOtherStatus, String giafCodeContractedStatus, String giafCodeAdistStatus, Boolean discountBonus,
	    Boolean discountA17Vacations, Boolean accumulate, Boolean inExercise, Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym, getIdInternal())) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	setAcronym(acronym);
	setDescription(description);
	setJustificationType(justificationType);
	setDayType(dayType);
	setJustificationGroup(justificationGroup);
	setActualWorkTime(actualWorkTime);
	setActive(active);
	setGiafCodeContractedStatus(giafCodeContractedStatus);
	setGiafCodeOtherStatus(giafCodeOtherStatus);
	setGiafCodeAdistStatus(giafCodeAdistStatus);
	setDiscountBonus(discountBonus);
	setDiscountA17Vacations(discountA17Vacations);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
	setInExercise(inExercise);
	if (justificationType != JustificationType.TIME) {
	    accumulate = Boolean.FALSE;
	}
	setAccumulate(accumulate);
    }

    public boolean getIsUsed() {
	LocalDate lastDay = ClosedMonth.getLastClosedLocalDate();
	for (Justification justification : getJustifications()) {
	    if (!justification.getDate().toLocalDate().isAfter(lastDay)) {
		return true;
	    }
	}
	return false;
    }

    public String getGiafCode(AssiduousnessStatusHistory assiduousnessStatusHistory) {
	if (assiduousnessStatusHistory.getAssiduousnessStatus().isContractedEmployee()) {
	    if (getGiafCodeContractedStatus() == null) {
		throw new InvalidGiafCodeException("errors.invalidGiafCodeException", getAcronym(), assiduousnessStatusHistory
			.getAssiduousness().getEmployee().getEmployeeNumber().toString());
	    }
	    return getGiafCodeContractedStatus();
	}
	if (assiduousnessStatusHistory.getAssiduousnessStatus().isADISTEmployee()) {
	    if (getGiafCodeAdistStatus() == null) {
		throw new InvalidGiafCodeException("errors.invalidGiafCodeException", getAcronym(), assiduousnessStatusHistory
			.getAssiduousness().getEmployee().getEmployeeNumber().toString());
	    }
	    return getGiafCodeAdistStatus();
	}
	if (getGiafCodeOtherStatus() == null) {
	    throw new InvalidGiafCodeException("errors.invalidGiafCodeException", getAcronym(), assiduousnessStatusHistory
		    .getAssiduousness().getEmployee().getEmployeeNumber().toString());
	}
	return getGiafCodeOtherStatus();
    }

    @Override
    public String getGiafCodeContractedStatus() {
	if (StringUtils.isEmpty(super.getGiafCodeContractedStatus())) {
	    return null;
	}
	return super.getGiafCodeContractedStatus();
    }

    @Override
    public void setGiafCodeContractedStatus(String giafCodeContractedStatus) {
	if (StringUtils.isEmpty(giafCodeContractedStatus)) {
	    super.setGiafCodeContractedStatus(null);
	}
	super.setGiafCodeContractedStatus(giafCodeContractedStatus);
    }

    @Override
    public String getGiafCodeAdistStatus() {
	if (StringUtils.isEmpty(super.getGiafCodeAdistStatus())) {
	    return null;
	}
	return super.getGiafCodeAdistStatus();
    }

    @Override
    public void setGiafCodeAdistStatus(String giafCodeAdistStatus) {
	if (StringUtils.isEmpty(giafCodeAdistStatus)) {
	    super.setGiafCodeAdistStatus(null);
	}
	super.setGiafCodeAdistStatus(giafCodeAdistStatus);
    }

    @Override
    public String getGiafCodeOtherStatus() {
	if (StringUtils.isEmpty(super.getGiafCodeOtherStatus())) {
	    return null;
	}
	return super.getGiafCodeOtherStatus();
    }

    @Override
    public void setGiafCodeOtherStatus(String giafCodeOtherStatus) {
	if (StringUtils.isEmpty(giafCodeOtherStatus)) {
	    super.setGiafCodeAdistStatus(null);
	}
	super.setGiafCodeOtherStatus(giafCodeOtherStatus);
    }

    public static JustificationMotive getJustificationMotiveByGiafCode(String code,
	    AssiduousnessStatusHistory assiduousnessStatusHistory) {
	for (JustificationMotive justificationMotive : RootDomainObject.getInstance().getJustificationMotives()) {
	    try {
		if (justificationMotive.getGiafCode(assiduousnessStatusHistory).equals(code)
			&& justificationMotive.getAccumulate()) {
		    return justificationMotive;
		}
	    } catch (InvalidGiafCodeException e) {

	    }
	}
	return null;
    }

    public static List<JustificationMotive> getJustificationMotivesByGroup(JustificationGroup justificationGroup) {
	List<JustificationMotive> result = new ArrayList<JustificationMotive>();
	for (JustificationMotive justificationMotive : RootDomainObject.getInstance().getJustificationMotives()) {
	    if (justificationMotive.getJustificationGroup() != null
		    && justificationMotive.getJustificationGroup().equals(justificationGroup)) {
		result.add(justificationMotive);
	    }
	}
	return result;
    }

}