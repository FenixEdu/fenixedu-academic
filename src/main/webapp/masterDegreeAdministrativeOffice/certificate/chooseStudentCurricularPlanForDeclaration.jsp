<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

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
				<html:radio property="studentCurricularPlanID" value="externalId" idName="studentCurricularPlan"/>
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
         	<logic:iterate id="item" name="<%= PresentationConstants.DOCUMENT_REASON %>" >
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
