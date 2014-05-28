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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>en/education/fct-phd-programmes/">FCT Doctoral Programmes</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<br/>

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<h2>Reference Letter</h2>

<%-- ### End of Title ### --%>

<logic:present name="has-letter">
	<p>You have already submitted the recommendation shown below. Thank you for the time and effort you have taken in responding to this request. We greatly appreciate your opinion on this application. If you have any questions or require further information, please contact us at fct-doctoral-programmes@ist.utl.pt</p>
	
	<fr:view name="letter">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter">
			<fr:slot name="howLongKnownApplicant" />
			<fr:slot name="capacity" />
			<fr:slot name="comparisonGroup" />
			<fr:slot name="rankInClass" />
			<fr:slot name="academicPerformance" />
			<fr:slot name="socialAndCommunicationSkills" />
			<fr:slot name="potencialToExcelPhd" />
			<fr:slot name="file" layout="link" />
			<fr:slot name="comments" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="thlight thleft"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>		
		</fr:layout>
	</fr:view>
</logic:present>

<logic:present name="created-with-success">
	<p>Thank you for the time and effort you have taken in responding to this request. We greatly appreciate your opinion on this application. If you have any questions or require further information, please contact us at fct-doctoral-programmes@ist.utl.pt</p>
</logic:present>

<logic:present name="no-information">
	<p>No information found</p>
</logic:present>