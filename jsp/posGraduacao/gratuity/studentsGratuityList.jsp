<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.person.RoleType" %>

<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>
<% boolean masterDegreeUser = false; %>
<logic:iterate id="role" name="userView" property="roles">
	<logic:equal name="role" property="roleType" value="<%= RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE.toString() %>" >
		<% masterDegreeUser = true; %>
	</logic:equal>	
</logic:iterate>	
	
<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.listStudents"/></h2>
<span class="error"><html:errors/></span>
<p />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
			<bean:define id="executionYearLabel"><%=pageContext.findAttribute("executionYear")%></bean:define>	
			<b><bean:message key="label.masterDegree.gratuity.executionYear" /></b>&nbsp;<bean:write name="executionYearLabel"/><br>

			<bean:define id="degreeString"><%=pageContext.findAttribute("degree").toString()%></bean:define>	
			<logic:notEqual name="degreeString" value="all">0
				<bean:define id="degreeId"><%= pageContext.findAttribute("degree").toString().substring(pageContext.findAttribute("degree").toString().indexOf("#")+1)%></bean:define>
				<bean:define id="degreeLabel"><%= degreeString.toString().substring(0, degreeString.toString().indexOf(">"))%></bean:define>	
				<b><bean:message key="label.qualification.degree" /></b>&nbsp;<bean:write name="degreeLabel"/><br>
			</logic:notEqual>
			<logic:equal name="degreeString" value="all">
				<b><bean:message key="label.qualification.degree" /></b>&nbsp;<bean:message key="label.masterDegree.gratuity.all"/><br>
			</logic:equal>
	
			<bean:define id="specializationLabel"><%=pageContext.findAttribute("specialization")%></bean:define>	
			<logic:equal name="specializationLabel" value="all">
				<b><bean:message key="label.masterDegree.gratuity.specializationArea" /></b>&nbsp;<bean:message key="label.gratuitySituationType.all"/><br>			
			</logic:equal>
			<logic:notEqual name="specializationLabel" value="all">
				<b><bean:message key="label.masterDegree.gratuity.specializationArea" /></b>&nbsp;<bean:write name="specializationLabel"/><br>	
			</logic:notEqual>
			
			<bean:define id="gratuitySituationName"><%=pageContext.findAttribute("situation")%></bean:define>	
			<b><bean:message key="label.masterDegree.gratuity.situation" /></b>&nbsp;<bean:message name="gratuitySituationName" bundle="ENUMERATION_RESOURCES"/><br>					
		</td>
	</tr>
</table>
<p />

<logic:notPresent name="infoGratuitySituationList">
	<span class="error"><bean:message key="error.masterDegree.gratuity.impossible.studentsGratuityList" /></span>
</logic:notPresent>

