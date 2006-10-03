<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3><bean:message key="message.questionBank.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.managePermissionUnits" bundle="TESTS_RESOURCES" />
    <fr:view name="question" property="owner.name" /></h2>

<ul>
	<li>
		<html:link page="/tests/questionBank.do?method=manageQuestionBank&amp;view=tree"
		           paramId="oid" paramName="question" paramProperty="questionBank.idInternal">
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</html:link>
	</li>
</ul>

<fr:form action="/tests/questionBank.do?method=prepareManagePermissionUnits">
<fr:create type="net.sourceforge.fenixedu.domain.tests.NewPermissionUnit"
           schema="permissionUnit.create"
           service="CreatePermissionUnit"
           nested="true">
	<fr:hidden slot="question" name="question" />
	<fr:destination name="success" path="<%= "/tests/questionBank.do?method=prepareManagePermissionUnits&oid=" + request.getParameter("oid") %>" />
</fr:create>
<html:submit><bean:message key="label.button.create" bundle="TESTS_RESOURCES" /></html:submit>
</fr:form>

<fr:view name="question"
         property="permissionUnits"
         schema="permissionUnit.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
		<fr:property name="key(delete)" value="message.delete" />
		<fr:property name="bundle(delete)" value="TESTS_RESOURCES" />
		<fr:property name="link(delete)" value="<%= "/tests/questionBank.do?method=deletePermissionUnit&oid=" + request.getParameter("oid") %>" />
		<fr:property name="param(delete)" value="idInternal/oid" />
	</fr:layout>
</fr:view>
