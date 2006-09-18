<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal() %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
<%--		(<bean:write name="teacherServiceDistribution" property="executionPeriod.semester"/>º<bean:message key="label.common.courseSemester"/>--%>
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.createAutomaticCourseValuations"/>
</h3>

<logic:equal name="valuationPhasesManagementForm" property="dataManagementOption" value="2">
	<br/>
	<h4>
		<img src="<%= request.getContextPath() %>/images/success01.gif"/>
		&nbsp;<b><bean:message key="label.teacherService.successfulValuation"/></b>
	</h4>
	<html:link page='<%= "/teacherServiceDistributionValuation.do?method=prepareForTeacherServiceDistributionValuation&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:message key="link.teacherServiceDistribution.goToteacherServiceDistributionVisualization"/>
	</html:link>
</logic:equal>

<logic:notEqual name="valuationPhasesManagementForm" property="dataManagementOption" value="2">
	<html:form action="/valuationPhasesManagement">
	<html:hidden property="method" value="prepareToChooseValuationPhase"/>
	<html:hidden property="teacherServiceDistribution"/>
	<html:hidden property="page" value="0"/>
	
	<html:radio property="dataManagementOption" value="0" onclick="this.form.method.value='showValuationPhaseDataManagementOptions'; this.form.submit();"> 
		<bean:message key="label.teacherServiceDistribution.copyLastYearRealData"/> <%= "(" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getPreviousExecutionYear().getYear() + ")" %>
	</html:radio>
	<br/>
	<html:radio property="dataManagementOption" value="1" onclick="this.form.method.value='prepareToChooseValuationPhase'; this.form.submit();"> <bean:message key="label.teacherServiceDistribution.copyPreviousValuationPhaseData"/> </html:radio>
	<br/>
	<br/>
	<br/>
	
	<logic:equal name="valuationPhasesManagementForm" property="dataManagementOption" value="1">
		<table class='vtsbc'>
		<tr>
			<th colspan="2" align="center">
				<bean:write name="teacherServiceDistribution" property="department.realName"/>
			</th>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.teacherServiceDistribution.executionYear"/>:</b>
				&nbsp;&nbsp;
				<html:select property="executionYear" onchange="this.form.method.value='resetSelectedTeacherServiceDistribution'; this.form.submit();">
					<html:option value="-1">&nbsp;</html:option>
					<html:options collection="executionYearList" property="idInternal" labelProperty="year"/>
				</html:select>
			</td>
			<td>
				<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
				&nbsp;&nbsp;
				<html:select property="executionPeriod" onchange="this.form.method.value='resetSelectedTeacherServiceDistribution'; this.form.submit();">
					<html:option value="-1">&nbsp;</html:option>
					<html:options collection="executionPeriodsList" property="idInternal" labelProperty="semester"/>
				</html:select>
			</td>
		</tr>
		</table>
		<br/>
		<br/>
		
		<logic:empty name="teacherServiceDistributionList">
			<span class="error">
				<b><bean:message key="label.teacherServiceDistribution.nonAvailableTeacherServiceDistributions"/></b>
			</span>
		</logic:empty>
		<logic:notEmpty name="teacherServiceDistributionList">
			<b><bean:message key="label.teacherServiceDistribution.availableTeacherServiceDistributions"/>:</b>
			<br/>
					
			<table class='vtsbc'>
				<tr>
					<th/>
					<th>
						<bean:message key="label.teacherServiceDistribution.name"/>
					</th>
					<th>
						<bean:message key="label.teacherServiceDistribution.executionYear"/>
					</th>
					<th>
						<bean:message key="label.teacherServiceDistribution.semesters"/>
					</th>
				</tr>
			<logic:iterate name="teacherServiceDistributionList" id="teacherServiceDistribution"> 
				<tr>
					<td>
						<bean:define id="teacherServiceDistributionId" name="teacherServiceDistribution" property="idInternal" />
						<html:radio property="selectedTeacherServiceDistribution" value="<%= ((Integer)teacherServiceDistributionId).toString() %>" onchange="this.form.method.value='prepareToChooseValuationPhase'; this.form.submit();" />
					</td>
					<td class="courses" align="left" width="200">
						<bean:write name="teacherServiceDistribution" property="name"/>
					</td>
					<td class="courses" align="center">
						<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
					</td>
					<td class="courses" align="center">
						<logic:iterate id="executionPeriod" name="teacherServiceDistribution" property="orderedExecutionPeriods">
							<bean:write name="executionPeriod" property="semester"/>º&nbsp;
						</logic:iterate>
					</td>
				</tr>
			</logic:iterate>
			</table>
		</logic:notEmpty>
		
		<logic:greaterThan name="valuationPhasesManagementForm" property="selectedTeacherServiceDistribution" value="0" >
			<br/>
			<br/>
			<table class='vtsbc'>
			<tr>
				<th align="center" colspan="2">
					<bean:write name="selectedTeacherServiceDistribution" property="name" />
				</th>
			</tr>
			<tr>
				<td class="courses">
					<b><bean:message key="label.teacherServiceDistribution.executionYear"/>: </b>
					<bean:write name="selectedTeacherServiceDistribution" property="executionYear.year"/>
					&nbsp;&nbsp;&nbsp;
					<b><bean:message key="label.teacherServiceDistribution.semesters"/>: </b>
					<logic:iterate id="executionPeriod" name="teacherServiceDistribution" property="orderedExecutionPeriods">
						<bean:write name="executionPeriod" property="semester"/>º&nbsp;
					</logic:iterate></b>
				</th>
			</tr>
			<tr>
				<logic:empty name="valuationPhaseList">
					<td colspan="2" class="courses">
						<span class="error">
							<bean:message key="label.teacherServiceDistribution.emptyPhaseList"/>
						</span>
					</td>
				</logic:empty>
				<logic:notEmpty name="valuationPhaseList">
					<td align="left">
						<b><bean:message key="label.teacherServiceDistribution.availabeValuationPhases"/>: </b>
						&nbsp;&nbsp;
						<html:select property="valuationPhase" onchange="this.form.method.value='prepareToChooseValuationPhase'; this.form.submit();">
							<html:options collection="valuationPhaseList" property="idInternal" labelProperty="name"/>
						</html:select>
					</td>
				</logic:notEmpty>
			</tr>
			</table>
			<logic:notEmpty name="valuationPhaseList">
				<html:button property="" onclick="this.form.method.value='manageCurrentValuationPhaseData'; this.form.submit();">
					<bean:message key="label.teacherServiceDistribution.valuate"/>
				</html:button>
				<br/><br/>
			</logic:notEmpty>
		</logic:greaterThan>
	</logic:equal>
	
	<logic:notEqual name="valuationPhasesManagementForm" property="dataManagementOption" value="1">
		<br/><br/>
		<html:button property="" onclick="this.form.method.value='manageCurrentValuationPhaseData'; this.form.page.value=0; this.form.submit();">
			<bean:message key="label.teacherServiceDistribution.valuate"/>
		</html:button>
		<br/><br/>
	</logic:notEqual>
	</html:form>
</logic:notEqual>

<br/><br/>
<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>


