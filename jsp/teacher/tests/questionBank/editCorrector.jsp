<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<h3><bean:message key="message.questionBank.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.editCorrector" bundle="TESTS_RESOURCES" /></h2>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean" />

<ul>
<li><html:link page="/tests/questionBank.do?method=editTestElement" paramId="oid" paramName="bean" paramProperty="question.idInternal">Voltar</html:link></li>
</ul>

<h4><bean:message key="label.corrector.atomicQuestion" bundle="TESTS_RESOURCES" /></h4>
<fr:view name="bean" property="question" schema="tests.testElement.simple">
	<fr:layout name="values">
		<fr:property name="style" value="border: thin solid #ccc; background-color: #fefefe; padding: 0.5em; display: block; width: 60em;" />
	</fr:layout>
</fr:view>

<logic:empty name="bean" property="predicateType">
<h4><bean:message key="message.addRule" bundle="TESTS_RESOURCES" /></h4>
<html:form action="/tests/questionBank.do?method=prepareCreateAtomicPredicate">
<html:hidden property="returnTo" value="editCorrector" />
<fr:edit name="bean"
         schema="tests.predicate.choose-type"
         id="chooseAtomicPredicateType"
         nested="true">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thright"/>
	</fr:layout>
	<fr:destination name="prepare-create" path="/tests/questionBank.do?method=prepareCreateAtomicPredicate" />
</fr:edit>
</html:form>
</logic:empty>

<logic:notEmpty name="bean" property="predicateType">
<h4><bean:message key="message.addRule.parameter" bundle="TESTS_RESOURCES" /></h4>
<html:form action="/tests/questionBank.do?method=createAtomicPredicate">
<html:hidden property="returnTo" value="editCorrector" />
<fr:edit name="bean"
         schema="<%=
        	 "tests.predicate.choose-details-for." + bean.getPredicateType().getName()
        	 %>"
         id="createAtomicPredicate">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thright"/>
	</fr:layout>
	<fr:destination name="invalid" path="/tests/questionBank.do?method=prepareCreateAtomicPredicate" />
	<fr:destination name="prepare-create" path="/tests/questionBank.do?method=prepareCreateAtomicPredicate" />
</fr:edit>
<html:submit><bean:message key="label.button.add" bundle="TESTS_RESOURCES" /></html:submit>
</html:form>
</logic:notEmpty>

<logic:empty name="bean" property="action">
<h4><bean:message key="message.composeRules" bundle="TESTS_RESOURCES" /></h4>
<html:form action="/tests/questionBank.do?method=managePredicates">
<html:hidden property="returnTo" value="editCorrector" />
<fr:edit name="bean"
         schema="tests.predicate.predicates"
         id="predicateList">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thright"/>
	</fr:layout>
	<fr:destination name="action" path="/tests/questionBank.do?method=managePredicates" />
</fr:edit>
</html:form>
</logic:empty>

<logic:notEmpty name="bean" property="action">
<h4><bean:message key="message.composeRules" bundle="TESTS_RESOURCES" /></h4>
<fr:form action="/tests/questionBank.do?method=composePredicates">
<html:hidden property="returnTo" value="editCorrector" />
<fr:edit name="bean"
         schema="tests.predicate.predicates.compose"
         id="predicateList"
         nested="true">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thright"/>
	</fr:layout>
	<fr:destination name="action" path="/tests/questionBank.do?method=managePredicates" />
</fr:edit>
<html:submit><bean:message key="label.button.execute" bundle="TESTS_RESOURCES" /></html:submit>
</fr:form>
</logic:notEmpty>

<h4><bean:message key="message.chooseRule" bundle="TESTS_RESOURCES" /></h4>
<logic:notEmpty name="bean" property="predicates">
<fr:form action="/tests/questionBank.do?method=chooseCorrector">
<html:hidden property="returnTo" value="editCorrector" />
<fr:edit name="bean"
         schema="tests.corrector.choose"
         id="choosePredicate">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thright"/>
	</fr:layout>
	<fr:destination name="invalid" path="/tests/questionBank.do?method=invalidChoosePredicate" />
</fr:edit>
<html:submit><bean:message key="label.button.choose" bundle="TESTS_RESOURCES" /></html:submit>
</fr:form>
</logic:notEmpty>
<logic:empty name="bean" property="predicates">
<span><bean:message key="message.rules.empty" bundle="TESTS_RESOURCES" /></span>
</logic:empty>

<h4><bean:message key="message.correctors.choosen" bundle="TESTS_RESOURCES" /></h4>
<logic:notEmpty name="bean" property="question.correctors">
<html:form action="/tests/questionBank.do?method=deleteCorrectors">
	<fr:edit name="bean" id="delete-correctors" visible="false" />
	<fr:view name="bean" property="question.correctors" schema="tests.corrector.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="sortBy" value="position" />
			<fr:property name="checkable" value="true" />
			<fr:property name="checkboxName" value="correctorOids" />
			<fr:property name="checkboxValue" value="idInternal" />
		</fr:layout>
	</fr:view>
<html:submit><bean:message key="message.delete" bundle="TESTS_RESOURCES" /></html:submit>
</html:form>
</logic:notEmpty>
<logic:empty name="bean" property="question.correctors">
<span><bean:message key="tests.correctors.empty" bundle="TESTS_RESOURCES" /></span>
</logic:empty>
