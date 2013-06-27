<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
<bean:message bundle="APPLICATION_RESOURCES" key="link.manage.finalWork"/>
</h2>

<h3>
	<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.orientatorDissertations"/>
</h3>
<logic:notEmpty name="orientatorDissertations">
	<fr:view name="orientatorDissertations">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.teacher.FinalWorkManagementAction">
			<fr:slot name="student.name" bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.student"/>
			<fr:slot name="title" bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.title"/>
			<fr:slot name="state.name" bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.state"/>
		</fr:schema>
			<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop15 tdcenter inobullet"/>
			<fr:property name="sortBy" value="title" />
			<fr:property name="columnClasses" value="width200px,width400px,width200px"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="orientatorDissertations">
	<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.noDissertations"/></span>
</logic:empty>

<h3>
	<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.coorientatorDissertations"/>
</h3>

<logic:notEmpty name="coorientatorDissertations">
	<fr:view name="coorientatorDissertations">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.teacher.FinalWorkManagementAction">
			<fr:slot name="student.name" bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.student"/>
			<fr:slot name="title" bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.title"/>
			<fr:slot name="state.name" bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.state"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop15 tdcenter inobullet"/>
			<fr:property name="sortBy" value="title" />
			<fr:property name="columnClasses" value="width200px,width400px,width200px"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="coorientatorDissertations">
	<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.noDissertations"/></span>
</logic:empty>

<h3>
	<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.submitedOrientatorProposal"/>
</h3>

<bean:define id="orientatorProposals" name="orientatorProposals"/>
<bean:define id="proposal" name="orientatorProposals"/>

<logic:notEmpty name="orientatorProposals">
	<table class="tstyle2 mtop15 tdcenter inobullet">
		<tr>
			<th class="listClasses-header" width="200px">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.proposal"/>
			</th>
			<th class="listClasses-header" width="400px">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.title"/>
			</th>
			<th class="listClasses-header" width="200px">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.state"/>
			</th>
		</tr>
		<tr>
		<logic:iterate id="orientatorProposal" name="orientatorProposals">
			<tr>
				<td>
					<bean:write name="orientatorProposal" property="proposalNumber"/>
				</td>
				<td>
					<html:link page="<%= "/finalWorkManagement.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal) orientatorProposal).getIdInternal() %>">
						<bean:write name="orientatorProposal" property="title"/>
					</html:link>
				</td>
				<td>
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

<h3>
	<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.submitedCoorientatorProposal"/>
</h3>
	
<logic:notEmpty name="coorientatorProposals">
	<table class="tstyle2 mtop15 tdcenter inobullet">
		<tr>
			<th class="listClasses-header" width="200px">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.proposal"/>
			</th>
			<th class="listClasses-header" width="400px">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.title"/>
			</th>
			<th class="listClasses-header" width="200px">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.dissertations.state"/>
			</th>
		</tr>
		<tr>
		<logic:iterate id="coorientatorProposal" name="coorientatorProposals">
			<tr>
				<td>
					<bean:write name="coorientatorProposal" property="proposalNumber"/>
				</td>
				<td>
					<html:link page="<%= "/finalWorkManagement.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal) coorientatorProposal).getIdInternal() %>">
						<bean:write name="coorientatorProposal" property="title"/>
					</html:link>
				</td>
				<td>
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