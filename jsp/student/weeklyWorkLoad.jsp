<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="link.weekly.work.load"/></h2>

<br/>

<p><span class="error"><html:errors/></span></p>

<html:form action="/weeklyWorkLoad.do">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="page" value="0"/>

	<html:select property="executionPeriodID" onchange="this.form.submit();">
		<html:options collection="executionPeriods" property="idInternal" labelProperty="qualifiedName"/>
	</html:select>
</html:form>

<br/>

<logic:present name="weeklyWorkLoadView">
<logic:present name="selectedExecutionPeriod" property="previousWeek">
	<bean:define id="previousWeek" name="selectedExecutionPeriod" property="previousWeek"/>
	<table class="style1">
		<tr>
			<td colspan="4" class="listClasses-header">
				<bean:message key="title.weekly.work.load.week.to.submit"/>:
				<bean:define id="start" type="org.joda.time.DateTime" name="previousWeek" property="start"/>
				<bean:define id="end" type="org.joda.time.DateTime" name="previousWeek" property="end"/>				
				[<bean:write name="start" property="year"/>-<bean:write name="start" property="monthOfYear"/>-<bean:write name="start" property="dayOfMonth"/>,
				 <bean:write name="end" property="year"/>-<bean:write name="end" property="monthOfYear"/>-<bean:write name="end" property="dayOfMonth"/>[
			</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="title.execution.course"/>
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
			<logic:notPresent name="attend" property="weeklyWorkLoadOfPreviousWeek">
				<tr>
					<html:form action="/weeklyWorkLoad.do">
						<html:hidden property="method" value="createFromForm"/>
						<html:hidden property="page" value="1"/>
						<bean:define id="attendsID" type="java.lang.Integer" name="attend" property="idInternal"/>
						<html:hidden property="attendsID" value="<%= attendsID.toString() %>"/>
						<html:hidden property="executionPeriodID"/>

						<td class="listClasses">
							<bean:write name="attend" property="disciplinaExecucao.nome"/>
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
			</logic:notPresent>
		</logic:iterate>
	</table>
</logic:present>
</logic:present>

<br/>

<logic:present name="weeklyWorkLoadView">
	<table class="style1">
		<tr>
			<td class="listClasses-header" rowspan="2">
				<bean:message key="title.execution.course"/>
			</td>
			<td class="listClasses-header" rowspan="2">
			</td>
			<bean:size id="numberOfIntervals" name="weeklyWorkLoadView" property="intervals"/>
			<td class="listClasses-header" colspan="<%= numberOfIntervals %>">
				<bean:message key="title.weekly.work.load.week"/>
			</td>
			<td class="listClasses-header" rowspan="2">
				<bean:message key="title.weekly.work.load.total"/>
			</td>
		</tr>
		<tr>
			<logic:iterate id="interval" indexId="i" type="org.joda.time.Interval" name="weeklyWorkLoadView" property="intervals">
				<bean:define id="intervalString" type="java.lang.String">
					<bean:define id="start" type="org.joda.time.DateTime" name="interval" property="start"/>
					<bean:define id="end" type="org.joda.time.DateTime" name="interval" property="end"/>				
					[<bean:write name="start" property="year"/>-<bean:write name="start" property="monthOfYear"/>-<bean:write name="start" property="dayOfMonth"/>, <bean:write name="end" property="year"/>-<bean:write name="end" property="monthOfYear"/>-<bean:write name="end" property="dayOfMonth"/>[
				</bean:define>
				<td class="listClasses-header" title="<%= intervalString %>">
					<%= i.intValue() + 1 %>
				</td>
			</logic:iterate>
		</tr>

		<logic:iterate id="weeklyWorkLoadEntry" name="weeklyWorkLoadView" property="weeklyWorkLoadMap">
			<tr>
				<td class="listClasses-header" rowspan="4">
					<bean:write name="weeklyWorkLoadEntry" property="key.disciplinaExecucao.nome"/>
				</td>
				<td class="courses">
					<bean:message key="title.weekly.work.load.contact"/>
				</td>
				<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value">
					<td class="listClasses">
						<logic:present name="weeklyWorkLoad">
							<bean:write name="weeklyWorkLoad" property="contact"/>
						</logic:present>
					</td>
				</logic:iterate>
				<td class="listClasses">
					<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value" length="1">
						<logic:present name="weeklyWorkLoad">
							<strong>
								<bean:write name="weeklyWorkLoad" property="attends.weeklyWorkLoadContact"/>
							</strong>
						</logic:present>
					</logic:iterate>
				</td>
			</tr>
			<tr>
				<td class="courses">
					<bean:message key="title.weekly.work.load.autonomousStudy"/>
				</td>
				<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value">
					<td class="listClasses">
						<logic:present name="weeklyWorkLoad">
							<bean:write name="weeklyWorkLoad" property="autonomousStudy"/>
						</logic:present>
					</td>
				</logic:iterate>
				<td class="listClasses">
					<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value" length="1">
						<logic:present name="weeklyWorkLoad">
							<strong>
								<bean:write name="weeklyWorkLoad" property="attends.weeklyWorkLoadAutonomousStudy"/>
							</strong>
						</logic:present>
					</logic:iterate>
				</td>
			</tr>
			<tr>
				<td class="courses">
					<bean:message key="title.weekly.work.load.other"/>
				</td>
				<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value">
					<td class="listClasses">
						<logic:present name="weeklyWorkLoad">
							<bean:write name="weeklyWorkLoad" property="other"/>
						</logic:present>
					</td>
				</logic:iterate>
				<td class="listClasses">
					<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value" length="1">
						<logic:present name="weeklyWorkLoad">
							<strong>
								<bean:write name="weeklyWorkLoad" property="attends.weeklyWorkLoadOther"/>
							</strong>
						</logic:present>
					</logic:iterate>
				</td>
			</tr>
			<tr>
				<td class="courses">
					<strong>
						<bean:message key="title.weekly.work.load.total"/>
					</strong>
				</td>
				<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value">
					<td class="listClasses">
						<logic:present name="weeklyWorkLoad">
							<strong>
								<bean:write name="weeklyWorkLoad" property="total"/>
							</strong>
						</logic:present>
					</td>
				</logic:iterate>
				<td class="listClasses">
					<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value" length="1">
						<logic:present name="weeklyWorkLoad">
							<strong>
								<bean:write name="weeklyWorkLoad" property="attends.weeklyWorkLoadTotal"/>
							</strong>
						</logic:present>
					</logic:iterate>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>


<%--
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
--%>