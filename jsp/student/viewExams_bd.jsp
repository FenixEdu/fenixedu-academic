<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="examsToEnroll" name="component" property="examsToEnroll" />
<bean:define id="examsEnrolled" name="component" property="examsEnrolled" />
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
	<bean:define id="infoExecutionCourse" name="exam" property="infoExecutionCourse"/>	
	<tr>
	<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome"/>-<bean:write name="infoExecutionCourse" property="sigla"/></td>		
	<td class="listClasses"><bean:write name="exam" property="season"/></td>		
	<td class="listClasses"><bean:write name="exam" property="date"/></td>
	<td class="listClasses"><bean:write name="exam" property="beginningHour"/></td>
	<td class="listClasses">
		<html:link 
			page="<%= "/examEnrollmentManager.do?method=enrollStudent&amp;objectCode="+ pageContext.findAttribute("objectCode") %>" >
				<bean:message key="label.enroll" />
		</html:link>
	</td>
	</tr>
</logic:iterate>
</table>
</logic:notEmpty>
<logic:empty name="examsToEnroll" >
	<h2><bean:message key="message.no.exams.to.enroll"/></h2>
</logic:empty>
<br/>
<br/>
<logic:notEmpty name="examsEnrolled" >
<h2><bean:message key="label.examsEnrolled"/></h2>
<table width="90%" align="center">
<tr>
	<td class="listClasses-header" ><bean:message key="label.course"/></td>
	<td class="listClasses-header" ><bean:message key="label.season"/></td>
	<td class="listClasses-header" ><bean:message key="label.day"/></td>
	<td class="listClasses-header" ><bean:message key="label.beginning"/></td>
	<td class="listClasses-header" ><bean:message key="label.unEnroll" /></td>
	
</tr>	
	<logic:iterate id="exam" name="examsEnrolled">
	<bean:define id="objectCode" name="exam" property="idInternal"/>	
	<bean:define id="infoExecutionCourse" name="exam" property="infoExecutionCourse"/>
	<tr>
	<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome"/>-<bean:write name="infoExecutionCourse" property="sigla"/></td>			
	<td class="listClasses"><bean:write name="exam" property="season"/></td>		
	<td class="listClasses"><bean:write name="exam" property="date"/></td>
	<td class="listClasses"><bean:write name="exam" property="beginningHour"/></td>
	<td class="listClasses">
		<html:link 
			page="<%= "/examEnrollmentManager.do?method=unEnrollStudent&amp;objectCode="+ pageContext.findAttribute("objectCode") %>" >
				<bean:message key="label.unEnroll" />
		</html:link>
	</td>
	
	</tr>
</logic:iterate>
</table>
</logic:notEmpty>
