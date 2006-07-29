<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuideSituation" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="net.sourceforge.fenixedu.domain.GuideState" %>

   		<bean:define id="infoGuide" name="<%= SessionConstants.GUIDE %>" scope="session"/>  		
   		<bean:define id="guideSituation" name="infoGuide" property="infoGuideSituation"/>  		
   		<bean:define id="days" name="<%= SessionConstants.MONTH_DAYS_KEY %>" scope="session"/>
   		<bean:define id="months" name="<%= SessionConstants.MONTH_LIST_KEY %>" scope="session"/>
   		<bean:define id="years" name="<%= SessionConstants.YEARS_KEY %>" scope="session"/>
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
    	 
      <br/>
      <br/>
      <span class="error"><!-- Error messages go here --><html:errors /></span>
      <br/>

	<bean:define id="action">/editGuideSituation.do?method=editGuideSituation<%= "&" %>year=<bean:write name="infoGuide" property="year"/><%= "&" %>number=<bean:write name="infoGuide" property="number"/><%= "&" %>version=<bean:write name="infoGuide" property="version"/>
	</bean:define>



  <html:form action='<%= pageContext.findAttribute("action").toString() %>'>
   	  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>  
   <strong><bean:message key="label.masterDegree.administrativeOffice.activeSituation" /></strong>
   <br/>
   <br/>
   <table border="0">
  	   
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
			<td><%= Data.format2DayMonthYear((Date) date) %></td>   
		</logic:present>
       </tr>
       <% 
       		if (((InfoGuideSituation) guideSituation).getSituation().equals(GuideState.PAYED)) 
       		{ 
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
					<td> &nbsp;	</td>
				</tr>
				<tr>
					<td> &nbsp;	</td>
				</tr>
				<tr>
					<td> &nbsp;	</td>
				</tr>
				<tr>
					<td> &nbsp;	</td>
				</tr>
				<tr>
					<td> &nbsp;	</td>
				</tr>
				<tr>
					<td> &nbsp;	</td>
				</tr>
			
				<tr>
					<td>
						<b><bean:message key="label.masterDegree.administrativeOffice.nonChangeableGuideSituation" /></b>
					</td>
				</tr>

     	<% 
     		}
     		else
     		{
     	%>
				<tr>
					<td> &nbsp;	</td>
				</tr>
				<tr>
					<td> &nbsp;	</td>
				</tr>

	   			<!-- Guide Situation -->
       			<tr>
        			<td><bean:message key="label.masterDegree.administrativeOffice.remarks"/> </td>
       				<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.remarks" property="remarks"/></td>
       			</tr>
       			<tr>
       				<td>&nbsp;</td>
       			</tr>
       			<tr>
        			<td>
        				<bean:message key="label.masterDegree.administrativeOffice.newGuideSituation" />
        			</td>
        			<td>
        				<e:labelValues id="situations" enumeration="net.sourceforge.fenixedu.domain.GuideState" bundle="ENUMERATION_RESOURCES"/>
            			<html:select bundle="HTMLALT_RESOURCES" altKey="select.guideSituation" property="guideSituation">
            				<html:option key="dropDown.Default" value=""/>
           					<html:options collection="situations" property="value" labelProperty="label" />
            			</html:select>          
       				</td>
       			</tr>

				<tr>
					<td> &nbsp;	</td>
				</tr>
		
				<!-- Payment Date -->
        		<tr>
         			<td><bean:message key="label.masterDegree.administrativeOffice.paymentDate" /></td>
          			<td>
          				<html:select bundle="HTMLALT_RESOURCES" altKey="select.paymentDateYear" property="paymentDateYear">
                			<html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
	             		</html:select>
    	         		<html:select bundle="HTMLALT_RESOURCES" altKey="select.paymentDateMonth" property="paymentDateMonth">
        	        		<html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
            	 		</html:select>
             			<html:select bundle="HTMLALT_RESOURCES" altKey="select.paymentDateDay" property="paymentDateDay">
                			<html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
             			</html:select>
	          		</td>          
        		</tr>
	
				<tr>
					<td> &nbsp;	</td>
				</tr>

    			<tr>
    				<td>
    					<bean:message key="label.masterDegree.administrativeOffice.payment" />
    				</td>
    				<td>
    					<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.transactions.PaymentType" bundle="ENUMERATION_RESOURCES"/>
    				    <html:select bundle="HTMLALT_RESOURCES" altKey="select.paymentType" property="paymentType">
					       	<html:option key="dropDown.Default" value=""/>
					        <html:options collection="values" property="value" labelProperty="label"/>
					    </html:select> 
    				</td>
    			</tr>

				<tr>
					<td> &nbsp;	</td>
				</tr>
				<tr>
					<td> &nbsp;	</td>
				</tr>
   				<tr>
   					<td>
   						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.Alterar" property="Alterar">Alterar Situação</html:submit>
   						<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.Reset" property="Reset">Dados Originais</html:reset>
   					</td>
   				</tr>
        		
        <%
     		}
        %>
        
   </table>

   
</html:form>     

      
      