<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="org.apache.struts.action.ActionMessages" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.student.create.dismissal" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" property="<%= ActionMessages.GLOBAL_MESSAGE %>" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mtop2">
<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="dismissalBean" property="studentCurricularPlan.student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>


<p class="breadcumbs">
	<span><bean:message key="label.studentDismissal.step.one" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span class="actual"><bean:message key="label.studentDismissal.step.two" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="label.studentDismissal.confirmCreateDismissals" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span><bean:message key="label.studentDismissal.step.three" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
</p>


<bean:define id="dismissalTypeName" name="dismissalBean" property="dismissalType.name" />
<fr:form action="/studentDismissals.do">
	<html:hidden property="method" value="confirmCreateDismissals"/>
	<fr:edit id="dismissalType" name="dismissalBean" schema="DismissalBean.chooseEquivalents">
		<fr:destination name="dismissalTypePostBack" path="/studentDismissals.do?method=dismissalTypePostBack"/>
		<fr:destination name="invalid" path="/studentDismissals.do?method=stepTwo"/>
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight"/>
			<fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
	</fr:edit>

	
	<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
		<p>
			<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
		</p>
	</html:messages>

	<fr:edit id="b" name="dismissalBean" layout="student-dismissal"/>
	
	<p class="mtop15">
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>	
		<html:cancel onclick="this.form.method.value='stepOne'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
		<html:cancel onclick="this.form.method.value='back'; return true;"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>		
	</p>
</fr:form>