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

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<jsp:include page="/phd/candidacy/publicProgram/epfl/createCandidacyStepsBreadcrumb.jsp?step=2"></jsp:include>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean.focus.area" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<fr:form id="candidacyForm" action="/applications/epfl/phdProgramCandidacyProcess.do" >
	<html:hidden name="method" property="method" value="createCandidacyStepThree"/>
	<html:hidden property="order" value="1"/>

	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
	
	<div class="fs_form">
		<p class="mvert05"><span><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></span></p>
	</div>

	<div class="fs_form">
	<fieldset style="display: block;">
		<legend><bean:message key="title.public.phd.focus.area" bundle="PHD_RESOURCES"/></legend>
	
		<fr:edit id="candidacyBean.focus.area" name="candidacyBean" 
				 schema="Public.PhdProgramCandidacyProcessBean.focus.area">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepTwoInvalid" />
			<fr:destination name="postBack" path="/applications/epfl/phdProgramCandidacyProcess.do?method=prepareCreateCandidacyStepTwoFocusAreaPostback" />
		</fr:edit>
		
		<logic:notEmpty name="candidacyBean" property="thesisSubjectBeans">
		
		<h3 class="mtop3"><bean:message key="title.phd.thesis.subject.ranking" bundle="PHD_RESOURCES" /></h3>
		<p class="mtop1"><em><bean:message key="message.phd.thesis.subject.rank" bundle="PHD_RESOURCES" />:</em></p>
		
		<table class="tstyle2 thlight mtop15">
			<tr>
				<td class="aright"><bean:message key="label.order" bundle="PHD_RESOURCES"/></td>
				<td><bean:message key="label.net.sourceforge.fenixedu.domain.phd.ThesisSubject.name" bundle="PHD_RESOURCES"/></td>
				<td><bean:message key="label.phd.guiding" bundle="PHD_RESOURCES"/></td>
				<td><bean:message key="label.phd.guiding" bundle="PHD_RESOURCES"/></td>
			</tr>
			<logic:iterate id="thesisSubjectBean" name="candidacyBean" property="thesisSubjectBeans" type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdThesisSubjectOrderBean">
				<tr>
					<td class="aright"><fr:view name="thesisSubjectBean" property="order"/></td>
					<td><fr:view name="thesisSubjectBean" property="thesisSubject.name.content"/></td>
					<td><fr:view name="thesisSubjectBean" property="thesisSubject.teacher.person.name"/></td>
					<td>
						<logic:notEmpty name="thesisSubjectBean" property="thesisSubject.externalAdvisorName">
							<fr:view name="thesisSubjectBean" property="thesisSubject.externalAdvisorName"/>
						</logic:notEmpty>
						<logic:empty name="thesisSubjectBean" property="thesisSubject.externalAdvisorName">
							-
						</logic:empty>
					</td>
					<td>
						<html:image onclick="<%=	"javascript:" +
													"var form = document.getElementById('candidacyForm');" +
													"form.method.value='moveUpThesisSubjectForCandidacyStepTwo';" +
													"form.order.value='" + thesisSubjectBean.getOrder() + "';" +
													"form.submit();" %>"
									border="0" src="<%= request.getContextPath() + "/images/move_up.gif"%>" altKey="up" bundle="IMAGE_RESOURCES"/>
															
						<html:image onclick="<%=	"javascript:" +
													"var form = document.getElementById('candidacyForm');" +
													"form.method.value='moveDownThesisSubjectForCandidacyStepTwo';" +
													"form.order.value='" + thesisSubjectBean.getOrder() + "';" +
													"form.submit();" %>"
									border="0" src="<%= request.getContextPath() + "/images/move_down.gif"%>" altKey="down" bundle="IMAGE_RESOURCES"/>
					</td>
				</tr>
			</logic:iterate>
		</table>
		</logic:notEmpty>
		
	</fieldset>
	</div>
	
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="<%= "document.getElementById('skipValidationId').value='true'; document.getElementById('method').value='returnCreateCandidacyStepOne'; document.getElementById('candidacyForm').submit();" %>">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.submit"/> »</html:submit>
	</p>

</fr:form>
