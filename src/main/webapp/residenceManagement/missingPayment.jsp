<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<%@page import="net.sourceforge.fenixedu.domain.accounting.ResidenceEvent"%>
<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></em>
<h2><bean:message key="label.missingPayments" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2>

<logic:notEmpty name="list">
	<fr:view name="list" schema="show.residenceEvent.with.open.date">
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle1 tdcenter thlight mtop05" />
			<fr:property name="columnClasses" value=",,aleft,,,,," />
			<fr:property name="sortParameter" value="sortBy" />
			<fr:property name="sortUrl" value="<%= "/residenceManagement.do?method=missingPayments"%>" />
			<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "person.student.number=asc" : request.getParameter("sortBy") %>" />
			<fr:property name="sortableSlots" value="person.student.number, person.socialSecurityNumber, person.name, roomValue, room,eventStateDate" />

		</fr:layout>
		<fr:destination name="personLink"
			path="<%= "/residenceEventManagement.do?method=viewPersonResidenceEvents&person=${person.OID}&monthOID=${residenceMonth.OID}" + (request.getParameter("sortBy") != null ? "&sortBy=" + request.getParameter("sortBy") : "") %>" />
	</fr:view>
</logic:notEmpty>