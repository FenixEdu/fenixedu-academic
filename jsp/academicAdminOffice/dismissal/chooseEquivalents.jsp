<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="org.apache.struts.action.ActionMessages" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.studentDismissal.management" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
<br/>
<html:messages id="message" property="<%= ActionMessages.GLOBAL_MESSAGE %>" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
	<br/>
</html:messages>
<fr:view name="dismissalBean" schema="DismissalBean.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>
<br/>
<h3><bean:message key="label.studentDismissal.step.one" bundle="ACADEMIC_OFFICE_RESOURCES"/> &gt; <u><bean:message key="label.studentDismissal.step.two" bundle="ACADEMIC_OFFICE_RESOURCES"/></u></h3>
<br/>
<bean:define id="dismissalTypeName" name="dismissalBean" property="dismissalType.name" />
<fr:form action="/studentDismissals.do">
	<html:hidden property="method" value="confirmCreateDismissals"/>
	<fr:edit id="dismissalType" name="dismissalBean" schema="DismissalBean.chooseEquivalents">
		<fr:destination name="dismissalTypePostBack" path="/studentDismissals.do?method=dismissalTypePostBack"/>
		<fr:destination name="invalid" path="/studentDismissals.do?method=stepTwo"/>
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4"/>
		</fr:layout>
	</fr:edit>
	<br/>
	
	<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
		<span class="error"><!-- Error messages go here --><bean:write name="errMsg" /></span>
		<br/><br/>
	</html:messages>
	<fr:edit id="b" name="dismissalBean" layout="student-dismissal"/>
	<br/>
	<br/>
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>	
	<html:cancel onclick="this.form.method.value='stepOne'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
	<html:cancel onclick="this.form.method.value='back'; return true;"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>		
</fr:form>