<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.Thesis"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<style>
	.dissertations-table-short-cell {
		width: 200px;
	}
	.dissertations-table-long-cell {
		width: 660px;
	}
</style>

<script type="text/javascript">
	$(document).ready(function () {
		$('h3[id^="table-header"]').css('cursor','pointer');
		$('h3[id^="table-header"]').click(function () {
			var index = $(this).attr('id').match(/\d+/g)[0];
			$('#table'+index).toggle();
		});
	});
</script>

<h2>
<bean:message bundle="APPLICATION_RESOURCES" key="link.manage.finalWork"/>
</h2>

<h3 id="table-header1">
	<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.orientatorDissertations"/>
</h3>

<logic:notEmpty name="orientatorDissertations">
	<bean:define id="executionYear" name="executionYear"/>
	<bean:define id="executionYearId" name="executionYearId"/>
	<table class="tstyle2 mtop15 tdcenter inobullet" id="table1">
		<tr>
			<th class="listClasses-header dissertations-table-short-cell" >
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.student"/>
			</th>
			<th class="listClasses-header dissertations-table-long-cell" >
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.title"/>
			</th>
			<th class="listClasses-header dissertations-table-short-cell" >
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.state"/>
			</th>
		</tr>
		<tr>
		<logic:iterate id="orientatorDissertation" name="orientatorDissertations">
			<tr>
				<td class="dissertations-table-short-cell">
					<bean:define id="student" name="orientatorDissertation" property="student"/>
					<bean:write name="student" property="name"/>
				</td>
				<td class="dissertations-table-long-cell">
					<html:link page="<%= "/finalWorkManagement.do?method=editProposal&amp;degreeCurricularPlanID=" + ((Thesis) orientatorDissertation).getDegree().getDegreeCurricularPlansForYear((net.sourceforge.fenixedu.domain.ExecutionYear) executionYear).get(0).getIdInternal() + "&amp;executionYearId=" + executionYearId %>">
						<bean:write name="orientatorDissertation" property="title"/>
					</html:link>
				</td>
				<td class="dissertations-table-short-cell">
					<bean:write name="orientatorDissertation" property="state"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>


<logic:empty name="orientatorDissertations">
	<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.noDissertations"/></span>
</logic:empty>

<h3 id="table-header2">
	<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.coorientatorDissertations"/>
</h3>

<logic:notEmpty name="coorientatorDissertations">
	<bean:define id="dissertation" name="coorientatorDissertations"/>
	<table class="tstyle2 mtop15 tdcenter inobullet" id="table2">
		<tr>
			<th class="listClasses-header dissertations-table-short-cell" >
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.student"/>
			</th>
			<th class="listClasses-header dissertations-table-long-cell" >
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.title"/>
			</th>
			<th class="listClasses-header dissertations-table-short-cell" >
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.state"/>
			</th>
		</tr>
		<tr>
		<logic:iterate id="coorientatorDissertation" name="coorientatorDissertations">
			<tr>
				<td class="dissertations-table-short-cell">
					<bean:define id="student" name="coorientatorDissertation" property="student"/>
					<bean:write name="student" property="name"/>
				</td>
				<td class="dissertations-table-long-cell">
					<bean:write name="coorientatorDissertation" property="title"/>
				</td>
				<td class="dissertations-table-short-cell">
					<bean:write name="coorientatorDissertation" property="state"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>

<logic:empty name="coorientatorDissertations">
	<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.noDissertations"/></span>
</logic:empty>

<h3 id="table-header3">
	<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.submitedOrientatorProposal"/>
</h3>

<logic:notEmpty name="orientatorProposals">
	<bean:define id="proposal" name="orientatorProposals"/>
	<table class="tstyle2 mtop15 tdcenter inobullet" id="table3">
		<tr>
			<th class="listClasses-header dissertations-table-short-cell">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.proposal"/>
			</th>
			<th class="listClasses-header dissertations-table-long-cell">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.title"/>
			</th>
			<th class="listClasses-header dissertations-table-short-cell">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.state"/>
			</th>
		</tr>
		<tr>
		<logic:iterate id="orientatorProposal" name="orientatorProposals">
			<tr>
				<td class="dissertations-table-short-cell">
					<bean:write name="orientatorProposal" property="proposalNumber"/>
				</td>
				<td class="dissertations-table-long-cell">
					<html:link page="<%= "/finalWorkManagement.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal) orientatorProposal).getIdInternal() %>">
						<bean:write name="orientatorProposal" property="title"/>
					</html:link>
				</td>
				<td class="dissertations-table-short-cell">
					<bean:define id="status" name="orientatorProposal" property="status"/>
					<bean:write name="status" property="key"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>

<logic:empty name="orientatorProposals">
	<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.noProposals"/></span>
</logic:empty>

<h3 id="table-header4">
	<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.submitedCoorientatorProposal"/>
</h3>
	
<logic:notEmpty name="coorientatorProposals">
	<bean:define id="coorientatorProposals" name="coorientatorProposals"/>
	<bean:define id="proposal" name="coorientatorProposals"/>
	<table class="tstyle2 mtop15 tdcenter inobullet" id="table4">
		<tr>
			<th class="listClasses-header dissertations-table-short-cell">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.proposal"/>
			</th>
			<th class="listClasses-header dissertations-table-long-cell">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.title"/>
			</th>
			<th class="listClasses-header dissertations-table-short-cell">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.state"/>
			</th>
		</tr>
		<tr>
		<logic:iterate id="coorientatorProposal" name="coorientatorProposals">
			<tr>
				<td class="dissertations-table-short-cell">
					<bean:write name="coorientatorProposal" property="proposalNumber"/>
				</td>
				<td class="dissertations-table-long-cell">
					<html:link page="<%= "/finalWorkManagement.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal) coorientatorProposal).getIdInternal() %>">
						<bean:write name="coorientatorProposal" property="title"/>
					</html:link>
				</td>
				<td class="dissertations-table-short-cell">
					<bean:define id="status" name="coorientatorProposal" property="status"/>
					<bean:write name="status" property="key"/>
				</td>
			</tr>
		</logic:iterate>
	</table></logic:notEmpty>

<logic:empty name="coorientatorProposals">
	<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.noProposals"/></span>
</logic:empty>


<script type="text/javascript" language="javascript">
switchGlobal();
</script>