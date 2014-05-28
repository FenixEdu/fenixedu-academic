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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

	
<h2><bean:message key="label.submited.markSheets" bundle="APPLICATION_RESOURCES"/></h2>

<p>
	<bean:define id="executionCourseID" name="executionCourseID" />
	<html:link  page="/evaluation/finalEvaluationIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:link>
	<logic:empty name="markSheets">
		<em><bean:message bundle="APPLICATION_RESOURCES" key="label.no.submited.markSheets.found"/></em>
	</logic:empty>
	<logic:notEmpty name="markSheets">
		<fr:view name="markSheets"
				 schema="markSheet.teacher.gradeSubmission.view.submited.markSheets">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="creationDateDateTime"/>
					<fr:property name="classes" value="tstyle4"/>
				    <fr:property name="columnClasses" value="listClasses,,"/>
				    <fr:property name="link(viewMarkSheet)" value='<%= "/markSheetManagement.do?method=viewMarkSheet&amp;executionCourseID=" + executionCourseID %>'/>
					<fr:property name="key(viewMarkSheet)" value="label.view.markSheet"/>
					<fr:property name="param(viewMarkSheet)" value="externalId/msID"/>				
					<fr:property name="bundle(viewMarkSheet)" value="APPLICATION_RESOURCES" />				    
				</fr:layout>
		</fr:view>
	</logic:notEmpty>
</p>