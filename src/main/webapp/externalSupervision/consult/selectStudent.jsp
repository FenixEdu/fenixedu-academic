<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="externalSupervision"/></em>
<h2><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="title.section.viewStudent"/></h2>

<logic:present name="errorNoPermission">
<span class="error"><bean:message key="error.noPermission" bundle="EXTERNAL_SUPERVISION_RESOURCES"/></span>
</logic:present>

<fr:form action="/viewStudent.do?method=showStats">
	<fr:edit name="sessionBean" id="sessionBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult.ExternalSupervisorViewsBean" bundle="EXTERNAL_SUPERVISION_RESOURCES">
			<fr:slot name="student" layout="autoComplete" key="label.selectStudent.nameOrID" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredAutoCompleteSelectionValidator">
				<fr:property name="size" value="35" />
				<fr:property name="labelField" value="name" />
				<fr:property name="format" value="${name} - <strong>${istUsername}</strong>" />
				<fr:property name="serviceArgs" value="slot=name,size=20" />
				<fr:property name="minChars" value="3" />
				<fr:property name="serviceName" value="SearchPeopleByNameOrISTID" />
				<fr:property name="indicatorShown" value="true" />
				<fr:property name="className" value="net.sourceforge.fenixedu.domain.Person" />
				<fr:property name="required" value="true"/>
			</fr:slot>
			<fr:destination name="invalid" path="/viewStudent.do?method=invalidStudent"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thleft thlight thmiddle mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:schema>
	</fr:edit>
	<html:submit>
		<bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="button.consult" />
	</html:submit>
</fr:form>