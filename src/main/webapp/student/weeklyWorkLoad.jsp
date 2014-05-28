<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="link.weekly.work.load"/></h2>

<div class="infoop2">
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
</div>

<br/>

<html:form action="/weeklyWorkLoad.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepare"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<bean:message key="label.semester"/>: 
	<html:select bundle="HTMLALT_RESOURCES" property="executionPeriodID" onchange="this.form.submit();">
		<html:options collection="executionPeriods" property="externalId" labelProperty="qualifiedName"/>
	</html:select>
<%--
	<logic:equal name="selectedExecutionPeriod" property="state.stateCode" value="C">
		<html:select bundle="HTMLALT_RESOURCES" property="weekOffset" onchange="this.form.submit();">
			<html:options collection="weeks" property="weekOffset" labelProperty="presentationName"/>
		</html:select>
	</logic:equal>
--%>
</html:form>

<p><!-- Error messages go here --><html:errors/></p>


<bean:define id="contact_tooltip" type="java.lang.String"><bean:message key="title.weekly.work.load.contact.tooltip"/></bean:define>
<bean:define id="autonomousStudy_tooltip" type="java.lang.String"><bean:message key="title.weekly.work.load.autonomousStudy.tooltip"/></bean:define>
<bean:define id="other_tooltip" type="java.lang.String"><bean:message key="title.weekly.work.load.other.tooltip"/></bean:define>

<bean:define id="submitConfirm" type="java.lang.String">
	return confirm('<bean:message key="message.confirm.submit.weekly.work.load"/>')
</bean:define>

<logic:notPresent name="firstAttends" property="responseWeek">
	<table class="tstyle3">
		<tr>
			<th colspan="4">
				<bean:message key="title.weekly.work.load.week"/>:
			</th>
		</tr>
		<tr>
			<th>
				<bean:message key="title.execution.course"/>
			</th>
			<th title="<%= contact_tooltip %>">
				<bean:message key="title.weekly.work.load.contact"/>
			</th>
			<th title="<%= autonomousStudy_tooltip %>">
				<bean:message key="title.weekly.work.load.autonomousStudy"/>
			</th>
			<th title="<%= other_tooltip %>">
				<bean:message key="title.weekly.work.load.other"/>
			</th>
		</tr>
		<tr>
			<td colspan="4">
				<em><bean:message key="no.previouse.response.period"/></em>
			</td>
		</tr>
	</table>
</logic:notPresent>

<p class="mbottom0">
	<em><bean:message key="label.attention.nonCaps"/>: <bean:message key="label.attention.text"/></em>
</p>

<logic:present name="firstAttends" property="responseWeek">
	<bean:define id="previousWeek" name="firstAttends" property="responseWeek"/>
	<table class="tstyle3 mbottom0">
		<tr>
			<th colspan="5">
				<bean:message key="title.weekly.work.load.week"/>
				<bean:define id="start" type="org.joda.time.DateTime" name="previousWeek" property="start"/>
				<bean:define id="end" type="org.joda.time.DateTime" name="previousWeek" property="end"/>				
				<bean:write name="firstAttends" property="calculatePreviousWeek"/>
				<span style="font-weight: normal;">
					(<bean:message key="label.from"/>
					<bean:write name="start" property="year"/>-<bean:write name="start" property="monthOfYear"/>-<bean:write name="start" property="dayOfMonth"/>
					<bean:message key="label.to"/>
					<bean:write name="end" property="year"/>-<bean:write name="end" property="monthOfYear"/>-<bean:write name="end" property="dayOfMonth"/>)
				</span>
			</th>
		</tr>
		<tr>
			<th style="width: 300px">
				<bean:message key="title.execution.course"/>
			</th>
			<th  title="<%= contact_tooltip %>" style="width: 100px">
				<bean:message key="title.weekly.work.load.contact"/><br/><span style="font-weight: normal;"><bean:message key="label.weekly.work.load.hoursperweek"/></span>
			</th>
			<th title="<%= autonomousStudy_tooltip %>" style="width: 100px">
				<bean:message key="title.weekly.work.load.autonomousStudy"/><br/><span style="font-weight: normal;"><bean:message key="label.weekly.work.load.hoursperweek"/></span>
			</th>
			<th title="<%= other_tooltip %>" style="width: 100px">
				<bean:message key="title.weekly.work.load.other"/><br/><span style="font-weight: normal;"><bean:message key="label.weekly.work.load.hoursperweek"/></span>
			</th>
			<th style="width: 100px">
			</th>
		</tr>

