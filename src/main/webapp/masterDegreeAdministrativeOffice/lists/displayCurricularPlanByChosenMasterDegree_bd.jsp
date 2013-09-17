<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

   <span class="error"><!-- Error messages go here --><html:errors /></span>

	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
  <bean:define id="masterDegreeCPlanList" name="<%= PresentationConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST %>" scope="request" />
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
    		<bean:write name="link"/><bean:write name="degreeCurricularPlan" property="externalId"/>&degreeID=<bean:write name="degreeCurricularPlan" property="infoDegree.externalId"/>
    	</bean:define>
    	<html:link page='<%= pageContext.findAttribute("degreeCurricularPlanLink").toString() %>'>
			<bean:write name="degreeCurricularPlan" property="infoDegree.nome"/> - 
			<bean:write name="degreeCurricularPlan" property="name"/>
			<br/>
        </html:link>
	</logic:iterate>

