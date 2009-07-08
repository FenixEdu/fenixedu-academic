<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/about-IST/global-cooperation/IST-EPFL/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<jsp:include page="/phd/candidacy/publicProgram/createCandidacyStepsBreadcrumb.jsp?step=2"></jsp:include>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean.focus.area" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<fr:form id="candidacyForm" action="/candidacies/phdProgramCandidacyProcess.do" >

	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodId" name="method" value="createCandidacyStepThree"/>
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>

	<div class="fs_form">
		<p class="mvert05"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>
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
			<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepTwoInvalid" />
		</fr:edit>
	</fieldset>
	</div>
	
	<div class="fs_form">
	<fieldset style="display: block;">
		<legend><bean:message key="title.public.phd.thesis.title" bundle="PHD_RESOURCES"/></legend>

		<fr:edit id="candidacyBean.thesis.title" name="candidacyBean" 
				 schema="Public.PhdProgramCandidacyProcessBean.thesis.title">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
		</fr:edit>
	</fieldset>
	</div>
	
	<div class="fs_form">
	<fieldset style="display: block;">
		<legend><bean:message key="title.public.phd.guidings" bundle="PHD_RESOURCES"/></legend>

		<logic:notEmpty name="candidacyBean" property="guidings">
			<logic:iterate id="guiding" name="candidacyBean" property="guidings" indexId="index">
				<strong><%= index.intValue() + 1 %>.</strong>
				<fr:edit id="<%= "candidacyBean.guiding" + index %>" name="guiding" schema="Public.PhdProgramGuidingBean.edit">
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
			        	<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepTwoInvalid" />
				</fr:edit>
				<p class="mtop05"><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeGuiding\"; document.getElementById(\"candidacyForm\").submit();" %>' href="#" >- <bean:message key="label.remove" bundle="PHD_RESOURCES"/></a></p>
			</logic:iterate>
		</logic:notEmpty>
		<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"methodId\").value=\"addGuiding\"; document.getElementById(\"candidacyForm\").submit();" %>' href="#" >+ <bean:message key="label.add" bundle="PHD_RESOURCES"/></a></p>
	</fieldset>	
	</div>

	<div class="fs_form">
	<fieldset style="display: block;">
		<legend><bean:message key="title.public.phd.qualifications" bundle="PHD_RESOURCES"/></legend>

		<logic:notEmpty name="candidacyBean" property="qualifications">
			<logic:iterate id="qualification" name="candidacyBean" property="qualifications" indexId="index">
				<strong><%= index.intValue() + 1 %>.</strong>
				<fr:edit id="<%= "candidacyBean.qualification" + index %>" name="qualification" schema="Public.PhdProgramCandidacyProcess.qualification">
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
			        	<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepTwoInvalid" />
				</fr:edit>
				<p class="mtop05"><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeQualification\"; document.getElementById(\"candidacyForm\").submit();" %>' href="#" >- <bean:message key="label.remove" bundle="PHD_RESOURCES"/></a></p>
			</logic:iterate>
		</logic:notEmpty>
		<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"methodId\").value=\"addQualification\"; document.getElementById(\"candidacyForm\").submit();" %>' href="#" >+ <bean:message key="label.add" bundle="PHD_RESOURCES"/></a></p>
	</fieldset>	
	</div>
	
	<div class="fs_form">
	<fieldset style="display: block;">
		<legend><bean:message key="title.public.phd.reference.letters.authors" bundle="PHD_RESOURCES"/></legend>
		<p><bean:message key="label.phd.public.reference.letters.authors.note" bundle="PHD_RESOURCES" /></p>	

		<logic:notEmpty name="candidacyBean" property="candidacyReferees">
			<logic:iterate id="referee" name="candidacyBean" property="candidacyReferees" indexId="index">
				<strong><%= index.intValue() + 1 %>.</strong>
				<fr:edit id="<%= "candidacyBean.referee" + index %>" name="referee" schema="Public.PhdProgramCandidacyProcess.referee">
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
			        	<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepTwoInvalid" />
				</fr:edit>
				<p class="mtop05"><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeCandidacyReferee\"; document.getElementById(\"candidacyForm\").submit();" %>' href="#" >- <bean:message key="label.remove" bundle="PHD_RESOURCES"/></a></p>
			</logic:iterate>
		</logic:notEmpty>
		<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"methodId\").value=\"addCandidacyReferee\"; document.getElementById(\"candidacyForm\").submit();" %>' href="#" >+ <bean:message key="label.add" bundle="PHD_RESOURCES"/></a></p>
	</fieldset>
	</div>

	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="<%= "document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='returnCreateCandidacyStepOne'; document.getElementById('candidacyForm').submit();" %>">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.continue"/> »</html:submit>
	</p>

</fr:form>
