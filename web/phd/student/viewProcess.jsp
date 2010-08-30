<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<logic:present role="STUDENT">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.student.breadcrumb" bundle="PHD_RESOURCES"/></em>
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