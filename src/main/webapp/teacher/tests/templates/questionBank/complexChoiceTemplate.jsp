<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

<h2><bean:message key="title.editChoice" bundle="TESTS_RESOURCES" /></h2>

<ft:define id="choice" type="net.sourceforge.fenixedu.domain.tests.NewChoice" />

<ul>
<li><html:link page="/tests/questionBank.do?method=editTestElement" paramId="oid" paramName="choice" paramProperty="multipleChoiceQuestion.externalId">Voltar</html:link></li>
</ul>

<h4>Materiais de apresenta��o</h4>

<div class="questionBlockHeader">
	<strong><bean:message key="label.testElement.presentationMaterials" bundle="TESTS_RESOURCES" />:</strong>
	(<f:parameterLink page="/tests/questionBank/presentationMaterial.do?method=prepareEditPresentationMaterials">
	 	<f:parameter id="oid" name="choice" property="externalId" />
	 	<f:parameter id="returnPath" value="/tests/questionBank.do?method=editTestElement" />
	 	<f:parameter id="returnId" name="choice" property="externalId" />
	 	<f:parameter id="contextKey" value="message.questionBank.manage" />
	 	Editar
	 </f:parameterLink>)
</div>
<ft:view schema="tests.testElement.simple">
	<ft:layout name="values">
		<ft:property name="style" value="border: thin solid #ccc; background-color: #fefefe; padding: 0.5em; display: block; width: 60em;" />
	</ft:layout>
</ft:view>