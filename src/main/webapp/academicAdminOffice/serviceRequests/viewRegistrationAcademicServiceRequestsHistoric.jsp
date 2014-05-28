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
<html:xhtml/>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="requests.historic"/></h2>

<html:messages id="messages" message="true">
	<p><span class="error0"><bean:write name="messages" bundle="ACADEMIC_OFFICE_RESOURCES"/></span></p>
</html:messages>

<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail.short" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
	</fr:layout>
</fr:view>





<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="requests.historic"/></h3>
<bean:define id="historicalAcademicServiceRequests" name="registration" property="historicalAcademicServiceRequests"/>
<logic:notEmpty name="historicalAcademicServiceRequests">
	<fr:view name="historicalAcademicServiceRequests" schema="AcademicServiceRequest.view-for-given-registration">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mtop0" />
			<fr:property name="columnClasses" value="smalltxt acenter nowrap,smalltxt acenter nowrap,acenter,,acenter,tdhl1 nowrap,,acenter nowrap,nowrap" />
			<fr:property name="linkFormat(view)" value="/academicServiceRequestsManagement.do?method=viewAcademicServiceRequest&academicServiceRequestId=${externalId}&backAction=academicServiceRequestsManagement&backMethod=viewRegistrationAcademicServiceRequestsHistoric"/>
			<fr:property name="key(view)" value="view"/>

            <fr:property name="linkFormat(print)" value="/documentRequestsManagement.do?method=downloadDocument&amp;documentRequestId=${externalId}&amp;"/>
            <fr:property name="key(print)" value="print"/>
            <fr:property name="visibleIf(print)" value="downloadPossible"/>
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


<p class="mtop2">
	<html:form action="/student.do?method=visualizeRegistration">
		<bean:define id="registrationID" name="registration" property="externalId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.registrationID" property="registrationID" value="<%=registrationID.toString()%>"/>
		<html:submit><bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</html:form>
</p>
