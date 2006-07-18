<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<jsp:include page="../context.jsp"/>
<p><span class="error"><html:errors/></span></p>
<p>
    <bean:define id="candidateList" name="masterDegreeCandidateList" scope="request" />
    <bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />        
    <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
	<bean:define id="link">
		<bean:write name="path"/>.do?method=chooseCandidate<%= "&" %>page=0<%= "&" %>degreeCurricularPlanID=<%= degreeCurricularPlanID %>
	</bean:define>
    <span class="emphasis"><%= ((List) candidateList).size()%></span> <bean:message key="label.masterDegree.administrativeOffice.candidatesFound"/>        
    <% if (((List) candidateList).size() != 0) { %>
</p>    
	    <table>
    		<tr>
	    		<th class="listClasses-header"><bean:message key="label.name" /></th>
				<th class="listClasses-header"><bean:message key="label.masterDegree.administrativeOffice.specialization" /></th>
				<th class="listClasses-header"><bean:message key="label.masterDegree.administrativeOffice.candidateSituation" /></th>
				<th class="listClasses-header"><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></th>
			</tr>
    		<logic:iterate id="candidate" name="candidateList" >
    			<bean:define id="candidateLink">
    				<bean:write name="link"/>&candidateID=<bean:write name="candidate" property="idInternal"/>
    			</bean:define>
    			<tr>
    				<td class="listClasses">
      				    <html:link page='<%= pageContext.findAttribute("candidateLink").toString() %>'>
    						<bean:write name="candidate" property="infoPerson.nome" />
    					</html:link>
    				</td>
    				<td class="listClasses"><bean:message name="candidate" property="specialization.name" bundle="ENUMERATION_RESOURCES" /></td>
    				<td class="listClasses"><bean:write name="candidate" property="infoCandidateSituation.situation" /></td>
		            <logic:present name="candidate" property="infoCandidateSituation.date" >
	    	            <bean:define id="date" name="candidate" property="infoCandidateSituation.date" />
						<td class="listClasses"><%= Data.format2DayMonthYear((Date) date) %></td>   
					</logic:present>
    			</tr>
    		</logic:iterate>
          </table>
        <% } %>
