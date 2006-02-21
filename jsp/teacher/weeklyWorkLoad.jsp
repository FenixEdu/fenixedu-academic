<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:write name="siteView" property="commonComponent.executionCourse.nome"/></h2>
<h3>
	<bean:write name="executionCourse" property="executionPeriod.semester"/>
	<bean:message key="label.semester"/>
	<bean:write name="executionCourse" property="executionPeriod.executionYear.year"/>
</h3>

<br/>
<br/>

<logic:present name="weeklyWorkLoadView">
	<table class="style1">
		<tr>
			<logic:iterate id="interval" type="org.joda.time.Interval" name="weeklyWorkLoadView" property="intervals">
				<td class="listClasses-header">
					<bean:define id="start" type="org.joda.time.DateTime" name="interval" property="start"/>
					<bean:define id="end" type="org.joda.time.DateTime" name="interval" property="end"/>				
					[<bean:write name="start" property="year"/>-<bean:write name="start" property="monthOfYear"/>-<bean:write name="start" property="dayOfMonth"/>,
					 <bean:write name="end" property="year"/>-<bean:write name="end" property="monthOfYear"/>-<bean:write name="end" property="dayOfMonth"/>[
				</td>
			</logic:iterate>
		</tr>

		<logic:iterate id="weeklyWorkLoadArray" name="weeklyWorkLoadView" property="weeklyWorkLoadArrays">
			<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadArray">
				<tr>
					<td rowspan="3">
						1
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td>
					</td>
				</tr>
				<tr>
					<td>
					</td>
				</tr>
			</logic:iterate>		
		</logic:iterate>
	</table>
</logic:present>