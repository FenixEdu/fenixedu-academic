<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

	<em><bean:message key="title.masterDegree.administrativeOffice"/></em>
	<h2><bean:message key="title.masterDegree.administrativeOffice.createCandidate"/></h2>

	<p>
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</p>

  <bean:define id="masterDegreeCPlanList" name="<%= PresentationConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST %>" scope="request" />
  <bean:define id="link">/listMasterDegreesCandidate.do?method=prepareChooseExecutionYear<%= "&" %>page=0<%= "&" %>curricularPlanID=</bean:define>

    <p><strong><%= ((List) masterDegreeCPlanList).size()%> <bean:message key="label.masterDegree.administrativeOffice.curricularPlanFound"/></strong></p>
    <% if (((List) masterDegreeCPlanList).size() != 0) { %>

    <%--<bean:message key="label.masterDegree.chooseOne"/>--%>

	<p><bean:message key="label.masterDegree.administrativeOffice.curricularPlans" /></p>

  	<% } %>
  
  	<ul>
  	<logic:iterate id="degreeCurricularPlan" name="masterDegreeCPlanList">
  		<li>
    	<bean:define id="degreeCurricularPlanLink">
    		<bean:write name="link"/><bean:write name="degreeCurricularPlan" property="externalId"/>&degreeID=<bean:write name="degreeCurricularPlan" property="infoDegree.externalId"/>
    	</bean:define>
    	<html:link page='<%= pageContext.findAttribute("degreeCurricularPlanLink").toString() %>'>
			<bean:write name="degreeCurricularPlan" property="infoDegree.nome"/> - 
			<bean:write name="degreeCurricularPlan" property="name"/>
        </html:link>
        </li>
	</logic:iterate>
	</ul>

