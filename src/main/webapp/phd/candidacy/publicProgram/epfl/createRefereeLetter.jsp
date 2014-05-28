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
<%@page import="org.fenixedu.bennu.core.domain.Bennu"%>
<%@ page language="java" %>
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

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<h2>Reference Letter</h2>

<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=createRefereeLetterBean.overall.promise" />
<%--  ### End of Error Messages  ### --%>

<logic:present name="createRefereeLetterBean">

<p class="mbottom05"><strong>Applicant's Name:</strong><bean:write name="createRefereeLetterBean" property="person.name" /></p>
<p class="mtop05"><strong>Focus Area:</strong>
	<logic:notEmpty name="createRefereeLetterBean" property="focusArea">
		<bean:write name="createRefereeLetterBean" property="focusArea.name.content" />
	</logic:notEmpty>
	<logic:empty name="createRefereeLetterBean" property="focusArea"> -- </logic:empty>
</p>

<p>You have been indicated as a referee on behalf of a prospective student applying to a FCT Doctoral Programme. We would appreciate your personal evaluation concerning the applicant's intellectual ability, aptitude in research or professional skill.</p>

<ul>
	<li>For the recommendation to be complete this form has to be fully filled out and an additional letter has to be attached, commenting on the applicant's character, the quality of previous work and the promise of productive scholarship.</li>
	<li>If the applicant's first language is not English, please evaluate her / his proficiency to read, write and speak English.</li>
	<li>If you have any reason to believe that the applicant should not be considered, please give your reasons.</li>
	<li>If you know of other students who have entered institutions in <%= Bennu.getInstance().getInstitutionUnit().getCountry().getName() %> from your institution, a comparison will be especially valuable.</li>
</ul>

<p>Please feel free to add information about your own educational and professional background if you feel that such information will enhance our understanding of your evaluation.</p>


<div class="fs_form">	

	<fr:form id="refereeForm" action="/applications/epfl/phdProgramCandidacyProcess.do" encoding="multipart/form-data">
		<input type="hidden" id="methodForm" name="method" value="createRefereeLetter" />
		<fr:edit id="createRefereeLetterBean" name="createRefereeLetterBean" visible="false" />

		<fieldset style="display: block;">
			<legend>Referee form</legend>
			<p class="mtop05"><span><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></span></p>
		
			<fr:edit id="Public.PhdCandidacyRefereeLetterBean.applicant.information" name="createRefereeLetterBean" 
				schema="Public.PhdCandidacyRefereeLetterBean.applicant.information">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createRefereeLetterInvalid" />
			</fr:edit>
			
			<br/>
			<p class="mvert05">On the scale below, please rate the applicant relative to others you have taught who have gone on to graduate studies.</p>
			<fr:edit id="createRefereeLetterBean.overall.promise" name="createRefereeLetterBean" 
				schema="Public.PhdCandidacyRefereeLetterBean.overall.promise">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createRefereeLetterInvalid" />
			</fr:edit>
			
			<br/>
			<fr:edit id="createRefereeLetterBean.comments" name="createRefereeLetterBean" 
				schema="Public.PhdCandidacyRefereeLetterBean.comments">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
			
				<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createRefereeLetterInvalid" />
			</fr:edit>
		
			<br/>
			<p class="mvert05">Your personal information as we received it is displayed below. Please correct as necessary: </p>
			<fr:edit id="createRefereeLetterBean.referee.information" name="createRefereeLetterBean" 
				schema="Public.PhdCandidacyRefereeLetterBean.referee.information">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
			
				<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createRefereeLetterInvalid" />
			</fr:edit>
		</fieldset>
		
		<p>
			Once you have submitted the recommendation, you will no longer be able to modify it. Thank you for the time and effort you have taken in responding to this request. We greatly appreciate your opinion on this application. If you have any questions or require further information, please contact the doctoral program at fct-doctoral-programmes@ist.utl.pt
		</p>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
	</fr:form>
</div>

</logic:present>
