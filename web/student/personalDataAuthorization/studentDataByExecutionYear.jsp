<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><strong><bean:message key="label.studentDataByExecutionYear"/></strong></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<logic:notEmpty name="sortedStudentDataByExecutionYear">
	<fr:view name="sortedStudentDataByExecutionYear" schema="StudentDataByExecutionYear">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thcenter" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="sortedStudentDataByExecutionYear">
	<em><bean:message key="label.student.no.studentDataByExecutionYear" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
</logic:empty>
