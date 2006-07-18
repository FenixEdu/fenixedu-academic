<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="executionDegree">
	<html:form action="/chooseContext">

		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
		<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>

		<table>
			<tr>
				<td colspan="2">
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeOID" property="executionDegreeOID"
								 size="1"
								 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"
								 onchange="document.chooseScheduleContextForm.submit();">
				  		<html:options collection="licenciaturas"
		    	   					  property="value"
		       						  labelProperty="label"/>
			       </html:select>
				</td>
			</tr>
			<tr>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularYear" property="curricularYear" size="1"
								 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"
								 onchange="document.chooseScheduleContextForm.submit();">
			       		<html:options collection="anosCurriculares"
		    	   					  property="value"
		       						  labelProperty="label"/>
			       	</html:select> Ano
				</td>

	</html:form>
	<html:form action="/chooseExecutionPeriod">
				<td>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.index" property="index" size="1"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"
					 onchange="document.pagedIndexForm.submit();">
	    	<html:options	property="value" 
	     					labelProperty="label" 
							collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>"
							/>
		</html:select>
	</html:form>    
				</td>
			</tr>
		</table>
</logic:present>
<%--
<logic:present name="<%= SessionConstants.CLASS_VIEW %>"  >
	<bean:define id="infoTurma" name="<%= SessionConstants.CLASS_VIEW %>" scope="request"/>
	<br />
	<bean:message key="label.class"/> <jsp:getProperty name="infoTurma" property="nome" />
	<br />
</logic:present>

<logic:present name="executionCourse">
	<br />
	<bean:message key="property.course"/>: <bean:write name="executionCourse" property="nome"/>
	<br />
</logic:present>

<logic:present name="shift">
	<bean:message key="property.shift"/>: <bean:write name="shift" property="nome"/>
	<br />
</logic:present>
--%>