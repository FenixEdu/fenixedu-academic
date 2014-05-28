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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>

<html:xhtml/>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean"/>

<h2>
	<bean:define id="academicSituationType" name="bean" property="academicServiceRequestSituationType" type="net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType"/>
	<logic:equal name="academicSituationType" value="NEW">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="new.requests" />
	</logic:equal>
	<logic:equal name="academicSituationType" value="PROCESSING">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="processing.requests" />
	</logic:equal>
	<logic:equal name="academicSituationType" value="CONCLUDED">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="concluded.requests" />
	</logic:equal>
</h2>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
		<p><span class="warning0"><bean:write name="messages" /></span></p>
	</html:messages>
</logic:messagesPresent>

<bean:define id="newRequestUrl">
	/academicServiceRequestsManagement.do?method=processNewAcademicServiceRequest&academicSituationType=<bean:write name="academicSituationType" property="name"/>
</bean:define>
<bean:define id="processRequestUrl">
	/academicServiceRequestsManagement.do?method=prepareConcludeAcademicServiceRequest
</bean:define>
<bean:define id="deliveredRequestUrl">
	/academicServiceRequestsManagement.do?method=deliveredAcademicServiceRequest
</bean:define>
<bean:define id="paymentsUrl">
	/payments.do?method=showOperations
</bean:define>

<%
	String sortCriteria = request.getParameter("sortBy");
    if (sortCriteria == null) {
    	sortCriteria = "activeSituationDate=desc";
    }
%>

<fr:form action="/academicServiceRequestsManagement.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="academicSituationType" value="<%=academicSituationType.getName()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="serviceRequestYear" value="<%=bean.getServiceRequestYear().toString()%>"/>

	<fr:edit id="bean" 
		name="bean"
		visible="false"/>
	<fr:edit id="bean-service.request.year" name="bean" schema="AcademicServiceRequestBean.service.request.year.edit" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight mtop025 thmiddle"/>
			<fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/>
	</html:submit>
</fr:form>

<p class="mtop5">
<logic:equal name="academicSituationType" value="NEW">
	<p class="mtop2 mbottom05">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="academic.service.requests.requested.in.student.portal"/>
	</p>
</logic:equal>
<logic:notEqual name="academicSituationType" value="NEW">
	<p class="mtop2 mbottom05">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="academic.service.requests.treated.by.employee"/> <bean:write name="bean" property="responsible.username"/>:
	</p>
</logic:notEqual>

<logic:empty name="specificRequests">
	<p class="mtop05">
		<em>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.academic.service.requests.found" />
		</em>
	</p>
</logic:empty>
	
<logic:notEmpty name="specificRequests">
	<fr:view name="specificRequests" schema="DocumentRequest.view-documentPurposeTypeInformation" >
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle4 tdcenter mtop05" />
			<fr:property name="rowClasses" value="bgwhite," />
			<fr:property name="groupLinks" value="true" />
			<fr:property name="columnClasses" value="smalltxt,smalltxt,smalltxt  aleft nowrap,smalltxt,smalltxt,smalltxt nowrap,smalltxt nowrap," />
			
			<fr:property name="linkFormat(processing)" value="<%= newRequestUrl + "&academicServiceRequestId=${externalId}" %>"/>
			<fr:property name="key(processing)" value="processing"/>
			<fr:property name="bundle(processing)" value="APPLICATION_RESOURCES"/>
			<fr:property name="visibleIf(processing)" value="newRequest"/>
            <fr:property name="visibleIfNot(processing)" value="piggyBackedOnRegistry"/>

			<fr:property name="linkFormat(concluded)" value="<%= processRequestUrl + "&academicServiceRequestId=${externalId}" %>"/>
			<fr:property name="key(concluded)" value="conclude"/>
			<fr:property name="bundle(concluded)" value="APPLICATION_RESOURCES"/>
			<fr:property name="visibleIf(concluded)" value="processing"/>
            <fr:property name="visibleIfNot(concluded)" value="piggyBackedOnRegistry"/>

			<fr:property name="linkFormat(delivered)" value="<%= deliveredRequestUrl + "&academicServiceRequestId=${externalId}" %>"/>
			<fr:property name="key(delivered)" value="deliver"/>
			<fr:property name="bundle(delivered)" value="APPLICATION_RESOURCES"/>
			<fr:property name="visibleIf(delivered)" value="concluded"/>

			<fr:property name="linkFormat(payments)" value="<%= paymentsUrl + "&personId=${registration.person.externalId}" %>"/>
			<fr:property name="key(payments)" value="payments"/>
			<fr:property name="bundle(payments)" value="APPLICATION_RESOURCES"/>
			<fr:property name="visibleIfNot(payments)" value="isPayed"/>
			<fr:property name="visibleIf(payments)" value="paymentsAccessible"/>
			
			<fr:property name="sortBy" value="<%= sortCriteria %>"/>
			<fr:property name="sortUrl" value="<%= "/academicServiceRequestsManagement.do?method=search&academicSituationType=" + academicSituationType.getName() + "&serviceRequestYear=" + bean.getServiceRequestYear() %>"/>
			<fr:property name="sortParameter" value="sortBy"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<p class="mtop2 mbottom05">
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="academic.service.requests.treated.by.others"/>:
</p>
<logic:empty name="remainingRequests">
	<p>
		<em>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.academic.service.requests.found" />
		</em>
	</p>
