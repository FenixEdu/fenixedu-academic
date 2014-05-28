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
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdParticipant"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%-- ### Title #### --%>
<h2><bean:message key="title.phd.process.transfer" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="process" name="process" />
<bean:define id="processId" name="process" property="externalId" />

<p>
	<html:link action="/phdIndividualProgramProcess.do?method=preparePhdConfigurationManagement" paramId="processId" paramName="process" paramProperty="externalId">
		« <bean:message bundle="PHD_RESOURCES" key="label.back" />
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<logic:equal name="enrolmentsInCurrentYear" value="true">
	<em><bean:message key="message.phd.student.has.enrolments.current.year" bundle="PHD_RESOURCES" /></em>
</logic:equal>

<logic:equal name="enrolmentsInCurrentYear" value="false">

<logic:empty name="studentProcesses">
	<em><bean:message key="message.phd.student.other.processes.empty" bundle="PHD_RESOURCES" /></em>
</logic:empty>

<logic:notEmpty name="studentProcesses"> 

<p><strong><bean:message key="label.phd.transfer.process" bundle="PHD_RESOURCES"/></strong></p>

<p><bean:message  key="label.phd.actual.process" bundle="PHD_RESOURCES"/></p>

<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
	</fr:layout>
</fr:view>

<em><strong><bean:message key="message.choose.new.phd.process.to.transfer" bundle="PHD_RESOURCES" /></strong></em>

<fr:view name="studentProcesses">
	<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess">
		<fr:slot name="processNumber" />
		<fr:slot name="phdProgram" layout="null-as-label">
			<fr:property name="subLayout" value="values" />
			<fr:property name="subSchema" value="PhdProgram.name" />
		</fr:slot>
		<fr:slot name="activeState" layout="phd-enum-renderer">
			<fr:property name="classes" value="italic"/>
		</fr:slot>
		<fr:slot name="executionYear" layout="format">
			<fr:property name="format" value="${year}" />
		</fr:slot>
		<fr:slot name="phdStudentNumber" layout="null-as-label" />
		<fr:slot name="whenStartedStudies" />
		<fr:slot name="whenCreated" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
		
		<fr:link 	name="choose" 
					link="<%= "/phdIndividualProgramProcess.do?method=prepareFillRemarksOnTransfer&amp;destinyId=${externalId}&amp;processId=" + processId %>" 
					label="link.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.choose.process,PHD_RESOURCES"/>
	</fr:layout>
</fr:view>

</logic:notEmpty>

</logic:equal>
