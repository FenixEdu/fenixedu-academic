<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<%@page import="net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopSessions"%>
<%@page import="net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopThemes"%>
<html:xhtml />

<em><bean:message key="title.student.portalTitle" /></em>
<h2><bean:message key="label.careerWorkshopApplication.title" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
<bean:define id="applicationX" name="application" type="net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplication"/>
<h4>
	<bean:message key="label.careerWorkshopApplication.subTitle"/>
	<bean:write name="applicationX" property="careerWorkshopApplicationEvent.formattedBeginDate"/>
	<bean:message key="label.careerWorkshopApplication.subTitleSeparator"/>
	<bean:write name="applicationX" property="careerWorkshopApplicationEvent.formattedEndDate"/>
	<%
	if(applicationX.getCareerWorkshopApplicationEvent().getRelatedInformation() != null) {
	%>
		<bean:message key="label.careerWorkshopApplication.dashSeparator"/>
		<bean:write name="applicationX" property="careerWorkshopApplicationEvent.relatedInformation"/>
	<%
	}
	%>
</h4>

<logic:present name="comingFromSubmission">
	<div class="success0">
		<bean:message key="label.careerWorkshopApplication.successfulEdit" bundle="STUDENT_RESOURCES"/><br/>
	</div>
</logic:present>

<div class="infoop2">
	<bean:message key="label.careerWorkshopApplication.applicationRules" bundle="STUDENT_RESOURCES"/><br/>
</div>

<form action="<%= request.getContextPath() + "/student/careerWorkshopApplication.do" %>" method="post">
	<html:hidden property="applicationExternalId" value="<%= applicationX.getExternalId() %>"/>
	<html:hidden property="method" value="submitApplication"/>
	<table>
		<tr>
			<td></td>
<%
	for (int i = 0; i < CareerWorkshopSessions.values().length; i++) {
%>
			<td><%= i+1 %></td>
<%
	}
%>
</tr>			

<%
	for (final CareerWorkshopSessions careerWorkshopSessions : CareerWorkshopSessions.values()) {
%>  
		  <tr>
			<td><%=careerWorkshopSessions.getDescription() %></td>
			<%
				for (int i = 0; i < CareerWorkshopSessions.values().length; i++) {
			%>
				<td><input type="radio" name="<%= careerWorkshopSessions.name() %>" value="<%= i %>"
					<% if (applicationX.getSession(careerWorkshopSessions) == i) { %>checked="checked"<% }%>
				/></td>
			<%
				}
			%>
		</tr>
<%
	}
%>

	</table>
	
	<table>
		<tr>
			<td></td>
<%
	for (int i = 0; i < CareerWorkshopThemes.values().length; i++) {
%>
			<td><%= i+1 %></td>
<%
	}
%>
</tr>			

<%
	for (final CareerWorkshopThemes careerWorkshopThemes : CareerWorkshopThemes.values()) {
%>  
		  <tr>
			<td><%=careerWorkshopThemes.getDescription() %></td>
			<%
				for (int i = 0; i < CareerWorkshopThemes.values().length; i++) {
			%>
				<td><input type="radio" name="<%= careerWorkshopThemes.name() %>" value="<%= i %>"
					<% if (applicationX.getTheme(careerWorkshopThemes) == i) { %>checked="checked"<% }%>
				/></td>
			<%
				}
			%>
		</tr>
<%
	}
%>

	</table>
	
	<html:submit>
		<bean:message bundle="STUDENT_RESOURCES" key="button.submit" />
	</html:submit>
</form>


<script type="text/javascript">
	function checkerHit(chkbox) {
		if(chkbox.checked) {
			crossLock(chkbox);
		} else {
			crossUnlock(chkbox);
		}
	}

	function crossLock(chkbox) {
		var row = chkbox.name;
		var column = chkbox.value;

		var rows = document.getElementsByName(row);
		for(var i=0; i<rows.length; i++) {
			rows[i].disabled = true;
		}

		var columns = getElementsByValue(column);
		for(var i=0; i<columns.length; i++) {
			columns[i].disabled = true;
		}

		chkbox.disabled = false;
	}

	function crossUnlock(chkbox) {
		var row = chkbox.name;
		var column = chkbox.value;
		
		var rows = document.getElementsByName(row);
		for(var i=0; i<rows.length; i++) {
			var actualChk = rows[i];
			if(isClear(actualChk)) {
				actualChk.disabled = false;
			}
		}

		var columns = getElementsByValue(column);
		for(var i=0; i<columns.length; i++) {
			var actualChk = columns[i];
			if(isClear(actualChk)) {
				actualChk.disabled = false;
			}
		}
	}

	function getElementsByValue(column) {
		var nodes = document.getElementsByTagName("input");
		var result = [];
		for(var i=0; i<nodes.length; i++) {
			var node = nodes[i];
			if(node.getAttribute("value") == column) {
				result.push(node);
			}
		}
		return result;
	}

	function isClear(chkbox) {
		var row = chkbox.name;
		var column = chkbox.value;

		var rows = document.getElementsByName(row);
		for(z=0; z<rows.length; z++) {
			if(rows[z].checked) {
				return false;
			}
		}
		
		var columns = getElementsByValue(column);
		for(y=0; y<columns.length; y++) {
			if(columns[y].checked) {
				return false;
			}
		}
		return true;
	}
</script>