<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2>
	<bean:message bundle="APPLICATION_RESOURCES" key="label.define.instructions"/>:
	<bean:write name="executionSemester" property="executionYear.year"/>
	-
	<bean:write name="executionSemester" property="semester"/>
</h2>

<fr:edit id="enrolmentInstructions" name="executionSemester" property="enrolmentInstructions" action="/manageEnrolementPeriods.do?method=prepare">
	<fr:schema type="net.sourceforge.fenixedu.domain.EnrolmentInstructions" bundle="APPLICATION_RESOURCES">
		<fr:slot name="instructions" key="label.enrolmentInstructions.instructions" layout="longText" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			<fr:property name="columns" value="100"/>
			<fr:property name="rows" value="110"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>