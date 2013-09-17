<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

<ft:define id="section" type="net.sourceforge.fenixedu.domain.tests.NewSection" />

<strong>
<ft:view property="path">
	<ft:layout name="flowLayout">
		<ft:property name="htmlSeparator" value="." />
	</ft:layout>
</ft:view>) Sec��o
</strong>

<logic:notEmpty name="section" property="presentationMaterials">
<ft:view schema="tests.testElement.simple">
	<ft:layout name="values">
		<ft:property name="style" value="border: thin solid #ccc; background-color: #fefefe; padding: 0.5em; display: block; width: 60em;" />
	</ft:layout>
</ft:view>
</logic:notEmpty>

<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person" />
<% pageContext.setAttribute("testElements", section.getCorrectableOrderedTestElements(person)); %>

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
