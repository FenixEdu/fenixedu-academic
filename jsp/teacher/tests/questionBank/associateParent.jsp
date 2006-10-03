<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3><bean:message key="message.questionBank.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.associateParent" bundle="TESTS_RESOURCES" /></h2>

<ul>
	<li>
		<html:link page="/tests/questionBank.do?method=editTestElement"
		           paramId="oid"
		           paramName="bean"
		           paramProperty="child.idInternal">
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
