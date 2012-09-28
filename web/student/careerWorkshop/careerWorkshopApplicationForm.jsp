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
<bean:define id="applicationX" name="application" type="net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplication" />
<h4>
	<bean:message key="label.careerWorkshopApplication.subTitle" />
	<bean:write name="applicationX" property="careerWorkshopApplicationEvent.formattedBeginDate" />
	<bean:message key="label.careerWorkshopApplication.subTitleSeparator" />
	<bean:write name="applicationX" property="careerWorkshopApplicationEvent.formattedEndDate" />
<%
	if (applicationX.getCareerWorkshopApplicationEvent().getRelatedInformation() != null) {
 %>
 	<bean:message key="label.careerWorkshopApplication.dashSeparator" />
	<bean:write name="applicationX" property="careerWorkshopApplicationEvent.relatedInformation" />
<%
     }
 %>
</h4>

<p><html:link action="/careerWorkshopApplication.do?method=prepare">
	<bean:message bundle="STUDENT_RESOURCES" key="link.student.back" />
</html:link></p>

<logic:present name="comingFromSubmission">
	<div class="success0 CareerWorkshop-Sucess">
		<bean:message key="label.careerWorkshopApplication.successfulEdit" bundle="STUDENT_RESOURCES" /><br />
	</div>
</logic:present>

<div class="infoop2 CareerWorkshop-Rules">
	<bean:message key="label.careerWorkshopApplication.applicationRules" bundle="STUDENT_RESOURCES" /><br />
	<p class="CareerWorkshop-TextCenter">
		<a href="http://tt.ist.utl.pt/desenvolvimento-de-carreiras-alunos/concurso-de-bolsas-para-os-ist-career-workshops/"><bean:message key="link.careerWorkshopApplication.rules" /></a>
	</p>
</div>

<form action="<%=request.getContextPath() + "/student/careerWorkshopApplication.do"%>" method="post">
	<html:hidden property="applicationExternalId" value="<%= applicationX.getExternalId() %>" />
	<html:hidden property="method" value="submitApplication" />
	
	<table class="CareerWorkshop-ShiftsTable">
		<tr>
			<th class="CareerWorkshop-TableHeader"><bean:message key="label.careerWorkshop.shiftProposals" /></th>
		<th class="CareerWorkshop-TableHeader" colspan="<%=CareerWorkshopSessions.values().length%>">
			<bean:message key="label.careerWorkshop.preference" arg0="<%= Integer.toString(CareerWorkshopSessions.values().length) %>" />
		</th>
	</tr>
	<tr>
		<td></td>
		<%
		    for (int i = 0; i < CareerWorkshopSessions.values().length; i++) {
		%>
		<td class="CareerWorkshop-TextCenter"><%=i + 1%></td>
		<%
		    }
		%>
	</tr>

	<%
	    for (final CareerWorkshopSessions careerWorkshopSessions : CareerWorkshopSessions.values()) {
			String backgroundClass;
			if (careerWorkshopSessions.ordinal() % 2 == 1) {
			    backgroundClass = "CareerWorkshop-OddRow";
			} else {
			    backgroundClass = "CareerWorkshop-EvenRow";
			}
	%>
	<tr class="<%= backgroundClass %>">
		<td class="CareerWorkshop-TextRight CareerWorkshop-BigText"><%=careerWorkshopSessions.getDescription()%></td>
		<%
		    for (int i = 0; i < CareerWorkshopSessions.values().length; i++) {
		%>
		<td><input type="radio"
			name="<%=careerWorkshopSessions.name()%>" value="<%=i%>"
			<%if (applicationX.getSession(careerWorkshopSessions) == i) {%>
			checked="checked" <%}%> /></td>
		<%
		    }
		%>
	</tr>
	<%
	    }
	%>

</table>

<table class="CareerWorkshop-ThemesTable">
	<tr>
		<th class="CareerWorkshop-TableHeader"><bean:message
			key="label.careerWorkshop.themeProposals" /></th>
		<th class="CareerWorkshop-TableHeader"
			colspan="<%=CareerWorkshopThemes.values().length%>"><bean:message
			key="label.careerWorkshop.preference"
			arg0="<%= Integer.toString(CareerWorkshopThemes.values().length) %>" /></th>
	</tr>
	<tr>
		<td></td>
		<%
		    for (int i = 0; i < CareerWorkshopThemes.values().length; i++) {
		%>
		<td class="CareerWorkshop-TextCenter"><%=i + 1%></td>
		<%
		    }
		%>
	</tr>

	<%
	    for (final CareerWorkshopThemes careerWorkshopThemes : CareerWorkshopThemes.values()) {
			String backgroundClass;
			if (careerWorkshopThemes.ordinal() % 2 == 1) {
			    backgroundClass = "CareerWorkshop-OddRow";
			} else {
			    backgroundClass = "CareerWorkshop-EvenRow";
			}
	%>
	<tr class="<%= backgroundClass %>">
		<td class="CareerWorkshop-TextRight CareerWorkshop-BigText"><%=careerWorkshopThemes.getDescription()%></td>
		<%
		    for (int i = 0; i < CareerWorkshopThemes.values().length; i++) {
		%>
		<td><input type="radio" name="<%=careerWorkshopThemes.name()%>"
			value="<%=i%>"
			<%if (applicationX.getTheme(careerWorkshopThemes) == i) {%>
			checked="checked" <%}%> /></td>
		<%
		    }
		%>
	</tr>
	<%
	    }
	%>

</table>

<div class="CareerWorkshop-Terms"><bean:message
	key="label.careerWorkshopApplication.termsOfResponsability" /></div>

<div class="CareerWorkshop-Submit"><html:submit>
	<bean:message bundle="STUDENT_RESOURCES" key="button.submit" />
</html:submit></div>
</form>

<style type="text/css">
	.CareerWorkshop-Rules {
		width: 550px;
		margin-left: 100px;
	}
	
	.CareerWorkshop-BigText {
		font-size: 12px;
	}
	
	.CareerWorkshop-TextCenter {
		text-align: center;
	}
	
	.CareerWorkshop-TextLeft {
		text-align: left;
	}
	
	.CareerWorkshop-TextRight {
		text-align: right;
		padding-right: 10px;
	}
	
	.CareerWorkshop-ShiftsTable {
		margin-left: 220px;
		margin-top: 50px;
		border-collapse: collapse;
	}
	
	.CareerWorkshop-TableHeader {
		padding-bottom: 10px;
	}
	
	.CareerWorkshop-ThemesTable {
		margin-left: 170px;
		margin-top: 50px;
		border-collapse: collapse;
	}
	
	.CareerWorkshop-EvenRow {
		background-color: #FDFBFE;
		border-top: 2px solid #ddd;
		border-bottom: 2px solid #ddd;
	}
	
	.CareerWorkshop-OddRow {
		background-color: #F6F4FA;
		border-top: 2px solid #ddd;
		border-bottom: 2px solid #ddd;
	}
	
	.CareerWorkshop-Terms {
		width: 300px;
		margin-top: 80px;
		margin-left: 225px;
		margin-bottom: 10px;
		border-style: solid;
		border-width: 1px;
		padding: 8px;
		background-color: #F6F4FA;
		font-size: 12px;
	}
	
	.CareerWorkshop-Submit {
		margin-left: 340px;
	}
	
	.CareerWorkshop-Sucess {
		width: 550px;
		margin-left: 100px;
	}
</style>
