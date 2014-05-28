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
<%@ page language="java"%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.title"/></em>

<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.upload.title" /></h2>

<logic:present name="fileUploadBean">
	<bean:define id="urlUploadInvalid">/teacherEvaluation.do?method=<bean:write name="backAction" />&evalueeOID=<bean:write
			name="fileUploadBean" property="teacherEvaluationProcess.evaluee.externalId" />
	</bean:define>
	<bean:define id="type" name="fileUploadBean" property="teacherEvaluationFileType.name" type="java.lang.String" />
	<bean:define id="backAction" name="backAction" type="java.lang.String" />
	<fr:edit id="fileUploadBean" name="fileUploadBean"
		action="<%= "/teacherEvaluation.do?method=uploadEvaluationFile&amp;backAction=" + backAction %>">
		<fr:schema bundle="ENUMERATION_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.evaluation.FileUploadBean">
			<fr:slot name="inputStream" key="<%= type %>"
				validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="fileNameSlot" value="filename" />
				<fr:property name="size" value="30" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thmiddle mtop05" />
			<fr:property name="columnClasses" value=",,tderror1 tdclear" />
		</fr:layout>
		<fr:destination name="cancel" path="<%= urlUploadInvalid %>" />
		<fr:destination name="invalid" path="<%= urlUploadInvalid %>" />
	</fr:edit>
</logic:present>
