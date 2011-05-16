<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />
	
	<bean:message key="title.edit.candidacy.guidings" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="hash" name="process" property="candidacyHashCode.value" />
<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=viewApplication" paramId="hash" paramName="hash">
	« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/>
<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.guidings" bundle="PHD_RESOURCES"/> <span style="font-weight: normal; font-size: 13px; color: #777;">(<bean:message key="title.public.phd.if.applicable" bundle="PHD_RESOURCES"/>)</span></h2>

<bean:define id="processId" name="process" property="externalId" />
<fr:form id="editGuidingsForm" action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=addGuiding&processId=" + processId %>">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />

<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<logic:notPresent name="candidacyBean">
		<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">

		<div class="fs_form">
		<fieldset style="display: block;">
			<legend><bean:message key="label.phd.public.candidacy.createCandidacy.edit.guidings" bundle="PHD_RESOURCES"/></legend>
			<p class="mtop05"><span><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></span></p>
		
				<fr:edit id="guidingBean" name="guidingBean" >
					<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdParticipantBean" bundle="PHD_RESOURCES">
						<fr:slot name="name" required="true">
							<fr:property name="size" value="50"/>
						</fr:slot>
						<fr:slot name="qualification" required="true">
							<fr:property name="size" value="50"/>
						</fr:slot>
						<fr:slot name="institution" required="true">
							<fr:property name="size" value="50"/>
						</fr:slot>
						<fr:slot name="address" required="true">
							<fr:property name="size" value="50"/>
						</fr:slot>
						<fr:slot name="email">
							<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
							<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.EmailValidator" />
							<fr:property name="size" value="50"/>
						</fr:slot>
						<fr:slot name="phone">
							<fr:property name="size" value="50"/>
						</fr:slot>
					</fr:schema>
				
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="thlight thleft"/>
						<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=addGuidingInvalid&processId=" + processId %>"/>
				</fr:edit>
		</fieldset>
		</div>
		<p><html:submit><bean:message bundle="PHD_RESOURCES" key="label.public.phd.add.guidings"/></html:submit></p>
		
		<logic:notEmpty name="process" property="individualProgramProcess.guidings">
			<logic:iterate id="guiding" name="process" property="individualProgramProcess.guidings" >
				<fr:view name="guiding">
					<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdParticipant" bundle="PHD_RESOURCES" >
						<fr:slot name="name" />
						<fr:slot name="qualification" />
						<fr:slot name="institution" />
						<fr:slot name="address" />
						<fr:slot name="email" />
						<fr:slot name="phone" />
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
				        <fr:property name="columnClasses" value="width175px,,,,"/>
					</fr:layout>
				</fr:view>
				<p class="mtop05">
					<html:link action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=removeGuiding&amp;processId=" + processId %>" paramId="guidingId" paramName="guiding" paramProperty="externalId">
						- <bean:message key="label.remove" bundle="PHD_RESOURCES"/>
					</html:link>
				</p>
			</logic:iterate>
		</logic:notEmpty>
			
	</logic:present>
</logic:equal>

</fr:form>
