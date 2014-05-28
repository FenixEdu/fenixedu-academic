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

<%@page import="net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.providers.NotClosedExecutionPeriodsProvider"%>
<%@page import="net.sourceforge.fenixedu.domain.EnrolmentPeriod"%>

<html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manage.enrolment.periods" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<fr:form action="/managePhdEnrolmentPeriods.do?method=manageEnrolmentPeriods">

	<fr:edit id="manageEnrolmentsBean" name="manageEnrolmentsBean">
		
		<fr:schema bundle="PHD_RESOURCES" type="<%= ManageEnrolmentsBean.class.getName() %>">
			<fr:slot name="semester" layout="menu-select-postback">
				<fr:property name="providerClass" value="<%= NotClosedExecutionPeriodsProvider.class.getName()  %>"/>
				<fr:property name="format" value="${qualifiedName}" />
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="postback" path="/managePhdEnrolmentPeriods.do?method=manageEnrolmentPeriods" />
	</fr:edit>
</fr:form>

<html:link action="/managePhdEnrolmentPeriods.do?method=prepareCreateEnrolmentPeriod" paramId="executionIntervalId" paramName="manageEnrolmentsBean" paramProperty="semester.externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.phd.create.enrolment.period"/>
</html:link>

<logic:empty name="manageEnrolmentsBean" property="enrolmentPeriods">
	<br/>
	<br/>
	<em><bean:message key="label.phd.no.enrolment.periods.found" bundle="PHD_RESOURCES" /></em>
</logic:empty>

<fr:view name="manageEnrolmentsBean" property="enrolmentPeriods">
	
	<fr:schema bundle="PHD_RESOURCES" type="<%= EnrolmentPeriod.class.getName() %>">
		<fr:slot name="degreeCurricularPlan.presentationName" />
		<fr:slot name="startDate" />
		<fr:slot name="endDate" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop10" />
		<fr:property name="sortBy" value="degreeCurricularPlan.presentationName,startDate" />
	
		<fr:link name="edit" label="label.edit,PHD_RESOURCES" order="1" link="/managePhdEnrolmentPeriods.do?method=prepareEditEnrolmentPeriod&periodId=${externalId}" />
		<fr:link name="delete" label="label.delete,PHD_RESOURCES" order="2" confirmation="label.phd.delete.enrolment.period.confirmation,PHD_RESOURCES" 
				 link="/managePhdEnrolmentPeriods.do?method=deleteEnrolmentPeriod&periodId=${externalId}" />
	
	</fr:layout>
</fr:view>
