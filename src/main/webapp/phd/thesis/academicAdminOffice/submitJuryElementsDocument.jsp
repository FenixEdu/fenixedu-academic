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
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdJuryElementsRatificationEntity"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="pt.ist.fenixWebFramework.renderers.validators.FileValidator"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean"%><html:xhtml/>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="individualProcessId" name="process" property="individualProgramProcess.externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.thesis.jury.elements.document" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProcessId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdIndividualProgramProcess.view.resume" name="process" property="individualProgramProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<logic:notEmpty name="process" property="juryPresidentDocument">
	<br/>
	<strong><bean:message  key="label.phd.thesis.jury.president.document" bundle="PHD_RESOURCES"/>: </strong>
	<bean:define id="url2" name="process" property="juryPresidentDocument.downloadUrl" />
	<a href="<%= url2.toString() %>">
		<bean:write name="process" property="juryPresidentDocument.documentType.localizedName"/> 
		(<bean:message  key="label.version" bundle="PHD_RESOURCES" /> <bean:write name="process" property="juryPresidentDocument.documentVersion"/>)
	</a>
	<logic:equal name="process" property="juryPresidentDocument.documentAccepted" value="false">
		<span style="color:red"> 
			<bean:message key="label.net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean.document.rejected" bundle="PHD_RESOURCES" />
		</span>
	</logic:equal>
	<br/>
</logic:notEmpty>

<logic:notEmpty name="process" property="juryElementsDocument">
	<br/>
	<strong><bean:message  key="label.phd.thesis.jury.elements.document" bundle="PHD_RESOURCES"/>: </strong>
	<bean:define id="url1" name="process" property="juryElementsDocument.downloadUrl" />
	<a href="<%= url1.toString() %>">
		<bean:write name="process" property="juryElementsDocument.documentType.localizedName"/> 
		(<bean:message  key="label.version" bundle="PHD_RESOURCES" /> <bean:write name="process" property="juryElementsDocument.documentVersion"/>)
	</a>
	<logic:equal name="process" property="juryElementsDocument.documentAccepted" value="false">
		<span style="color:red"> 
			<bean:message key="label.net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean.document.rejected" bundle="PHD_RESOURCES" />
		</span>
	</logic:equal>
	<br/>
</logic:notEmpty>

<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<bean:define id="processId" name="process" property="externalId" />


<fr:form action="<%= "/phdThesisProcess.do?method=submitJuryElementsDocument&processId=" + processId.toString() %>" encoding="multipart/form-data">
	<fr:edit id="thesisProcessBean" name="thesisProcessBean" visible="false" />
	
	<fr:edit id="thesisProcessBean.edit.generateAlert" name="thesisProcessBean">
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisProcessBean.class.getName() %>">
			<fr:slot name="whenJuryDesignated" required="true" />
			<fr:slot name="generateAlert" layout="radio" required="true" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=submitJuryElementsDocumentInvalid&processId=" + processId.toString() %>"/>
	</fr:edit>
	
	<fr:edit id="thesisProcessBean.edit.documents" name="thesisProcessBean" property="documents">
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisProcessBean.class.getName() %>">
			<fr:slot name="type" readOnly="true" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean.type">
				<fr:property name="bundle" value="PHD_RESOURCES"/>
			</fr:slot>
			<fr:slot name="file" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean.file">
				<fr:validator name="<%= FileValidator.class.getName() %>" />
				<fr:property name="fileNameSlot" value="filename"/>
				<fr:property name="size" value="20"/>
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=submitJuryElementsDocumentInvalid&processId=" + processId.toString() %>"/>
		<fr:destination name="cancel" path="<%= "/phdThesisProcess.do?method=viewIndividualProgramProcess&processId=" + processId.toString() %>" />
	</fr:edit>

	<em>(submeter apenas os documentos que pretende)</em>
	<br/>
	<br/>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" ><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" ><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>	
</fr:form>

<p><strong><bean:message  key="title.phd.thesis.jury.element.ratification.entity" bundle="PHD_RESOURCES"/></strong></p>


<bean:define id="thesisProcessBean" name="thesisProcessBean" type="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean" />
<fr:form action="<%= "/phdThesisProcess.do?method=setPhdJuryElementsRatificationEntity&processId=" + processId.toString() %>">
	<fr:edit id="thesisProcessBean" name="thesisProcessBean" visible="false" />
	
	<fr:edit id="thesisProcessBeanPostback" name="thesisProcessBean">
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisProcessBean.class.getName() %>">
			<fr:slot name="phdJuryElementsRatificationEntity" layout="menu-select-postback" >
				<fr:property name="format" value="${localizedName}" />
				<fr:property name="providerClass"	value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.PhdJuryElementsRatificationEntityProvider" />
				<fr:property name="destination" value="postback" />
			</fr:slot>
			
		<% if(PhdJuryElementsRatificationEntity.CUSTOM.equals(thesisProcessBean.getPhdJuryElementsRatificationEntity())) { %>
			<fr:slot name="ratificationEntityCustomMessage" layout="longText" required="true">
				<fr:property name="rows" value="5" />
				<fr:property name="columns" value="60" />
			</fr:slot>
		<% } %>
		
			<fr:slot name="presidentTitle" />
		</fr:schema>
		
		<fr:destination name="postback" path="<%= "/phdThesisProcess.do?method=setPhdJuryElementsRatificationEntityPostback&processId=" + processId.toString() %>"/>
		<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=setPhdJuryElementsRatificationEntityInvalid&processId=" + processId.toString() %>" />
	</fr:edit>

	<p><html:submit><bean:message key="button.change" bundle="APPLICATION_RESOURCES" /></html:submit></p>
</fr:form>

<%--  ### End of Operation Area  ### --%>

<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>
