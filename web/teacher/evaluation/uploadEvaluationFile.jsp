<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2>
	<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.evaluees.title"/>
</h2>

<logic:present name="fileUploadBean">
	<bean:define id="urlUploadInvalid">/teacherEvaluation.do?method=viewEvaluation&amp;evalueeOID=<bean:write name="fileUploadBean" property="teacherEvaluationProcess.evaluee.externalId"/></bean:define>
	<fr:edit id="fileUploadBean" name="fileUploadBean" action="/teacherEvaluation.do?method=uploadEvaluationFile">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.evaluation.FileUploadBean">
			<fr:slot name="inputStream" key="label.file" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="fileNameSlot" value="filename"/>
				<fr:property name="size" value="30"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thmiddle mtop05"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
		<fr:destination name="cancel" path="<%= urlUploadInvalid %>"/>
	</fr:edit>
</logic:present>
