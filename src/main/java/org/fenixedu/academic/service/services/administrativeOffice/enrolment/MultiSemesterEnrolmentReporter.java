/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.service.services.administrativeOffice.enrolment;

//TODO: DELETE
@Deprecated
public class MultiSemesterEnrolmentReporter {

//    private final int hoursToReport;
//    private final int semesterToReport;
//
//    public MultiSemesterEnrolmentReporter(final int hoursToReportFromStart, final int semesterToReport) {
//        this.hoursToReport = hoursToReportFromStart;
//        this.semesterToReport = semesterToReport;
//    }
//
//    public void report(final ExecutionSemester semester, final int year, final int month, final int day, final int hour) {
//        final DateTime enrolmentStartTime = new DateTime(year, month, day, hour, 0, 0, 0);
//        final DateTime endTimeToReport = enrolmentStartTime.plusHours(hoursToReport);
//
//        if (semester.getSemester().intValue() == semesterToReport) {
//            semester.getCurriculumLineLogsSet().stream()
//
//            .filter(l -> l instanceof EnrolmentLog)
//
//            .filter(l -> !l.getDateDateTime().isBefore(enrolmentStartTime) && !l.getDateDateTime().isAfter(endTimeToReport))
//
//            .sorted((l1, l2) -> l1.getDateDateTime().compareTo(l2.getDateDateTime()))
//
//            .forEach(l -> process(semester, enrolmentStartTime, l));
//
//            semester.getAssociatedExecutionCoursesSet().stream()
//
//            .flatMap(ec -> ec.getCourseLoadsSet().stream())
//
//            .flatMap(cl -> cl.getShiftsSet().stream())
//
//            .flatMap(s -> s.getShiftEnrolmentsSet().stream())
//
//            .map(se -> se.getCreatedOn())
//
//            .filter(dt -> !dt.isBefore(enrolmentStartTime) && !dt.isAfter(endTimeToReport))
//
//            .map(dt -> new Interval(enrolmentStartTime, dt).toDuration().getStandardSeconds())
//
//            .forEach(seconds -> add(semester, seconds, new int[] { 0, 0, 1 }));
//        }
//    }
//
//    public JsonArray getStats() {
//        return getStats("registrations", "enrolments", "shifts");
//    }
//
//    private final Map<ExecutionSemester, Set<Registration>> registrationsBySemester = new HashMap<>();
//    private SortedMap<ExecutionSemester, SortedMap<Long, int[]>> stats = new TreeMap<>();
//
//    protected void process(final ExecutionSemester semester, final DateTime enrolmentStartTime, final CurriculumLineLog log) {
//        final Long seconds = new Interval(enrolmentStartTime, log.getDateDateTime()).toDuration().getStandardSeconds();
//        int regCount = countRegistration(semester, log.getStudent()) ? 1 : 0;
//        int enrolCount = log.getAction().equals(EnrolmentAction.ENROL) ? 1 : -1;
//        add(semester, seconds, new int[] { regCount, enrolCount, 0 });
//    }
//
//    private boolean countRegistration(final ExecutionSemester semester, final Registration registration) {
//        final Set<Registration> registrations = getRegistrations(semester);
//        if (registrations.contains(registration)) {
//            return false;
//        }
//        registrations.add(registration);
//        return true;
//    }
//
//    private Set<Registration> getRegistrations(final ExecutionSemester semester) {
//        if (!registrationsBySemester.containsKey(semester)) {
//            registrationsBySemester.put(semester, new HashSet<Registration>());
//        }
//        return registrationsBySemester.get(semester);
//    }
//
//    public void add(final ExecutionSemester semester, final Long seconds, final int[] counts) {
//        final SortedMap<Long, int[]> values = get(stats, semester);
//        if (!values.containsKey(seconds)) {
//            values.put(seconds, new int[counts.length]);
//        }
//        int[] ia = values.get(seconds);
//        for (int i = 0; i < counts.length; ia[i] += counts[i++]);
//    }
//
//    private static SortedMap<Long, int[]> get(final SortedMap<ExecutionSemester, SortedMap<Long, int[]>> map,
//            final ExecutionSemester semester) {
//        if (!map.containsKey(semester)) {
//            map.put(semester, new TreeMap<Long, int[]>());
//        }
//        return map.get(semester);
//    }
//
//    private JsonArray getStats(String... labels) {
//        final JsonArray result = new JsonArray();
//        for (final Entry<ExecutionSemester, SortedMap<Long, int[]>> e : stats.entrySet()) {
//            final String semester = e.getKey().getQualifiedName();
//
//            int[] accumulatedValues = new int[labels.length];
//
//            for (final Entry<Long, int[]> v : e.getValue().entrySet()) {
//                final Long second = v.getKey();
//                final int[] values = v.getValue();
//                final JsonObject obj = new JsonObject();
//                obj.addProperty("label", semester);
//                obj.addProperty("second", second);
//                for (int i = 0; i < labels.length; i++) {
//                    int a = accumulatedValues[i] += values[i];
//                    obj.addProperty(labels[i], a);
//                }
//                result.add(obj);
//            }
//        }
//        return result;
//    }

}
