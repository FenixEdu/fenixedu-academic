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

<%! 
	private static String f(String format, Object ... args) {
    	return String.format(format, args);
	}
%>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.curriculum.validation.set.end.stage.date" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<bean:define id="studentCurricularPlanId" name="studentCurricularPlan" property="externalId"/>

<p>
	<html:link page="<%= "/curriculumValidation.do?method=prepareCurriculumValidation&amp;studentCurricularPlanId=" + studentCurricularPlanId  %>">
		<bean:message key="label.back" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
</p>

<logic:equal name="studentCurriculumValidationAllowed" value="false">
	<bean:message key="message.curriculum.validation.not.allowed" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:equal>

<logic:equal name="studentCurriculumValidationAllowed" value="true">
	<p class="mtop15">
		<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="notDelivered.requests"/></b>
		<bean:define id="concludedAcademicServiceRequests" name="registration" property="toDeliverAcademicServiceRequests"/>
		<logic:notEmpty name="concludedAcademicServiceRequests">
			<fr:view name="concludedAcademicServiceRequests" schema="AcademicServiceRequest.view">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thlight mtop0" />
					<fr:property name="columnClasses" value="smalltxt acenter nowrap,smalltxt acenter nowrap,acenter,,acenter,tdhl1 nowrap,,,acenter nowrap,nowrap" />
	
					<fr:property name="linkFormat(print)" value="<%= f("/curriculumValidationDocumentRequestsManagement.do?method=preparePrintDocument&amp;documentRequestId=${externalId}&amp;studentCurricularPlanId=%s", studentCurricularPlanId) %>" />
					<fr:property name="key(print)" value="print"/>
					<fr:property name="visibleIf(print)" value="toPrint"/>
					
					<fr:property name="sortBy" value="requestDate=desc, activeSituation.situationDate=desc, urgentRequest=desc, description=asc"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		<logic:empty name="concludedAcademicServiceRequests">
			<p>
				<em>
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.concluded.academic.service.requests"/>
				</em>
			</p>
		</logic:empty>
	</p>	
</logic:equal>