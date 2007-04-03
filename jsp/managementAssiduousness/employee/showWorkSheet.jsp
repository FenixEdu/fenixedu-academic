<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.workSheet" /></h2>


<logic:present name="employeeWorkSheet">
	<logic:present name="yearMonth">
		<bean:define id="month" name="yearMonth" property="month" />
		<bean:define id="year" name="yearMonth" property="year" />
		<bean:define id="employeeNumber" name="employeeWorkSheet" property="employee.employeeNumber" />

		<%net.sourceforge.fenixedu.applicationTier.IUserView user = (net.sourceforge.fenixedu.applicationTier.IUserView) session
                    .getAttribute(net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants.U_VIEW);
            if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) {
                %>
       <logic:equal name="yearMonth" property="isThisYearMonthClosed" value="false">
		<logic:present name="employeeJustificationFactory">
		<span class="error0 mtop0"><html:messages id="errorMessage" message="true" property="errorMessage">
			<bean:write name="errorMessage" />
			<br />
		</html:messages></span>
			<bean:define id="method" value="editEmployeeJustification" />
			<fr:form action="<%="/employeeAssiduousness.do?method="+method.toString()%>" encoding="multipart/form-data">
				<fr:edit id="editEmployeeJustificationType" name="employeeJustificationFactory"
					type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
					schema="edit.employeeCorrectionType">
					<fr:destination name="justificationMotivePostBack" path="/employeeAssiduousness.do?method=chooseJustificationMotivePostBack" />
					<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
						<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
					</fr:layout>
				</fr:edit>
				<logic:notEmpty name="employeeJustificationFactory" property="correctionType">
					<logic:equal name="employeeJustificationFactory" property="correctionType" value="<%= net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.CorrectionType.JUSTIFICATION.toString()%>">
						<fr:edit id="editEmployeeJustificationType" name="employeeJustificationFactory"
							type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
							schema="edit.employeeJustificationType">
							<fr:destination name="justificationMotivePostBack" path="/employeeAssiduousness.do?method=chooseJustificationMotivePostBack" />
							<fr:layout>
								<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
								<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
							</fr:layout>
						</fr:edit>
						<logic:notEmpty name="employeeJustificationFactory" property="justificationType">
							<logic:equal name="employeeJustificationFactory" property="justificationType" value="<%= net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType.TIME.toString()%>">
								<fr:edit id="editEmployeeJustificationMotive" name="employeeJustificationFactory"
									type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
									schema="edit.employeeTimeJustificationMotive">
									<fr:layout>
										<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
										<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
									</fr:layout>
								</fr:edit>
							</logic:equal>
							<logic:equal name="employeeJustificationFactory" property="justificationType" value="<%= net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType.OCCURRENCE.toString()%>">
								<fr:edit id="editEmployeeJustificationMotive" name="employeeJustificationFactory"
									type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
									schema="edit.employeeOccurrenceJustificationMotive">
									<fr:layout>
										<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
										<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
									</fr:layout>									
								</fr:edit>
							</logic:equal>
							<logic:equal name="employeeJustificationFactory" property="justificationType" value="<%= net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType.BALANCE.toString()%>">
								<fr:edit id="editEmployeeJustificationMotive" name="employeeJustificationFactory"
									type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
									schema="edit.employeeBalanceJustificationMotive">
									<fr:layout>
										<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
										<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
									</fr:layout>									
								</fr:edit>
							</logic:equal>
							<logic:equal name="employeeJustificationFactory" property="justificationType" value="<%= net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType.MULTIPLE_MONTH_BALANCE.toString()%>">
								<fr:edit id="editEmployeeJustificationMotive" name="employeeJustificationFactory"
									type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
									schema="edit.employeeOccurrenceJustificationMotive">
									<fr:layout>
										<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
										<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
									</fr:layout>
								</fr:edit>
							</logic:equal>
							<p><html:submit>
								<bean:message key="button.submit" />
							</html:submit></p>
						</logic:notEmpty>
					</logic:equal>
					<logic:equal name="employeeJustificationFactory" property="correctionType" value="<%= net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.CorrectionType.REGULARIZATION.toString()%>">
						<fr:edit id="editEmployeeJustificationMotive" name="employeeJustificationFactory"
							type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
							schema="edit.employeeRegularization">
							<fr:layout>
								<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
								<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
							</fr:layout>							
						</fr:edit>
						<p><html:submit>
							<bean:message key="button.submit" />
						</html:submit></p>
					</logic:equal>
				</logic:notEmpty>		
			</fr:form>
		<br/>
		<br/>
		</logic:present>
		</logic:equal>
	 	<%}%>			
			
		<p><bean:message key="label.show"/>: <html:link
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
		<fr:view name="employeeWorkSheet" property="employee" schema="show.employeeInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="showinfo1 thbold" />
			</fr:layout>
		</fr:view>

		<logic:messagesPresent message="true">
			<html:messages id="message" message="true" property="message">
				<p><span class="error0"><bean:write name="message" /></span></p>
			</html:messages>
		</logic:messagesPresent>
		
		<div class="mvert1 invisible">
		<fr:form action="/viewEmployeeAssiduousness.do">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method"
				name="employeeForm" property="method" value="showWorkSheet" />
			<html:hidden bundle="HTMLALT_RESOURCES"
				altKey="hidden.employeeNumber" name="employeeForm"
				property="employeeNumber" value="<%= employeeNumber.toString() %>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page"
				name="employeeForm" property="page" value="0" />
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
		
	<logic:empty name="employeeWorkSheet" property="workDaySheetList">
		<p>
			<em><bean:message key="message.employee.noWorkSheet" /></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="employeeWorkSheet" property="workDaySheetList">
		<fr:view name="employeeWorkSheet" property="workDaySheetList"
			schema="show.workDaySheet.management">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder tpadding1" />
				<fr:property name="columnClasses"
					value="bgcolor3 acenter,,acenter,aright,aright,aleft,aleft,acenter invisible" />
				<fr:property name="headerClasses" value="acenter" />
				<%if (net.sourceforge.fenixedu.domain.ManagementGroups.isAssiduousnessManagerMember(user.getPerson())) { %>
                <logic:equal name="yearMonth" property="isThisYearMonthClosed" value="false">
	                <fr:property name="link(justification)" value="<%="/employeeAssiduousness.do?method=prepareCreateEmployeeJustification&correction=JUSTIFICATION&employeeNumber="+employeeNumber.toString()%>" />
					<fr:property name="key(justification)" value="label.justification" />
					<fr:property name="param(justification)" value="date" />
					<fr:property name="bundle(justification)" value="ASSIDUOUSNESS_RESOURCES" />
	                <fr:property name="link(regularization)" value="<%="/employeeAssiduousness.do?method=prepareCreateEmployeeJustification&correction=REGULARIZATION&employeeNumber="+employeeNumber.toString()%>" />
					<fr:property name="key(regularization)" value="label.regularization" />
					<fr:property name="param(regularization)" value="date" />
					<fr:property name="bundle(regularization)" value="ASSIDUOUSNESS_RESOURCES" />
				</logic:equal>
                <%}%>
			</fr:layout>
		</fr:view>

		<logic:notEmpty name="displayCurrentDayNote">
			<bean:message key="message.employee.currentDayIgnored" />
		</logic:notEmpty>

		<logic:present name="employeeWorkSheet" property="totalBalance">
			<p class="mvert05"><bean:message key="label.totalBalance" />: <b><bean:write
				name="employeeWorkSheet" property="totalBalanceString" /></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="unjustifiedBalance">
			<p class="mvert05"><bean:message key="label.totalUnjustified" />: <b><bean:write
				name="employeeWorkSheet" property="unjustifiedBalanceString" /></b></p>
		</logic:present>
		<%--		<logic:present name="employeeWorkSheet" property="complementaryWeeklyRest">
			<p class="mvert05"><bean:message key="label.totalComplementaryWeeklyRest" />: <b><bean:write name="employeeWorkSheet" property="complementaryWeeklyRestString"/></b></p>
		</logic:present>
		<logic:present name="employeeWorkSheet" property="weeklyRest">
			<p class="mvert05"><bean:message key="label.totalWeeklyRest" />: <b><bean:write name="employeeWorkSheet" property="weeklyRestString"/></b></p>
		</logic:present> --%>
	</logic:notEmpty>
	</logic:present>
</logic:present>

<logic:notPresent name="employeeWorkSheet">
	<p>
		<em><bean:message key="message.employee.noWorkSheet" /></em>
	</p>
</logic:notPresent>
