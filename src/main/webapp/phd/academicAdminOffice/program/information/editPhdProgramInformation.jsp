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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<html:xhtml />

<%-- ### Title #### --%>
<h2><bean:message key="title.phdProgramInformation" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="phdProgramInformationId" name="phdProgramInformation" property="externalId" />
<bean:define id="phdProgramId" name="phdProgramInformation" property="phdProgram.externalId" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<p>
	<html:link action="/phdProgramInformation.do?method=listPhdProgramInformations" paramId="phdProgramId" paramName="phdProgramId">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
 
<p><strong><bean:message  key="title.phdProgramInformation.edit" bundle="PHD_RESOURCES"/></strong></p>

<fr:form action="<%= String.format("/phdProgramInformation.do?method=edit&amp;phdProgramInformationId=%s&amp;phdProgramId=%s", phdProgramInformationId, phdProgramId) %>" >
	<fr:edit id="phdProgramInformationBean" name="phdProgramInformationBean" visible="false" />
		
	<fr:edit id="phdProgramInformationBean.create" name="phdProgramInformationBean">	

		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdProgramInformationBean" >
			<fr:slot name="beginDate" required="true" />
			
			<fr:slot name="minThesisEctsCredits" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator" />
				<fr:property name="size" value="3" />
				<fr:property name="maxlength" value="3" />
			</fr:slot>
			<fr:slot name="maxThesisEctsCredits" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator" />
				<fr:property name="size" value="3" />
				<fr:property name="maxlength" value="3" />			
			</fr:slot>
			<fr:slot name="minStudyPlanEctsCredits" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator" />
				<fr:property name="size" value="3" />
				<fr:property name="maxlength" value="3" />			
			</fr:slot>
			<fr:slot name="maxStudyPlanEctsCredits" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator" />
				<fr:property name="size" value="3" />
				<fr:property name="maxlength" value="3" />			
			</fr:slot>
			<fr:slot name="numberOfYears" required="true">
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
				<fr:property name="size" value="3" />
				<fr:property name="maxlength" value="3" />			
			</fr:slot>
			<fr:slot name="numberOfSemesters" required="true">
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
				<fr:property name="size" value="3" />
				<fr:property name="maxlength" value="3" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			<fr:property name="requiredMarkShown" value="true" />			
		</fr:layout>
		
		<fr:destination name="cancel" path="<%= "/phdProgramInformation.do?method=listPhdProgramInformations&amp;phdProgramId=" + phdProgramId %>" />
		<fr:destination name="invalid" path="<%= "/phdProgramInformation.do?method=editInvalid&phdProgramInformationId=" + phdProgramInformationId %>" />
	</fr:edit>
	
	<html:submit><bean:message key="label.submit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
</fr:form>
