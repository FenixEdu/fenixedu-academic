<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.masterDegree.administrativeOffice.studentGuide" /></h2>
 <bean:define id="studentGuideList" name="<%= SessionConstants.CERTIFICATE_LIST %>"/>
<br />


<span class="error"><!-- Error messages go here --><html:errors /></span>
   
   <table>
<html:form action="/studentGuideDispatchAction?method=createReady">
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
    <bean:define id="graduationType" name="graduationType"/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.graduationType" property="graduationType" value='<%= pageContext.findAttribute("graduationType").toString()%>'/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.requester" property="requester" value='<%= pageContext.findAttribute(SessionConstants.REQUESTER_TYPE).toString()%>'/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.number" property="number" value='<%= pageContext.findAttribute(SessionConstants.REQUESTER_NUMBER).toString()%>'/>
    
    <bean:define id="certificateList" name="<%= SessionConstants.CERTIFICATE_LIST %>"/>
    
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



 
