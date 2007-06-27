<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<h2><bean:message key="label.certificate.declaration.create" /></h2>
<br/>

<logic:present name="registrations">
	<html:form action="/chooseDeclarationInfoAction.do?method=chooseFinal">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden property="requesterNumber" />
		
		<logic:iterate id="registration" name="registrations">
			<strong>
				<bean:write name="registration" property="student.person.name"/>
				 (<bean:write name="registration" property="student.number"/>) - 
			</strong>
			<bean:write name="registration" property="degree.name"/>
			 (<bean:write name="registration" property="number"/>)
			<br/>
			<logic:iterate id="studentCurricularPlan" name="registration" property="studentCurricularPlans">
				<html:radio property="studentCurricularPlanID" value="idInternal" idName="studentCurricularPlan"/>
				<bean:write name="studentCurricularPlan" property="name"/>
				<br/>
			</logic:iterate>
			<br/>
		</logic:iterate>
	
		<br/>
	
		<table>
	    	<tr>
	         	<td><h2><bean:message key="label.masterDegree.administrativeOffice.destination"/><h2></td>
	       	</tr>
         	<logic:iterate id="item" name="<%= SessionConstants.DOCUMENT_REASON %>" >
		      	<tr> 
		        	<td>        
		       			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.destination" property="destination">
							<bean:write name="item" />
						</html:multibox>	
						<bean:message name="item" property="name" bundle="ENUMERATION_RESOURCES"/>
				    </td>
		       </tr>
			</logic:iterate>
		</table>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Submeter" styleClass="inputbutton" property="ok"/>
	</html:form>
</logic:present>
