<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<a href="http://www.ist.utl.pt/en/about-IST/global-cooperation/IST-EPFL/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<logic:notEmpty name="candidacyPeriod">
	<bean:define id="startDate" name="candidacyPeriod" property="start" type="org.joda.time.DateTime" />
	<bean:define id="endDate" name="candidacyPeriod" property="end" type="org.joda.time.DateTime" />
	
	<p><em><bean:message key="message.phd.epfl.application.out.of.candidacy.period" arg0="<%= startDate.toString("dd/MM/yyyy") %>" arg1="<%= endDate.toString("dd/MM/yyyy") %>" bundle="PHD_RESOURCES"/></em></p>
</logic:notEmpty>

<logic:empty name="candidacyPeriod">
	<p><em><bean:message key="message.phd.epfl.application.out.of.candidacy.period.not.found" bundle="PHD_RESOURCES"/></em></p>
</logic:empty>
