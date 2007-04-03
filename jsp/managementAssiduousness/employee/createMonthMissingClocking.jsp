<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.justifications" /></h2>

<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session.getAttribute(net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants.U_VIEW); %>
<logic:present name="yearMonth">
	<bean:define id="month" name="yearMonth" property="month" />
	<bean:define id="year" name="yearMonth" property="year" />

	<logic:present name="employee">
		<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
		<%if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) {%>
		<logic:equal name="yearMonth" property="isThisYearMonthClosed" value="false">
		
		<p class="mtop2">
			<span class="error0 mtop0">
				<html:messages id="errorMessage" message="true" property="errorMessage">
					<bean:write name="errorMessage" />
				</html:messages>
			</span>
		</p>	

		<div class="mbottom2">
		<fr:form action="/employeeAssiduousness.do?method=createMissingClockingMonth">			
		
			<fr:edit name="regularizationMonthFactory" visible="false"/>
			<fr:edit id="justificationMotive" name="regularizationMonthFactory"	schema="edit.employeeMonthRegularization">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle width40em"/>
					<fr:property name="columnClasses" value="width8em,,tdclear"/>				
				</fr:layout>
			</fr:edit>
			
			<table class="tstyle1 thlight width48em mtop1">
				<tr>
					<th><bean:message key="label.day"/></th>
					<th><bean:message key="label.weekDay"/></th>					
					<th colspan="4" align="center"><bean:message key="link.clockings"/></th>
				</tr>
				<logic:iterate indexId="index" id="regularizationDay" name="regularizationMonthFactory" property="regularizationList">
					<bean:define id="lineID" value="<%= "line" + index %>"/>
					<tr>
						<td>
							<bean:write name="regularizationDay" property="date.dayOfMonth"/>
						</td>
						<td>
							<bean:write name="regularizationDay" property="weekDay"/>
						</td>
						<td>
							<logic:notEmpty name="regularizationDay" property="firstTimeClock">
								<fr:view name="regularizationDay" property="firstTimeClock">
									<fr:layout name="hour-minute">
										<fr:property name="format" value="HH:mm" />
									</fr:layout>
								</fr:view>
							</logic:notEmpty>
							<logic:empty name="regularizationDay" property="firstTimeClock">
								<fr:edit id="<%= lineID + 1%>" name="regularizationDay" slot="firstTimeClockToFill">
									<fr:layout name="multiple-format-hour-minute">
										<fr:property name="format" value="HH:mm"/>
										<fr:property name="inputFormat" value="H:m"/>
										<fr:property name="formatText" value=""/>
										<fr:property name="size" value="6"/>
										<fr:property name="maxLength" value="6"/>										
									</fr:layout>
								</fr:edit>
							</logic:empty>			
						</td>
						<td>
							<logic:notEmpty name="regularizationDay" property="secondTimeClock">
								<fr:view name="regularizationDay" property="secondTimeClock">
									<fr:layout name="hour-minute">
										<fr:property name="format" value="HH:mm" />
									</fr:layout>
								</fr:view>
							</logic:notEmpty>
							<logic:empty name="regularizationDay" property="secondTimeClock">
								<fr:edit id="<%= lineID + 2%>" name="regularizationDay" slot="secondTimeClockToFill">
									<fr:layout name="multiple-format-hour-minute">
										<fr:property name="format" value="HH:mm" />
										<fr:property name="inputFormat" value="H:m"/>
										<fr:property name="formatText" value=""/>
										<fr:property name="size" value="6"/>
										<fr:property name="maxLength" value="6"/>										
									</fr:layout>
								</fr:edit>
							</logic:empty>
						</td>
						<td>
							<logic:notEmpty name="regularizationDay" property="thirdTimeClock">
								<fr:view name="regularizationDay" property="thirdTimeClock">
									<fr:layout name="hour-minute">
										<fr:property name="format" value="HH:mm" />
									</fr:layout>
								</fr:view>
							</logic:notEmpty>
							<logic:empty name="regularizationDay" property="thirdTimeClock">
								<fr:edit id="<%= lineID + 3%>" name="regularizationDay" slot="thirdTimeClockToFill">
									<fr:layout name="multiple-format-hour-minute">
										<fr:property name="format" value="HH:mm" />
										<fr:property name="inputFormat" value="H:m"/>
										<fr:property name="formatText" value=""/>
										<fr:property name="size" value="6"/>
										<fr:property name="maxLength" value="6"/>										
									</fr:layout>
								</fr:edit>
							</logic:empty>						
						</td>
						<td>
							<logic:notEmpty name="regularizationDay" property="fourthTimeClock">
								<fr:view name="regularizationDay" property="fourthTimeClock">
									<fr:layout name="hour-minute">
										<fr:property name="format" value="HH:mm" />
									</fr:layout>
								</fr:view>
							</logic:notEmpty>
							<logic:empty name="regularizationDay" property="fourthTimeClock">
								<fr:edit id="<%= lineID + 4%>" name="regularizationDay" slot="fourthTimeClockToFill">
									<fr:layout name="multiple-format-hour-minute">
										<fr:property name="format" value="HH:mm" />
										<fr:property name="inputFormat" value="H:m"/>
										<fr:property name="formatText" value=""/>
										<fr:property name="size" value="6"/>
										<fr:property name="maxLength" value="6"/>										
									</fr:layout>
								</fr:edit>
							</logic:empty>						
						</td>
						<td class="tdclear">
							<fr:hasMessages for="<%= lineID +1%>">
								<p><span class="error0"><fr:message for="<%= lineID +1%>" show="message"/></span></p>
							</fr:hasMessages>
							<fr:hasMessages for="<%= lineID +2%>">
								<p><span class="error0"><fr:message for="<%= lineID +2%>" show="message"/></span></p>
							</fr:hasMessages>
							<fr:hasMessages for="<%= lineID +3%>">
								<p><span class="error0"><fr:message for="<%= lineID +3%>" show="message"/></span></p>
							</fr:hasMessages>
							<fr:hasMessages for="<%= lineID +4%>">
								<p><span class="error0"><fr:message for="<%= lineID +4%>" show="message"/></span></p>
							</fr:hasMessages>
						</td>
					</tr>
				</logic:iterate>
				<tr>
					<td colspan="6">
						<html:submit>
							<bean:message key="button.confirm" />
						</html:submit>
					</td>
				</tr>
			</table>
		</fr:form>
		</div>

		</logic:equal>

		<%}%>
		<p><bean:message key="label.show" />: <html:link
			page="<%="/viewEmployeeAssiduousness.do?method=showWorkSheet&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
			<bean:message key="link.workSheet" />
		</html:link>, <html:link
			page="<%="/viewEmployeeAssiduousness.do?method=showSchedule&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
			<bean:message key="label.schedule" />
		</html:link>, <html:link
			page="<%="/viewEmployeeAssiduousness.do?method=showClockings&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
			<bean:message key="link.clockings" />
		</html:link>, <html:link
			page="<%="/viewEmployeeAssiduousness.do?method=showJustifications&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
			<bean:message key="link.justifications" />
		</html:link></p>


		<span class="toprint"><br />
		</span>
		<fr:view name="employee" schema="show.employeeInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="showinfo1 thbold" />
			</fr:layout>
		</fr:view>
	</logic:present>

	<logic:messagesPresent message="true">
		<html:messages id="message" message="true" property="message">
			<p><span class="error0"><bean:write name="message" /></span></p>
		</html:messages>
	</logic:messagesPresent>

	<bean:define id="employeeNumber" name="employee" property="employeeNumber"/>
	<div class="mvert1 invisible">
	<fr:form action="/viewEmployeeAssiduousness.do?method=showJustifications">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method"
			name="employeeForm" property="method" value="showJustifications" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.employeeNumber"
			name="employeeForm" property="employeeNumber" value="<%= employeeNumber.toString() %>" />
		<fr:edit id="yearMonth" name="yearMonth" schema="choose.date">
			<fr:layout>
				<fr:property name="classes" value="thlight thright" />
			</fr:layout>
		</fr:edit>
		<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			styleClass="invisible">
			<bean:message key="button.submit" />
		</html:submit></p>
	</fr:form></div>
		
	<div class="toprint">
	<p class="bold mbottom0"><bean:define id="month" name="yearMonth"
		property="month" /> <bean:message key="<%=month.toString()%>"
		bundle="ENUMERATION_RESOURCES" /> <bean:write name="yearMonth"
		property="year" /></p>
	<br />
	</div>

	<fr:view name="employeeStatusList" schema="show.employeeStatus">
		<fr:layout name="tabular">
			<fr:property name="classes" value="showinfo1 thbold" />
		</fr:layout>
	</fr:view>

