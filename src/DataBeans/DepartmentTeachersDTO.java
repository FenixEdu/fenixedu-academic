/*
 * Created on 4/Jul/2003 by jpvl
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author jpvl
 */
public class DepartmentTeachersDTO extends InfoObject {
    private List infoTeacherList;

    private InfoDepartment infoDepartment;

    public DepartmentTeachersDTO() {
    }

    /**
     * @return
     */
    public InfoDepartment getInfoDepartment() {
        return this.infoDepartment;
    }

    /**
     * @param infoDepartment
     */
    public void setInfoDepartment(InfoDepartment infoDeparment) {
        this.infoDepartment = infoDeparment;
    }

    /**
     * @return
     */
    public List getInfoTeacherList() {
        return this.infoTeacherList;
    }

    /**
     * @param infoTeacherList
     */
    public void setInfoTeacherList(List infoTeacherList) {
        this.infoTeacherList = infoTeacherList;
    }
}