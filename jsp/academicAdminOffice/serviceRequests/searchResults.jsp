<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2>
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
		<p><span class="warning0"><bean:write name="messages" /></p>
	</html:messages>
</logic:messagesPresent>

<bean:define id="newRequestUrl">
	/academicServiceRequestsManagement.do?method=processNewAcademicServiceRequest&academicSituationType=<bean:write name="academicSituationType" property="name"/>
</bean:define>
<bean:define id="processRequestUrl">
	/academicServiceRequestsManagement.do?method=prepareConcludeAcademicServiceRequest
</bean:define>

<fr:form action="/academicServiceRequestsManagement.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>

	<logic:notEqual name="academicSituationType" value="NEW">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="academic.service.requests.treated.by.employee"/> <bean:write name="employee" property="roleLoginAlias"/>:

		<logic:empty name="employeeRequests">
			<p class="mtop1 mleft1">
				<em>
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.academic.service.requests.found" />
				</em>
			</p>
		</logic:empty>
		<logic:notEmpty name="employeeRequests">
			<fr:view name="employeeRequests" schema="DocumentRequest.view-documentPurposeTypeInformation">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 tdcenter" />
					<fr:property name="rowClasses" value="bgwhite," />
					
					<fr:property name="columnClasses" value="smalltxt,smalltxt,smalltxt  aleft nowrap,smalltxt,smalltxt,smalltxt nowrap,smalltxt nowrap," />
					<fr:property name="linkFormat(processing)" value="<%= newRequestUrl + "&academicServiceRequestId=${idInternal}" %>"/>
					<fr:property name="key(processing)" value="processing"/>
					<fr:property name="visibleIf(processing)" value="newRequest"/>
					<fr:property name="linkFormat(concluded)" value="<%= processRequestUrl + "&academicServiceRequestId=${idInternal}" %>"/>
					<fr:property name="key(concluded)" value="conclude"/>
					<fr:property name="visibleIf(concluded)" value="processing"/>
					<fr:property name="sortBy" value="urgentRequest=desc,creationDate=asc"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>

		<p class="mtop2">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="academic.service.requests.treated.by.others"/>:
		</p>
	</logic:notEqual>

	<logic:empty name="academicServiceRequests">
		<p class="mtop1 mleft1">
			<em>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.academic.service.requests.found" />
			</em>
		</p>
	</logic:empty>
	<logic:notEmpty name="academicServiceRequests">
		<fr:view name="academicServiceRequests" schema="DocumentRequest.view-documentPurposeTypeInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 tdcenter" />
				<fr:property name="rowClasses" value="bgwhite," />
				
				<fr:property name="columnClasses" value="smalltxt,smalltxt,smalltxt  aleft nowrap,smalltxt,smalltxt,smalltxt nowrap,smalltxt nowrap," />
				<fr:property name="linkFormat(processing)" value="<%= newRequestUrl + "&academicServiceRequestId=${idInternal}" %>"/>
				<fr:property name="key(processing)" value="processing"/>
				<fr:property name="visibleIf(processing)" value="newRequest"/>
				<fr:property name="linkFormat(concluded)" value="<%= processRequestUrl + "&academicServiceRequestId=${idInternal}" %>"/>
				<fr:property name="key(concluded)" value="conclude"/>
				<fr:property name="visibleIf(concluded)" value="processing"/>
				<fr:property name="sortBy" value="urgentRequest=desc,creationDate=asc"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

</fr:form>
