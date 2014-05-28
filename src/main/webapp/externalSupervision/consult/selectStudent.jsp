<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

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
				<fr:property name="args" value="slot=name,size=20" />
				<fr:property name="minChars" value="3" />
				<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchPeopleByNameOrISTID" />
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