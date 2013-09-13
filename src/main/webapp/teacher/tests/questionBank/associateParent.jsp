<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h3><bean:message key="message.questionBank.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.associateParent" bundle="TESTS_RESOURCES" /></h2>

<ul>
	<li>
		<html:link page="/tests/questionBank.do?method=editTestElement"
		           paramId="oid"
		           paramName="bean"
		           paramProperty="child.externalId">
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</html:link>
	</li>
</ul>

<div>
	<strong>
		<bean:message key="label.element.associate" bundle="TESTS_RESOURCES" />:
	</strong> 
	<fr:view name="bean" property="child" layout="short" />
</div>

<html:form action="/tests/questionBank.do?method=associateParent" method="post">
	<fr:edit type="net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.GroupElementBean"
	         schema="tests.groupElement.edit"
	         name="bean"
	         id="associateParent"
	         nested="true">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="label.button.associate" bundle="TESTS_RESOURCES" /></html:submit>
</html:form>
