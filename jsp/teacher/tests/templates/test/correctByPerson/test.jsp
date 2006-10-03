<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<ft:define id="test" type="net.sourceforge.fenixedu.domain.tests.NewTest" />
<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person" />
<% pageContext.setAttribute("testElements", test.getCorrectableOrderedTestElements(person)); %>

<logic:notEmpty name="test" property="presentationMaterials">
<ft:view schema="tests.testElement.simple">
	<ft:layout name="values">
		<ft:property name="style" value="border: thin solid #ccc; background-color: #fefefe; padding: 0.5em; display: block; width: 60em;" />
	</ft:layout>
</ft:view>
</logic:notEmpty>

<logic:notEmpty name="testElements">
	<fr:view name="testElements">
		<fr:layout name="flowLayout">
			<fr:property name="style" value="margin-left: 2em; display: block;" />
			<fr:property name="eachStyle" value="margin-top: 1em;" />
			<fr:property name="eachInline" value="false" />
			<fr:property name="eachLayout" value="template-correctByPerson-tree" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
