<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
   
    <h2><bean:message key="label.certificate.create" /></h2>
    <bean:define id="certificateList" name="<%= SessionConstants.CERTIFICATE_LIST %>"/>
   <span class="error"><html:errors/></span>
   
   <table>
    <html:form action="/chooseCertificateInfoAction?method=choose">
   	 
       <!-- Requester Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/> </td>
         <td><html:text property="requesterNumber" value=""/></td>
         </td>
       </tr>

	
      <!-- Graduation Type --> 
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.graduationType"/> </td>
         <td>
            <html:select property="graduationType">
                <html:options collection="<%= SessionConstants.SPECIALIZATIONS %>" property="value" labelProperty="label" />
             </html:select>          
         </td>
        </tr>
        
        <!-- Certificate List --> 
          <html:hidden property="page" value="2"/>
       <tr>
         <td><bean:message key="label.certificate.list"/> </td>
         <td>
            <html:select property="certificateList">
        	    <option value="" selected="selected"><bean:message key="label.certificate.default"/></option>
                <html:options name="certificateList" />
             </html:select>          
         </td>
        </tr>
        
        <tr>
        <td></td>
        <td></td>
        </tr>
        <tr>
        <td>
         
         <!-- destination -->
         <table>
         <tr>
         <br>
         <td><h2><bean:message key="label.masterDegree.administrativeOffice.destination"/><h2> </td>
         </tr>
         <logic:iterate id="item" name="<%= SessionConstants.DOCUMENT_REASON %>" >
           <tr> 
           <td>        
       			<html:multibox property="destination">
					<bean:write name="item" />
				</html:multibox>	
				<bean:write name="item" />
		    </td>
	       </tr>
		</logic:iterate>
		</table>
		</td>
		</tr>
		<tr>
        <td> <html:submit value="Submeter" property="ok"/></td>
        </tr>
        
    </html:form>
   </table>

  



 
