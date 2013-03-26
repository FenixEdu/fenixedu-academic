<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.CandidacyGroupContestState.CandidacyGroupContestStateStage"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.CandidacyGroupContestState"%>

<style>
.tableStyle {
	text-align: center;
	width: 100%;
}

.stateTableStyle {
	border-collapse: separate;
	border-spacing: 10px;
}

.legendTableStyle {
	border-collapse: separate;
	border-spacing: 10px;
	border-style: dotted;
	border-width: thin;
	background-color: #FEFEFE;
}

.box {
	border-style: solid;
	border-width: thin;
	padding: 5px;
	border-radius: 2em;
	-moz-border-radius: 2em;
	text-align: center;
}

.state {
	width: 120px;
}

.legend {
	width: 12px;
}

.underWay {
	background-color: #F6E3CE;
	border-color: #B45F04;
}

.complete {
	background-color: #CEF6CE;
	border-color: #04B404;
}
</style>

<table class="tableStyle">
	<tr><td align="center"><table class="stateTableStyle"><tr>
		<% for (final CandidacyGroupContestState state : CandidacyGroupContestState.values()) {
		    	final CandidacyGroupContestStateStage stage = state.getStage(group, period);
		    	final String stageClass = stage == CandidacyGroupContestStateStage.UNDER_WAY ? "underWay" : stage == CandidacyGroupContestStateStage.COMPLETED ? "complete" : "";
		%>
				<td class="box state <%= stageClass %>"><%=state.getLocalizedName()%></td>
		<% } %>
	</tr></table></td></tr>
	<tr><td align="center"><table class="legendTableStyle"><tr>
		<td align="center"><strong> Legenda </strong></td>
		<td class="box legend"></td>
		<td><%= CandidacyGroupContestStateStage.NOT_STARTED.getLocalizedName() %></td>
		<td class="box legend underWay"></td>
		<td><%= CandidacyGroupContestStateStage.UNDER_WAY.getLocalizedName() %></td>
		<td class="box legend complete"></td>
		<td><%= CandidacyGroupContestStateStage.COMPLETED.getLocalizedName() %></td>
	</tr></table></td></tr>
</table>
