<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.lang.Integer" %>

<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuideSituation" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuide" %>
<%@ page import="net.sourceforge.fenixedu.util.State" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>

<%@ page import="net.sourceforge.fenixedu.domain.GuideState" %>

     <bean:define id="infoGuide" name="<%= SessionConstants.GUIDE %>" scope="request" />
     <bean:define id="number" name="infoGuide" property="number" />
     <bean:define id="year" name="infoGuide" property="year" />
     <bean:define id="version" name="infoGuide" property="version" />

	 <strong>
	 <bean:message key="label.masterDegree.administrativeOffice.guideInformation" 
				   arg0='<%= pageContext.findAttribute("version").toString() %>'
				   arg1='<%= pageContext.findAttribute("number").toString() %>' 
				   arg2='<%= pageContext.findAttribute("year").toString() %>' 
	  />
	 </strong>
	 
     <table>
     

          <tr>
            <td><bean:message key="label.person.name" /></td>
            <td><bean:write name="infoGuide" property="infoPerson.nome"/></td>
          </tr>

          <tr>
            <td> <bean:message key="label.masterDegree.administrativeOffice.degree"/> </td>
            <td> <bean:write name="infoGuide" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/> </td>
          </tr>

          <tr> 
            <td><strong>Entidade Pagadora:</strong></td>
            <td>&nbsp;</td>
          </tr>
          <logic:empty name="guide" property="infoContributor.contributorNumber">
          	  <tr> 
	            <td><bean:message key="label.identificationDocumentType"/>:</td>
	            <td><bean:message name="guide" property="infoContributor.documentType.name" bundle="ENUMERATION_RESOURCES"/></td>
	          </tr>
	          <tr> 
	            <td><bean:message key="label.identificationDocumentNumber"/>:</td>
	            <td><bean:write name="guide" property="infoContributor.documentIdNumber"/></td>
	          </tr>	          
          </logic:empty>
          <logic:notEmpty name="guide" property="infoContributor.contributorNumber">
	          <tr> 
	            <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/>:</td>
	            <td><bean:write name="guide" property="infoContributor.contributorNumber"/></td>
	          </tr>
          </logic:notEmpty>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/>:</td>
            <td><bean:write name="infoGuide" property="infoContributor.contributorName"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/>:</td>
            <td><bean:write name="infoGuide" property="infoContributor.contributorAddress"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.person.postCode"/></td>
            <td><bean:write name="infoGuide" property="infoContributor.areaCode"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.person.areaOfPostCode"/></td>
            <td><bean:write name="infoGuide" property="infoContributor.areaOfAreaCode"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.person.place"/></td>
            <td><bean:write name="infoGuide" property="infoContributor.area"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.person.addressParish"/></td>
            <td><bean:write name="infoGuide" property="infoContributor.parishOfResidence"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.person.addressMunicipality"/></td>
            <td><bean:write name="infoGuide" property="infoContributor.districtSubdivisionOfResidence"/></td>
          </tr>
          <tr> 
            <td><bean:message key="label.person.addressDistrict"/></td>
            <td><bean:write name="infoGuide" property="infoContributor.districtOfResidence"/></td>
          </tr>

	</table>
	<br/>
	<br/>
	<table>
		<tr align="center">
			<td><bean:message key="label.masterDegree.administrativeOffice.documentType" /></td>
			<td><bean:message key="label.masterDegree.administrativeOffice.description" /></td>
			<td><bean:message key="label.masterDegree.administrativeOffice.quantity" /></td>
			<td><bean:message key="label.masterDegree.administrativeOffice.price" /></td>
		</tr>

		<logic:notEmpty name="infoGuide" property="infoGuideEntries">
         <logic:iterate id="guideEntry" name="infoGuide" property="infoGuideEntries">
           <tr>
            <td>
            	<bean:define id="documentType"><bean:write name="guideEntry" property="documentType"/></bean:define>
				<bean:message name="documentType" bundle="ENUMERATION_RESOURCES" />
            </td>
            <td><bean:write name="guideEntry" property="description"/></td>
            <td><bean:write name="guideEntry" property="quantity"/></td>
            <td align="right"><bean:write name="guideEntry" property="price"/> <bean:message key="label.currencySymbol" /></td>
		   </tr>
         </logic:iterate>
        </logic:notEmpty>
		<logic:empty name="infoGuide" property="infoGuideEntries">
			<span class="error"><!-- Error messages go here --><bean:message key="error.exception.masterDegree.nonExistingEntriesGuide" /></span>
        </logic:empty>
        
         <tr>
         	<td></td>
         	<td></td>
         	<td><bean:message key="label.masterDegree.administrativeOffice.guideTotal" />:</td>
         	<td align="right"><bean:write name="infoGuide" property="total"/> <bean:message key="label.currencySymbol" /></td>
         </tr>
     </table>
     
     <br/>
     <br/>
     <table>
		<tr>
			<td><bean:message key="label.masterDegree.administrativeOffice.creationDate" /></td>
            <logic:present name="infoGuide" property="creationDate" >
	            <bean:define id="date" name="infoGuide" property="creationDate" />
				<td><%= Data.format2DayMonthYear((Date) date,"-") 
%></td>   
			</logic:present>
		</tr>
		<tr>
			<td><bean:message key="label.masterDegree.administrativeOffice.remarks" /></td>
			<td><bean:write name="infoGuide" property="remarks"/></td>
		</tr>
     </table>

	<br/>
	<br/>

	<strong><bean:message key="label.masterDegree.administrativeOffice.guideSituationList" /></strong>
	
	<br/>
	<br/>


		<logic:notEmpty name="infoGuide" property="infoGuideSituations">
	         <logic:iterate id="guideSituation" name="infoGuide" property="infoGuideSituations">
		      <table>
	            <% if (((InfoGuideSituation) guideSituation).getState().equals(new State(State.ACTIVE))) { 
%>
		            
					<tr>
	         	    <td><p><b><center><bean:message key="label.masterDegree.administrativeOffice.activeSituation" /></center></b></p></td>
					</tr>
	         	<% } 
