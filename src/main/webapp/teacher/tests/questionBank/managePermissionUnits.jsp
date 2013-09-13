<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h3><bean:message key="message.questionBank.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.managePermissionUnits" bundle="TESTS_RESOURCES" />
    <fr:view name="question" property="owner.name" /></h2>

<ul>
	<li>
		<html:link page="/tests/questionBank.do?method=manageQuestionBank&amp;view=tree"
		           paramId="oid" paramName="question" paramProperty="questionBank.externalId">
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</html:link>
	</li>
</ul>

<fr:form action="/tests/questionBank.do?method=prepareManagePermissionUnits">

<%-- CreatePermissionUnit was deleted --%>
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
		<fr:property name="param(delete)" value="externalId/oid" />
	</fr:layout>
</fr:view>
