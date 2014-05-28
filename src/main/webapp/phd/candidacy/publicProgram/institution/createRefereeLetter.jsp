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
<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>


<%-- ### Title #### --%>
<div class="breadcumbs">
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />

	<bean:message key="title.phd.candidacy.referee.letter" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<h2><bean:message key="title.phd.candidacy.referee.letter" bundle="PHD_RESOURCES" /></h2>

<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=createRefereeLetterBean.overall.promise" />
<%--  ### End of Error Messages  ### --%>

<logic:present name="createRefereeLetterBean">

<p class="mbottom05"><strong><bean:message key="label.phd.institution.public.candidacy.applicant.name" bundle="PHD_RESOURCES" />: </strong><bean:write name="createRefereeLetterBean" property="person.name" /></p>

<bean:define id="doctoralProgramme" name="refereeLetterHash" property="phdProgramCandidacyProcess.individualProgramProcess.phdProgram.name.content" type="String"/>

<bean:message key="message.phd.institution.public.candidacy.fill.referee.letter" bundle="PHD_RESOURCES" arg0="<%= doctoralProgramme %>" arg1="<%= Unit.getInstitutionAcronym() %>"/>

<bean:define id="hash" name="refereeLetterHash" property="value" />
<div class="fs_form">	

	<fr:form id="refereeForm" action="<%=  "/applications/phd/phdProgramApplicationProcess.do?method=createRefereeLetter&hash=" + hash %>" encoding="multipart/form-data">
		<fr:edit id="createRefereeLetterBean" name="createRefereeLetterBean" visible="false" />

		<fieldset style="display: block;">
			<legend><bean:message key="label.phd.institution.public.candidacy.referee.form" bundle="PHD_RESOURCES" /></legend>
		
			<fr:edit id="Public.PhdCandidacyRefereeLetterBean.applicant.information" name="createRefereeLetterBean" 
				schema="Public.PhdCandidacyRefereeLetterBean.applicant.information">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
						<fr:property name="optionalMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=createRefereeLetterInvalid&hash=" + hash %>"/>
			</fr:edit>
			

			<p class="mtop2"><bean:message key="message.phd.institution.public.candidacy.fill.referee.overall.promise" bundle="PHD_RESOURCES" /></p>
			<fr:edit id="createRefereeLetterBean.overall.promise" name="createRefereeLetterBean" 
				schema="Public.PhdCandidacyRefereeLetterBean.overall.promise">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
						<fr:property name="optionalMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=createRefereeLetterInvalid&hash=" + hash %>" />
			</fr:edit>
			

			<fr:edit id="createRefereeLetterBean.comments" name="createRefereeLetterBean" 
				schema="Public.PhdCandidacyRefereeLetterBean.comments">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
						<fr:property name="optionalMarkShown" value="true" />
				</fr:layout>
			
				<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=createRefereeLetterInvalid&hash=" + hash %>" />
			</fr:edit>
		

			<p class="mtop2"><bean:message key="message.phd.institution.public.candidacy.fill.referee.personal.data" bundle="PHD_RESOURCES" />: </p>
			<fr:edit id="createRefereeLetterBean.referee.information" name="createRefereeLetterBean" 
				schema="Public.PhdCandidacyRefereeLetterBean.referee.information">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
						<fr:property name="optionalMarkShown" value="true" />
				</fr:layout>
			
				<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=createRefereeLetterInvalid&hash=" + hash %>" />
			</fr:edit>
		</fieldset>
		
		<p class="mvert15">
			<bean:message key="message.phd.institution.public.candidacy.fill.referee.submit.only.once" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="PHD_RESOURCES" />
		</p>
		
		<html:submit ><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
	</fr:form>
</div>

</logic:present>
