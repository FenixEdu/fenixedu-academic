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
			<logic:equal name="specializationLabel" value="all">
				<b><bean:message key="label.masterDegree.gratuity.specializationArea" /></b>&nbsp;<bean:message key="label.gratuitySituationType.all"/><br>			
			</logic:equal>
			<logic:notEqual name="specializationLabel" value="all">
				<b><bean:message key="label.masterDegree.gratuity.specializationArea" /></b>&nbsp;<bean:write name="specializationLabel"/><br>	
			</logic:notEqual>
			
			<bean:define id="gratuitySituationName"><%=pageContext.findAttribute("situation")%></bean:define>	
			<bean:define id="gratuitySituationNameKEY" value="<%= "label.gratuitySituationType." + gratuitySituationName.toString() %>"/>							
			<b><bean:message key="label.masterDegree.gratuity.situation" /></b>&nbsp;<bean:message key="<%= gratuitySituationNameKEY.toString() %>"/><br>
		</td>
	</tr>
</table>
<p />

<logic:notPresent name="infoGratuitySituationList">
	<span class="error"><bean:message key="error.masterDegree.gatuyiuty.impossible.studentsGratuityList" /></span>
</logic:notPresent>

<logic:present name="infoGratuitySituationList">
	<logic:empty name="infoGratuitySituationList">
		<span class="error"><bean:message key="error.masterDegree.gatuyiuty.impossible.studentsGratuityList.empty" /></span>
	</logic:empty>
	<logic:notEmpty name="infoGratuitySituationList">
		<bean:size id="sizeList" name="infoGratuitySituationList"/>
		<bean:message key="label.masterDegree.gratuity.sizeList" arg0="sizeList" />	

		<table>
			<tr>
				<td><bean:message key="label.number"/></td>
				<td><bean:message key="label.masterDegree.administrativeOffice.studentName"/></td>
				<td><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.state"/></td>
				<td><bean:message key="label.masterDegree.gartuity.payedValue"/></td>
				<td><bean:message key="label.masterDegree.gartuity.notPayedValue"/></td>
			</tr>
			<logic:iterate id="infoGratuitySituation" name="infoGratuitySituationList" indexId="row">
				<bean:define id="isEven">
					<%= String.valueOf(row.intValue() % 2) %>
				</bean:define>
				<logic:equal name="isEven" value="0"> <!-- Linhas pares -->
					<tr>
						<td><bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.number"/></td>
						<td><bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/></td>
						<logic:greaterThan  name="infoGratuitySituation" property="remainingValue" value="0">
							<td><bean:message key="label.gratuitySituationType.debtor"/></td>
						</logic:greaterThan>
						<logic:lessThan  name="infoGratuitySituation" property="remainingValue" value="0">
							<td><bean:message key="label.gratuitySituationType.creditor"/></td>
						</logic:lessThan>
						<logic:equal name="infoGratuitySituation" property="remainingValue" value="0">
							<td><bean:message key="label.gratuitySituationType.regularized"/></td>
						</logic:equal>						
						<td><bean:write name="infoGratuitySituation" property="payedValue"/></td>
						<td><bean:write name="infoGratuitySituation" property="remainingValue"/></td>
					</tr>
				</logic:equal>
			
				<logic:equal name="isEven" value="1"> <!-- Linhas pares  -->
					<tr>
						<td><bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.number"/></td>
						<td><bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/></td>
						<logic:greaterThan  name="infoGratuitySituation" property="remainingValue" value="0">
							<td><bean:message key="label.gratuitySituationType.debtor"/></td>
						</logic:greaterThan>
						<logic:lessThan  name="infoGratuitySituation" property="remainingValue" value="0">
							<td><bean:message key="label.gratuitySituationType.creditor"/></td>
						</logic:lessThan>
						<logic:equal name="infoGratuitySituation" property="remainingValue" value="0">
							<td><bean:message key="label.gratuitySituationType.regularized"/></td>
						</logic:equal>						
						<td><bean:write name="infoGratuitySituation" property="payedValue"/></td>
						<td><bean:write name="infoGratuitySituation" property="remainingValue"/></td>
					</tr>
				</logic:equal>
			</logic:iterate>
		<table>			
	</logic:notEmpty>	
</logic:present>

