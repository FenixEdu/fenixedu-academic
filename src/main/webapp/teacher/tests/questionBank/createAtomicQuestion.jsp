<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h3><bean:message key="message.questionBank.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.createAtomicQuestion" bundle="TESTS_RESOURCES" /></h2>

<div><bean:message key="message.willAssociateTo" bundle="TESTS_RESOURCES" />
<fr:view name="questionGroup" layout="short" />
</div>

<html:form method="post"
           action="/tests/questionBank.do?method=createAtomicQuestion">
<fr:edit id="edit-atomic-question-type-bean"
         name="atomicQuestionTypeBean"
         nested="true"
         schema="tests.atomicQuestion.create">
	<fr:hidden slot="parentQuestionGroup" name="questionGroup" />
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle1"/>
        <fr:property name="columnClasses" value=",,tdwarning tdclear"/>
    </fr:layout>
    <fr:destination name="invalid" path="/tests/questionBank.do?method=invalidCreateAtomicQuestion" />
</fr:edit>
<html:submit><bean:message key="label.button.add" bundle="TESTS_RESOURCES" /></html:submit>
</html:form>
