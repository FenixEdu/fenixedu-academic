<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.justifications" /></h2>
	<p class="mtop2">
		<span class="error0 mtop0">
			<html:messages id="errorMessage" message="true" property="errorMessage">
				<bean:write name="errorMessage" filter="false"/>
			</html:messages>
		</span>
	</p>	
<logic:present name="employeeJustificationFactory">
	<div class="mbottom15">
	
	<fr:form action="/employeeAssiduousness.do?method=insertJustification" encoding="multipart/form-data">
		<fr:edit id="editEmployeeDayJustificationType" name="employeeJustificationFactory"
			type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
			schema="edit.employeeDayJustificationType">
			<fr:destination name="justificationMotivePostBack" path="/employeeAssiduousness.do?method=chooseInsertJustificationMotivePostBack" />
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
				<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
			</fr:layout>
		</fr:edit>
		<logic:notEmpty name="employeeJustificationFactory" property="justificationDayType">
			<fr:edit id="editEmployeeJustificationType" name="employeeJustificationFactory"
				type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory"
				schema="edit.employeeJustificationType">
				<fr:destination name="justificationMotivePostBack" path="/employeeAssiduousness.do?method=chooseInsertJustificationMotivePostBack" />
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
				<fr:edit id="severalEmployeeJustificationFactoryCreator" name="employeeJustificationFactory"
						type="net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory$SeveralEmployeeJustificationFactoryCreator"
						schema="edit.severalEmployeeJustificationFactoryCreator">
						<fr:layout>
							<fr:property name="classes" value="tstyle5 thlight mvert0 thright thmiddle"/>
							<fr:property name="columnClasses" value="width8em,width40em,tdclear"/>
						</fr:layout>
					</fr:edit>
				<p><html:submit>
					<bean:message key="button.submit" />
				</html:submit></p>
			</logic:notEmpty>
		</logic:notEmpty>
	</fr:form>
	</div>
</logic:present>
