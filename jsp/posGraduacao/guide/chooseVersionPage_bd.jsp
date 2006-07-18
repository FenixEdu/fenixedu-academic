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
<%@ page import="net.sourceforge.fenixedu.util.State" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="net.sourceforge.fenixedu.domain.GuideState" %>

   <span class="error"><html:errors/></span>

    <bean:define id="versionList" name="<%= SessionConstants.GUIDE_LIST %>" scope="request" />
    <bean:define id="guideNumber" name="<%= SessionConstants.GUIDE_NUMBER%>" scope="request" />
    <bean:define id="guideYear" name="<%= SessionConstants.GUIDE_YEAR %>" scope="request" />
    
	<bean:define id="link">/chooseGuideDispatchAction.do?method=chooseVersion<%= "&" %>page=0<%= "&" %>year=<bean:write name="guideYear"/>
		<%= "&" %>number=<bean:write name="guideNumber"/><%= "&" %>version=
	</bean:define>

	
    <%= ((List) versionList).size()%> <bean:message key="label.masterDegree.administrativeOffice.versionsFound"/>        
    <br><bean:message key="label.masterDegree.chooseOne"/><br>
    <% if (((List) versionList).size() != 0) { %>
	<table>
    	<tr>
    		<td></td>
    		<td><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></td>
    		<td><bean:message key="label.masterDegree.administrativeOffice.situation" /></td>
    	</tr>
    	
    	<logic:iterate id="version" name="versionList">
        	<bean:define id="versionLink">
        		<bean:write name="link"/><bean:write name="version" property="version" />
        	</bean:define>
        	
        	<logic:iterate id="guideSituation" name="version" property="infoGuideSituations">
                <% if (((InfoGuideSituation) guideSituation).getState().equals(new State(State.ACTIVE))) { %>
                	<tr>
                        <td><html:link page='<%= pageContext.findAttribute("versionLink").toString() %>'>
                			Vers�o <bean:write name="version" property="version" />
                            </html:link>
                        </td>
			            <logic:present name="guideSituation" property="date" >
	            			<bean:define id="date" name="guideSituation" property="date" />
							<td><%= Data.format2DayMonthYear((Date) date) %></td>   
						</logic:present>
                        <td><bean:write name="guideSituation" property="situation"/></td>
        			</tr>               
             	<% } %>
			</logic:iterate>
    	</logic:iterate>
	</table>

	<% } %>