%>
	           <tr>
				<td><bean:message key="label.masterDegree.administrativeOffice.remarks" /></td>
	            <td><bean:write name="guideSituation" property="remarks"/></td>
	           </tr>
	           <tr>
				<td><bean:message key="label.masterDegree.administrativeOffice.situation" /></td>
	            <td><bean:message name="guideSituation" property="situation.name" bundle="ENUMERATION_RESOURCES"/></td>
	           </tr>
	           <tr>
				<td><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></td>
	            <logic:present name="guideSituation" property="date" >
		            <bean:define id="date" name="guideSituation" property="date" />
					<td><%= Data.format2DayMonthYear((Date) date, "-") 
%></td>   			
				</logic:present>
	           </tr>
	           <% if (((InfoGuideSituation) guideSituation).getSituation().equals(GuideState.PAYED)) { 
%>
	           		<tr>
	        			<td><bean:message key="label.masterDegree.administrativeOffice.payment" /></td>
	        			<td>
	        				<bean:define id="paymentType" >
	        					<bean:write name="infoGuide" property="paymentType"/>
	        				</bean:define>
	        				<bean:message name="paymentType" bundle="ENUMERATION_RESOURCES" />
	        			</td>
	        		</tr>
	           		<tr>
	        			<td><bean:message key="label.masterDegree.administrativeOffice.paymentDate" /></td>
			            <logic:present name="infoGuide" property="paymentDate" >
				            <bean:define id="date" name="infoGuide" property="paymentDate" />
							<td><%= Data.format2DayMonthYear((Date) date, "-") 
%></td>   			
						</logic:present>
	        		</tr>
	         	<% } 
%>
	          </table>
	          <br/><br/>
	         </logic:iterate>
         </logic:notEmpty>
		<logic:empty  name="infoGuide" property="infoGuideSituations">
			<span class="error"><!-- Error messages go here --><bean:message key="error.exception.masterDegree.nonExistingSituationGuide" /></span>
        </logic:empty>


	<br/>	
	<br/>

    <bean:define id="arguments"><%= "&" 
%>page=0<%= "&" 
%>year=<bean:write name="year"/><%= "&" 
%>number=<bean:write name="number"/><%= "&" 
%>version=<bean:write name="version"/>
    </bean:define>
	

			<% List guideList = (List) request.getAttribute(SessionConstants.GUIDE_LIST);
			   InfoGuide guide = (InfoGuide) request.getAttribute(SessionConstants.GUIDE);
			   if(guide.getVersion().equals(new Integer(guideList.size())) && guide.getInfoGuideSituation() != null && !guide.getInfoGuideSituation().getSituation().equals(GuideState.ANNULLED)) {
			
%>	
        	<table>
        		<tr>
        		<td width="20%">
        			<bean:define id="linkChangeSituation">/editGuideSituation.do?method=prepareEditSituation<bean:write name="arguments"/>
                	</bean:define>
        			<html:link page='<%= pageContext.findAttribute("linkChangeSituation").toString() %>'>
        				<bean:message key="link.masterDegree.administrativeOffice.changeGuideSituation" />
        			</html:link>
        		</td>
        		<% if (guide.getInfoGuideSituation() != null && guide.getInfoGuideSituation().getSituation().equals(GuideState.NON_PAYED)) { 
%>
					<td width="20%">
						<bean:define id="linkChangeInformation">/editGuideInformation.do?method=prepareEditInformation<bean:write name="arguments"/>
						</bean:define>
						<html:link page='<%= pageContext.findAttribute("linkChangeInformation").toString() %>'>
							<bean:message key="link.masterDegree.administrativeOffice.changeGuideInformation" />
						</html:link>
					</td>
        		<% } 
%>
        		<td width="20%">
        			<bean:define id="linkCreateReimbursementGuide">/createReimbursementGuide.do?method=prepare<bean:write name="arguments"/>
                	</bean:define>
        			<html:link page='<%= pageContext.findAttribute("linkCreateReimbursementGuide").toString() %>'>
        				<bean:message key="link.masterDegree.administrativeOffice.createReimbursementGuide" />
        			</html:link>
        		</td>
        		<td width="20%">
        			<bean:define id="linkViewReimbursementGuides">/viewReimbursementGuides.do?method=view<bean:write name="arguments"/>
                	</bean:define>
        			<html:link page='<%= pageContext.findAttribute("linkViewReimbursementGuides").toString() %>'>
        				<bean:message key="link.masterDegree.administrativeOffice.viewReimbursementGuides" />
        			</html:link>
        		</td>
	       		<td width="20%">
        			<bean:define id="linkPrintGuide">/printGuide.do?method=print<bean:write name="arguments"/>
                	</bean:define>
        			<html:link page='<%= pageContext.findAttribute("linkPrintGuide").toString() %>' target="_blank">
        				<bean:message key="link.masterDegree.administrativeOffice.printGuide" />
        			</html:link>
        		</td>
        		
        		</tr>
        	</table> 
        	<% } else if (guide.getInfoGuideSituation() != null && guide.getInfoGuideSituation().getSituation().equals(GuideState.ANNULLED)) {  
%>
        		<strong><bean:message key="label.masterDegree.administrativeOffice.nonChangeableGuide" /></strong>
        	<% } %>
        	

	