<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<ft:define id="atomicQuestion" type="net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion" />

<strong>
<ft:view property="path">
	<ft:layout name="flowLayout">
		<ft:property name="htmlSeparator" value="." />
	</ft:layout>
</ft:view>) Pergunta
</strong>
(<ft:view property="grade.value" />/<ft:view property="grade.scale" />)

<logic:notEmpty name="atomicQuestion" property="presentationMaterials">
	<ft:view schema="tests.testElement.simple">
		<ft:layout name="values">
			<ft:property name="style" value="border: thin solid #ccc; background-color: #fefefe; padding: 0.5em; display: block; width: 60em;" />
		</ft:layout>
	</ft:view>
</logic:notEmpty>

<logic:notEmpty name="atomicQuestion" property="orderedChoices">
<div style="border: thin solid #ccc; background-color: #f0f0f0; padding: 0.5em; display: block; width: 60em; margin-bottom: 0.5em;">
<ft:view property="orderedChoices">
	<ft:layout name="flowLayout">
		<ft:property name="eachInline" value="false" />
		<ft:property name="eachStyle" value="clear: both" />
		<ft:property name="eachLayout" value="template-short" />
		<ft:property name="emptyMessageKey" value="tests.empty" />
		<ft:property name="emptyMessageClasses" value="emptyMessage" />
		<ft:property name="emptyMessageBundle" value="TESTS_RESOURCES" />
	</ft:layout>
</ft:view>
</div>
</logic:notEmpty>
