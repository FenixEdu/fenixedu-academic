<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.showDistributedTest"/></h2>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoDistributedTest" name="component" property="infoDistributedTest"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>

<bean:define id="visible" name="infoDistributedTest" property="infoTest.visible"/>
<logic:equal name="visible" value="false">
	<span class="error"><bean:message key="message.tests.no.editDistributedTests"/></span>
</logic:equal>
<logic:notEqual name="visible" value="false">


<html:form action="/distributedTestEdition">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="editDistributedTest"/>
<html:hidden property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>

<table>
	<tr>
		<td><bean:message key="message.testBeginDate"/><bean:message key="message.dateFormat"/></td>
	</tr>
	<tr>
		<td><html:text name="infoDistributedTest" property="beginDateFormatted"/></td><td><span class="error"><html:errors property="testBeginDate"/></span></td>
	<tr/>
	<tr>
		<td><bean:message key="message.testBeginHour"/><bean:message key="message.hourFormat"/></td>
	</tr>
	<tr>
		<td><html:text name="infoDistributedTest" property="beginHourFormatted"/></td><td><span class="error"><html:errors property="testBeginHour"/></span></td>
	<tr/>
	<tr>
		<td><bean:message key="message.testEndDate"/><bean:message key="message.dateFormat"/></td>
	</tr>
	<tr>
		<td><html:text name="infoDistributedTest" property="endDateFormatted"/></td><td><span class="error"><html:errors property="testEndDate"/></span></td>
	<tr/>
	<tr>
		<td><bean:message key="message.testEndHour"/><bean:message key="message.hourFormat"/></td>
	</tr>
	<tr>
		<td><html:text name="infoDistributedTest" property="endHourFormatted"/></td><td><span class="error"><html:errors property="testEndHour"/></span></td>
	<tr/>
</table>
<br/>
<br/>
<table>
	<tr>
		<bean:define id="thisType" name="infoDistributedTest" property="testType.typeString"/>
		<bean:define id="thisTypeCode" name="infoDistributedTest" property="testType.type"/>
		<td><b><bean:message key="message.testType"/>:</b></td>
		<td>
		<html:select property="testType">
			<html:option value="<%=thisTypeCode.toString()%>"><bean:write name="thisType"/></html:option>
			<html:options collection="testTypeList" property="value" labelProperty="label"/>
		</html:select>
		</td>
	</tr>
	<tr>
		<bean:define id="thisCorrectionAvailability" name="infoDistributedTest" property="correctionAvailability.typeString"/>
		<bean:define id="thisCorrectionAvailabilityCode" name="infoDistributedTest" property="correctionAvailability.availability"/>
		<td><b><bean:message key="message.availableCorrection"/>:</b></td>
		<td>
			<html:select property="availableCorrection">
			<html:option value="<%=thisCorrectionAvailabilityCode.toString()%>"><bean:write name="thisCorrectionAvailability"/></html:option>
			<html:options collection="correctionAvailabilityList" property="value" labelProperty="label"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<bean:define id="thisStudentFeedback" name="infoDistributedTest" property="studentFeedback"/>
		<td><b><bean:message key="message.studentFeedback"/>:</b></td>
		<td>
			<html:select property="studentFeedback">
				<html:options collection="studentFeedbackList" property="value" labelProperty="label"/>
			</html:select>
		</td>
	</tr>
</table>
<br/>
<html:submit styleClass="inputbutton"><bean:message key="button.save"/></html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>  
</html:form>
</logic:notEqual>
