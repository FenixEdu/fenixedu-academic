/*
 * Created on 4/Jul/2003 by jpvl
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author jpvl
 */
public class DepartmentCreditsView {
	private List infoTeacherList;
	private List departmentList;
	/*
	 * Department owner of infoTeacherList.
	 */
	private InfoDeparment listDepartment;
	
	public DepartmentCreditsView () {
	}
	
	/**
	 * @return
	 */
	public List getDepartmentList() {
		return this.departmentList;
	}

	/**
	 * @param departmentList
	 */
	public void setDepartmentList(List departmentList) {
		this.departmentList = departmentList;
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

	/**
	 * @return
	 */
	public InfoDeparment getListDepartment() {
		return this.listDepartment;
	}

	/**
	 * @param listDepartment
	 */
	public void setListDepartment(InfoDeparment listDepartment) {
		this.listDepartment = listDepartment;
	}

}
