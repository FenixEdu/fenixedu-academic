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

<bean:define id="phdIndividualProgramProcess" name="phdIndividualProgramProcess" />
<bean:define id="phdIndividualProgramProcessId" name="phdIndividualProgramProcess" property="externalId" /> 

<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="phdIndividualProgramProcessId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>

<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="phdIndividualProgramProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>


<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="requests.historic"/></h3>
<bean:define id="historicalAcademicServiceRequests" name="phdIndividualProgramProcess" property="historicalAcademicServiceRequests"/>
<logic:notEmpty name="historicalAcademicServiceRequests">
	<fr:view name="historicalAcademicServiceRequests" >
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest">
			<fr:slot name="requestDate" readOnly="true" key="label.requestDate" layout="no-time" />
			<fr:slot name="activeSituationDate" readOnly="true" key="last.modification" layout="no-time" />
			<fr:slot name="urgentRequest" key="label.net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest.urgentRequest"/>	
			<fr:slot name="campus" key="campus" layout="null-as-label">
				<fr:property name="label" value="-"/>
				<fr:property name="subLayout" value="spaceInformation-presentationName-label" />
			</fr:slot>	    
			<fr:slot name="serviceRequestNumberYear"  />
			<fr:slot name="description" readOnly="true" key="label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.description"/>
			<fr:slot name="executionYear" layout="null-as-label">
		        <fr:property name="label" value="-"/>
		        <fr:property name="subLayout" value="values"/>
		        <fr:property name="subSchema" value="net.sourceforge.fenixedu.domain.ExecutionYear.view"/>
			</fr:slot>
			<fr:slot name="freeProcessed" key="label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.freeProcessed" />
			<fr:slot name="language"/>	
			<fr:slot name="academicServiceRequestSituationType" key="label.net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.academicServiceRequestSituationType" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mtop0" />
			<fr:property name="columnClasses" value="smalltxt acenter nowrap,smalltxt acenter nowrap,acenter,,acenter,tdhl1 nowrap,,acenter nowrap,nowrap" />
			<fr:property name="linkFormat(view)" value="/phdAcademicServiceRequestManagement.do?method=viewAcademicServiceRequest&amp;phdAcademicServiceRequestId=${externalId}&amp;fromHistory=true"/>
			<fr:property name="key(view)" value="view"/>
			<fr:property name="sortBy" value="requestDate=desc, activeSituation.situationDate=desc, urgentRequest=desc, description=asc"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="historicalAcademicServiceRequests">
	<p>
		<em>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.historical.academic.service.requests"/>
		</em>
	</p>
</logic:empty>
