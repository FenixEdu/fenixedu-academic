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

<logic:present role="role(STUDENT)">

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.viewProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<logic:present name="process">
	<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
	<table>
	  <tr>
	    <td>
			<fr:view schema="PhdIndividualProgramProcess.view" name="process">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop10" />
				</fr:layout>
			</fr:view>
		</td>
	    <td style="vertical-align: top; padding-top: 1em;">
	    	<ul class="operations">
				<li>
					<jsp:include page="/phd/alertMessagesNotifier.jsp?global=false" />
				</li>
			</ul>
	    </td>
	  </tr>
	</table>
	
	<%--Thesis --%>
	<jsp:include page="/phd/thesis/student/viewThesisProcess.jsp" />
	
	<%-- CAT --%>
	<jsp:include page="/phd/seminar/student/viewSeminarProcess.jsp" />

	<%-- School part --%>
	<jsp:include page="/phd/student/viewSchoolPart.jsp" />
	
	<%-- Candidacy Process --%>
	<jsp:include page="/phd/candidacy/student/viewCandidacyProcess.jsp" />
	
	<%--  ### End Of Context Information  ### --%>
</logic:present>

<logic:notPresent name="process">
	<em><bean:message bundle="PHD_RESOURCES" key="label.phd.no.processes" /></em>
</logic:notPresent>

</logic:present>