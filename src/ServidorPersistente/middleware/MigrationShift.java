/*
 * Created on 6/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware;

import java.util.ArrayList;
import java.util.List;

import Dominio.ISchoolClass;
import Dominio.IShift;

/**
 * @author jpvl
 */
public class MigrationShift {
    private Integer capacity;

    private boolean migrated = false;

    private MigrationExecutionCourse migrationExecutionCourse;

    private IShift shift = null;

    private String shiftName;

    private String type;

    private List classList;

    public MigrationShift() {
        this.classList = new ArrayList();
    }

    /**
     * @return Integer
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * @return MigrationExecutionCourse
     */
    public MigrationExecutionCourse getMigrationExecutionCourse() {
        return migrationExecutionCourse;
    }

    /**
     * @return IShift
     */
    public IShift getShift() {
        return shift;
    }

    /**
     * @return String
     */
    public String getShiftName() {
        return shiftName;
    }

    /**
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * @return boolean
     */
    public boolean isMigrated() {
        return migrated;
    }

    /**
     * Sets the capacity.
     * 
     * @param capacity
     *            The capacity to set
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
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
     * Sets the migrationExecutionCourse.
     * 
     * @param migrationExecutionCourse
     *            The migrationExecutionCourse to set
     */
    public void setMigrationExecutionCourse(MigrationExecutionCourse migrationExecutionCourse) {
        this.migrationExecutionCourse = migrationExecutionCourse;
    }

    /**
     * Sets the shift.
     * 
     * @param shift
     *            The shift to set
     */
    public void setShift(IShift shift) {
        this.shift = shift;
    }

    /**
     * Sets the shiftName.
     * 
     * @param shiftName
     *            The shiftName to set
     */
    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            The type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("");
        stringBuffer.append(migrationExecutionCourse.toString());
        return stringBuffer.toString();
    }

    public void setClass(ISchoolClass clazz) {
        if (!classList.contains(clazz))
            classList.add(clazz);
    }

    /**
     * @return List
     */
    public List getClassList() {
        return classList;
    }

}