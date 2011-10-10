package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Comparator;
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

    static final public Comparator<JustificationMotive> COMPARATOR_AND_ACRONYM = new Comparator<JustificationMotive>() {
	public int compare(final JustificationMotive o1, final JustificationMotive o2) {
	    return o1.getAcronym().compareTo(o2.getAcronym());
	}
    };

    // construtors used in scripts
    public JustificationMotive(String acronym, String description, Boolean actualWorkTime, JustificationType justificationType,
	    DayType dayType, JustificationGroup justificationGroup, DateTime lastModifiedDate, Employee modifiedBy) {
	super();
	init(Boolean.TRUE, acronym, description, actualWorkTime, justificationType, dayType, justificationGroup, null, null,
		null, null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE,
		Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, lastModifiedDate, modifiedBy);
    }

    public JustificationMotive(String acronym, String description, DateTime lastModifiedDate, Employee modifiedBy) {
	super();
	init(Boolean.TRUE, acronym, description, false, null, null, null, null, null, null, null, Boolean.FALSE, Boolean.FALSE,
		Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
		lastModifiedDate, modifiedBy);
    }//

    private void init(Boolean active, String acronym, String description, Boolean actualWorkTime,
	    JustificationType justificationType, DayType dayType, JustificationGroup justificationGroup,
	    String giafCodeOtherStatus, String giafCodeContractedStatus, String giafCodeAdistStatus, String giafCodeIstIdStatus,
	    Boolean discountBonus, Boolean discountA17, Boolean discountA17Vacations, Boolean accumulate, Boolean inExercise,
	    Boolean isJustificationForUnjustifiedDays, Boolean isJustificationForArticle66Days, Boolean processingInCurrentMonth,
	    Boolean hasReferenceDate, DateTime lastModifiedDate, Employee modifiedBy) {
	setRootDomainObject(RootDomainObject.getInstance());
	setAcronym(acronym);
	setDescription(description);
	setJustificationType(justificationType);
	setDayType(dayType);
	setJustificationGroup(justificationGroup);
	setActualWorkTime(actualWorkTime);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
	setActive(active);
	setDiscountBonus(discountBonus);
	setDiscountA17(discountA17);
	setDiscountA17Vacations(discountA17Vacations);
	setGiafCodeContractedStatus(giafCodeContractedStatus);
	setGiafCodeOtherStatus(giafCodeOtherStatus);
	setGiafCodeAdistStatus(giafCodeAdistStatus);
	setGiafCodeIstIdStatus(giafCodeIstIdStatus);
	setAccumulate(accumulate);
	setInExercise(inExercise);
	setIsJustificationForUnjustifiedDays(isJustificationForUnjustifiedDays);
	setIsJustificationForArticle66Days(isJustificationForArticle66Days);
	setProcessingInCurrentMonth(processingInCurrentMonth);
	setHasReferenceDate(hasReferenceDate);
    }

    public JustificationMotive(String acronym, String description, Boolean actualWorkTime, JustificationType justificationType,
	    DayType dayType, JustificationGroup justificationGroup, String giafCodeOtherStatus, String giafCodeContractedStatus,
	    String giafCodeAdistStatus, String giafCodeIstIdStatus, Boolean discountBonus, Boolean discountA17,
	    Boolean discountA17Vacations, Boolean accumulate, Boolean inExercise, Boolean isJustificationForUnjustifiedDays,
	    Boolean isJustificationForArticle66Days, Boolean processingInCurrentMonth, Boolean hasReferenceDate,
	    Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym)) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	if (isJustificationForUnjustifiedDays) {
	    JustificationMotive unjustifiedJustificationMotive = JustificationMotive.getUnjustifiedJustificationMotive();
	    if (unjustifiedJustificationMotive != null && !unjustifiedJustificationMotive.equals(this)) {
		throw new DomainException("error.alreadyExistsJustificationForUnjustifiedDays",
			unjustifiedJustificationMotive.getAcronym());
	    }
	}
	if (isJustificationForArticle66Days) {
	    JustificationMotive a66JustificationMotive = JustificationMotive.getA66JustificationMotive();
	    if (a66JustificationMotive != null && !a66JustificationMotive.equals(this)) {
		throw new DomainException("error.alreadyExistsJustificationForArticle66Days", a66JustificationMotive.getAcronym());
	    }
	}
	if (justificationType != JustificationType.TIME) {
	    accumulate = Boolean.FALSE;
	}
	init(Boolean.TRUE, acronym, description, actualWorkTime, justificationType, dayType, justificationGroup,
		giafCodeOtherStatus, giafCodeContractedStatus, giafCodeAdistStatus, giafCodeIstIdStatus, discountBonus,
		discountA17, discountA17Vacations, accumulate, inExercise, isJustificationForUnjustifiedDays,
		isJustificationForArticle66Days, processingInCurrentMonth, hasReferenceDate, new DateTime(), modifiedBy);
    }

    // construtor used for regularizations
    public JustificationMotive(String acronym, String description, Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym)) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	init(Boolean.TRUE, acronym, description, false, null, null, null, null, null, null, null, Boolean.FALSE, Boolean.FALSE,
		Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
		new DateTime(), modifiedBy);
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
	    String giafCodeContractedStatus, String giafCodeAdistStatus, String giafCodeIstIdStatus, Boolean discountBonus,
	    Boolean discountA17, Boolean discountA17Vacations, Boolean isJustificationForUnjustifiedDays,
	    Boolean isJustificationForArticle66Days, Boolean processingInCurrentMonth, Boolean hasReferenceDate,
	    Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym, getIdInternal())) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	if (isJustificationForUnjustifiedDays) {
	    JustificationMotive unjustifiedJustificationMotive = JustificationMotive.getUnjustifiedJustificationMotive();
	    if (unjustifiedJustificationMotive != null && !unjustifiedJustificationMotive.equals(this)) {
		throw new DomainException("error.alreadyExistsJustificationForUnjustifiedDays",
			unjustifiedJustificationMotive.getAcronym());
	    }
	}
	if (isJustificationForArticle66Days) {
	    JustificationMotive a66JustificationMotive = JustificationMotive.getA66JustificationMotive();
	    if (a66JustificationMotive != null && !a66JustificationMotive.equals(this)) {
		throw new DomainException("error.alreadyExistsJustificationForArticle66Days", a66JustificationMotive.getAcronym());
	    }
	}
	init(active, acronym, description, getActualWorkTime(), getJustificationType(), getDayType(), getJustificationGroup(),
		giafCodeOtherStatus, giafCodeContractedStatus, giafCodeAdistStatus, giafCodeIstIdStatus, discountBonus,
		discountA17, discountA17Vacations, getAccumulate(), getInExercise(), isJustificationForUnjustifiedDays,
		isJustificationForArticle66Days, processingInCurrentMonth, hasReferenceDate, new DateTime(), modifiedBy);
    }

    public void editJustificationMotive(String acronym, String description, Boolean actualWorkTime,
	    JustificationType justificationType, DayType dayType, JustificationGroup justificationGroup, Boolean active,
	    String giafCodeOtherStatus, String giafCodeContractedStatus, String giafCodeAdistStatus, String giafCodeIstIdStatus,
	    Boolean discountBonus, Boolean discountA17, Boolean discountA17Vacations, Boolean accumulate, Boolean inExercise,
	    Boolean isJustificationForUnjustifiedDays, Boolean isJustificationForArticle66Days, Boolean processingInCurrentMonth,
	    Boolean hasReferenceDate, Employee modifiedBy) {
	if (alreadyExistsJustificationMotiveAcronym(acronym, getIdInternal())) {
	    throw new DomainException("error.acronymAlreadyExists");
	}
	if (isJustificationForUnjustifiedDays) {
	    JustificationMotive unjustifiedJustificationMotive = JustificationMotive.getUnjustifiedJustificationMotive();
	    if (unjustifiedJustificationMotive != null && !unjustifiedJustificationMotive.equals(this)) {
		throw new DomainException("error.alreadyExistsJustificationForUnjustifiedDays",
			unjustifiedJustificationMotive.getAcronym());
	    }
	}
	if (isJustificationForArticle66Days) {
	    JustificationMotive a66JustificationMotive = JustificationMotive.getA66JustificationMotive();
	    if (a66JustificationMotive != null && !a66JustificationMotive.equals(this)) {
		throw new DomainException("error.alreadyExistsJustificationForArticle66Days", a66JustificationMotive.getAcronym());
	    }
	}
	if (justificationType != JustificationType.TIME) {
	    accumulate = Boolean.FALSE;
	}

	init(active, acronym, description, actualWorkTime, justificationType, dayType, justificationGroup, giafCodeOtherStatus,
		giafCodeContractedStatus, giafCodeAdistStatus, giafCodeIstIdStatus, discountBonus, discountA17,
		discountA17Vacations, accumulate, inExercise, isJustificationForUnjustifiedDays, isJustificationForArticle66Days,
		processingInCurrentMonth, hasReferenceDate, new DateTime(), modifiedBy);
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

	if (assiduousnessStatusHistory.getAssiduousnessStatus().isIstIdEmployee()) {
	    if (getGiafCodeIstIdStatus() == null) {
		throw new InvalidGiafCodeException("errors.invalidGiafCodeException", getAcronym(), assiduousnessStatusHistory
			.getAssiduousness().getEmployee().getEmployeeNumber().toString());
	    }
	    return getGiafCodeIstIdStatus();
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
    public String getGiafCodeIstIdStatus() {
	return StringUtils.isEmpty(super.getGiafCodeIstIdStatus()) ? null : super.getGiafCodeIstIdStatus();
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

    public static JustificationMotive getUnjustifiedJustificationMotive() {
	for (JustificationMotive justificationMotive : RootDomainObject.getInstance().getJustificationMotives()) {
	    if (justificationMotive.getIsJustificationForUnjustifiedDays() && justificationMotive.getActive()) {
		return justificationMotive;
	    }
	}
	return null;
    }

    public static JustificationMotive getA66JustificationMotive() {
	for (JustificationMotive justificationMotive : RootDomainObject.getInstance().getJustificationMotives()) {
	    if (justificationMotive.getIsJustificationForArticle66Days() && justificationMotive.getActive()) {
		return justificationMotive;
	    }
	}
	return null;
    }

    public void activate(Employee modifiedBy) {
	setActive(Boolean.TRUE);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
    }
}