</table>					

		<table class="tstyle3 mtop0 tdcenter">				
		<logic:iterate id="attend" name="attends">

		<logic:notPresent name="attend" property="weeklyWorkLoadOfPreviousWeek">

	<html:form action="/weeklyWorkLoad.do">

				 <bean:define id="attendsID" type="java.lang.String" name="attend" property="externalId"/>		
				 <tr class="dnone">
					<td >
				 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createFromForm"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/> 		
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.attendsID" property="attendsID" value="<%= attendsID.toString() %>"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodID" property="executionPeriodID"/>	
					</td>
					<td></td>
					<td></td>
					<td></td>					
					<td></td>
				 </tr>

				<tr>
					<td style="width: 300px">
						<bean:write name="attend" property="disciplinaExecucao.nome"/>
					</td>
					<td title="<%= contact_tooltip %>" style="width: 100px">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.contact" size="3" maxlength="3" property="contact"/>
					</td>
					<td  title="<%= autonomousStudy_tooltip %>" style="width: 100px">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.autonomousStudy" size="3" maxlength="3" property="autonomousStudy"/>
					</td>
					<td title="<%= other_tooltip %>" style="width: 100px">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.other" size="3" maxlength="3" property="other"/>
					</td>
					<td style="width: 100px">
			
						<html:submit bundle="HTMLALT_RESOURCES" altKey='submit.submit' onclick='<%= submitConfirm %>'>
							<bean:message key="button.submit"/>
						</html:submit>

					</td>
				</tr>

</html:form>					

		</logic:notPresent>
	</logic:iterate>
</table>	
</logic:present>









