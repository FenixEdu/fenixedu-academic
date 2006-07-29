<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

   <span class="error"><html:errors/></span>

	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
  <bean:define id="masterDegreeCPlanList" name="<%= SessionConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST %>" scope="request" />
  <bean:define id="link"><%=path%>.do?method=prepareList<%= "&" %>page=0<%= "&" %>curricularPlanID=</bean:define>
  	<p>
    <h3><%= ((List) masterDegreeCPlanList).size()%> <bean:message key="label.masterDegree.administrativeOffice.curricularPlanFound"/></h3>        
    <% if (((List) masterDegreeCPlanList).size() != 0) { %>
    </p>
    <bean:message key="label.masterDegree.chooseOne"/><br/><br/>
	<bean:message key="label.masterDegree.administrativeOffice.curricularPlans" /><br/>
  	<% } %>
  
  	<logic:iterate id="degreeCurricularPlan" name="masterDegreeCPlanList">
    	<bean:define id="degreeCurricularPlanLink">
    		<bean:write name="link"/><bean:write name="degreeCurricularPlan" property="idInternal"/>&degreeID=<bean:write name="degreeCurricularPlan" property="infoDegree.idInternal"/>
    	</bean:define>
    	<html:link page='<%= pageContext.findAttribute("degreeCurricularPlanLink").toString() %>'>
			<bean:write name="degreeCurricularPlan" property="infoDegree.nome"/> - 
			<bean:write name="degreeCurricularPlan" property="name"/>
			<br/>
        </html:link>
	</logic:iterate>

