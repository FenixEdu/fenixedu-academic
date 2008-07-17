<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<!-- viewClosedIdentityRequests.jsp -->

<em><bean:message key="operator.module.title" bundle="MANAGER_RESOURCES"/></em>
<h2><bean:message key="alumni.closed.identity.requests" bundle="MANAGER_RESOURCES"/></h2>


<html:link page="/alumni.do?method=prepareIdentityRequestsList">
	<bean:message key="label.back" bundle="MANAGER_RESOURCES"/>
</html:link>

	<logic:empty name="identityRequestsList">
		<p><em><bean:message key="alumni.no.identity.requests" bundle="MANAGER_RESOURCES" /></em></p>
	</logic:empty>

	<logic:notEmpty name="identityRequestsList">
		<p class="mbottom05"><bean:message key="label.closed.requests" bundle="MANAGER_RESOURCES"/>:</p>
		<fr:view name="identityRequestsList" layout="tabular" schema="alumni.identity.closed.request.list" >
			<fr:layout>
				<fr:property name="classes" value="tstyle1 tdcenter mtop05"/>
				<fr:property name="columnClasses" value="nowrap acenter,aleft,acenter,acenter,acenter,"/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="decisionDateTime,fullName"/>
	           	<fr:property name="sortBy" value="creationDateTime=desc" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
