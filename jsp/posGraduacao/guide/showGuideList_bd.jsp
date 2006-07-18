<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuideSituation" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate" %>
<%@ page import="net.sourceforge.fenixedu.util.State" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>

   <span class="error"><html:errors/></span>

    <bean:define id="guideList" name="<%= SessionConstants.GUIDE_LIST %>" scope="request" />
    
	<bean:define id="link">/guideListingByYear.do?method=chooseGuide<%= "&" %>page=0<%= "&" %>year=
	</bean:define>

    <%= ((List) guideList).size()%> <bean:message key="label.masterDegree.administrativeOffice.guidesFound"/>        

    <% if (((List) guideList).size() != 0) { %>
    <br><bean:message key="label.masterDegree.chooseOne"/><br>
	<table>
    	<tr>
    		<td><bean:message key="label.masterDegree.administrativeOffice.guideNumber" /></td>
    		<td><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></td>
    		<td><bean:message key="label.masterDegree.administrativeOffice.situation" /></td>
    		<td><bean:message key="label.masterDegree.administrativeOffice.requesterName" /></td>    		
    	</tr>
    	
    	<logic:iterate id="guide" name="guideList">
        	<bean:define id="guideLink">
        		<bean:write name="link"/><bean:write name="guide" property="year"/><%= "&" %>number=<bean:write name="guide" property="number"/>
        	</bean:define>
        	
        	<logic:iterate id="guideSituation" name="guide" property="infoGuideSituations">
                <% if (((InfoGuideSituation) guideSituation).getState().equals(new State(State.ACTIVE))) { %>
                	<tr>
                        <td><html:link page='<%= pageContext.findAttribute("guideLink").toString() %>'>
                			Guia N�mero <bean:write name="guide" property="number" />
                            </html:link>
                        </td>
			            <logic:present name="guideSituation" property="date" >
	                        <bean:define id="date" name="guideSituation" property="date" />
							<td><%= Data.format2DayMonthYear((Date) date) %></td>   
						</logic:present>
                        <td><bean:message name="guideSituation" property="situation.name" bundle="ENUMERATION_RESOURCES"/></td>
                        
                        
			        	<bean:define id="personLink">
			        		/guideListingByYear.do?method=chooseGuideByPerson<%= "&" %>page=0<%= "&" %>personID=<bean:write name="guide" property="infoPerson.idInternal"/>
			        	</bean:define>

                        <td><html:link page='<%= pageContext.findAttribute("personLink").toString() %>'>
                			<bean:write name="guide" property="infoPerson.nome"/>
                            </html:link>
                        </td>
        			</tr>               
             	<% } %>
			</logic:iterate>
    	</logic:iterate>
	</table>

	<% } %>
