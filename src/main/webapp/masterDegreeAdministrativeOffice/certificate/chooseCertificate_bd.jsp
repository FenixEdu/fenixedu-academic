<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<h2><bean:message key="label.certificate.create" /></h2>
<bean:define id="certificateList" name="<%= PresentationConstants.CERTIFICATE_LIST %>"/>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
   <table>
    <html:form action="/chooseCertificateInfoAction?method=choose">
       <!-- Requester Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/>: </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.requesterNumber" property="requesterNumber" value=""/></td>
         </td>
       </tr>
      <!-- Graduation Type --> 
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.graduationType"/>: </td>
         <td>
         	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" excludedFields="STUDENT_CURRICULAR_PLAN_INTEGRATED_MASTER_DEGREE" bundle="ENUMERATION_RESOURCES"/>
            <html:select bundle="HTMLALT_RESOURCES" altKey="select.graduationType" property="graduationType">
            	<html:option key="dropDown.Default" value=""/>
                <html:options collection="values" property="value" labelProperty="label" />
             </html:select>          
         </td>
        </tr>
        <!-- Certificate List --> 
          <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
       <tr>
         <td><bean:message key="label.certificate.list"/>: </td>
         <td>
       
            <html:select bundle="HTMLALT_RESOURCES" altKey="select.certificateList" property="certificateList" >
                <html:options collection="<%= PresentationConstants.CERTIFICATE_LIST %>" property="value" labelProperty="label"  />
             </html:select>       
        
         </td>
        </tr>
 	</table>
<br />
         <!-- destination -->
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
<br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Submeter" styleClass="inputbutton" property="ok"/>
</html:form>



 
