<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><strong><bean:message key="label.studentDataByExecutionYear" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<br />
<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<bean:define id="studentId" name="student" property="idInternal" />
<html:link action="/studentDataByExecutionYear.do?method=prepareCreate" paramName="student" paramProperty="idInternal" paramId="studentId">
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.create" />
</html:link>

<logic:notEmpty name="createBean">
	<fr:edit id="createBeanData" name="createBean" schema="StudentDataByExecutionYearBean"
		action="<%= "/studentDataByExecutionYear.do?method=create&amp;studentId=" + studentId.toString() %>">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="/studentDataByExecutionYear.do?method=prepareCreateInvalid"/>
		<fr:destination name="cancel" path="<%= "/studentDataByExecutionYear.do?method=show&amp;studentId=" + studentId.toString() %>"/>
	</fr:edit>
	<br />
</logic:notEmpty>

<logic:notEmpty name="sortedStudentDataByExecutionYear">
	<fr:view name="sortedStudentDataByExecutionYear" schema="StudentDataByExecutionYear">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thcenter" />
			
			<fr:property name="linkFormat(delete)" value="<%= "/studentDataByExecutionYear.do?method=delete&amp;studentDataId=${idInternal}&amp;studentId=" + studentId.toString() %>"/>
			<fr:property name="key(delete)" value="label.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="contextRelative(delete)" value="true"/>      
			<fr:property name="order(delete)" value="1"/>
			<fr:property name="confirmationKey(delete)" value="label.studentDataByExecutionYear.delete.confirmation.message"/>
			<fr:property name="confirmationBundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="sortedStudentDataByExecutionYear">
	<em><bean:message key="label.student.no.studentDataByExecutionYear" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
</logic:empty>

<html:link action="/student.do?method=visualizeStudent" paramName="student" paramProperty="idInternal" paramId="studentID">
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.back" />
</html:link>
