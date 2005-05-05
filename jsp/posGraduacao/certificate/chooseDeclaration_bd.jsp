<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.certificate.declaration.create" /></h2>
<span class="error"><html:errors/></span>
<br />
	<table>
		<html:form action="/chooseDeclarationInfoAction?method=choose">
   	  	<html:hidden property="page" value="1"/> 
  	<!-- Requester Number -->
   		<tr>
         	<td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/>: </td>
         	<td><html:text property="requesterNumber" value=""/></td>
       </tr>
    <!-- Graduation Type --> 
       	<tr>
        	 <td><bean:message key="label.masterDegree.administrativeOffice.graduationType"/>: </td>
         	<td>
            	<html:select property="graduationType">
               	<html:options collection="<%= SessionConstants.SPECIALIZATIONS %>" property="value" labelProperty="label" /></html:select>          
         	</td>
        </tr>
	</table>
	<br />
	<!-- destination -->
<h2><bean:message key="label.masterDegree.administrativeOffice.destination"/><h2>
	<table>
   		<logic:iterate id="item" name="<%= SessionConstants.DOCUMENT_REASON %>" >
    	<tr> 
        	<td>        
       			<html:multibox property="destination">
					<bean:write name="item" />
				</html:multibox>	
				<bean:message name="item" property="name" bundle="ENUMERATION_RESOURCES"/>
		  	</td>
		</tr>
		</logic:iterate>
	</table>
<br />
<html:submit value="Submeter" styleClass="inputbutton" property="ok"/></td>
</html:form>  
