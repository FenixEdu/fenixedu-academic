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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<bean:define id="executionSemesterBean" name="executionSemesterBean" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentExecutionSemester"/>
<bean:define id="departmentParam" value="&departmentUnitOID="/>

<logic:present name="fromPedagogicalCouncil">
	<h2><bean:message key="title.inquiries.resultsWithDescription" bundle="INQUIRIES_RESOURCES"/></h2>

	<bean:define id="departmentParam" value="<%= "&departmentUnitOID=" + executionSemesterBean.getDepartmentUnitOID() %>"/>	
</logic:present>

<logic:notPresent name="fromPedagogicalCouncil">
	<h2><bean:message key="title.inquiry.quc.department" bundle="INQUIRIES_RESOURCES"/></h2>
</logic:notPresent>

<p class="mtop15"><strong>
	<html:link action="<%= "/viewQucResults.do?method=resumeResults" + departmentParam %>" paramId="executionSemesterOID" paramName="executionSemester" paramProperty="externalId">
		<bean:message key="link.quc.resume" bundle="INQUIRIES_RESOURCES"/>
	</html:link> | 
	<html:link action="<%= "/viewQucResults.do?method=competenceResults" + departmentParam %>" paramId="executionSemesterOID" paramName="executionSemester" paramProperty="externalId">
		<bean:message key="link.quc.curricularUnits" bundle="INQUIRIES_RESOURCES"/>
	</html:link> | 
	<html:link action="<%= "/viewQucResults.do?method=teachersResults" + departmentParam %>" paramId="executionSemesterOID" paramName="executionSemester" paramProperty="externalId">
		<bean:message key="link.quc.teachers" bundle="INQUIRIES_RESOURCES"/>
	</html:link>
</strong></p>

<p class="mvert15">
	<fr:form>
		<fr:edit id="executionSemesterBean" name="executionSemesterBean">
			<fr:schema bundle="INQUIRIES_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentExecutionSemester">
				<fr:slot name="executionSemester" key="label.inquiries.semester" layout="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.departmentMember.ViewQUCResultsDA$ExecutionSemesterQucProvider" />
					<fr:property name="format" value="${executionYear.year} - ${semester}º Semestre" />
					<fr:property name="nullOptionHidden" value="true"/>
					<fr:property name="destination" value="resumePostBack"/>
				</fr:slot>
			</fr:schema>
			<fr:layout>
				<fr:property name="classes" value="thlight thmiddle" />
				<fr:property name="resumePostBack" value="/viewQucResults.do?method=resumeResults"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
</p>

<logic:present name="fromPedagogicalCouncil">
	<h3 style="display: inline;"><bean:write name="departmentName"/></h3> | 
	<html:link page="/viewQucResults.do?method=chooseDepartment">
		<bean:message key="link.inquiry.chooseAnotherDepartment" bundle="INQUIRIES_RESOURCES"/>
    </html:link>
</logic:present>