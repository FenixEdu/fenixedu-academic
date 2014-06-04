/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.elections;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.Region;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

public class YearDelegateElection extends YearDelegateElection_Base {

    /*
     * When created, must have the following attributes specified
     */
    public YearDelegateElection(ExecutionYear executionYear, Degree degree, CurricularYear curricularYear,
            DelegateElectionCandidacyPeriod candidacyPeriod) {
        super();

        setExecutionYear(executionYear);
        setDegree(degree);
        setCurricularYear(curricularYear);

        /*
         * Must be invoked after setExecutionYear
         */
        setCandidacyPeriod(candidacyPeriod);
    }

    @Override
    public void delete() {
        setCurricularYear(null);
        super.delete();
    }

    @Override
    public void setCandidacyPeriod(DelegateElectionCandidacyPeriod candidacyPeriod) {
        if (candidacyPeriod != null) {
            validatePeriodGivenExecutionYear(getExecutionYear(), candidacyPeriod);
            if (hasLastVotingPeriod()) {
                if (candidacyPeriod.endsBefore(getLastVotingPeriod())) {
                    throw new DomainException("error.elections.edit.colidesWithVotingPeriod", new String[] {
                            getDegree().getSigla(), getCurricularYear().getYear().toString(), candidacyPeriod.getPeriod(),
                            getLastVotingPeriod().getPeriod() });
                }
            }
        }

        super.setCandidacyPeriod(candidacyPeriod);

    }

    @Override
    public void addVotingPeriod(DelegateElectionVotingPeriod votingPeriod) {
        if (votingPeriod != null) {
            validatePeriodGivenExecutionYear(getExecutionYear(), votingPeriod);

            if (!hasCandidacyPeriod()) {
                throw new DomainException("error.elections.createVotingPeriod.mustCreateCandidacyPeriod", new String[] {
                        getDegree().getSigla(), getCurricularYear().getYear().toString() });
            }

            if (!getCandidacyPeriod().endsBefore(votingPeriod)) {
                throw new DomainException("error.elections.edit.colidesWithCandidacyPeriod", new String[] {
                        getDegree().getSigla(), getCurricularYear().getYear().toString(), getCandidacyPeriod().getPeriod(),
                        votingPeriod.getPeriod() });
            }
            if (!getLastElectionPeriod().endsBefore(votingPeriod)) {
                throw new DomainException("error.elections.edit.colidesWithPreviousVotingPeriod", new String[] {
                        getDegree().getSigla(), getCurricularYear().getYear().toString(), getCandidacyPeriod().getPeriod(),
                        votingPeriod.getPeriod() });

            }
        }
        super.addVotingPeriod(votingPeriod);
    }

    /*
     * Checks if given period belongs to given execution year
     */
    private void validatePeriodGivenExecutionYear(ExecutionYear executionYear, DelegateElectionPeriod period) {
        if (period.getStartDate().isBefore(executionYear.getBeginDateYearMonthDay())
                || period.getEndDate().isAfter(executionYear.getEndDateYearMonthDay())) {
            throw new DomainException("error.elections.setPeriod.invalidPeriod", new String[] { getDegree().getSigla(),
                    getCurricularYear().getYear().toString(), period.getPeriod(), executionYear.getYear() });
        }
    }

    public boolean getCanYearDelegateBeElected() {
        return (getLastVotingPeriod().isCurrentPeriod() || hasElectedStudent() ? false : true);
    }

    /*
     * Checks if the new election candidacy period colides with another election
     * candidacy period, previously added to this degree and curricular year
     */
    private static void checkNewElectionCandidacyPeriod(Degree degree, ExecutionYear executionYear,
            CurricularYear curricularYear, DelegateElectionCandidacyPeriod candidacyPeriod) throws DomainException {

        for (DelegateElection election : degree.getYearDelegateElectionsGivenExecutionYearAndCurricularYear(executionYear,
                curricularYear)) {
            if (!election.getCandidacyPeriod().endsBefore(candidacyPeriod)) {
                throw new DomainException("error.elections.newElection.invalidPeriod", new String[] { degree.getSigla(),
                        curricularYear.getYear().toString(), candidacyPeriod.getPeriod(),
                        election.getCandidacyPeriod().getPeriod() });
            }
        }
    }

