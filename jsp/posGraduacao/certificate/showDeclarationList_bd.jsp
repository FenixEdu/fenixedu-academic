<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoStudent" %>
<%@ page import="Util.State" %>

   <span class="error"><html:errors/></span>
Olá!
     	
<%--    <bean:define id="declarationList" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" scope="session" />
    
	<bean:define id="link">/chooseDeclarationInfoAction.do?method=chooseSuccess<%= "&" %>page=0</bean:define>

    <br><bean:message key="label.masterDegree.chooseOne"/><br>
	<table>
    	
    	
    	
   	
    	<logic:iterate id="guide" name="guideList">
        	<bean:define id="guideLink">
        		<bean:write name="link"/><bean:write name="guide" property="year"/><%= "&" %>number=<bean:write name="guide" property="number"/>
        	</bean:define>
        	
        	<logic:iterate id="guideSituation" name="guide" property="infoGuideSituations">
                <% if (((InfoGuideSituation) guideSituation).getState().equals(new State(State.ACTIVE))) { %>
                	<tr>
                        <td><html:link page='<%= pageContext.findAttribute("guideLink").toString() %>'>
                			Guia Número <bean:write name="guide" property="number" />
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
--%>
