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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgram"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.degreeStructure.DegreeCurricularPlanRendererConfig" %>

<logic:present role="role(COORDINATOR)">

<h2><bean:message key="label.phd.manage.enrolments" bundle="PHD_RESOURCES" /></h2>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<logic:empty name="phdProgram">

	<h3><bean:message key="label.phd.choose.phd.program" bundle="PHD_RESOURCES" />: </h3>
	<fr:view name="phdPrograms">
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgram.class.getName() %>">
			<fr:slot name="presentationName" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10" />
			<fr:property name="sortBy" value="presentationName=asc" />
			
			<fr:link name="view" label="label.view,PHD_RESOURCES" link="/phdEnrolmentsManagement.do?method=showPhdProgram&phdProgramOid=${externalId}" />
		</fr:layout>
	</fr:view>

</logic:empty>

<logic:notEmpty name="phdProgram">

	<br/>
	<h3><bean:write name="phdProgram" property="presentationName" /></h3>

	<fr:form action="/phdEnrolmentsManagement.do?method=changeDegreeCurricularPlanConfig">
		<%@include file="/commons/degreeStructure/degreeCurricularPlanRendererConfigForm.jsp" %>
	</fr:form>
	
	<fr:view name="rendererConfig" />
</logic:notEmpty>

</logic:present>
