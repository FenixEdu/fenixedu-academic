<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<h3><bean:message key="message.testModels.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><fr:view name="testModel" property="name" /></h2>

<span class="errors"><html:errors/></span>

<ul>
	<li>
		<f:parameterLink page="/tests/testModels.do?method=manageTestModels">
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
	<li>
		<f:parameterLink page="/tests/testModels.do?method=editTestModel">
			<f:parameter id="oid" name="testModel" property="idInternal" />
			<bean:message key="message.toModel" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
	<li>
		<f:parameterLink page="/tests/testModels.do?method=selectQuestions">
			<f:parameter id="oid" name="testModel" property="idInternal" />
			<bean:message key="message.toSelect" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
	<li>
		<bean:message key="message.toSort" bundle="TESTS_RESOURCES" />
	</li>
</ul>

<bean:define id="testModel" name="testModel" type="net.sourceforge.fenixedu.domain.tests.NewTestModel" />

<fr:view name="testModel" layout="template-sort-tree" />
