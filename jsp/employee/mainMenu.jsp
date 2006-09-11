<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>
<%@ page import="net.sourceforge.fenixedu.applicationTier.IUserView"%>
<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style>

<!-- Import new CSS for this section: #navlateral  -->

<%
	IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW); 
	if(userView.getUtilizador().equalsIgnoreCase("f2329") || userView.getUtilizador().equalsIgnoreCase("f1768") 
	        || userView.getUtilizador().equalsIgnoreCase("f1769") || userView.getUtilizador().equalsIgnoreCase("f4659") 
	        || userView.getUtilizador().equalsIgnoreCase("f4252") || userView.getUtilizador().equalsIgnoreCase("f3202") 
	        || userView.getUtilizador().equalsIgnoreCase("f4064") || userView.getUtilizador().equalsIgnoreCase("f2752") 
	        || userView.getUtilizador().equalsIgnoreCase("f2751") || userView.getUtilizador().equalsIgnoreCase("f3647") 
	        || userView.getUtilizador().equalsIgnoreCase("f4123") || userView.getUtilizador().equalsIgnoreCase("f1667") 
	        
	        || userView.getUtilizador().equalsIgnoreCase("f4439") || userView.getUtilizador().equalsIgnoreCase("F4506") 
	        || userView.getUtilizador().equalsIgnoreCase("f4616") || userView.getUtilizador().equalsIgnoreCase("f4519") 
	        || userView.getUtilizador().equalsIgnoreCase("f4646") || userView.getUtilizador().equalsIgnoreCase("f3487") 
	        || userView.getUtilizador().equalsIgnoreCase("f3883") || userView.getUtilizador().equalsIgnoreCase("f4520") 
	        || userView.getUtilizador().equalsIgnoreCase("f4528") || userView.getUtilizador().equalsIgnoreCase("f4578") 
	        || userView.getUtilizador().equalsIgnoreCase("f4579") || userView.getUtilizador().equalsIgnoreCase("f4580") 	        
	        || userView.getUtilizador().equalsIgnoreCase("f4581") || userView.getUtilizador().equalsIgnoreCase("f4599") 
	        || userView.getUtilizador().equalsIgnoreCase("f4459") || userView.getUtilizador().equalsIgnoreCase("f4655") 
	        || userView.getUtilizador().equalsIgnoreCase("f3000") ){

%>	    
<ul>
	<li class="navheader"><bean:message key="title.assiduousness" /></li>
	<li><html:link page="/assiduousnessRecords.do?method=showEmployeeInfo">
		<bean:message key="label.schedule" />
	</html:link></li>

	<li><html:link page="/assiduousnessRecords.do?method=showWorkSheet">
		<bean:message key="link.workSheet" />
	</html:link></li>

	<li><html:link page="/assiduousnessRecords.do?method=showClockings">
		<bean:message key="link.clockings" />
	</html:link></li>
	<li><html:link
		page="/assiduousnessRecords.do?method=showJustifications">
		<bean:message key="link.justifications" />
	</html:link></li>

	
    <%  if (userView.getPerson().hasFunctionType(net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType.ASSIDUOUSNESS_RESPONSIBLE)) {%>
	<li class="navheader"><bean:message
		key="title.assiduousnessResponsible" /></li>
	<li><html:link
		page="/assiduousnessResponsible.do?method=showEmployeeList">
		<bean:message key="link.showEmployeeWorkSheet" />
	</html:link></li>
	<% } %>
</ul>

<%	}	%>

