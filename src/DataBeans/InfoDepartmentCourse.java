/*
 * Created on 12/Ago/2003
 */
package DataBeans;

import java.io.Serializable;

/**
 * @author lmac1
 */

public class InfoDepartmentCourse extends InfoObject {
	
    private String name;
	private String code;
	private InfoDepartment infoDepartment;
	
	public InfoDepartmentCourse() {
		setName("");
		setCode("");
	}
	
	public InfoDepartmentCourse(String name, String code, InfoDepartment infoDepartment) {
		setName(name);
		setCode(code);
		setInfoDepartment(infoDepartment);
	}
	
	public boolean equals(Object obj) {
		boolean result = false;
		if(obj instanceof InfoDepartmentCourse ) {
			InfoDepartmentCourse d = (InfoDepartmentCourse)obj;
			result = ( getName().equals(d.getName()) && getCode().equals(d.getCode()) );
		}
		return result;
	}
	
	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += ", sigla=" + code;
		result += ", nome=" + name;
		result += ", departamento=" + infoDepartment;
		result += "]";
		return result;
	 } 
	 
	 public String getCode() {
	 	return code;
	 }
	 
	 public String getName() {
	 	return name;
	 }
	 
	 public InfoDepartment getInfoDepartment() {
	 	return infoDepartment;
	 }
	 
	 public void setCode(String code) {
	 	this.code = code;
	 }
	 
	 public void setName(String name) {
	 	this.name = name;
	 }
	 
	 public void setInfoDepartment(InfoDepartment infoDepartment) {
	 	this.infoDepartment = infoDepartment;
	 }

}
