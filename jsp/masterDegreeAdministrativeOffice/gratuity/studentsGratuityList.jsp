<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.domain.person.RoleType" %>

<bean:define id="userView" name="<%= pt.ist.fenixWebFramework.servlets.filters.USER_SESSION_ATTRIBUTE %>" scope="session"/>
<% boolean masterDegreeUser = false; %>
<logic:iterate id="roleType" name="userView" property="roleTypes">
	<logic:equal name="roleType" value="<%= RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE.toString() %>" >
		<% masterDegreeUser = true; %>
	</logic:equal>	
</logic:iterate>	
	
<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.listStudents"/></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<div class="infoop2">
	<bean:define id="executionYearLabel"><%=pageContext.findAttribute("executionYear")%></bean:define>	
	
	<p><bean:message key="label.masterDegree.gratuity.executionYear" />: <bean:write name="executionYearLabel"/></p>

	<bean:define id="degreeString"><%=pageContext.findAttribute("degree").toString()%></bean:define>	
	<logic:notEqual name="degreeString" value="all">
		<bean:define id="degreeId"><%= pageContext.findAttribute("degree").toString().substring(pageContext.findAttribute("degree").toString().indexOf("#")+1)%></bean:define>
		<bean:define id="degreeLabel"><%= degreeString.toString().substring(0, degreeString.toString().indexOf(">"))%></bean:define>	
		<bean:message key="label.qualification.degree" />:&nbsp;<bean:write name="degreeLabel"/><br/>
	</logic:notEqual>
	
	<logic:equal name="degreeString" value="all">
		<p><bean:message key="label.qualification.degree" />:</b>&nbsp;<bean:message key="label.masterDegree.gratuity.all"/></p>
	</logic:equal>

	<bean:define id="specializationLabel"><%=pageContext.findAttribute("specialization")%></bean:define>	
	<logic:equal name="specializationLabel" value="all">
		<p><bean:message key="label.masterDegree.gratuity.specializationArea" />: <bean:message key="label.gratuitySituationType.all"/></p>
	</logic:equal>
	<logic:notEqual name="specializationLabel" value="all">
		<p><bean:message key="label.masterDegree.gratuity.specializationArea" />: <bean:write name="specializationLabel"/></p>
	</logic:notEqual>
	
	<bean:define id="gratuitySituationName"><%=pageContext.findAttribute("situation")%></bean:define>	
	<p><bean:message key="label.masterDegree.gratuity.situation" />: <bean:message name="gratuitySituationName" bundle="ENUMERATION_RESOURCES"/></p>
</div>	

	

<logic:notPresent name="infoGratuitySituationList">
	<p><span class="error"><!-- Error messages go here --><bean:message key="error.masterDegree.gratuity.impossible.studentsGratuityList" /></span></p>
</logic:notPresent>

<logic:present name="infoGratuitySituationList">
	<logic:empty name="infoGratuitySituationList">
		<p><span class="error"><!-- Error messages go here --><bean:message key="error.masterDegree.gratuity.impossible.studentsGratuityList.empty" /></span></p>
	</logic:empty>
	<logic:notEmpty name="infoGratuitySituationList">
		<bean:size id="sizeList" name="infoGratuitySituationList"/>
		
		<p><em><bean:message key="label.masterDegree.gratuity.sizeList" arg0="<%= sizeList.toString() %>" /></em></p>
		<p class="mbottom05">
			<logic:present name="totalRemaingValue">
				<strong>
					<bean:message key="label.masterDegree.gratuity.total" /> <bean:message key="label.masterDegree.gratuity.notPayedValue"/>:
					&nbsp;<bean:write name="totalRemaingValue" />
				</strong>
			</logic:present>
		</p>
		<p class="mtop05">
			<logic:present name="totalPayedValue">
				<strong>
					<bean:message key="label.masterDegree.gratuity.total" /> <bean:message key="label.masterDegree.gratuity.payedValue"/>:
					&nbsp;<bean:write name="totalPayedValue" />
				</strong>
			</logic:present>
		</p>
		
		<table class="tstyle4 tdcenter">
			<tr>
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=studentNumber&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<bean:message key="label.number"/></html:link></th>
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=studentName&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<bean:message key="label.masterDegree.administrativeOffice.studentName"/></html:link></th>
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=dcplan&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<bean:message key="label.masterDegree.gratuity.DCPlan"/></html:link></th>				
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=scplanState&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<bean:message key="label.masterDegree.gratuity.SCPlanState"/></html:link></th>	
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=gratuitySituation&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.state"/></html:link></th>
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=notPayedValue&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<bean:message key="label.masterDegree.gratuity.notPayedValue"/></html:link></th>
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=payedValue&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<bean:message key="label.masterDegree.gratuity.payedValue"/></html:link></th>				
				<th><html:link page="<%= "/studentsGratuityList.do?method=studentsGratuityList&amp;order=insurance&amp;executionYear="+executionYearLabel+"&amp;specialization="+specializationLabel+"&amp;situation="+gratuitySituationName+"&amp;degree="+degreeString%>" >
					<bean:message key="label.masterDegree.gratuity.insurance"/></html:link></th>					
				
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
						<td><bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.number"/></td>
						<td class="aleft">		
							<% if(masterDegreeUser) {%>					
							<html:link page="<%= "/studentSituation.do?method=readStudent&degreeType=MASTER_DEGREE&studentNumber="+studentNumber %>" >
								<bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
							</html:link>
							<% } else {%>							
								<bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
							<% } %>
						</td>
						<td><bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoDegreeCurricularPlan.name"/></td>
						<td><bean:message name="infoGratuitySituation" property="infoStudentCurricularPlan.currentState.name" bundle="ENUMERATION_RESOURCES"/></td>
						<td><bean:message name="situationType" bundle="ENUMERATION_RESOURCES"/></td>
						<td><bean:write name="infoGratuitySituation" property="remainingValue"/></td>	
						<td><bean:write name="infoGratuitySituation" property="payedValue"/></td>	
						<td><bean:message key="<%= insurancePayedKey.toString() %>"/></td>										
					</tr>
				</logic:equal>
			
				<logic:equal name="isEven" value="1"> <!-- Linhas pares  -->
					<tr>
						<td><bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.number"/></td>
						<td class="aleft">
							<% if(masterDegreeUser) {%>					
							<html:link page="<%= "/studentSituation.do?method=readStudent&degreeType=MASTER_DEGREE&studentNumber="+studentNumber %>" >
								<bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
							</html:link>
							<% } else {%>							
								<bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
							<% } %>							
						</td>
						<td><bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoDegreeCurricularPlan.name"/></td>
						<td><bean:message name="infoGratuitySituation" property="infoStudentCurricularPlan.currentState.name" bundle="ENUMERATION_RESOURCES"/></td>
						<td><bean:message name="situationType" bundle="ENUMERATION_RESOURCES"/></td>					
						<td><bean:write name="infoGratuitySituation" property="remainingValue"/></td>
						<td><bean:write name="infoGratuitySituation" property="payedValue"/></td>
						<td><bean:message key="<%= insurancePayedKey.toString() %>"/></td>										
					</tr>
				</logic:equal>
			</logic:iterate>
		</table>						
	</logic:notEmpty>		
</logic:present>

