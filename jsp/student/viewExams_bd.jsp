<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="examsToEnroll" name="component" property="examsToEnroll" />
<bean:define id="examsEnrolled" name="component" property="examsEnrolledDistributions" />

<logic:empty name="examsToEnroll" >
	<logic:empty name="examsEnrolled">
		<span class="error"><bean:message key="message.no.exams.to.enroll"/></span>
	</logic:empty>
</logic:empty>


<logic:notEmpty name="examsToEnroll" >
	<h2><bean:message key="label.examsToEnroll"/></h2>
	<table width="90%" align="center">
	<tr>
		<td class="listClasses-header" ><bean:message key="label.course"/></td>
		<td class="listClasses-header" ><bean:message key="label.season"/></td>
		<td class="listClasses-header" ><bean:message key="label.day"/></td>
		<td class="listClasses-header" ><bean:message key="label.beginning"/></td>
		<td class="listClasses-header" ><bean:message key="label.enroll" /></td>
	</tr>	
		<logic:iterate id="exam" name="examsToEnroll">
		<bean:define id="objectCode" name="exam" property="idInternal"/>	
		<logic:present name="exam" property="infoExecutionCourses">
		<bean:size id="numberOfExecutionCourses" name="exam" property="infoExecutionCourses"/>
		<logic:iterate id="infoExecutionCourse" name="exam" property="infoExecutionCourses" length="1" >
		<tr>
			<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome"/>-<bean:write name="infoExecutionCourse" property="sigla"/></td>		
			<td class="listClasses" rowspan="<%= numberOfExecutionCourses %>" ><bean:write name="exam" property="season"/></td>		
			<td class="listClasses" rowspan="<%= numberOfExecutionCourses %>"><bean:write name="exam" property="date"/></td>
			<td class="listClasses" rowspan="<%= numberOfExecutionCourses %>"><bean:write name="exam" property="beginningHour"/></td>
			<td class="listClasses" rowspan="<%= numberOfExecutionCourses %>">
				<html:link 
					page="<%= "/examEnrollmentManager.do?method=enrollStudent&amp;objectCode="+ pageContext.findAttribute("objectCode") %>" >
						<bean:message key="label.enroll" />
				</html:link>
			</td>
		</tr>
		</logic:iterate>
		<logic:iterate id="infoExecutionCourse" name="exam" property="infoExecutionCourses" offset="1" >
		<tr>
			<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome"/>-<bean:write name="infoExecutionCourse" property="sigla"/></td>		
		</tr>
		</logic:iterate>
		</logic:present>
	</logic:iterate>
	</table>
</logic:notEmpty>
<br/>
<br/>
<html:errors/>
<logic:notEmpty name="examsEnrolled" >
	<h2><bean:message key="label.examsEnrolled"/></h2>
	<table width="90%" align="center">
		<tr>
			<td class="listClasses-header" ><bean:message key="label.course"/></td>
			<td class="listClasses-header" ><bean:message key="label.season"/></td>
			<td class="listClasses-header" ><bean:message key="label.day"/></td>
			<td class="listClasses-header" ><bean:message key="label.beginning"/></td>
			<td class="listClasses-header" >&nbsp;</td>
			
		</tr>	
		<logic:iterate id="examStudentRoom" name="examsEnrolled" type="net.sourceforge.fenixedu.dataTransferObject.InfoExamStudentRoom">
			<bean:define id="exam" name="examStudentRoom" property="infoExam"/>
			<bean:define id="objectCode" name="exam" property="idInternal"/>	
			<logic:present name="exam" property="infoExecutionCourses">
			<bean:size id="numberOfExecutionCourses" name="exam" property="infoExecutionCourses"/>
			<logic:iterate id="infoExecutionCourse" name="exam" property="infoExecutionCourses" length="1" >
			<tr>
				<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome"/>-<bean:write name="infoExecutionCourse" property="sigla"/></td>			
				<td class="listClasses" rowspan="<%= numberOfExecutionCourses %>"><bean:write name="exam" property="season"/></td>		
				<td class="listClasses" rowspan="<%= numberOfExecutionCourses %>"><bean:write name="exam" property="date"/></td>
				<td class="listClasses" rowspan="<%= numberOfExecutionCourses %>"><bean:write name="exam" property="beginningHour"/></td>
				<td class="listClasses" rowspan="<%= numberOfExecutionCourses %>">
					<logic:notPresent name="examStudentRoom" property="infoRoom">
						<logic:match name="exam" property="enrollmentAuthorization" value="true">
						<html:link 
							page="<%= "/examEnrollmentManager.do?method=unEnrollStudent&amp;objectCode="+ pageContext.findAttribute("objectCode") %>" >
								<bean:message key="label.unEnroll" />
						</html:link> 
						</logic:match>
						<logic:notMatch name="exam" property="enrollmentAuthorization" value="true">
						&nbsp;
						</logic:notMatch>
					</logic:notPresent>
					<logic:present name="examStudentRoom" property="infoRoom">
							<bean:write name="examStudentRoom" property="infoRoom.nome"/>						
					</logic:present>		
				</td>
			</tr>
			</logic:iterate>
			<logic:iterate id="infoExecutionCourse" name="exam" property="infoExecutionCourses" offset="1" >
			<tr>
				<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome"/>-<bean:write name="infoExecutionCourse" property="sigla"/></td>			
			</tr>
			</logic:iterate>
			</logic:present>
		</logic:iterate>
	</table>
</logic:notEmpty>
