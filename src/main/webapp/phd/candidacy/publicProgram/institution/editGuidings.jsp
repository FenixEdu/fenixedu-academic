<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />
	
	<bean:message key="title.edit.candidacy.guidings" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="hash" name="process" property="candidacyHashCode.value" />

<p>
	<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=viewApplication" paramId="hash" paramName="hash">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>

<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.guidings" bundle="PHD_RESOURCES"/> <span style="font-weight: normal; font-size: 13px; color: #777;">(<bean:message key="title.public.phd.if.applicable" bundle="PHD_RESOURCES"/>)</span></h2>

<bean:define id="processId" name="process" property="externalId" />

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<logic:equal name="canEditCandidacy" value="true">

<fr:form id="editGuidingsForm" action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=addGuiding&processId=" + processId %>" encoding="multipart/form-data">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<fr:edit id="guidingBean" name="guidingBean" visible="false" />
	<fr:edit id="assistantGuidingBean" name="assistantGuidingBean" visible="false" />
	
	<logic:notPresent name="candidacyBean">
		<p><em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em></p>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">

		<div class="fs_form">
		<fieldset style="display: block;">
			<legend><bean:message key="label.phd.public.candidacy.createCandidacy.edit.guidings" bundle="PHD_RESOURCES"/></legend>
			
				<fr:edit id="guidingBean.form" name="guidingBean" >
					<fr:schema type="org.fenixedu.academic.domain.phd.PhdParticipantBean" bundle="PHD_RESOURCES">
						<fr:slot name="name" required="true">
							<fr:property name="size" value="50"/>
						</fr:slot>
						<fr:slot name="qualification" required="true">
							<fr:property name="size" value="50"/>
						</fr:slot>
						<fr:slot name="institution" required="true">
							<fr:property name="size" value="50"/>
						</fr:slot>
						<fr:slot name="address" required="true"  key="label.org.fenixedu.academic.domain.phd.PhdParticipantBean.institution.address">
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
						
						<fr:slot name="guidingAcceptanceLetter.file" key="PhdIndividualProgramDocumentType.GUIDER_ACCEPTANCE_LETTER">
							<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
								<fr:property name="required" value="true" />
								<fr:property name="maxSize" value="2mb" />
								<fr:property name="acceptedExtensions" value="pdf" />
								<fr:property name="acceptedTypes" value="application/pdf" />
							</fr:validator>
							<fr:property name="fileNameSlot" value="guidingAcceptanceLetter.filename"/>
							<fr:property name="size" value="20"/>
						</fr:slot>				
						
					</fr:schema>
				
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="thlight thleft"/>
						<fr:property name="columnClasses" value="width200px,,tdclear tderror1"/>
						<fr:property name="optionalMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=addGuidingInvalid&processId=" + processId %>"/>
				</fr:edit>
		</fieldset>
		</div>
		<p><html:submit><bean:message bundle="PHD_RESOURCES" key="label.public.phd.add.guiding"/></html:submit></p>

		<logic:notEmpty name="process" property="individualProgramProcess.guidings">
			<logic:iterate id="guiding" name="process" property="individualProgramProcess.guidings" >
				<fr:view name="guiding">
					<fr:schema type="org.fenixedu.academic.domain.phd.PhdParticipant" bundle="PHD_RESOURCES" >
						<fr:slot name="name" />
						<fr:slot name="qualification" />
						<fr:slot name="institution" />
						<fr:slot name="address" />
						<fr:slot name="email" />
						<fr:slot name="phone" />
						<fr:slot name="acceptanceLetter" layout="link"/>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
				        <fr:property name="columnClasses" value="width200px,,,,"/>
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
</fr:form>

<fr:form id="editGuidingsForm" action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=addAssistantGuiding&processId=" + processId %>" encoding="multipart/form-data" >
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<fr:edit id="guidingBean" name="guidingBean" visible="false" />
	<fr:edit id="assistantGuidingBean" name="assistantGuidingBean" visible="false" />	
	
	<logic:notPresent name="candidacyBean">
		<p><em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em></p>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">

		<div class="fs_form">
		<fieldset style="display: block;">
			<legend><bean:message key="label.phd.public.candidacy.createCandidacy.edit.assistant.guidings" bundle="PHD_RESOURCES"/></legend>
		
				<fr:edit id="assistantGuidingBean.form" name="assistantGuidingBean" >
					<fr:schema type="org.fenixedu.academic.domain.phd.PhdParticipantBean" bundle="PHD_RESOURCES">
						<fr:slot name="name" required="true">
							<fr:property name="size" value="50"/>
						</fr:slot>
						<fr:slot name="qualification" required="true">
							<fr:property name="size" value="50"/>
						</fr:slot>
						<fr:slot name="institution" required="true">
							<fr:property name="size" value="50"/>
						</fr:slot>
						<fr:slot name="address" required="true" key="label.org.fenixedu.academic.domain.phd.PhdParticipantBean.institution.address">
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
						
						<fr:slot name="guidingAcceptanceLetter.file" key="PhdIndividualProgramDocumentType.ASSISTENT_GUIDER_ACCEPTANCE_LETTER">
							<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
							<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
								<fr:property name="required" value="true" />
								<fr:property name="maxSize" value="2mb" />
								<fr:property name="acceptedExtensions" value="pdf" />
								<fr:property name="acceptedTypes" value="application/pdf" />
							</fr:validator>
							<fr:property name="fileNameSlot" value="guidingAcceptanceLetter.filename"/>
							<fr:property name="size" value="20"/>
						</fr:slot>
					</fr:schema>
				
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="thlight thleft"/>
						<fr:property name="columnClasses" value="width200px,,tdclear tderror1"/>
						<fr:property name="optionalMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=addGuidingInvalid&processId=" + processId %>"/>
				</fr:edit>
		</fieldset>
		</div>
		<p><html:submit><bean:message bundle="PHD_RESOURCES" key="label.public.phd.add.assistant.guiding"/></html:submit></p>
		
		<logic:notEmpty name="process" property="individualProgramProcess.assistantGuidings">
			<logic:iterate id="guiding" name="process" property="individualProgramProcess.assistantGuidings" >
				<fr:view name="guiding">
					<fr:schema type="org.fenixedu.academic.domain.phd.PhdParticipant" bundle="PHD_RESOURCES" >
						<fr:slot name="name" />
						<fr:slot name="qualification" />
						<fr:slot name="institution" />
						<fr:slot name="address" />
						<fr:slot name="email" />
						<fr:slot name="phone" />
						<fr:slot name="acceptanceLetter" layout="link"/>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
				        <fr:property name="columnClasses" value="width200px,,,,"/>
					</fr:layout>
				</fr:view>
				<p class="mtop05">
					<html:link action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=removeAssistantGuiding&amp;processId=" + processId %>" paramId="guidingId" paramName="guiding" paramProperty="externalId">
						- <bean:message key="label.remove" bundle="PHD_RESOURCES"/>
					</html:link>
				</p>
			</logic:iterate>
		</logic:notEmpty>		
	</logic:present>
</fr:form>
	
</logic:equal>