</logic:empty>
<logic:notEmpty name="remainingRequests">

	<bean:define id="url" type="java.lang.String">/academicAdministration/academicServiceRequestsManagement.do?method=search&amp;academicSituationType=<%= academicSituationType.getName() %>&amp;serviceRequestYear=<bean:write name="bean" property="serviceRequestYear"/>&amp;sortBy=<%=sortCriteria%></bean:define>
	<cp:collectionPages url="<%= url %>" 
		pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>

	<fr:view name="resultPage" schema="DocumentRequest.view-documentPurposeTypeInformation">
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle4 tdcenter mtop05" />
			<fr:property name="rowClasses" value="bgwhite," />
			<fr:property name="groupLinks" value="true" />
			<fr:property name="columnClasses" value="smalltxt,smalltxt,smalltxt  aleft nowrap,smalltxt,smalltxt,smalltxt nowrap,smalltxt nowrap," />

			<fr:property name="linkFormat(processing)" value="<%= newRequestUrl + "&academicServiceRequestId=${externalId}" %>"/>
			<fr:property name="key(processing)" value="processing"/>
			<fr:property name="bundle(processing)" value="APPLICATION_RESOURCES"/>
			<fr:property name="visibleIf(processing)" value="newRequest"/>
            <fr:property name="visibleIfNot(processing)" value="piggyBackedOnRegistry"/>

			<fr:property name="linkFormat(concluded)" value="<%= processRequestUrl + "&academicServiceRequestId=${externalId}" %>"/>
			<fr:property name="key(concluded)" value="conclude"/>
			<fr:property name="bundle(concluded)" value="APPLICATION_RESOURCES"/>
			<fr:property name="visibleIf(concluded)" value="processing"/>
            <fr:property name="visibleIfNot(concluded)" value="piggyBackedOnRegistry"/>

			<fr:property name="linkFormat(delivered)" value="<%= deliveredRequestUrl + "&academicServiceRequestId=${externalId}" %>"/>
			<fr:property name="key(delivered)" value="deliver"/>
			<fr:property name="bundle(delivered)" value="APPLICATION_RESOURCES"/>
			<fr:property name="visibleIf(delivered)" value="concluded"/>

			<fr:property name="linkFormat(payments)" value="<%= paymentsUrl + "&personId=${registration.person.externalId}" %>"/>
			<fr:property name="key(payments)" value="payments"/>
			<fr:property name="bundle(payments)" value="APPLICATION_RESOURCES"/>
			<fr:property name="visibleIfNot(payments)" value="isPayed"/>
			<fr:property name="visibleIf(payments)" value="paymentsAccessible"/>
			
			<fr:property name="sortBy" value="<%= sortCriteria %>"/>
			<fr:property name="sortUrl" value="<%= "/academicServiceRequestsManagement.do?method=search&academicSituationType=" + academicSituationType.getName() + "&serviceRequestYear=" + bean.getServiceRequestYear() %>"/>
			<fr:property name="sortParameter" value="sortBy"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>