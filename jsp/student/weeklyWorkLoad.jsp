<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<style>
.instructions {
background-color: #fafadd;
border: 1px solid #ccc;
padding: 0.5em;
float: left;
}
</style>

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

<bean:define id="contact_tooltip" type="java.lang.String"><bean:message key="title.weekly.work.load.contact.tooltip"/></bean:define>
<bean:define id="autonomousStudy_tooltip" type="java.lang.String"><bean:message key="title.weekly.work.load.autonomousStudy.tooltip"/></bean:define>
<bean:define id="other_tooltip" type="java.lang.String"><bean:message key="title.weekly.work.load.other.tooltip"/></bean:define>

<table width="80em">
	<tr><td width="65em">
		<logic:notPresent name="firstAttends" property="responseWeek">
			<table class="style1">
				<tr>
					<td colspan="4" class="listClasses-header">
						<bean:message key="title.weekly.work.load.week.to.submit"/>:
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td class="listClasses-header">
						<bean:message key="title.execution.course"/>
					</td>
					<td class="listClasses-header" title="<%= contact_tooltip %>">
						<bean:message key="title.weekly.work.load.contact"/>
					</td>
					<td class="listClasses-header" title="<%= autonomousStudy_tooltip %>">
						<bean:message key="title.weekly.work.load.autonomousStudy"/>
					</td>
					<td class="listClasses-header" title="<%= other_tooltip %>">
						<bean:message key="title.weekly.work.load.other"/>
					</td>
				</tr>
				<tr>
					<td class="listClasses" colspan="4">
						<bean:message key="no.previouse.response.period"/>
					</td>
				</tr>
			</table>
		</logic:notPresent>

		<logic:present name="firstAttends" property="responseWeek">
			<bean:define id="previousWeek" name="firstAttends" property="responseWeek"/>
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
					<td class="listClasses-header" title="<%= contact_tooltip %>">
						<bean:message key="title.weekly.work.load.contact"/>
					</td>
					<td class="listClasses-header" title="<%= autonomousStudy_tooltip %>">
						<bean:message key="title.weekly.work.load.autonomousStudy"/>
					</td>
					<td class="listClasses-header" title="<%= other_tooltip %>">
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
								<td class="listClasses" title="<%= contact_tooltip %>">
									<html:text size="3" property="contact"/>
								</td>
								<td class="listClasses"  title="<%= autonomousStudy_tooltip %>">
									<html:text size="3" property="autonomousStudy"/>
								</td>
								<td class="listClasses" title="<%= other_tooltip %>">
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
	</td><td class="instructions" width="35em">
		<logic:present name="previousWeek">
			<bean:define id="start" type="org.joda.time.DateTime" name="previousWeek" property="start"/>
			<bean:define id="end" type="org.joda.time.DateTime" name="previousWeek" property="end"/>
			<bean:define id="intervalArg" type="java.lang.String">
				[<bean:write name="start" property="year"/>-<bean:write name="start" property="monthOfYear"/>-<bean:write name="start" property="dayOfMonth"/>,
			 	<bean:write name="end" property="year"/>-<bean:write name="end" property="monthOfYear"/>-<bean:write name="end" property="dayOfMonth"/>[
			</bean:define>
			<bean:message key="title.weekly.work.load.instructions.header" arg0="<%= intervalArg %>"/>
		</logic:present>
		<logic:notPresent name="previousWeek">
			<bean:message key="title.weekly.work.load.instructions.header" arg0=""/>
		</logic:notPresent>
		<ul>
			<li><bean:message key="title.weekly.work.load.contact.tooltip"/></li>
			<li><bean:message key="title.weekly.work.load.autonomousStudy.tooltip"/></li>
			<li><bean:message key="title.weekly.work.load.other.tooltip"/></li>
		</ul>
		<bean:message key="title.weekly.work.load.instructions.footer"/>
	</tr></td>
</table>

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
				<td class="courses" title="<%= contact_tooltip %>">
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
				<td class="courses" title="<%= autonomousStudy_tooltip %>">
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
				<td class="courses" title="<%= other_tooltip %>">
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