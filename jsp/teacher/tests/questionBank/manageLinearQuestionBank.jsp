<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3><bean:message key="message.questionBank.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.groupList" bundle="TESTS_RESOURCES" /></h2>

<p><strong><bean:message key="tests.questionBank.viewing" bundle="TESTS_RESOURCES" />:</strong> <fr:view name="questionBank" layout="short" /></p>

<ul>
	<li>
		<html:link page="/tests/questionBank.do?method=manageQuestionBank&amp;view=tree"
		           paramId="oid" paramName="questionBank" paramProperty="idInternal">
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</html:link>
	</li>
</ul>

<logic:empty name="questionBank" property="allChildQuestionGroups">
	<p><span class="warning0"><bean:message bundle="TESTS_RESOURCES" key="tests.questionGroups.empty" /></span></p>
</logic:empty>

<logic:notEmpty name="questionBank" property="allChildQuestionGroups">
<fr:view name="questionBank" property="allChildQuestionGroups">
	<fr:layout>
		<fr:property name="classes" value="list2" />
		<fr:property name="eachClasses" value="linearSubQuestionGroups" />
		<fr:property name="eachLayout" value="summary" />
		<fr:property name="sortBy" value="name" />
	</fr:layout>
</fr:view>
</logic:notEmpty>