    /*
     * If there is a voting period ocurring, it's not possible to add a new
     * election
     */
    private static void checkPreviousDelegateElectionExistence(Degree degree, CurricularYear curricularYear,
            ExecutionYear executionYear) throws DomainException {

        final DelegateElection previousElection =
                degree.getYearDelegateElectionWithLastCandidacyPeriod(executionYear, curricularYear);
        if (previousElection != null && previousElection.hasLastVotingPeriod()
                && previousElection.getLastVotingPeriod().isCurrentPeriod()) {
            throw new DomainException("error.elections.newElection.currentVotingPeriodExists", new String[] { degree.getSigla(),
                    curricularYear.getYear().toString(), previousElection.getLastVotingPeriod().getPeriod() });
        }

        if (previousElection != null && previousElection.getVotingPeriod() != null && previousElection.hasLastVotingPeriod()
                && !previousElection.getLastVotingPeriod().isPastPeriod()) {
            // future voting period (must be deleted)
            previousElection.getLastVotingPeriod().delete();
        }
    }

    /*
     * DOMAIN SERVICES
     */
    public static YearDelegateElection createDelegateElectionWithCandidacyPeriod(Degree degree, ExecutionYear executionYear,
            YearMonthDay candidacyPeriodStartDate, YearMonthDay candidacyPeriodEndDate, CurricularYear curricularYear) {

        DelegateElectionCandidacyPeriod period =
                new DelegateElectionCandidacyPeriod(candidacyPeriodStartDate, candidacyPeriodEndDate);
        checkNewElectionCandidacyPeriod(degree, executionYear, curricularYear, period);

        /* Checks if there is a previous delegate election and its state */
        checkPreviousDelegateElectionExistence(degree, curricularYear, executionYear);

        YearDelegateElection election = new YearDelegateElection(executionYear, degree, curricularYear, period);

        /* Add degree students to election students list */
        for (DegreeCurricularPlan dcp : degree.getActiveDegreeCurricularPlans()) {
            for (StudentCurricularPlan scp : dcp.getActiveStudentCurricularPlans()) {
                if (scp.getRegistration().getCurricularYear(executionYear) == curricularYear.getYear()) {
                    if (!hasDelegateElection(election, scp)) {
                        election.addStudents(scp.getRegistration().getStudent());
                    }
                }
            }
        }

        return election;
    }

