<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<html:messages id="message" message="true">
	<span class="error">
		<bean:write name="message" filter="false"/>
	</span>
</html:messages>

<logic:present name="teacherBean">
	<fr:edit id="teacherBean" name="teacherBean" action="/credits.do?method=showTeacherCredits">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.TeacherCreditsBean">
			<fr:slot name="teacher" layout="autoComplete">
				<fr:property name="size" value="80"/>
				<fr:property name="labelField" value="person.name"/>
				<fr:property name="format" value="${person.name} (${person.username})"/>
				<fr:property name="indicatorShown" value="true"/>
				<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchTeachersByNameOrISTID"/>
				<fr:property name="args" value="slot=person.name"/>
				<fr:property name="minChars" value="3"/>
				<fr:property name="errorStyleClass" value="error0"/>
				<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight mtop15" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
</logic:present>