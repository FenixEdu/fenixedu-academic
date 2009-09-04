<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="COORDINATOR">

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manageProcesses" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Results  ### --%>

<table >
	<tr>
		<td><bean:message key="label.phd.statistics.total.requests" bundle="PHD_RESOURCES" />: </td>
		<td><bean:write name="statistics" property="totalRequests" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.phd.statistics.total.with.candidacy" bundle="PHD_RESOURCES" />: </td>
		<td><bean:write name="statistics" property="totalCandidates" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.phd.statistics.total.validated" bundle="PHD_RESOURCES" />: </td>
		<td><bean:write name="statistics" property="totalValidated" /></td>
	</tr>
</table>

<logic:notEmpty name="candidacyHashCodes">
	<fr:view schema="PublicPhdCandidacyBean.view" name="candidacyHashCodes">
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			
			<fr:property name="linkFormat(view)" value="/candidacies/phdProgramCandidacyProcess.do?method=viewProcess&hashCodeId=${hashCode.externalId}"/>
			<fr:property name="key(view)" value="label.view"/>
			<fr:property name="bundle(view)" value="PHD_RESOURCES"/>
			
			<fr:property name="sortParameter" value="sortBy"/>
	        <fr:property name="sortUrl" value="/candidacies/phdProgramCandidacyProcess.do?method=listProcesses" />
    	    <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "email=asc" : request.getParameter("sortBy") %>"/>
			<fr:property name="ascendingImage" value="/images/upArrow.gif"/>
        	<fr:property name="descendingImage" value="/images/downArrow.gif"/>
			<fr:property name="sortableSlots" value="email, name, phdFocusArea, candidate, validated" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="candidacyHashCodes">
	<br/>
	<em><bean:message key="label.phd.no.processes" bundle="PHD_RESOURCES" /></em>
	<br/>
</logic:empty>

<%--  ### End of Results  ### --%>

</logic:present>
