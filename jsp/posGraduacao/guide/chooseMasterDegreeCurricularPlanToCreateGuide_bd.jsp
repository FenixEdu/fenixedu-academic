<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<span class="error"><html:errors/></span>

  
<bean:define id="link">/chooseDataToCreateGuide.do?method=chooseExecutionDegreeFromList<%= "&" %>page=0<%= "&" %>curricularPlanID=</bean:define>

<logic:present name="<%= SessionConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST %>" scope="request">
	<bean:define id="masterDegreeCPlanList" name="<%= SessionConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST %>" scope="request" />
	<h3><%= ((List) masterDegreeCPlanList).size()%> <bean:message key="label.masterDegree.administrativeOffice.curricularPlanFound"/></h3>        
	<bean:message key="label.masterDegree.chooseOne"/><br><br>
	<bean:message key="label.masterDegree.administrativeOffice.curricularPlans" /><br>
 	<logic:iterate id="degreeCurricularPlan" name="masterDegreeCPlanList">
    	<bean:define id="degreeCurricularPlanLink">
    		<bean:write name="link"/><bean:write name="degreeCurricularPlan" property="idInternal"/>
    	</bean:define>
    	<html:link page='<%= pageContext.findAttribute("degreeCurricularPlanLink").toString() %>'>
			<bean:write name="degreeCurricularPlan" property="infoDegree.nome"/> - 
			<bean:write name="degreeCurricularPlan" property="name"/>
			<br>
       	</html:link>
	</logic:iterate>
</logic:present>
  	
<logic:notPresent name="<%= SessionConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST %>" scope="request">
	<h3>0&nbsp;<bean:message key="label.masterDegree.administrativeOffice.curricularPlanFound"/></h3>        
</logic:notPresent>
