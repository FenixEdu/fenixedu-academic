package net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;

import org.joda.time.DateTime;

public class OutboundMobilityContextBean implements Serializable {

    private ExecutionYear executionYear;

    private SortedSet<OutboundMobilityCandidacyPeriod> candidacyPeriods = new TreeSet<OutboundMobilityCandidacyPeriod>();

    private DateTime startDateTime;
    private DateTime endDateTime;

    private MobilityProgram mobilityProgram;
    private ExecutionDegree executionDegree;
    private UniversityUnit unit;
    private String code;
    private Integer vacancies;

    public OutboundMobilityContextBean() {
        setExecutionYear(ExecutionYear.readCurrentExecutionYear());

        OutboundMobilityCandidacyPeriod last = null;
        for (final CandidacyPeriod candidacyPeriod : executionYear.getCandidacyPeriodsSet()) {
            if (candidacyPeriod instanceof OutboundMobilityCandidacyPeriod) {
                final OutboundMobilityCandidacyPeriod outboundMobilityCandidacyPeriod =
                        (OutboundMobilityCandidacyPeriod) candidacyPeriod;
                if (last == null || last.getStart().isBefore(outboundMobilityCandidacyPeriod.getStart())) {
                    last = outboundMobilityCandidacyPeriod;
                }
            }
        }
        if (last != null) {
            candidacyPeriods.add(last);
        }
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(final ExecutionYear executionYear) {
        if (executionYear != this.executionYear) {
            this.executionYear = executionYear;
            candidacyPeriods.clear();
            getPossibleCandidacyPeriods(candidacyPeriods);
        }
    }

    public void getPossibleCandidacyPeriods(final SortedSet<OutboundMobilityCandidacyPeriod> candidacyPeriods) {
        if (executionYear != null) {
            for (final CandidacyPeriod candidacyPeriod : executionYear.getCandidacyPeriodsSet()) {
                if (candidacyPeriod instanceof OutboundMobilityCandidacyPeriod) {
                    final OutboundMobilityCandidacyPeriod outboundMobilityCandidacyPeriod =
                            (OutboundMobilityCandidacyPeriod) candidacyPeriod;
                    candidacyPeriods.add(outboundMobilityCandidacyPeriod);
                }
            }
        }
    }

    public SortedSet<OutboundMobilityCandidacyPeriod> getCandidacyPeriods() {
        return candidacyPeriods;
    }

    public List<OutboundMobilityCandidacyPeriod> getCandidacyPeriodsAsList() {
        return new ArrayList<OutboundMobilityCandidacyPeriod>(candidacyPeriods);
    }

    public void setCandidacyPeriodsAsList(List<OutboundMobilityCandidacyPeriod> candidacyPeriodsAsList) {
        this.candidacyPeriods.retainAll(candidacyPeriodsAsList);
        this.candidacyPeriods.addAll(candidacyPeriodsAsList);
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void createNewOutboundMobilityCandidacyPeriod() {
        final OutboundMobilityCandidacyPeriod candidacyPeriod =
                OutboundMobilityCandidacyPeriod.create(getExecutionYear(), getStartDateTime(), getEndDateTime());
        candidacyPeriods.add(candidacyPeriod);
    }

    public void createNewOutboundMobilityCandidacyContest() {
        for (final OutboundMobilityCandidacyPeriod candidacyPeriod : candidacyPeriods) {
            candidacyPeriod.createOutboundMobilityCandidacyContest(executionDegree, mobilityProgram, unit, code, vacancies);
        }
        executionDegree = null;
        unit = null;
        code = null;
        vacancies = null;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public UniversityUnit getUnit() {
        return unit;
    }

    public void setUnit(UniversityUnit unit) {
        this.unit = unit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getVacancies() {
        return vacancies;
    }

    public void setVacancies(Integer vacancies) {
        this.vacancies = vacancies;
    }

    public MobilityProgram getMobilityProgram() {
        return mobilityProgram;
    }

    public void setMobilityProgram(MobilityProgram mobilityProgram) {
        this.mobilityProgram = mobilityProgram;
    }

    public SortedSet<OutboundMobilityCandidacyContest> getOutboundMobilityCandidacyContest() {
        final SortedSet<OutboundMobilityCandidacyContest> result = new TreeSet<OutboundMobilityCandidacyContest>();
        for (final OutboundMobilityCandidacyPeriod candidacyPeriod : candidacyPeriods) {
            result.addAll(candidacyPeriod.getOutboundMobilityCandidacyContestSet());
        }
        return result;
    }

}
