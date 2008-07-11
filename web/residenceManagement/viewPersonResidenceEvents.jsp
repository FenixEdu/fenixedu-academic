<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></em>
<h2><bean:message key="label.debtManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2>


<fr:view name="person" schema="show.residence.person">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thleft" />
	</fr:layout>
</fr:view>

<logic:notEmpty name="residenceEvents">
	<fr:view name="residenceEvents" schema="show.person.residenceEvents">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="link(cancel)" value="/residenceEventManagement.do?method=cancelResidenceEvent" />
			<fr:property name="key(cancel)" value="label.cancel.residence.event" />
			<fr:property name="param(cancel)" value="OID/event,person.OID/person,residenceMonth.OID/month" />
			<fr:property name="bundle(cancel)" value="RESIDENCE_MANAGEMENT_RESOURCES" />
			<fr:property name="visibleIfNot(cancel)" value="payed" />
			<fr:property name="sortBy" value="residenceMonth.year.year,residenceMonth.month"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="residenceEvents">
	<bean:message key="message.person.empty.events" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
</logic:empty>
