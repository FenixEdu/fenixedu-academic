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

<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.send.to.coordinator" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="individualProgramProcessId" name="process" property="individualProgramProcess.externalId" />

<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProgramProcessId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<table>
  <tr style="vertical-align: top;">
    <td style="width: 55%">
    	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process" property="individualProgramProcess">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</td>
    <td>
	    <strong><bean:message  key="label.phd.candidacyProcess" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdProgramCandidacyProcess.view.simple" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
    </td>
  </tr>
</table>

<%--  ### End Of Context Information  ### --%>


<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<bean:define id="processId" name="process" property="externalId" />

<fr:form action="<%= "/phdProgramCandidacyProcess.do?method=requestCandidacyReview&processId=" + processId.toString()%>">
  	
  	<p><strong><bean:message key="label.phd.send.to.coordinator.question" bundle="PHD_RESOURCES" /></strong></p>
  	
  	<fr:edit id="stateBean" name="stateBean" visible="false" />

  	<fr:edit id="stateBean-generateAlert" name="stateBean">
  		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean">
  			<fr:slot name="generateAlert" layout="radio-postback">
  				<fr:property name="destination" value="postback" />
  			</fr:slot>
  		</fr:schema>
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="postback" path="<%= "/phdProgramCandidacyProcess.do?method=prepareRequestCandidacyReviewPostback&amp;processId=" + processId.toString() %>"/>
		<fr:destination name="cancel" path="<%= "/phdProgramCandidacyProcess.do?method=viewIndividualProgramProcess&processId=" + processId.toString()%>" />
  	</fr:edit>
	
	<logic:equal name="stateBean" property="generateAlert" value="true">  	
	  	<fr:edit id="stateBean-remorks" name="stateBean" schema="PhdProgramCandidacyProcessStateBean.edit">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
	</logic:equal>
  	
	<%--  ### Buttons (e.g. Submit)  ### --%>
  	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" ><bean:message bundle="PHD_RESOURCES" key="label.phd.send.to.coordinator"/></html:submit>
  	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" ><bean:message bundle="PHD_RESOURCES" key="label.back"/></html:cancel>
	<%--  ### End of Buttons (e.g. Submit)  ### --%>

</fr:form>

<%--  ### End of Operation Area  ### --%>
