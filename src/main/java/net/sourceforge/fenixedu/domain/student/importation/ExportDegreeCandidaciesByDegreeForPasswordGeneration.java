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
package net.sourceforge.fenixedu.domain.student.importation;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.IMDCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.spaces.domain.Space;

public class ExportDegreeCandidaciesByDegreeForPasswordGeneration extends
        ExportDegreeCandidaciesByDegreeForPasswordGeneration_Base {

    private static final List<DegreeType> ACCEPTED_DEGREE_TYPES = Arrays.asList(DegreeType.BOLONHA_DEGREE,
            DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);

    public ExportDegreeCandidaciesByDegreeForPasswordGeneration() {
        super();
    }

    public ExportDegreeCandidaciesByDegreeForPasswordGeneration(final ExecutionYear executionYear, final EntryPhase entryPhase) {
        super.init(executionYear, entryPhase);
    }

    @Override
    public QueueJobResult execute() throws Exception {
        final Map<Degree, Set<Person>> result = new HashMap<Degree, Set<Person>>();

        for (final ExecutionDegree executionDegree : getExecutionYear().getExecutionDegrees()) {
            if (!isAcceptedDegreeType(executionDegree)) {
                continue;
            }

            for (final StudentCandidacy studentCandidacy : executionDegree.getStudentCandidacies()) {
                if (!(studentCandidacy instanceof DegreeCandidacy || studentCandidacy instanceof IMDCandidacy)) {
                    continue;
                }

                if (studentCandidacy.hasAnyCandidacySituations()) {
                    if (studentCandidacy.getActiveCandidacySituationType() == CandidacySituationType.STAND_BY
                            && studentCandidacy.getEntryPhase().equals(getEntryPhase())
                            && !studentCandidacy.getPerson().hasRole(RoleType.STUDENT)
                            && !studentCandidacy.getPerson().getStudent().hasAnyRegistrations()
                            && !studentCandidacy.getPerson().hasRole(RoleType.EMPLOYEE)) {
                        addPerson(result, executionDegree.getDegree(), studentCandidacy.getPerson());
                    }
                }
            }
        }

        ByteArrayOutputStream stream = null;
        PrintWriter writer = null;
        try {
            stream = new ByteArrayOutputStream();
            writer = new PrintWriter(new BufferedOutputStream(stream));

            for (final Map.Entry<Degree, Set<Person>> entry : result.entrySet()) {
                writer.println(String.format("\nCurso %s - %s ", entry.getKey().getNameI18N().getContent(),
                        getCampus(entry.getKey()).getName()));
                for (final Person person : entry.getValue()) {
                    writer.println(person.getIstUsername());
                }
            }
        } finally {
            writer.close();
            stream.close();
        }

        final QueueJobResult queueJobResult = new QueueJobResult();
        queueJobResult.setContentType("text/plain");
        queueJobResult.setContent(stream.toByteArray());

        return queueJobResult;
    }

    private Space getCampus(final Degree degree) {
        final Collection<Space> result = degree.getCampus(getExecutionYear());

        if (result.size() != 1) {
            throw new RuntimeException("Unexpected campus count");
        }

        return result.iterator().next();
    }

    private void addPerson(final Map<Degree, Set<Person>> result, final Degree degree, final Person person) {
        final Set<Person> persons;

        if (result.containsKey(degree)) {
            persons = result.get(degree);
        } else {
            persons = new HashSet<Person>();
            result.put(degree, persons);
        }

        persons.add(person);

    }

    private boolean isAcceptedDegreeType(final ExecutionDegree executionDegree) {
        return ACCEPTED_DEGREE_TYPES.contains(executionDegree.getDegree().getDegreeType());
    }

    public static boolean canRequestJob() {
        return QueueJob.getUndoneJobsForClass(ExportDegreeCandidaciesByDegreeForPasswordGeneration.class).isEmpty();
    }

    public static List<ExportDegreeCandidaciesByDegreeForPasswordGeneration> readAllJobs(final ExecutionYear executionYear) {
        List<ExportDegreeCandidaciesByDegreeForPasswordGeneration> jobList =
                new ArrayList<ExportDegreeCandidaciesByDegreeForPasswordGeneration>();

        CollectionUtils.select(executionYear.getDgesBaseProcess(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return arg0 instanceof ExportDegreeCandidaciesByDegreeForPasswordGeneration;
            }
        }, jobList);

        return jobList;
    }

    public static List<ExportDegreeCandidaciesByDegreeForPasswordGeneration> readDoneJobs(final ExecutionYear executionYear) {
        List<ExportDegreeCandidaciesByDegreeForPasswordGeneration> jobList =
                new ArrayList<ExportDegreeCandidaciesByDegreeForPasswordGeneration>();

        CollectionUtils.select(executionYear.getDgesBaseProcess(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return (arg0 instanceof ExportDegreeCandidaciesByDegreeForPasswordGeneration) && ((QueueJob) arg0).getDone();
            }
        }, jobList);

        return jobList;
    }

    public static List<ExportDegreeCandidaciesByDegreeForPasswordGeneration> readUndoneJobs(final ExecutionYear executionYear) {
        return new ArrayList(CollectionUtils.subtract(readAllJobs(executionYear), readDoneJobs(executionYear)));
    }

    public static List<ExportDegreeCandidaciesByDegreeForPasswordGeneration> readPendingJobs(final ExecutionYear executionYear) {
        List<ExportDegreeCandidaciesByDegreeForPasswordGeneration> jobList =
                new ArrayList<ExportDegreeCandidaciesByDegreeForPasswordGeneration>();

        CollectionUtils.select(executionYear.getDgesBaseProcess(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return (arg0 instanceof ExportDegreeCandidaciesByDegreeForPasswordGeneration)
                        && ((QueueJob) arg0).getIsNotDoneAndNotCancelled();
            }
        }, jobList);

        return jobList;
    }

}
