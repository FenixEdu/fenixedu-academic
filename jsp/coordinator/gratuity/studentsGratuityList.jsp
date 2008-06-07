<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree" %>
<%@ page import="net.sourceforge.fenixedu.domain.person.RoleType" %>
<%@ page import="java.lang.String" %>


<bean:define id="userView" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" scope="session"/>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request"/>
<bean:define id="degree" name="degree" scope="request"/>
<bean:define id="order" name="order" scope="request"/>
<bean:define id="chosenYear" name="chosenYear" />

<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.listStudents"/></h2>

<html:form action="/studentsGratuityList.do?method=coordinatorStudentsGratuityList">	
	<p class="mvert15">
		<bean:message key="link.masterDegree.administrativeOffice.gratuity.chosenYear"/>: 
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
	</p>
</html:form>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<div class="infoop2">
	<p><bean:message key="label.masterDegree.gratuity.executionYear" />: <bean:write name="chosenYear"/></p>
	<bean:define id="degreeString"><%=pageContext.findAttribute("degree").toString()%></bean:define>	
	<p><bean:message key="label.qualification.degree" />: <bean:write name="degree"/></p>
	<p><bean:message key="label.masterDegree.gratuity.specializationArea" />: <bean:message key="label.gratuitySituationType.all"/></p>
	<p><bean:message key="label.masterDegree.gratuity.situation" />: <bean:message key="label.gratuitySituationType.all"/></p>		
</div>


<logic:notPresent name="infoGratuitySituationList">
	<p>
		<span class="error"><!-- Error messages go here --><bean:message key="error.masterDegree.gratuity.impossible.studentsGratuityList" /></span>
	</p>
</logic:notPresent>

<logic:present name="infoGratuitySituationList">

	<logic:empty name="infoGratuitySituationList">
		<p>
			<span class="error"><!-- Error messages go here --><bean:message key="error.masterDegree.gratuity.impossible.studentsGratuityList.empty" /></span>
		</p>
	</logic:empty>
	
	<logic:notEmpty name="infoGratuitySituationList">
		<bean:size id="sizeList" name="infoGratuitySituationList"/>
		<p>
			<strong>
				<bean:message key="label.masterDegree.gratuity.sizeList" arg0="<%= sizeList.toString() %>" />
			</strong>
		</p>
		<p>
			<logic:present name="totalRemaingValue">
				<bean:message key="label.masterDegree.gratuity.total" />&nbsp;<bean:message key="label.masterDegree.gratuity.notPayedValue"/>:
				<strong><bean:write name="totalRemaingValue" /></strong>
			</logic:present>
		</p>
		<p>
			<logic:present name="totalPayedValue">
				<bean:message key="label.masterDegree.gratuity.total" />&nbsp;<bean:message key="label.masterDegree.gratuity.payedValue"/>:
				<strong><bean:write name="totalPayedValue" /></strong>
			</logic:present>
		</p>
		
		<table class="tstyle4">
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






