<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h3><bean:message key="message.questionBank.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.createQuestionGroup" bundle="TESTS_RESOURCES" /></h2>

<bean:define id="questionGroupId"><bean:write name="questionGroup" property="externalId" /></bean:define>

<div><bean:message key="message.willAssociateTo" bundle="TESTS_RESOURCES" />
<fr:view name="questionGroup" layout="short" />
</div>

<fr:form action="/tests/questionBank.do?method=createQuestionGroup">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oid" property="oid" value="<%= questionGroupId %>" />
	<fr:create type="net.sourceforge.fenixedu.domain.tests.NewQuestionGroup"
	           schema="tests.questionGroup.create"
	           id="createQuestionGroup"
	           nested="true">
		<fr:hidden slot="parentQuestionGroup" name="questionGroup" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tdwarning tdclear" />
		</fr:layout>
		<fr:destination name="invalid" path="/tests/questionBank.do?method=prepareCreateQuestionGroup" />
	</fr:create>
	<html:submit><bean:message key="label.button.create" bundle="TESTS_RESOURCES" /></html:submit>
</fr:form>
