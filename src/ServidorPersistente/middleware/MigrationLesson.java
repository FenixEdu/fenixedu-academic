/*
 * Created on 27/Fev/2003 by jpvl
 *
 */
package ServidorPersistente.middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Dominio.IAula;
import Dominio.SOPAulas;

/**
 * @author jpvl
 */
public class MigrationLesson {
    private List classList;

    private List courseInitialsList;

    private HashMap courseToClassHashMap;

    private int day;

    private int startIndex;

    private int endIndex;

    private String room;

    private int shiftNumber;

    private MigrationExecutionCourse migrationExecutionCourse;

    private List halfHourList;

    private String lessonType;

    private List lessonTypeList;

    private MigrationShift migrationShift = null;

    private boolean migrated = false;

    private IAula lesson = null;

    public MigrationLesson(SOPAulas startHalfHour) {
        courseToClassHashMap = new HashMap();
        classList = new ArrayList();
        halfHourList = new ArrayList();
        courseInitialsList = new ArrayList();
        lessonTypeList = new ArrayList();

        halfHourList.add(startHalfHour);
        classList.add(startHalfHour.getTurma());

        courseInitialsList.add(startHalfHour.getDisciplina());

        List courseClassList = new ArrayList();
        courseClassList.add(startHalfHour.getTurma());

        courseToClassHashMap.put(startHalfHour.getDisciplina(), courseClassList);

        startIndex = startHalfHour.getHora();
        endIndex = startIndex;

        shiftNumber = startHalfHour.getNumeroTurno();
        day = startHalfHour.getDia();
        room = startHalfHour.getSala();
        if (room == null)
            room = "";

        lessonTypeList.add(startHalfHour.getTipoAula());

        lessonType = startHalfHour.getTipoAula();

    }

    public void addHalfHour(SOPAulas halfHour) {
        if (!belongTo(halfHour) && !isNextTo(halfHour)) {
            System.out.println(halfHour);
            System.out.println(this);
            throw new IllegalStateException("Are you trying to add an inconsistent half hour! belongs="
                    + belongTo(halfHour) + ";isNext=" + isNextTo(halfHour));
        }

        /* case where half hours are sequenced */

        endIndex = Math.max(halfHour.getHora(), endIndex);

        halfHourList.add(halfHour);

        if (!lessonTypeList.contains(halfHour.getTipoAula()))
            lessonTypeList.add(halfHour.getTipoAula());

        addToClassList(halfHour.getTurma());
        addToCourseList(halfHour.getDisciplina());

        addToCourseToClassHashMap(halfHour.getDisciplina(), halfHour.getTurma());

    }

    private void addToClassList(String className) {
        if (!classList.contains(className))
            this.classList.add(className);
    }

    /**
     * @param courseInitials
     */
    private void addToCourseList(String courseInitials) {
        if (!courseInitialsList.contains(courseInitials)) {
            courseInitialsList.add(courseInitials);
        }
    }

    /**
     * @param string
     * @param string2
     */
    private void addToCourseToClassHashMap(String courseInitials, String className) {
        List classList = (List) courseToClassHashMap.get(courseInitials);
        if (classList == null) {
            classList = new ArrayList();
            courseToClassHashMap.put(courseInitials, classList);
        }
        if (!classList.contains(className)) {
            classList.add(className);
        }
    }

