<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="Util.GratuitySituationType" %>


<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.listStudents"/></h2>
<span class="error"><html:errors/></span>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
			<bean:define id="executionYearLabel"><%=pageContext.findAttribute("executionYear")%></bean:define>	
			<b><bean:message key="label.masterDegree.gratuity.executionYear" /></b>&nbsp;<bean:write name="executionYearLabel"/><br>
			<bean:define id="degreeString"><%=pageContext.findAttribute("degree").toString()%></bean:define>	
			<bean:define id="degreeLabel"><%= degreeString.toString().substring(0, degreeString.toString().indexOf("#"))%></bean:define>	
			<b><bean:message key="label.qualification.degree" /></b>&nbsp;<bean:write name="degreeLabel"/><br>
			<bean:define id="specializationLabel"><%=pageContext.findAttribute("specialization")%></bean:define>	
			<b><bean:message key="label.masterDegree.gratuity.specializationArea" /></b>&nbsp;<bean:write name="specializationLabel"/><br>
			<bean:define id="gratuitySituationName"><%=pageContext.findAttribute("situation")%></bean:define>	
			<bean:define id="gratuitySituationNameKEY" value="<%= "label.gratuitySituationType." + gratuitySituationName.toString() %>"/>							
			<b><bean:message key="label.masterDegree.gratuity.situation" /></b>&nbsp;<bean:message key="<%= gratuitySituationNameKEY.toString() %>"/><br>
		</td>
	</tr>
</table>
<p />

<logic:notPresent name="studentsGratuityList">
	<span class="error"><bean:message key="error.masterDegree.gatuyiuty.impossible.studentsGratuityList" /></span>
</logic:notPresent>

<logic:present name="studentsGratuityList">
	<logic:empty name="studentsGratuityList">
		<span class="error"><bean:message key="error.masterDegree.gatuyiuty.impossible.studentsGratuityList.empty" /></span>
	</logic:empty>
	<logic:notEmpty name="studentsGratuityList">
		<bean:size id="sizeList" name="studentsGratuityList"/>
		<bean:write name="sizeList"/>
	</logic:notEmpty>	
</logic:present>

