package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Tania Pousao Created on 30/Out/2003
 */
public class DegreeInfo extends DegreeInfo_Base {

    public static Comparator<DegreeInfo> COMPARATOR_BY_EXECUTION_YEAR = new Comparator<DegreeInfo>() {
        @Override
        public int compare(DegreeInfo info1, DegreeInfo info2) {
            int result = ExecutionYear.COMPARATOR_BY_YEAR.compare(info1.getExecutionYear(), info2.getExecutionYear());
            if (result != 0) {
                return result;
            }
            return DomainObjectUtil.COMPARATOR_BY_ID.compare(info1, info2);
        }
    };

    protected DegreeInfo(Degree degree, ExecutionYear executionYear) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());

        DegreeInfo degreeInfo = degree.getMostRecentDegreeInfo(executionYear);

        if (degreeInfo != null && degreeInfo.getExecutionYear() == executionYear) {
            throw new DomainException(
                    "error.net.sourceforge.fenixdu.domain.cannot.create.degreeInfo.already.exists.one.for.that.degree.and.executionYear");
        }

        super.setExecutionYear(executionYear);
        super.setName(degree.getNameFor(executionYear));
        super.setDegree(degree);

        new DegreeInfoCandidacy(this);
        new DegreeInfoFuture(this);
    }

    @Override
    public void setName(MultiLanguageString name) {
        if (hasSameName(name)) {
            return;
        }

        if (hasName() && !canEdit()) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.DegreeInfo.can.only.change.name.for.future.execution.years");
        }
        super.setName(name);
    }

    private boolean hasName() {
        return getName() != null && !getName().isEmpty();
    }

    private boolean hasSameName(final MultiLanguageString name) {
        return hasName() && getName().equals(name);
    }

    public boolean canEdit() {
        final DegreeCurricularPlan firstDegreeCurricularPlan = getDegree().getFirstDegreeCurricularPlan();
        final DegreeCurricularPlan lastActiveDegreeCurricularPlan = getDegree().getLastActiveDegreeCurricularPlan();
        if (firstDegreeCurricularPlan == null) {
            return true;
        }
        ExecutionYear firstExecutionYear =
                ExecutionYear.readByDateTime(firstDegreeCurricularPlan.getInitialDateYearMonthDay().toDateTimeAtMidnight());
        if (getExecutionYear().isBefore(firstExecutionYear)) {
            return true;
        }
        if (lastActiveDegreeCurricularPlan == null) {
            return true;
        }
        if (!lastActiveDegreeCurricularPlan.hasAnyExecutionDegrees()) {
            return true;
        }
        if (getExecutionYear().isAfter(ExecutionYear.readCurrentExecutionYear())) {
            return true;
        }
        if (getExecutionYear().isCurrent()) {
            return true;
        }
        return false;
    }

    protected DegreeInfo(DegreeInfo degreeInfo, ExecutionYear executionYear) {
        this(degreeInfo.getDegree(), executionYear);

        setName(degreeInfo.getName());
        setDescription(degreeInfo.getDescription());
        setHistory(degreeInfo.getHistory());
        setObjectives(degreeInfo.getObjectives());
        setDesignedFor(degreeInfo.getDesignedFor());
        setProfessionalExits(degreeInfo.getProfessionalExits());
        setOperationalRegime(degreeInfo.getOperationalRegime());
        setGratuity(degreeInfo.getGratuity());
        setAdditionalInfo(degreeInfo.getAdditionalInfo());
        setLinks(degreeInfo.getLinks());

        setTestIngression(degreeInfo.getTestIngression());
        setClassifications(degreeInfo.getClassifications());
        setAccessRequisites(degreeInfo.getAccessRequisites());
        setCandidacyDocuments(degreeInfo.getCandidacyDocuments());
        setDriftsInitial(degreeInfo.getDriftsInitial());
        setDriftsFirst(degreeInfo.getDriftsFirst());
        setDriftsSecond(degreeInfo.getDriftsSecond());
        setMarkMin(degreeInfo.getMarkMin());
        setMarkMax(degreeInfo.getMarkMax());
        setMarkAverage(degreeInfo.getMarkAverage());

        setQualificationLevel(degreeInfo.getQualificationLevel());
        setRecognitions(degreeInfo.getRecognitions());
    }

    public void delete() {
        getDegreeInfoCandidacy().delete();
        getDegreeInfoFuture().delete();

        setRootDomainObject(null);
        setDegree(null);
        setExecutionYear(null);

        deleteDomainObject();
    }

    public MultiLanguageString getAccessRequisites() {
        return getDegreeInfoCandidacy().getAccessRequisites();
    }

    public MultiLanguageString getCandidacyDocuments() {
        return getDegreeInfoCandidacy().getCandidacyDocuments();
    }

    public MultiLanguageString getCandidacyPeriod() {
        return getDegreeInfoCandidacy().getCandidacyPeriod();
    }

    public MultiLanguageString getClassifications() {
        return getDegreeInfoFuture().getClassifications();
    }

    public MultiLanguageString getDesignedFor() {
        return getDegreeInfoFuture().getDesignedFor();
    }

    public String getDesignedFor(final Language language) {
        return hasDesignedFor(language) ? getDesignedFor().getContent(language) : "";
    }

    public boolean hasDesignedFor(final Language language) {
        return getDesignedFor() != null && getDesignedFor().hasContent(language);
    }

    public MultiLanguageString getEnrolmentPeriod() {
        return getDegreeInfoCandidacy().getEnrolmentPeriod();
    }

    public MultiLanguageString getObjectives() {
        return getDegreeInfoFuture().getObjectives();
    }

    public boolean hasObjectives(final Language language) {
        return getObjectives() != null && getObjectives().hasContent(language);
    }

    public String getObjectives(final Language language) {
        return hasObjectives(language) ? getObjectives().getContent(language) : "";
    }

    public MultiLanguageString getProfessionalExits() {
        return getDegreeInfoFuture().getProfessionalExits();
    }

    public boolean hasProfessionalExits(final Language language) {
        return getProfessionalExits() != null && getProfessionalExits().hasContent(language);
    }

    public String getProfessionalExits(final Language language) {
        return hasProfessionalExits(language) ? getProfessionalExits().getContent(language) : "";
    }

    public MultiLanguageString getQualificationLevel() {
        return getDegreeInfoFuture().getQualificationLevel();
    }

    public MultiLanguageString getRecognitions() {
        return getDegreeInfoFuture().getRecognitions();
    }

    public MultiLanguageString getSelectionResultDeadline() {
        return getDegreeInfoCandidacy().getSelectionResultDeadline();
    }

    public MultiLanguageString getTestIngression() {
        return getDegreeInfoCandidacy().getTestIngression();
    }

    public void setAccessRequisites(MultiLanguageString accessRequisites) {
        getDegreeInfoCandidacy().setAccessRequisites(accessRequisites);
    }

    public void setCandidacyDocuments(MultiLanguageString candidacyDocuments) {
        getDegreeInfoCandidacy().setCandidacyDocuments(candidacyDocuments);
    }

    public void setCandidacyPeriod(MultiLanguageString candidacyPeriod) {
        getDegreeInfoCandidacy().setCandidacyPeriod(candidacyPeriod);
    }

    public void setClassifications(MultiLanguageString classifications) {
        getDegreeInfoFuture().setClassifications(classifications);
    }

    public void setDesignedFor(MultiLanguageString designedFor) {
        getDegreeInfoFuture().setDesignedFor(designedFor);
    }

    public void setEnrolmentPeriod(MultiLanguageString enrolmentPeriod) {
        getDegreeInfoCandidacy().setEnrolmentPeriod(enrolmentPeriod);
    }

    public void setObjectives(MultiLanguageString objectives) {
        getDegreeInfoFuture().setObjectives(objectives);
    }

    public void setProfessionalExits(MultiLanguageString professionalExits) {
        getDegreeInfoFuture().setProfessionalExits(professionalExits);
    }

    public void setQualificationLevel(MultiLanguageString qualificationLevel) {
        getDegreeInfoFuture().setQualificationLevel(qualificationLevel);
    }

    public void setRecognitions(MultiLanguageString recognitions) {
        getDegreeInfoFuture().setRecognitions(recognitions);
    }

    public void setSelectionResultDeadline(MultiLanguageString selectionResultDeadline) {
        getDegreeInfoCandidacy().setSelectionResultDeadline(selectionResultDeadline);
    }

    public void setTestIngression(MultiLanguageString testIngression) {
        getDegreeInfoCandidacy().setTestIngression(testIngression);
    }

    public boolean hasOperationalRegime(final Language language) {
        return getOperationalRegime() != null && getOperationalRegime().hasContent(language);
    }

    public String getOperationalRegime(final Language language) {
        return hasOperationalRegime(language) ? getOperationalRegime().getContent(language) : "";
    }

    public boolean hasAdditionalInfo(final Language language) {
        return getAdditionalInfo() != null && getAdditionalInfo().hasContent(language);
    }

    public String getAdditionalInfo(final Language language) {
        return hasAdditionalInfo(language) ? getAdditionalInfo().getContent(language) : "";
    }

    public AcademicInterval getAcademicInterval() {
        return getExecutionYear().getAcademicInterval();
    }

    @SuppressWarnings("unchecked")
    public static String readAllDegreeInfos() {
        JSONArray infos = new JSONArray();
        for (Degree degree : RootDomainObject.getInstance().getDegrees()) {
            List<DegreeInfo> degreeInfos = degree.getDegreeInfos();
            if (!degreeInfos.isEmpty()) {
                for (DegreeInfo degreeInfo : degreeInfos) {
                    JSONObject obj = new JSONObject();
                    String degreeType = JSONObject.escape(degreeInfo.getDegree().getDegreeType().toString());
                    String degreeName = JSONObject.escape(degreeInfo.getName().getContent());
                    String degreeOid = Long.toString(degreeInfo.getDegree().getOID());
                    System.out.printf("with info : %s %s %s\n", degreeOid, degreeType, degreeName);
                    obj.put("degreeOid", degreeOid);
                    obj.put("degreeType", degreeType);
                    obj.put("degreeName", degreeName);

                    infos.add(obj);
                }
            } else {
                JSONObject obj = new JSONObject();
                String degreeOid = Long.toString(degree.getOID());
                String degreeType = JSONObject.escape(degree.getDegreeType().toString());
                String degreeName = JSONObject.escape(degree.getNome());
                System.out.printf("no info : %s %s %s\n", degreeOid, degreeType, degreeName);
                obj.put("degreeOid", degreeOid);
                obj.put("degreeType", degreeType);
                obj.put("degreeName", degreeName);
                infos.add(obj);
            }

        }
        return infos.toJSONString();
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDegreeInfoCandidacy() {
        return getDegreeInfoCandidacy() != null;
    }

    @Deprecated
    public boolean hasGratuity() {
        return getGratuity() != null;
    }

    @Deprecated
    public boolean hasDriftsSecond() {
        return getDriftsSecond() != null;
    }

    @Deprecated
    public boolean hasAdditionalInfo() {
        return getAdditionalInfo() != null;
    }

    @Deprecated
    public boolean hasMarkMin() {
        return getMarkMin() != null;
    }

    @Deprecated
    public boolean hasOperationalRegime() {
        return getOperationalRegime() != null;
    }

    @Deprecated
    public boolean hasDegreeInfoFuture() {
        return getDegreeInfoFuture() != null;
    }

    @Deprecated
    public boolean hasSchoolCalendar() {
        return getSchoolCalendar() != null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasLinks() {
        return getLinks() != null;
    }

    @Deprecated
    public boolean hasMarkMax() {
        return getMarkMax() != null;
    }

    @Deprecated
    public boolean hasHistory() {
        return getHistory() != null;
    }

    @Deprecated
    public boolean hasMarkAverage() {
        return getMarkAverage() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasDriftsFirst() {
        return getDriftsFirst() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

    @Deprecated
    public boolean hasDriftsInitial() {
        return getDriftsInitial() != null;
    }

}
