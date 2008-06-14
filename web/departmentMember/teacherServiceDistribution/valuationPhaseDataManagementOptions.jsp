<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.createAutomaticTSDCourses"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal() %>'>
			<bean:write name="tsdProcess" property="name"/>&nbsp;
			<bean:write name="tsdProcess" property="executionYear.year"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.createAutomaticTSDCourses"/>
	</em>
</p>

<logic:equal name="tsdProcessPhasesManagementForm" property="dataManagementOption" value="2">
	<p>
		<html:img src="<%= request.getContextPath() + "/images/success01.gif" %>" />
		&nbsp;<bean:message key="label.teacherService.successfulValuation"/>
	</p>
	<p>
		<html:link page='<%= "/tsdProcessValuation.do?method=prepareForTSDProcessValuation&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.goTotsdProcessVisualization"/>
		</html:link>
	</p>
</logic:equal>

<logic:notEqual name="tsdProcessPhasesManagementForm" property="dataManagementOption" value="2">
	<html:form action="/tsdProcessPhasesManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareToChooseTSDProcessPhase"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProcess" property="tsdProcess"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	
	<p>	
		<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.dataManagementOption" property="dataManagementOption" value="0" onclick="this.form.method.value='showTSDProcessPhaseDataManagementOptions'; this.form.submit();"> 
			<bean:message key="label.teacherServiceDistribution.copyLastYearRealData"/> <%= "(" + ((TSDProcess) request.getAttribute("tsdProcess")).getPreviousExecutionYear().getYear() + ")" %>
		</html:radio>
	</p>
	<p>	
		<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.dataManagementOption" property="dataManagementOption" value="1" onclick="this.form.method.value='prepareToChooseTSDProcessPhase'; this.form.submit();"> 
			<bean:message key="label.teacherServiceDistribution.copyPreviousTSDProcessPhaseData"/>
		</html:radio>
	</p>
	
	<logic:equal name="tsdProcessPhasesManagementForm" property="dataManagementOption" value="1">
		<br/>
		<br/>
		<logic:empty name="tsdProcessPhaseList">
			<p>
				<em>
					<bean:message key="label.teacherServiceDistribution.nonAvailableTSDProcessPhases"/>
				</em>
			</p>
		</logic:empty>
		<logic:notEmpty name="tsdProcessPhaseList">
			<b><bean:message key="label.teacherServiceDistribution.availabeTSDProcessPhases"/>:</b>
			<br/>
			<table class='tstyle4 thlight mtop05'>
				<tr>
					<th/>
					<th>
						<bean:message key="label.teacherServiceDistribution.name"/>
					</th>
				</tr>
			<logic:iterate name="tsdProcessPhaseList" id="tsdProcessPhase"> 
				<tr>
					<td>
						<bean:define id="tsdProcessPhaseId" name="tsdProcessPhase" property="idInternal" />
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.selectedTSDProcess" property="tsdProcessPhase" value="<%= ((Integer)tsdProcessPhaseId).toString() %>" />
					</td>
					<td width="250">
						<bean:write name="tsdProcessPhase" property="name"/>
					</td>
				</tr>
			</logic:iterate>
			</table>
			<br/>
			<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='manageCurrentTSDProcessPhaseData'; this.form.page.value=0; this.form.submit();">
				<bean:message key="label.teacherServiceDistribution.valuate"/>
			</html:button>
		</logic:notEmpty>		
	</logic:equal>
	<logic:equal name="tsdProcessPhasesManagementForm" property="dataManagementOption" value="0">
		<br/>
		<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='manageCurrentTSDProcessPhaseData'; this.form.page.value=0; this.form.submit();">
			<bean:message key="label.teacherServiceDistribution.valuate"/>
		</html:button>
	</logic:equal>
	
	</html:form>
</logic:notEqual>

<br/>
<br/>
<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>


