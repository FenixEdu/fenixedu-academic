/*
 * Created on 7/Nov/2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package DataBeans.gesdis;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoObject;


/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class InfoCourseReport extends InfoObject {

	private String report;
	private InfoExecutionCourse  infoExecutionCourse;

	public InfoCourseReport() {
	}

	public String getReport() {
		return report;
	}
	
	public InfoExecutionCourse getInfoExecutionCourse(){
		return infoExecutionCourse;
	}

	public void setReport(String report) {
		this.report = report;
	}
	
	public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse){
		this.infoExecutionCourse = infoExecutionCourse;
	}

}