<logic:present name="infoGratuitySituationList">
	<logic:empty name="infoGratuitySituationList">
		<span class="error"><bean:message key="error.masterDegree.gratuity.impossible.studentsGratuityList.empty" /></span>
	</logic:empty>
	<logic:notEmpty name="infoGratuitySituationList">
		<bean:size id="sizeList" name="infoGratuitySituationList"/>
		<h2><bean:message key="label.masterDegree.gratuity.sizeList" arg0="<%= sizeList.toString() %>" /></h2>	
		<h2>
		<logic:present name="totalRemaingValue">
			<bean:message key="label.masterDegree.gratuity.total" />&nbsp;<bean:message key="label.masterDegree.gratuity.notPayedValue"/>:
			&nbsp;<bean:write name="totalRemaingValue" />
		</logic:present>
		</h2>
		<h2>
		<logic:present name="totalPayedValue">
			<bean:message key="label.masterDegree.gratuity.total" />&nbsp;<bean:message key="label.masterDegree.gratuity.payedValue"/>:
			&nbsp;<bean:write name="totalPayedValue" />
		</logic:present>
		</h2>
		
		<table>
			<tr>
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=studentNumber&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<center><bean:message key="label.number"/></center></html:link></th>
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=studentName&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<center><bean:message key="label.masterDegree.administrativeOffice.studentName"/></center></html:link></th>
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=dcplan&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<center><bean:message key="label.masterDegree.gratuity.DCPlan"/></center></html:link></th>				
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=scplanState&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<center><bean:message key="label.masterDegree.gratuity.SCPlanState"/></center></html:link></th>	
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=gratuitySituation&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<center><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.state"/></center></html:link></th>
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=notPayedValue&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<center><bean:message key="label.masterDegree.gratuity.notPayedValue"/></center></html:link></th>
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=payedValue&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<center><bean:message key="label.masterDegree.gratuity.payedValue"/></center></html:link></th>				
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=insurance&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<center><bean:message key="label.masterDegree.gratuity.insurance"/></center></html:link></th>					
				
			</tr>
			<logic:iterate id="infoGratuitySituation" name="infoGratuitySituationList" indexId="row">
				<bean:define id="isEven">
					<%= String.valueOf(row.intValue() % 2) %>
				</bean:define>
				<bean:define id="studentNumber" name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.number"/>
				<bean:define id="situationType" name="infoGratuitySituation" property="situationType.name"/>
				<bean:define id="insurancePayedKey" name="infoGratuitySituation" property="insurancePayed"/>
							
				<logic:equal name="isEven" value="0"> <!-- Linhas pares -->
					<tr>
						<td bgcolor='#C0C0C0'><center><bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.number"/></center></td>
						<td bgcolor='#C0C0C0'>		
							<% if(masterDegreeUser) {%>					
							<html:link page="<%= "/studentSituation.do?method=readStudent&degreeType=MASTER_DEGREE&studentNumber="+studentNumber %>" >
								<bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
							</html:link>
							<% } else {%>							
								<bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
							<% } %>
						</td>
						<td bgcolor='#C0C0C0'><center><bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoDegreeCurricularPlan.name"/></center></td>
						<td bgcolor='#C0C0C0'><center><bean:message name="infoGratuitySituation" property="infoStudentCurricularPlan.currentState.name" bundle="ENUMERATION_RESOURCES"/></center></td>
						<td bgcolor='#C0C0C0'><center><bean:message name="situationType" bundle="ENUMERATION_RESOURCES"/></center></td>
						<td bgcolor='#C0C0C0'><center><bean:write name="infoGratuitySituation" property="remainingValue"/></center></td>	
						<td bgcolor='#C0C0C0'><center><bean:write name="infoGratuitySituation" property="payedValue"/></center></td>	
						<td bgcolor='#C0C0C0'><center><bean:message key="<%= insurancePayedKey.toString() %>"/></center></td>										
					</tr>
				</logic:equal>
			
				<logic:equal name="isEven" value="1"> <!-- Linhas pares  -->
					<tr>
						<td><center><bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.number"/></center></td>
						<td>
							<% if(masterDegreeUser) {%>					
							<html:link page="<%= "/studentSituation.do?method=readStudent&degreeType=MASTER_DEGREE&studentNumber="+studentNumber %>" >
								<bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
							</html:link>
							<% } else {%>							
								<bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
							<% } %>							
						</td>
						<td><center><bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoDegreeCurricularPlan.name"/></center></td>
						<td><center><bean:message name="infoGratuitySituation" property="infoStudentCurricularPlan.currentState.name" bundle="ENUMERATION_RESOURCES"/></center></td>
						<td><center><bean:message name="situationType" bundle="ENUMERATION_RESOURCES"/></center></td>					
						<td><center><bean:write name="infoGratuitySituation" property="remainingValue"/></center></td>
						<td><center><bean:write name="infoGratuitySituation" property="payedValue"/></center></td>
						<td><center><bean:message key="<%= insurancePayedKey.toString() %>"/></center></td>										
					</tr>
				</logic:equal>
			</logic:iterate>
		</table>						
	</logic:notEmpty>		
</logic:present>

