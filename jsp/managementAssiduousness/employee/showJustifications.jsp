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
		<%if (net.sourceforge.fenixedu.domain.assiduousness.StaffManagementSection.isMember(user.getPerson())) {%>
		<logic:equal name="yearMonth" property="isThisYearMonthClosed" value="false">
		<logic:present name="employeeJustificationFactory">
		
		<p class="mtop2">
			<span class="error0 mtop0">
				<html:messages id="errorMessage" message="true" property="errorMessage">
					<bean:write name="errorMessage" />
				</html:messages>
			</span>
		</p>
		
			<bean:define id="method" value="editEmployeeJustification" />
			<%--
			<logic:notEmpty name="" property="">
					<bean:define id="method" value="editEmployeeJustification"/>
			</logic:notEmpty>		
			--%>
			<div class="mbottom15">
			<fr:form action="<%="/employeeAssiduousness.do?method="+method.toString()%>" encoding="multipart/form-data">
				<fr:edit id="editEmployeeCorrectionType" name="employeeJustificationFactory"
					type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
					schema="edit.employeeCorrectionType">
					<fr:destination name="justificationMotivePostBack" path="/employeeAssiduousness.do?method=chooseJustificationMotivePostBack" />
					<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
						<fr:property name="columnClasses" value="width8em,width46em,tdclear"/>
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
								<fr:property name="columnClasses" value="width8em,width46em,tdclear"/>
							</fr:layout>
						</fr:edit>
						<logic:notEmpty name="employeeJustificationFactory" property="justificationType">
							<logic:equal name="employeeJustificationFactory" property="justificationType" value="<%= net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType.TIME.toString()%>">
								<fr:edit id="editEmployeeJustificationMotive" name="employeeJustificationFactory"
									type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
									schema="edit.employeeTimeJustificationMotive">
									<fr:layout>
										<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
										<fr:property name="columnClasses" value="width8em,width46em,tdclear"/>
									</fr:layout>
								</fr:edit>
							</logic:equal>
							<logic:equal name="employeeJustificationFactory" property="justificationType" value="<%= net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType.OCCURRENCE.toString()%>">
								<fr:edit id="editEmployeeJustificationMotive" name="employeeJustificationFactory"
									type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
									schema="edit.employeeOccurrenceJustificationMotive">
									<fr:layout>
										<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
										<fr:property name="columnClasses" value="width8em,width46em,tdclear"/>
									</fr:layout>
								</fr:edit>
							</logic:equal>
							<logic:equal name="employeeJustificationFactory" property="justificationType" value="<%= net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType.BALANCE.toString()%>">
								<fr:edit id="editEmployeeJustificationMotive" name="employeeJustificationFactory"
									type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
									schema="edit.employeeBalanceJustificationMotive">
									<fr:layout>
										<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
										<fr:property name="columnClasses" value="width8em,width46em,tdclear"/>
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
								<fr:property name="columnClasses" value="width8em,width46em,tdclear"/>
							</fr:layout>
						</fr:edit>
						<p>
						<html:submit>
							<bean:message key="button.submit" />
						</html:submit>
						</p>
					</logic:equal>
				</logic:notEmpty>		
			</fr:form>
			</div>
		</logic:present>
		</logic:equal>

		<logic:notPresent name="employeeJustificationFactory">
			<p><bean:message key="link.insert" />: 
			<html:link page="<%="/employeeAssiduousness.do?method=prepareCreateEmployeeJustification&correction=JUSTIFICATION&employeeNumber="+employeeNumber.toString()+"&month="+month.toString()+"&year="+year.toString()%>"><bean:message key="label.justification" /></html:link>,
			<html:link page="<%="/employeeAssiduousness.do?method=prepareCreateEmployeeJustification&correction=REGULARIZATION&employeeNumber="+employeeNumber.toString()+"&month="+month.toString()+"&year="+year.toString()%>"><bean:message key="label.regularization" /></html:link>
			</p>
		</logic:notPresent>

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

	<div class="mvert1 invisible"><fr:form
		action="/viewEmployeeAssiduousness.do?method=showJustifications">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method"
			name="employeeForm" property="method" value="showJustifications" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.employeeNumber"
			name="employeeForm" property="employeeNumber" />
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



<logic:present name="justifications">
	<logic:empty name="justifications">
		<p>
			<em><bean:message key="message.employee.noJustifications" /></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="justifications">
		<fr:view name="justifications" schema="show.justifications">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 printborder" />
				<fr:property name="columnClasses" value="acenter" />
				<fr:property name="headerClasses" value="acenter" />
				<%if (net.sourceforge.fenixedu.domain.assiduousness.StaffManagementSection.isMember(user.getPerson())) {%>
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
