<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoGuideSituation" %>
<%@ page import="Util.State" %>
<%@ page import="Util.Data" %>
<%@ page import="Util.SituationOfGuide" %>

   <span class="error"><html:errors/></span>

Ola! ;D

  
    <bean:define id="masterDegreeList" name="<%= SessionConstants.MASTER_DEGREE_LIST %>" scope="session" />
    
<!--	<bean:define id="link">/guideListingByYear.do?method=chooseGuide<%= "&" %>page=0<%= "&" %>year=
	</bean:define>
-->
    <%= ((List) masterDegreeList).size()%> <bean:message key="label.masterDegree.administrativeOffice.degreesFound"/>        
1
    <% if (((List) masterDegreeList).size() != 0) { %>
    <br><bean:message key="label.masterDegree.chooseOne"/><br>
	<table>
    	<tr>
    		<td><bean:message key="label.masterDegree.administrativeOffice.degree" /></td>
    	</tr>
2    	
    	<logic:iterate id="mDegree" name="masterDegreeList">
        	<bean:define id="mDegreeLink">
        		<bean:write name="link"/><bean:write name="mDegree" property="infoDegree.nome"/>
        	</bean:define>
    	</logic:iterate>
	</table>

	<% } %>
3