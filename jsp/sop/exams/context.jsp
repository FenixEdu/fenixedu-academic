<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	
<logic:present name="executionDegree">
	<html:form action="/chooseExamsContext">

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
								 onchange="this.form.submit();">
				  		<html:options collection="<%= SessionConstants.DEGREES %>"
		    	   					  property="value"
		       						  labelProperty="label"/>
			       </html:select>
					<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
				</td>
			</tr>
			<tr>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularYear" property="curricularYear" size="1"
								 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"
								 onchange="this.form.submit();">
			       		<html:options collection="<%= SessionConstants.LABELLIST_CURRICULAR_YEARS %>"
		    	   					  property="value"
		       						  labelProperty="label"/>
			       	</html:select> Ano
					<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
			</td>

	</html:form>
	<html:form action="/chooseExamsPeriodContext">
				<td>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriod" property="executionPeriod" size="1"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"
					 onchange="this.form.submit();">
	    	<html:options	property="value" 
	     					labelProperty="label" 
							collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>"
							/>
		</html:select>
		<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>
	</html:form>    
				</td>
			</tr>
		</table>
</logic:present>
