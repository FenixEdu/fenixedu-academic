<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<!-- masterDegreeAdministrativeOffice/guide/chooseStudentGuide_bd.jsp -->

<h2><bean:message key="label.masterDegree.administrativeOffice.studentGuide" /></h2>
<br />

<span class="error"><!-- Error messages go here --><html:errors /></span>
   
<html:form action="/studentGuideDispatchAction?method=createReady">
   <table>
	<fr:edit visible="false" name="createGuideBean" id="createGuideBean" />
	
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
    
    <bean:define id="certificateList" name="createGuideBean" property="infoPrices"/>
    
    <logic:iterate id="price" name="certificateList" >
    	<tr>
    		<td>
				<bean:define id="documentType"><bean:write name="price" property="documentType"/></bean:define>
				<bean:message name="documentType" bundle="ENUMERATION_RESOURCES" /> : <bean:write name="price" property="description" />
  	        </td>
  	       
    		<td>
    		<div align="right">
	  			<html:text bundle="HTMLALT_RESOURCES" altKey="text.quantityList" property="quantityList" size='2' maxlength='3' value='0'/> 
  			</div>
  	        </td>
  	         <td>
  	        ----------------
  	        </td>
    		<td >
    		<div align="right">
	  			<bean:write name="price" property="price" /> <bean:message key="label.currencySymbol"/>
	  	    </div>
  	        </td>
  	        
    	</tr>               
	</logic:iterate>
	
	
   
   </table>
   <br />
   <br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Submeter" styleClass="inputbutton" property="ok"/>
</html:form>



 
