<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import = "java.util.Iterator" %>
<%@ page import = "net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes" %>
<%@ page import = "java.util.List" %>


<h2>
	<bean:message key="title.teacher.inquiries.results" bundle="INQUIRIES_RESOURCES"/>
</h2>

<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td nowrap="nowrap">
			<bean:message key="property.executionPeriod"/>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	</td>
    
    	
		<td>
			<html:form action="/viewOldInquiriesTeachersResults">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepare"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value='<%= request.getParameter("degreeCurricularPlanID") %>'/>
			
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodId" property="executionPeriodId" onchange="this.form.submit()">
					<html:option key="label.inquiries.chooseExecutionPeriod" bundle="INQUIRIES_RESOURCES" value=""/>
			
					<bean:define id="value"><bean:message key="value.inquiries.allExecutionPeriods" bundle="INQUIRIES_RESOURCES"/></bean:define>
					<html:option key="label.inquiries.allExecutionPeriods" bundle="INQUIRIES_RESOURCES" value='<%= value %>'/>
			
					<logic:notEmpty name="executionPeriodList">
						<logic:iterate id="executionPeriod" name="executionPeriodList" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod"> 
							<bean:define id="currentPeriodId" name="executionPeriod" property="idInternal"/>
							<html:option value="<%= currentPeriodId.toString() %>">
								<bean:write name="executionPeriod" property="semester"/>
								<bean:message key="label.inquiries.ord.semester" bundle="INQUIRIES_RESOURCES"/>
								-
								<bean:write name="executionPeriod" property="infoExecutionYear.year"/>
							</html:option>  
						</logic:iterate>
						
					</logic:notEmpty>
			
				</html:select>&nbsp;
				
			</html:form>
		</td>
		
	</tr>
</table>


<logic:present name="oldInquiriesTeachersResListOfLists">
	<br/>
	<br/>
	<table width="90%"cellpadding="5" border="0">
		<tr>
			<th class="listClasses-header" style="text-align:left">
				<bean:message key="table.header.inquiries.courseName" bundle="INQUIRIES_RESOURCES"/>
			</th>
			<th class="listClasses-header" style="text-align:left">
				<bean:message key="table.header.inquiries.executionPeriod" bundle="INQUIRIES_RESOURCES"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="table.header.inquiries.teachers" bundle="INQUIRIES_RESOURCES"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="table.header.inquiries.teachersnumber" bundle="INQUIRIES_RESOURCES"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="table.header.inquiries.curricularYear" bundle="INQUIRIES_RESOURCES"/>
			</th>
		</tr>
		<logic:iterate id="oldInquiriesTeachersResList" name="oldInquiriesTeachersResListOfLists" type="java.util.List">
			<%
			Iterator iter = oldInquiriesTeachersResList.iterator();
			InfoOldInquiriesTeachersRes oldInquiriesTeachersRes = (InfoOldInquiriesTeachersRes) iter.next();
			int rowspan = oldInquiriesTeachersResList.size();
			%>
			<tr>
				<td rowspan='<%= rowspan %>' class="listClasses" style="text-align:left">
					<%
					String courseNameLink = "/viewOldInquiriesTeachersResults.do?method=viewResults";
					courseNameLink += "&executionPeriodId=" + oldInquiriesTeachersRes.getKeyExecutionPeriod();
					courseNameLink += "&degreeId=" + oldInquiriesTeachersRes.getKeyDegree();
					courseNameLink += "&curricularYear=" + oldInquiriesTeachersRes.getCurricularYear();
					courseNameLink += "&courseCode=" + oldInquiriesTeachersRes.getCourseCode();
					courseNameLink += "&degreeCurricularPlanID=" + request.getParameter("degreeCurricularPlanID");
					%>
					<html:link page='<%= courseNameLink %>' >
						<%= oldInquiriesTeachersRes.getGepCourseName() %>
					</html:link>
				</td>			
				<td rowspan='<%= rowspan %>' class="listClasses" style="text-align:left">
					<%= oldInquiriesTeachersRes.getExecutionPeriod().getSemester() %>
					<bean:message key="label.inquiries.ord.semester" bundle="INQUIRIES_RESOURCES"/>
					<%= oldInquiriesTeachersRes.getExecutionPeriod().getInfoExecutionYear().getYear() %>
				</td>

				<%
				String teacherNameLink = "/viewOldInquiriesTeachersResults.do?method=viewResults";
				teacherNameLink += "&executionPeriodId=" + oldInquiriesTeachersRes.getKeyExecutionPeriod();
				teacherNameLink += "&degreeId=" + oldInquiriesTeachersRes.getKeyDegree();
				teacherNameLink += "&curricularYear=" + oldInquiriesTeachersRes.getCurricularYear();
				teacherNameLink += "&courseCode=" + oldInquiriesTeachersRes.getCourseCode();
				teacherNameLink += "&teacherNumber=" + oldInquiriesTeachersRes.getTeacherNumber();
				teacherNameLink += "&degreeCurricularPlanID=" + request.getParameter("degreeCurricularPlanID");
				%>
				<td class="listClasses">
					<html:link page='<%= teacherNameLink %>' >
						<%
						if(oldInquiriesTeachersRes.getTeacher() != null) {
							out.print(oldInquiriesTeachersRes.getTeacher().getInfoPerson().getNome());
						} else {
							out.print(" ");
						}
						%>
					</html:link>
				</td>
				<td class="listClasses">
					<html:link page='<%= teacherNameLink %>' >
						<%= oldInquiriesTeachersRes.getTeacherNumber() %><br/>
					</html:link>
				</td>
				<td rowspan='<%= rowspan %>' class="listClasses">
					<%= oldInquiriesTeachersRes.getCurricularYear() %>
				</td>

				<%
				while(iter.hasNext()) {
					oldInquiriesTeachersRes = (InfoOldInquiriesTeachersRes) iter.next();
					teacherNameLink = "/viewOldInquiriesTeachersResults.do?method=viewResults";
					teacherNameLink += "&executionPeriodId=" + oldInquiriesTeachersRes.getKeyExecutionPeriod();
					teacherNameLink += "&degreeId=" + oldInquiriesTeachersRes.getKeyDegree();
					teacherNameLink += "&curricularYear=" + oldInquiriesTeachersRes.getCurricularYear();
					teacherNameLink += "&courseCode=" + oldInquiriesTeachersRes.getCourseCode();
					teacherNameLink += "&teacherNumber=" + oldInquiriesTeachersRes.getTeacherNumber();
					teacherNameLink += "&degreeCurricularPlanID=" + request.getParameter("degreeCurricularPlanID");
				%>
				<tr>
					<td class="listClasses">
						<html:link page='<%= teacherNameLink %>' >
							<%
							if(oldInquiriesTeachersRes.getTeacher() != null) {
								out.print(oldInquiriesTeachersRes.getTeacher().getInfoPerson().getNome());
							} else {
								out.print(" ");
							}
							%>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page='<%= teacherNameLink %>' >
							<%= oldInquiriesTeachersRes.getTeacherNumber() %><br/>
						</html:link>
					</td>
				</tr>
				<% } %>
									
			</tr>
		</logic:iterate>
	</table>

</logic:present>
