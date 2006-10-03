<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3><bean:message key="message.questionBank.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.groupTree" bundle="TESTS_RESOURCES" /> </h2>

<p><strong><bean:message key="tests.questionBank.viewing" bundle="TESTS_RESOURCES" />:</strong> <fr:view name="questionBank" layout="short" /></p>

<script type="text/javascript" language="javascript" src="<%= request.getContextPath() + "/teacher/tests/tests.js" %>"></script>

<bean:define id="questionGroup" name="questionBank" type="net.sourceforge.fenixedu.domain.tests.NewQuestionGroup" toScope="request" />

<ul>
<li><html:link page="/tests/questionBank.do?method=prepareCreateQuestionGroup" paramId="oid" paramName="questionBank" paramProperty="idInternal">Criar grupo</html:link></li>
<li><html:link page="/tests/questionBank.do?method=prepareCreateAtomicQuestion" paramId="oid" paramName="questionBank" paramProperty="idInternal">Criar pergunta</html:link></li>
<li><html:link page="/tests/questionBank.do?method=manageQuestionBank&amp;view=linear" paramId="oid" paramName="questionBank" paramProperty="idInternal">Ver lista de grupos</html:link></li>
<%--<logic:equal name="isOwnBank" value="true">
<li><html:link page="/tests/questionBank.do?method=prepareManagePermissionUnits" paramId="oid" paramName="UserView" paramProperty="person.questionBank.idInternal">Gerir permissões</html:link></li>
</logic:equal>--%>
</ul>

<h4><bean:message key="message.select.questionGroup.header" bundle="TESTS_RESOURCES" /></h4>
<fr:view name="questionBank" property="orderedChildQuestionGroups">
	<fr:layout name="flowLayout">
		<fr:property name="eachLayout" value="template-tree" />
		<fr:property name="eachInline" value="false" />
		<fr:property name="eachStyle" value="margin-left: 2em; margin-top: 0.5em;" />
		<fr:property name="emptyMessageKey" value="tests.questionGroups.empty" />
		<fr:property name="emptyMessageBundle" value="TESTS_RESOURCES" />
	</fr:layout>
</fr:view>

<h4><bean:message key="message.select.atomicQuestion.header" bundle="TESTS_RESOURCES" /></h4>
<fr:view name="questionBank" property="orderedChildAtomicQuestions" >
	<fr:layout name="flowLayout">
		<fr:property name="eachLayout" value="summary" />
		<fr:property name="emptyMessageKey" value="tests.atomicQuestions.empty" />
		<fr:property name="emptyMessageClasses" value="emptyMessage" />
		<fr:property name="emptyMessageBundle" value="TESTS_RESOURCES" />
	</fr:layout>
</fr:view>

<h3><bean:message key="message.questionBanks.reachable" bundle="TESTS_RESOURCES" /></h3>
<fr:view name="questionBanks">
	<fr:layout>
		<fr:property name="eachLayout" value="short" />
	</fr:layout>
</fr:view>