<logic:present name="justifications">
	<logic:empty name="justifications">
		<p>
			<em><bean:message key="message.employee.noJustifications" /></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="justifications">
		<fr:view name="justifications" schema="show.justifications.management">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder" />
				<fr:property name="columnClasses" value="acenter" />
				<fr:property name="headerClasses" value="acenter" />
				<%if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) {%>
				<logic:equal name="yearMonth" property="isThisYearMonthClosed" value="false">
	                <fr:property name="link(edit)" value="<%="/employeeAssiduousness.do?method=prepareEditEmployeeJustification&month="+month.toString()+"&year="+year.toString()%>" />
					<fr:property name="key(edit)" value="label.edit" />
					<fr:property name="param(edit)" value="idInternal" />
					<fr:property name="bundle(edit)" value="ASSIDUOUSNESS_RESOURCES" />
					<fr:property name="link(delete)" value="<%="/employeeAssiduousness.do?method=deleteEmployeeJustification&month="+month.toString()+"&year="+year.toString()%>" />
					<fr:property name="key(delete)" value="label.delete" />
					<fr:property name="param(delete)" value="idInternal" />
					<fr:property name="bundle(delete)" value="ASSIDUOUSNESS_RESOURCES" />
				</logic:equal>
                <%}%>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
</logic:present>
<logic:notPresent name="justifications">
	<p>
		<em><bean:message key="message.employee.noJustifications" /></em>
	</p>
</logic:notPresent>
