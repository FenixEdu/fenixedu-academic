<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></em>
<h2><bean:message key="label.resident" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2>


<bean:define id="monthOID" name="month" property="OID"/>

<p>
	<html:link page="<%= "/residenceEventManagement.do?method=manageResidenceEvents&monthOID="  + monthOID + (request.getParameter("sortBy") != null ? "&sortBy=" + request.getParameter("sortBy") : "")%>"> 
		&laquo; <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</p>

<fr:view name="person" schema="show.residence.person">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom2" />
	</fr:layout>
</fr:view>

<logic:notEmpty name="residenceEvents">
	<fr:view name="residenceEvents" schema="show.person.residenceEvents">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 tdcenter thlight mtop05" />
			<fr:property name="link(cancel)" value="<%= "/residenceEventManagement.do?method=cancelResidenceEvent&monthOID=" + monthOID%>"/>
			<fr:property name="key(cancel)" value="label.cancel.residence.event" />
			<fr:property name="param(cancel)" value="OID/event,person.OID/person,residenceMonth.OID/month" />
			<fr:property name="bundle(cancel)" value="RESIDENCE_MANAGEMENT_RESOURCES" />
			<fr:property name="visibleIfNot(cancel)" value="payed" />
			<fr:property name="link(pay)" value="<%= "/residenceEventManagement.do?method=preparePayResidenceEvent&monthOID=" + monthOID%>"/>
			<fr:property name="key(pay)" value="label.pay.residence.event" />
			<fr:property name="param(pay)" value="OID/event,person.OID/person,residenceMonth.OID/month" />
			<fr:property name="bundle(pay)" value="RESIDENCE_MANAGEMENT_RESOURCES" />
			<fr:property name="visibleIfNot(pay)" value="payed" />
			<fr:property name="sortBy" value="residenceMonth.year.year=desc,residenceMonth.month=desc"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="residenceEvents">
	<p class="mvert05">
		<em><bean:message key="message.person.has.no.events" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>.</em>
	</p>
</logic:empty>