<logic:present name="weeklyWorkLoadView">
	<table class="tstyle3 tpadding01">
		<tr>
			<th rowspan="2">
				<bean:message key="title.execution.course"/>
			</th>
			<th rowspan="2">
			</th>
			<bean:size id="numberOfIntervals" name="weeklyWorkLoadView" property="intervals"/>
			<th colspan="<%= numberOfIntervals %>">
				<bean:message key="title.weekly.work.load.week"/>
			</th>
			<th rowspan="2" style="padding: 1.5em 1.5em;">
				<bean:message key="title.weekly.work.load.total"/>
			</th>
		</tr>
		<tr>
			<logic:iterate id="interval" indexId="i" type="org.joda.time.Interval" name="weeklyWorkLoadView" property="intervals">
				<bean:define id="intervalString" type="java.lang.String">
					<bean:define id="start" type="org.joda.time.DateTime" name="interval" property="start"/>
					<bean:define id="end" type="org.joda.time.DateTime" name="interval" property="end"/>
					(<bean:message key="label.from"/> <bean:write name="start" property="year"/>-<bean:write name="start" property="monthOfYear"/>-<bean:write name="start" property="dayOfMonth"/> <bean:message key="label.to"/> <bean:write name="end" property="year"/>-<bean:write name="end" property="monthOfYear"/>-<bean:write name="end" property="dayOfMonth"/>)
				</bean:define>
				<th title="<%= intervalString %>" style="width: auto; ">
					<%= i.intValue() + 1 %>
				</th>
			</logic:iterate>
		</tr>

		<logic:iterate id="weeklyWorkLoadEntry" name="weeklyWorkLoadView" property="weeklyWorkLoadMap">
			<tr>
				<th rowspan="4" style="padding: 0.5em 0.5em;">
					<bean:write name="weeklyWorkLoadEntry" property="key.disciplinaExecucao.nome"/>
				</th>
				<td class="" title="<%= contact_tooltip %>" style="padding: 0.5em 0.5em;">
					<bean:message key="title.weekly.work.load.contact"/>
				</td>
				<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value">
					<td>
						<logic:present name="weeklyWorkLoad">
							<bean:write name="weeklyWorkLoad" property="contact"/>
						</logic:present>
					</td>
				</logic:iterate>
				<td class="highlight2">
					<bean:define id="contactWrittenValue" value=""/>
					<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value">
						<logic:present name="weeklyWorkLoad">						
							<logic:empty name="contactWrittenValue">
								<bean:define id="contactWrittenValue" value="alreadyWritten"/>
								<strong>
									<bean:write name="weeklyWorkLoad" property="attends.weeklyWorkLoadContact"/>
								</strong>
							</logic:empty>
						</logic:present>
					</logic:iterate>
				</td>
			</tr>
			<tr>
				<td class="" title="<%= autonomousStudy_tooltip %>" style="padding: 0.5em 0.5em;">
					<bean:message key="title.weekly.work.load.autonomousStudy"/>
				</td>
				<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value">
					<td>
						<logic:present name="weeklyWorkLoad">
							<bean:write name="weeklyWorkLoad" property="autonomousStudy"/>
						</logic:present>
					</td>
				</logic:iterate>
				<td class="highlight2">
					<bean:define id="autonomoustWrittenValue" value=""/>
					<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value">
						<logic:present name="weeklyWorkLoad">
							<logic:empty name="autonomoustWrittenValue">
								<bean:define id="autonomoustWrittenValue" value="alreadyWritten"/>
								<strong>
									<bean:write name="weeklyWorkLoad" property="attends.weeklyWorkLoadAutonomousStudy"/>
								</strong>
							</logic:empty>
						</logic:present>
					</logic:iterate>
				</td>
			</tr>
			<tr>
				<td class="" title="<%= other_tooltip %>" style="padding: 0.5em 0.5em;">
					<bean:message key="title.weekly.work.load.other"/>
				</td>
				<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value">
					<td>
						<logic:present name="weeklyWorkLoad">
							<bean:write name="weeklyWorkLoad" property="other"/>
						</logic:present>
					</td>
				</logic:iterate>
				<td class="highlight2">
					<bean:define id="otherWrittenValue" value=""/>
					<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value">
						<logic:present name="weeklyWorkLoad">
							<logic:empty name="otherWrittenValue">
								<bean:define id="otherWrittenValue" value="alreadyWritten"/>
								<strong>
									<bean:write name="weeklyWorkLoad" property="attends.weeklyWorkLoadOther"/>
								</strong>
							</logic:empty>
						</logic:present>
					</logic:iterate>
				</td>
			</tr>
			<tr>
				<td class="highlight2" style="padding: 0.5em 0.5em;">
					<strong>
						<bean:message key="title.weekly.work.load.total"/>
					</strong>
				</td>
				<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value">
					<td class="highlight2">
						<logic:present name="weeklyWorkLoad">
							<strong>
								<bean:write name="weeklyWorkLoad" property="total"/>
							</strong>
						</logic:present>
					</td>
				</logic:iterate>
				<td class="highlight2">
					<bean:define id="totalWrittenValue" value=""/>
					<logic:iterate id="weeklyWorkLoad" name="weeklyWorkLoadEntry" property="value">
						<logic:present name="weeklyWorkLoad">
							<logic:empty name="totalWrittenValue">
								<bean:define id="totalWrittenValue" value="alreadyWritten"/>
								<strong>
									<bean:write name="weeklyWorkLoad" property="attends.weeklyWorkLoadTotal"/>
								</strong>
							</logic:empty>
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
		<fr:hidden slot="attendsID" name="attend" property="externalId"/>
	</fr:edit>
	<br/>
	<br/>
</logic:iterate>
--%>