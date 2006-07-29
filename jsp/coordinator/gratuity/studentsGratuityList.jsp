<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree" %>
<%@ page import="net.sourceforge.fenixedu.domain.person.RoleType" %>
<%@ page import="java.lang.String" %>


<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request"/>
<bean:define id="degree" name="degree" scope="request"/>
<bean:define id="order" name="order" scope="request"/>
<bean:define id="chosenYear" name="chosenYear" />

<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.listStudents"/></h2>

<html:form action="/studentsGratuityList.do?method=coordinatorStudentsGratuityList">	
	<h2>
		<bean:message key="link.masterDegree.administrativeOffice.gratuity.chosenYear"/>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.chosenYear" onchange="this.form.submit();" property="chosenYear">
			<logic:iterate id="executionYearTemp" name="executionYears">
				<bean:define id="executionYearString" name="executionYearTemp" property="year" type="java.lang.String"/>
				<html:option value="<%= executionYearString %>">
					<bean:write name="executionYearString" />
				</html:option>
			</logic:iterate>
		</html:select>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.order" property="order" />
		<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>
	</h2>
</html:form>


<span class="error"><html:errors/></span>
<p />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
			<b><bean:message key="label.masterDegree.gratuity.executionYear" /></b>&nbsp;<bean:write name="chosenYear"/><br/>

			<bean:define id="degreeString"><%=pageContext.findAttribute("degree").toString()%></bean:define>	
		
			<b><bean:message key="label.qualification.degree" /></b>&nbsp;<bean:write name="degree"/><br/>

			<b><bean:message key="label.masterDegree.gratuity.specializationArea" /></b>&nbsp;<bean:message key="label.gratuitySituationType.all"/><br/>			

			<b><bean:message key="label.masterDegree.gratuity.situation" /></b>&nbsp;<bean:message key="label.gratuitySituationType.all"/><br/>					
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
			&nbsp;<bean:write name="totalRemaingValue" />�
		</logic:present>
		</h2>
		<h2>
		<logic:present name="totalPayedValue">
			<bean:message key="label.masterDegree.gratuity.total" />&nbsp;<bean:message key="label.masterDegree.gratuity.payedValue"/>:
			&nbsp;<bean:write name="totalPayedValue" />�
		</logic:present>
		</h2>
		
		<table>
			<tr>
				<th><html:link page='<%="/studentsGratuityList.do?method=coordinatorStudentsGratuityList&amp;chosenYear="+chosenYear+"&amp;order=studentNumber&amp;degreeCurricularPlanID="+degreeCurricularPlanID%>' >					
					<center><bean:message key="label.number"/></center></html:link></th>
				<th><html:link page='<%="/studentsGratuityList.do?method=coordinatorStudentsGratuityList&amp;chosenYear="+chosenYear+"&amp;order=studentName&amp;degreeCurricularPlanID="+degreeCurricularPlanID%>' >
					<center><bean:message key="label.masterDegree.administrativeOffice.studentName"/></center></html:link></th>
				<th><html:link page='<%= "/studentsGratuityList.do?method=coordinatorStudentsGratuityList&amp;chosenYear="+chosenYear+"&amp;order=scplan&amp;degreeCurricularPlanID="+degreeCurricularPlanID%>' >
					<center><bean:message key="label.masterDegree.gratuity.SCPlan"/></center></html:link></th>	
				<th><html:link page='<%= "/studentsGratuityList.do?method=coordinatorStudentsGratuityList&amp;chosenYear="+chosenYear+"&amp;order=gratuitySituation&amp;degreeCurricularPlanID="+degreeCurricularPlanID%>' >
					<center><bean:message key="label.masterDegree.administrativeOffice.guide.reimbursementGuide.state"/></center></html:link></th>
				<th><html:link page='<%= "/studentsGratuityList.do?method=coordinatorStudentsGratuityList&amp;chosenYear="+chosenYear+"&amp;order=notPayedValue&amp;degreeCurricularPlanID="+degreeCurricularPlanID%>' >
					<center><bean:message key="label.masterDegree.gratuity.notPayedValue"/></center></html:link></th>
				<th><html:link page='<%= "/studentsGratuityList.do?method=coordinatorStudentsGratuityList&amp;chosenYear="+chosenYear+"&amp;order=payedValue&amp;degreeCurricularPlanID="+degreeCurricularPlanID%>' >
					<center><bean:message key="label.masterDegree.gratuity.payedValue"/></center></html:link></th>				
				<th><html:link page='<%= "/studentsGratuityList.do?method=coordinatorStudentsGratuityList&amp;chosenYear="+chosenYear+"&amp;order=insurance&amp;degreeCurricularPlanID="+degreeCurricularPlanID%>' >
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
						
								<bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>

						</td>
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
						
							<bean:write name="infoGratuitySituation" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
						
						</td>
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






