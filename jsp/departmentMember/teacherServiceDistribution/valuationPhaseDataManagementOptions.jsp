<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.professorshipValuationService"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal() %>'>
			<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
		<%--	(<bean:write name="teacherServiceDistribution" property="executionPeriod.semester"/>�<bean:message key="label.common.courseSemester"/>--%>
			<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.createAutomaticCourseValuations"/>
	</em>
</p>

<logic:equal name="valuationPhasesManagementForm" property="dataManagementOption" value="2">
	<p>
		<span class="success0"><bean:message key="label.teacherService.successfulValuation"/></span>
	</p>
	<p>
		<html:link page='<%= "/teacherServiceDistributionValuation.do?method=prepareForTeacherServiceDistributionValuation&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.goToteacherServiceDistributionVisualization"/>
		</html:link>
	</p>
</logic:equal>

<logic:notEqual name="valuationPhasesManagementForm" property="dataManagementOption" value="2">
	<html:form action="/valuationPhasesManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareToChooseValuationPhase"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

	<p>	
		<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.dataManagementOption" property="dataManagementOption" value="0" onclick="this.form.method.value='showValuationPhaseDataManagementOptions'; this.form.submit();"> 
			<bean:message key="label.teacherServiceDistribution.copyLastYearRealData"/> <%= "(" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getPreviousExecutionYear().getYear() + ")" %>
		</html:radio>
	</p>
	<p>
		<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.dataManagementOption" property="dataManagementOption" value="1" onclick="this.form.method.value='prepareToChooseValuationPhase'; this.form.submit();"> <bean:message key="label.teacherServiceDistribution.copyPreviousValuationPhaseData"/> </html:radio>
	</p>
	
	<logic:equal name="valuationPhasesManagementForm" property="dataManagementOption" value="1">
		<table class='tstyle5 thlight'>
		<tr>
			<th colspan="2" align="center">
				<bean:write name="teacherServiceDistribution" property="department.realName"/>
			</th>
		</tr>
		<tr>
			<td>
				<bean:message key="label.teacherServiceDistribution.executionYear"/>:
				<html:select property="executionYear" onchange="this.form.method.value='resetSelectedTeacherServiceDistribution'; this.form.submit();">
					<html:option value="-1">&nbsp;</html:option>
					<html:options collection="executionYearList" property="idInternal" labelProperty="year"/>
				</html:select>
			</td>
			<td>
				<bean:message key="label.teacherServiceDistribution.semester"/>:
				<html:select property="executionPeriod" onchange="this.form.method.value='resetSelectedTeacherServiceDistribution'; this.form.submit();">
					<html:option value="-1">&nbsp;</html:option>
					<html:options collection="executionPeriodsList" property="idInternal" labelProperty="semester"/>
				</html:select>
			</td>
		</tr>
		</table>
		
		<logic:empty name="teacherServiceDistributionList">
			<p>
				<span class="error0">
					<bean:message key="label.teacherServiceDistribution.nonAvailableTeacherServiceDistributions"/>
				</span>
			</p>
		</logic:empty>
		
		<logic:notEmpty name="teacherServiceDistributionList">

			<p class="mbottom05">
				<b><bean:message key="label.teacherServiceDistribution.availableTeacherServiceDistributions"/>:</b>
			</p>
					
			<table class='tstyle4 mtop05'>
				<tr>
					<th>
					</th>
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
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.selectedTeacherServiceDistribution" property="selectedTeacherServiceDistribution" value="<%= ((Integer)teacherServiceDistributionId).toString() %>" onchange="this.form.method.value='prepareToChooseValuationPhase'; this.form.submit();" />
					</td>
					<td>
						<bean:write name="teacherServiceDistribution" property="name"/>
					</td>
					<td>
						<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
					</td>
					<td>
						<logic:iterate id="executionPeriod" name="teacherServiceDistribution" property="orderedExecutionPeriods">
							<bean:write name="executionPeriod" property="semester"/>�&nbsp;
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
						<bean:write name="executionPeriod" property="semester"/>�&nbsp;
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
				<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='manageCurrentValuationPhaseData'; this.form.submit();">
					<bean:message key="label.teacherServiceDistribution.valuate"/>
				</html:button>
				<br/><br/>
			</logic:notEmpty>
		</logic:greaterThan>
	</logic:equal>
	
	<logic:notEqual name="valuationPhasesManagementForm" property="dataManagementOption" value="1">
		<p>
			<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='manageCurrentValuationPhaseData'; this.form.page.value=0; this.form.submit();">
				<bean:message key="label.teacherServiceDistribution.valuate"/>
			</html:button>
		</p>
	</logic:notEqual>
	</html:form>
</logic:notEqual>

<p>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:message key="link.back"/>
	</html:link>
</p>


