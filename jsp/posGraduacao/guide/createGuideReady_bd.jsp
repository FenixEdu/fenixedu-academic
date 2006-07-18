<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.lang.Integer" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>


   <span class="error"><html:errors/><br></span>

   <table>
    <bean:define id="infoGuide" name="<%= SessionConstants.GUIDE%>" />
    <bean:define id="graduationType" name="graduationType"/>
    
       <!-- Requester Name -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.requesterName"/> </td>
         <td><bean:write name="infoGuide" property="infoPerson.nome"/></td>
        </tr>
        
       <!-- Requester Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/> </td>
         <td><bean:write name="<%= SessionConstants.REQUESTER_NUMBER %>" /></td>
        </tr>

       <!-- Requester degree -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.degree"/> </td>
         <td><bean:write name="infoGuide" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/></td>
       </tr>

	<!-- Requester especialization -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.graduationType"/> </td>
         <td><bean:message name="graduationType" bundle="ENUMERATION_RESOURCES"/></td>
       </tr>


       <!-- Contributor Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/> </td>
         <td><bean:write name="infoGuide" property="infoContributor.contributorNumber"/></td>
       </tr>

       <!-- Contributor Name -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/> </td>
         <td><bean:write name="infoGuide" property="infoContributor.contributorName"/></td>
       </tr>

       <!-- Contributor Address -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/> </td>
         <td><bean:write name="infoGuide" property="infoContributor.contributorAddress"/></td>
       </tr>


		<!-- List of Items in the Guide -->
	</table>
	<br>
	<br>
	<table>
		<logic:iterate id="guideEntry" name="infoGuide" property="infoGuideEntries" >
		<tr>
			<td>
				<bean:define id="documentType"><bean:write name="guideEntry" property="documentType"/></bean:define>
				<bean:message name="documentType" bundle="ENUMERATION_RESOURCES" /> - <bean:write name="guideEntry" property="description" /></td>
			<td></td>
			<td><bean:write name="guideEntry" property="price" /> <bean:message key="label.currencySymbol"/></td>
		</tr>
			
		</logic:iterate>

      <html:form action="/createGuideReadyDispatchAction?method=create">
       <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.graduationType" property="graduationType" value='<%= pageContext.findAttribute("graduationType").toString()%>'/>
       <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.requester" property="requester" value='<%= pageContext.findAttribute(SessionConstants.REQUESTER_TYPE).toString()%>'/>
       <html:hidden alt="<%= SessionConstants.REQUESTER_NUMBER %>" property="<%= SessionConstants.REQUESTER_NUMBER %>" value='<%= pageContext.findAttribute(SessionConstants.REQUESTER_NUMBER).toString()%>'/>
       <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.others"/> </td>
         <td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.othersRemarks" property="othersRemarks"/></td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.othersPrice" property="othersPrice"/> <bean:message key="label.currencySymbol"/></td>
       </tr>
       
    </table>
    

	<br>
	<br>
	<br>
	
	<bean:message key="label.masterDegree.administrativeOffice.guideSituation"/>
	<table>
	   <!-- Guide Situation -->
       <tr>
        <td><bean:message key="label.masterDegree.administrativeOffice.remarks"/> </td>
       	<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.remarks" property="remarks"/></td>
        <td><bean:message key="label.masterDegree.administrativeOffice.newGuideSituation" />
        	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.GuideState" bundle="ENUMERATION_RESOURCES" includedFields="NON_PAYED,PAYED"/>  
            <html:select bundle="HTMLALT_RESOURCES" altKey="select.guideSituation" property="guideSituation">
           		<html:options collection="values" property="value" labelProperty="label" />
            </html:select>          
       	</td>
       </tr>
	</table>

	
    <bean:message key="label.masterDegree.administrativeOffice.payment" />
	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.transactions.PaymentType"  bundle="ENUMERATION_RESOURCES"/>
    <html:select bundle="HTMLALT_RESOURCES" altKey="select.paymentType" property="paymentType">
       	<html:option key="dropDown.Default" value=""/>
        <html:options collection="values" property="value" labelProperty="label"/>
    </html:select> 

   <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.Criar" property="Criar">Criar Guia</html:submit>
      </html:form>
   </table>

