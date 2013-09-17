<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

<ft:define id="test" type="net.sourceforge.fenixedu.domain.tests.NewTest" />

<logic:notEmpty name="test" property="presentationMaterials">
<ft:view schema="tests.testElement.simple">
	<ft:layout name="values">
		<ft:property name="style" value="border: thin solid #ccc; background-color: #fefefe; padding: 0.5em; display: block; width: 60em;" />
	</ft:layout>
</ft:view>
</logic:notEmpty>

<logic:equal name="test" property="showAllElements" value="false">
	<div class="warning0">Consoante as respostas podem vir a aparecer mais perguntas</div>
</logic:equal>

<logic:notEmpty name="test" property="visibleOrderedTestElements">
	<ft:view property="visibleOrderedTestElements">
		<ft:layout name="flowLayout">
			<ft:property name="style" value="margin-left: 2em; display: block;" />
			<ft:property name="eachStyle" value="margin-top: 1em;" />
			<ft:property name="eachInline" value="false" />
			<ft:property name="eachLayout" value="template-student-tree" />
		</ft:layout>
	</ft:view>
</logic:notEmpty>