    private static boolean hasDelegateElection(YearDelegateElection election, StudentCurricularPlan scp) {
        for (DelegateElection delegateElection : scp.getRegistration().getStudent().getDelegateElections()) {
            if (delegateElection instanceof YearDelegateElection) {
                if (delegateElection.getDegree().equals(election.getDegree())
                        && delegateElection.getExecutionYear().equals(election.getExecutionYear())
                        && (delegateElection.hasLastVotingPeriod() && !delegateElection.getLastVotingPeriod().isPastPeriod())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void createVotingPeriod(YearMonthDay startDate, YearMonthDay endDate) {
        if (hasLastVotingPeriod()) {
            if (getLastVotingPeriod().isPastPeriod() && !getLastVotingPeriod().isFirstRoundElections()) {
                throw new DomainException("error.elections.createVotingPeriod.mustCreateNewCandidacyPeriod", new String[] {
                        getDegree().getSigla(), getCurricularYear().getYear().toString() });
            }
            if (getLastVotingPeriod().isCurrentPeriod()) {
                throw new DomainException("error.elections.createVotingPeriod.onlyCanExtendPeriod", new String[] {
                        getDegree().getSigla(), getCurricularYear().getYear().toString() });
            }
            if (hasVotingPeriodIntersecting(startDate, endDate)) {
                throw new DomainException("error.elections.createVotingPeriod.votingPeriodIntersecting", new String[] {
                        getDegree().getSigla(), getCurricularYear().getYear().toString() });
            }
        }

        DelegateElectionVotingPeriod period = new DelegateElectionVotingPeriod(startDate, endDate);
        addVotingPeriod(period);
    }

    @Override
    public void editCandidacyPeriod(final YearMonthDay startDate, final YearMonthDay endDate) {
        final DelegateElectionCandidacyPeriod candidacyPeriod = getCandidacyPeriod();
        final DelegateElectionVotingPeriod votingPeriod = getVotingPeriod(startDate, endDate);

        if (candidacyPeriod.isPastPeriod() && votingPeriod != null && votingPeriod.getStartDate().isBefore(new YearMonthDay())) {
            throw new DomainException("error.yearDelegateElections.edit.pastPeriod", new String[] { getDegree().getSigla(),
                    getCurricularYear().getYear().toString(), getCandidacyPeriod().getPeriod() });
        } else {
            try {
                candidacyPeriod.delete();
                setCandidacyPeriod(new DelegateElectionCandidacyPeriod(startDate, endDate));
            } catch (DomainException ex) {
                throw new DomainException(ex.getMessage(), ex.getArgs());
            }
        }
    }

    @Override
    public void editVotingPeriod(YearMonthDay startDate, YearMonthDay endDate, DelegateElectionVotingPeriod votingPeriod) {
        if (!endDate.isAfter(getLastVotingEndDate())) {
            throw new DomainException("error.elections.edit.newEndDateMustBeGreater", getDegree().getSigla(), getCurricularYear()
                    .getYear().toString());
        }

        if (!votingPeriod.isPastPeriod()) {
            votingPeriod.setEndDate(endDate);
        } else {
            throw new DomainException("error.yearDelegateElections.edit.pastPeriod", new String[] { getDegree().getSigla(),
                    getCurricularYear().getYear().toString(), votingPeriod.getPeriod() });
        }
    }

    @Override
    public void deleteCandidacyPeriod() {
        if (!getCandidacyPeriod().isPastPeriod()) {
            this.delete();
        } else {
            throw new DomainException("error.yearDelegateElections.delete.pastPeriod", new String[] { getDegree().getSigla(),
                    getCurricularYear().getYear().toString(), getCandidacyPeriod().getPeriod() });
        }
    }

    @Override
    public void deleteVotingPeriod(DelegateElectionVotingPeriod votingPeriod, boolean removeElection) {

        if (!votingPeriod.isPastPeriod() && !votingPeriod.isCurrentPeriod()) {
            super.deleteVotingPeriod(votingPeriod);
            if (removeElection) {
                this.deleteCandidacyPeriod();
            }
        } else {
            throw new DomainException("error.yearDelegateElections.delete.pastPeriod", new String[] { getDegree().getSigla(),
                    getCurricularYear().getYear().toString(),
                    getVotingPeriod(votingPeriod.getStartDate(), votingPeriod.getEndDate()).getPeriod() });
        }

    }

    public DelegateElectionVotingPeriod getCurrentVotingPeriod() {
        for (DelegateElectionVotingPeriod votingPeriod : getVotingPeriod()) {
            if (votingPeriod.isCurrentPeriod()) {
                return votingPeriod;
            }
        }
        return null;
    }

    public static StyledExcelSpreadsheet exportElectionsResultsToFile(List<Degree> degrees, ExecutionYear executionYear)
            throws IOException {
        StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet();

        for (Degree degree : degrees) {
            spreadsheet.getSheet(degree.getSigla());
            List<YearDelegateElection> elections = sortByYear(degree.getYearDelegateElectionsGivenExecutionYear(executionYear));
            for (YearDelegateElection election : elections) {
                if (election.hasLastVotingPeriod()) {
                    DelegateElectionVotingPeriod votingPeriod = election.getLastVotingPeriod();
                    spreadsheet.newHeaderRow();
                    int fistHeaderRow = spreadsheet.getRow().getRowNum();
                    spreadsheet.addHeader(String.format("%s - %s (%s)", BundleUtil.getString(Bundle.PEDAGOGICAL, "label.elections.excel.curricularYear"),
                            election.getCurricularYear().getYear(), votingPeriod.getPeriod()), 10000);
                    spreadsheet.getSheet().addMergedRegion(new Region(fistHeaderRow, (short) 0, fistHeaderRow, (short) 5));
                    spreadsheet.newRow();
                    if (votingPeriod.getVotesSet().size() == 0) {
                        spreadsheet.addCell(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.elections.excel.not.have.votes"));
                    } else {
                        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.elections.excel.studentNumber"), 6000);
                        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.elections.excel.studentName"), 10000);
                        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.phone"), 4000);
                        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.email"), 6000);
                        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.address"), 12000);
                        spreadsheet.addHeader(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.elections.excel.nrTotalVotes"), 5000);
                        List<DelegateElectionResultsByStudentDTO> resultsByStudent =
                                sortByResults(votingPeriod.getDelegateElectionResults());
                        for (DelegateElectionResultsByStudentDTO resultByStudent : resultsByStudent) {
                            Student student = resultByStudent.getStudent();
                            Person person = student.getPerson();
                            String phone =
                                    (StringUtils.isEmpty(person.getDefaultPhoneNumber())) ? "-" : person.getDefaultPhoneNumber();
                            String email =
                                    (StringUtils.isEmpty(person.getDefaultEmailAddressValue())) ? "-" : person
                                            .getDefaultEmailAddressValue();
                            String address = (StringUtils.isEmpty(person.getAddress())) ? "-" : person.getAddress();

                            spreadsheet.newRow();
                            spreadsheet.addCell(student.getNumber());
                            spreadsheet.addCell(student.getName());
                            spreadsheet.addCell(phone);
                            spreadsheet.addCell(email);
                            spreadsheet.addCell(address);
                            spreadsheet.addCell(resultByStudent.getVotesNumber());

                        }
                        spreadsheet.setRegionBorder(fistHeaderRow, spreadsheet.getRow().getRowNum() + 1, 0, 2);
                        spreadsheet.newRow();
                        spreadsheet.newRow();
                        spreadsheet.addCell(BundleUtil.getString(Bundle.PEDAGOGICAL, "label.elections.excel.nrBlankTotalVotes"));
                        spreadsheet.addCell(votingPeriod.getBlankVotesElection(), spreadsheet.getExcelStyle().getValueStyle());
                    }
                }
                spreadsheet.newRow();
                spreadsheet.newRow();

            }

        }
        return spreadsheet;

    }

    private static List<DelegateElectionResultsByStudentDTO> sortByResults(
            List<DelegateElectionResultsByStudentDTO> resultsByStudent) {
        Collections.sort(resultsByStudent, new Comparator<DelegateElectionResultsByStudentDTO>() {
            @Override
            public int compare(DelegateElectionResultsByStudentDTO o1, DelegateElectionResultsByStudentDTO o2) {
                return o2.getVotesNumber() - o1.getVotesNumber();
            }
        });
        return resultsByStudent;
    }

    private static List<YearDelegateElection> sortByYear(List<YearDelegateElection> elections) {
        Collections.sort(elections, new Comparator<YearDelegateElection>() {
            @Override
            public int compare(YearDelegateElection o1, YearDelegateElection o2) {
                return o1.getCurricularYear().getYear() - o2.getCurricularYear().getYear();
            }
        });
        return elections;
    }

    @Deprecated
    public boolean hasCurricularYear() {
        return getCurricularYear() != null;
    }

}
