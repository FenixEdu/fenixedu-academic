<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.masterDegree.administrativeOffice.studentGuide" /></h2>
 <bean:define id="studentGuideList" name="<%= SessionConstants.CERTIFICATE_LIST %>"/>
<br />


<span class="error"><html:errors/></span>
   
   <table>
<html:form action="/studentGuideDispatchAction?method=createReady">
    <html:hidden property="page" value="1"/>
    <bean:define id="graduationType" name="graduationType"/>
    <html:hidden property="graduationType" value='<%= pageContext.findAttribute("graduationType").toString()%>'/>
    <bean:define id="certificateList" name="<%= SessionConstants.CERTIFICATE_LIST %>" scope="session" />
    
    <logic:iterate id="price" name="certificateList" >
    	<tr>
    		<td>
	  			<bean:write name="price" property="documentType" /> : <bean:write name="price" property="description" />
  	        </td>
  	       
    		<td>
    		<div align="right">
	  			<html:text property="quantityList" size='2' maxlength='3' value='0'/> 
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
	
	<tr>
		<td><bean:message key="label.specializationGratuity" /></td>
		<td><html:text property="specializationGratuityQuantity" size='2'/></td>
         <td>
  	        ----------------
        </td>
		<td><html:text property="specializationGratuityAmount" size='2'/> <bean:message key="label.currencySymbol" /></td>
	</tr>
   
	<tr>
		<td>
			<bean:message key="label.otherGratuity" /><html:text property="othersGratuityDescription" size='40'/>
		</td>
		
		<td>
		</td>
         <td>
        </td>
		<td><html:text property="othersGratuityAmount" size='2'/> <bean:message key="label.currencySymbol" /></td>
	</tr>
   
   </table>
   <br />
   <br />
<html:submit value="Submeter" styleClass="inputbutton" property="ok"/>
</html:form>



 
