<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<html:xhtml/>

<logic:present role="COORDINATOR">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.viewProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>

<bean:define id="process" name="process" />

<logic:present name="backMethod">
	<bean:define id="backMethod" name="backMethod"/>
	<html:link action="<%="/phdIndividualProgramProcess.do?method=" + backMethod %>">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</logic:present>

<logic:notPresent name="backMethod">
	<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</logic:notPresent>

<br/><br/>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


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
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=manageGuidanceDocuments" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message key="link.phd.guidance.documents.management" bundle="PHD_RESOURCES" />
				</html:link>
			</li>
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=viewRefereeLetters" paramId="processId" paramName="process" paramProperty="externalId" >
					<bean:message key="link.phd.referee.letters.view" bundle="PHD_RESOURCES" />
				</html:link>			
			</li>
		</ul>
    </td>
  </tr>
</table>

<%--Thesis --%>
<jsp:include page="/phd/thesis/coordinator/viewThesisProcess.jsp" />

<%-- CAT --%>
<jsp:include page="/phd/seminar/coordinator/viewSeminarProcess.jsp" />

<%-- School part --%>
<jsp:include page="/phd/coordinator/viewSchoolPart.jsp" />


<%--Candidacy --%>
<jsp:include page="/phd/candidacy/coordinator/viewCandidacyProcess.jsp" />

<%--  ### End Of Context Information  ### --%>

</logic:present>