<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<logic:present name="<%= SessionConstants.EXECUTION_DEGREE %>"  >
	<bean:define id="infoDegree" name="<%= SessionConstants.EXECUTION_DEGREE %>" property="infoDegreeCurricularPlan.infoDegree"scope="request" />
   	<bean:define id="infoExecutionPeriod" name="<%= SessionConstants.EXECUTION_PERIOD %>" scope="request"/>
   	<jsp:getProperty name="infoDegree" property="tipoCurso" /> em 
	<jsp:getProperty name="infoDegree" property="nome" />
	<br/>
	<jsp:getProperty name="infoExecutionPeriod" property="name"/> -
	<bean:write name="infoExecutionPeriod" property="infoExecutionYear.year"/>
</logic:present>