    /**
     * @param halfHour
     * @return boolean
     */
    private boolean belongTo(SOPAulas halfHour) {
        if ((day != halfHour.getDia())
                || (!room.equals(halfHour.getSala() == null ? "" : halfHour.getSala()))
                || (endIndex < halfHour.getHora()) || (startIndex > halfHour.getHora())) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @return List
     */
    public List getClassList() {
        return classList;
    }

    /**
     * @return List
     */
    public List getCourseInitialsList() {
        return courseInitialsList;
    }

    /**
     * @return HashMap
     */
    public HashMap getCourseToClassHashMap() {
        return courseToClassHashMap;
    }

    /**
     * @return int
     */
    public int getDay() {
        return day;
    }

    /**
     * @return int
     */
    public int getEndIndex() {
        return endIndex;
    }

    /**
     * @return MigrationExecutionCourse
     */
    public MigrationExecutionCourse getMigrationExecutionCourse() {
        return migrationExecutionCourse;
    }

    /**
     * @return List
     */
    public List getHalfHourList() {
        return halfHourList;
    }

    /**
     * @return String
     */
    public String getLessonType() {
        return lessonType;
    }

    /**
     * @return List
     */
    public List getLessonTypeList() {
        return lessonTypeList;
    }

    /**
     * @return MigrationShift
     */
    public MigrationShift getMigrationShift() {
        return this.migrationShift;
    }

    /**
     * @return String
     */
    public String getRoom() {
        return room;
    }

    /**
     * @return int
     */
    public int getShiftNumber() {
        return shiftNumber;
    }

    /**
     * @return int
     */
    public int getStartIndex() {

        return this.startIndex;
    }

    /**
     * @return boolean
     */
    public boolean isMigrated() {
        return migrated;
    }

    public boolean isNextTo(SOPAulas halfHour) {
        if ((day != halfHour.getDia())
                || (!room.equals(halfHour.getSala() == null ? "" : halfHour.getSala()))
                || (shiftNumber != halfHour.getNumeroTurno()) || (endIndex + 1 != halfHour.getHora())
                || (!lessonType.equals(halfHour.getTipoAula()))) {
            return false;
        }
        return true;
    }

    /**
     * @param executionCourse
     */
    public void setMigrationExecutionCourse(MigrationExecutionCourse executionCourse) {
        this.migrationExecutionCourse = executionCourse;

    }

    /**
     * Sets the migrated.
     * 
     * @param migrated
     *            The migrated to set
     */
    public void setMigrated(boolean migrated) {
        this.migrated = migrated;
    }

    /**
     * @param migrationShift
     */
    public void setMigrationShift(MigrationShift migrationShift) {
        if (this.migrationShift != null) {
            System.out.println(migrationShift.getMigrationExecutionCourse().toString());
            throw new IllegalArgumentException("Setting another shift!");
        }
        this.migrationShift = migrationShift;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("");

        stringBuffer.append("[MigrationLesson=").append("startIndex=").append(this.startIndex).append(
                ",endIndex=").append(this.endIndex).append(",tipoAula=").append(this.lessonType).append(
                ",room=").append(this.room).append(",numeroTurno=").append(this.shiftNumber).append(
                ",dia=").append(this.day).append("\r\n");

        Iterator courseIterator = courseToClassHashMap.keySet().iterator();
        stringBuffer.append("\t Disciplinas(").append("size=").append(courseInitialsList.size()).append(
                ")").append(":\r\n");
        while (courseIterator.hasNext()) {
            String courseInitials = (String) courseIterator.next();
            stringBuffer.append("\t\t(").append(courseInitials).append(":");

            List classList = (List) courseToClassHashMap.get(courseInitials);
            Iterator classIterator = classList.iterator();
            while (classIterator.hasNext()) {
                String className = (String) classIterator.next();
                stringBuffer.append("(").append(className).append(")");
            }
            stringBuffer.append(")\r\n");
        }

        Iterator lessonTypeIterator = this.lessonTypeList.iterator();

        stringBuffer.append("\tTiposAula(").append("size=").append(lessonTypeList.size()).append(")")
                .append(":");
        while (lessonTypeIterator.hasNext()) {
            String lessonType = (String) lessonTypeIterator.next();
            stringBuffer.append("(").append(lessonType).append(")");
        }

        return stringBuffer.toString();

    }

    /**
     * @return IAula
     */
    public IAula getLesson() {
        return lesson;
    }

    /**
     * Sets the lesson.
     * 
     * @param lesson
     *            The lesson to set
     */
    public void setLesson(IAula lesson) {
        this.lesson = lesson;
    }

}