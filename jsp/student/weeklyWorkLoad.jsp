<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/joda.tld" prefix="joda" %>

<h2><bean:message key="link.weekly.work.load"/></h2>

<br/>

<html:form action="/weeklyWorkLoad.do">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="page" value="1"/>

	<html:select property="executionPeriodID" onchange="this.form.submit();">
		<html:options collection="executionPeriods" property="idInternal" labelProperty="qualifiedName"/>
	</html:select>
</html:form>

<br/>

<table class="style1">
	<tr>
		<td colspan="4" class="listClasses-header">
			<bean:message key="title.execution.course"/>
		</td>
		<td>
		</td>
	</tr>
	<tr>
		<td class="listClasses-header">
			<bean:message key="title.weekly.work.load.week"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="title.weekly.work.load.contact"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="title.weekly.work.load.autonomousStudy"/>
		</td>
		<td class="listClasses-header">
			<bean:message key="title.weekly.work.load.other"/>
		</td>
	</tr>
<logic:iterate id="attend" name="attends">
	<tr>
		<td colspan="4" class="courses">
			<bean:write name="attend" property="disciplinaExecucao.nome"/>
		</td>
		<td>
		</td>
	</tr>
	<logic:iterate id="weeklyWorkLoad" name="attend" property="sortedWeeklyWorkLoads">
		<tr>
			<td class="listClasses">
				<bean:define id="interval" type="org.joda.time.Interval" name="weeklyWorkLoad" property="interval"/>
				<bean:define id="start" type="org.joda.time.DateTime" name="interval" property="start"/>
				<bean:define id="end" type="org.joda.time.DateTime" name="interval" property="end"/>				
				[<bean:write name="start" property="year"/>-<bean:write name="start" property="monthOfYear"/>-<bean:write name="start" property="dayOfMonth"/>,
				 <bean:write name="end" property="year"/>-<bean:write name="end" property="monthOfYear"/>-<bean:write name="end" property="dayOfMonth"/>[
			</td>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoad" property="contact"/>
			</td>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoad" property="autonomousStudy"/>
			</td>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoad" property="other"/>
			</td>
			<td>
			</td>
		</tr>		
	</logic:iterate>
	<tr>
		<html:form action="/weeklyWorkLoad.do">
			<html:hidden property="method" value="createFromForm"/>
			<html:hidden property="page" value="1"/>
			<bean:define id="attendsID" type="java.lang.Integer" name="attend" property="idInternal"/>
			<html:hidden property="attendsID" value="<%= attendsID.toString() %>"/>

			<td class="listClasses">
			</td>
			<td class="listClasses">
				<html:text size="3" property="contact"/>
			</td>
			<td class="listClasses">
				<html:text size="3" property="autonomousStudy"/>
			</td>
			<td class="listClasses">
				<html:text size="3" property="other"/>
			</td>
			<td>
				<html:submit value="Submit"/>
			</td>
		</html:form>
	</tr>
</logic:iterate>
</table>

<br/>
<br/>
<br/>

<logic:iterate id="attend" name="attends">
	<fr:view name="attend" property="disciplinaExecucao" layout="format">
		<fr:layout>
			<fr:property name="format" value="${nome}"/>
		</fr:layout>
	</fr:view>
	<br/>
	<fr:view name="attend" property="weeklyWorkLoads">
		<fr:layout name="tabular">
			<fr:property name="format" value="${weekOffset}"/>
			<fr:property name="format" value="${contact}"/>
			<fr:property name="format" value="${autonomousStudy}"/>
			<fr:property name="format" value="${other}"/>
		</fr:layout>
	</fr:view>
	<br/>
	<fr:edit name="weeklyWorkLoadBean" action="/weeklyWorkLoad.do?method=create" schema="weekly.work.load.bean">
		<fr:hidden slot="attendsID" name="attend" property="idInternal"/>
	</fr:edit>
	<br/>
	<br/>
</logic:iterate>

