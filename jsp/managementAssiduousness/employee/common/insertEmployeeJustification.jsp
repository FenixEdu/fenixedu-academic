<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present name="employee">
	<bean:define id="month" value='<%=request.getParameter("month")%>'/>
	<bean:define id="year" value='<%=request.getParameter("year")%>'/>
	
	<bean:define id="method" value="editEmployeeJustification" />
	<div class="mbottom15">
	<fr:form action="<%="/employeeAssiduousness.do?method="+method.toString()%>" encoding="multipart/form-data">
		<fr:edit id="editEmployeeCorrectionType" name="employeeJustificationFactory"
			type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory" schema="edit.employeeCorrectionType">
			<fr:destination name="justificationMotivePostBack" path="/employeeAssiduousness.do?method=chooseJustificationMotivePostBack" />
			<fr:hidden slot="year" name="year" />
			<fr:hidden slot="month" value="<%=month.toString()%>" />
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
				<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
			</fr:layout>
		</fr:edit>
		<logic:notEmpty name="employeeJustificationFactory" property="correctionType">
			<logic:equal name="employeeJustificationFactory" property="correctionType" value="<%= net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.CorrectionType.JUSTIFICATION.toString()%>">
				<fr:edit id="editEmployeeDayJustificationType" name="employeeJustificationFactory"
					type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory" schema="edit.employeeDayJustificationType">
					<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
					<fr:destination name="justificationMotivePostBack" path="/employeeAssiduousness.do?method=chooseJustificationMotivePostBack" />
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
						<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
					</fr:layout>
				</fr:edit>
				<logic:notEmpty name="employeeJustificationFactory" property="justificationDayType">
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
					<logic:equal name="employeeJustificationFactory" property="justificationType" value="<%= net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType.HALF_OCCURRENCE_TIME.toString()%>">
						<fr:edit id="editEmployeeJustificationMotive" name="employeeJustificationFactory"
							type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
							schema="edit.employeeHalfOccurrenceTimeJustificationMotive">
							<fr:layout>
								<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
								<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
							</fr:layout>
						</fr:edit>
					</logic:equal>
					<logic:equal name="employeeJustificationFactory" property="justificationType" value="<%= net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType.HALF_OCCURRENCE.toString()%>">
						<fr:edit id="editEmployeeJustificationMotive" name="employeeJustificationFactory"
							type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
							schema="edit.employeeHalfOccurrenceJustificationMotive">
							<fr:layout>
								<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
								<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
							</fr:layout>
						</fr:edit>
					</logic:equal>
					<logic:equal name="employeeJustificationFactory" property="justificationType" value="<%= net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType.HALF_MULTIPLE_MONTH_BALANCE.toString()%>">
						<fr:edit id="editEmployeeJustificationMotive" name="employeeJustificationFactory"
							type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
							schema="edit.employeeHalfOccurrenceJustificationMotive">
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
				</logic:notEmpty>
			</logic:equal>
			<logic:equal name="employeeJustificationFactory" property="correctionType" value="<%= net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.CorrectionType.REGULARIZATION.toString()%>">
				<fr:edit id="editEmployeeJustificationMotive" name="employeeJustificationFactory"
					type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
					schema="edit.employeeRegularization">
					<fr:hidden slot="modifiedBy" name="UserView" property="person.employee" />
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
						<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
					</fr:layout>
				</fr:edit>
				<p>
				<html:submit>
					<bean:message key="button.confirm" />
				</html:submit>
				</p>
			</logic:equal>
		</logic:notEmpty>		
	</fr:form>
	</div>
</logic:present>