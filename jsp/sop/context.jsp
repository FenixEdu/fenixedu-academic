<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="executionDegree">
	<table>
		<tr>
			<td colspan="2">
				<html:form action="/chooseContext">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
					<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
								 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="select.curricularYear" property="curricularYear"
								 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
					<html:select bundle="HTMLALT_RESOURCES" property="executionDegreeOID"
								 size="1"
								 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"
								 onchange="this.form.submit();">
				  		<html:options collection="licenciaturas"
	    	   						  property="value"
	       							  labelProperty="label"/>
			       </html:select>
					<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
		      	</html:form>
			</td>
		</tr>
		<tr>
			<td>
				<html:form action="/chooseContext">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
					<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
								 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeOID" property="executionDegreeOID"
								 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
					<html:select bundle="HTMLALT_RESOURCES" property="curricularYear" size="1"
								 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"
								 onchange="this.form.submit();">
			       		<html:options collection="anosCurriculares"
		    	   					  property="value"
		       						  labelProperty="label"/>
			       	</html:select> Ano
					<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
				</html:form>
			</td>
			<td>
				<html:form action="/chooseExecutionPeriod">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
					<html:select bundle="HTMLALT_RESOURCES" property="index" size="1"
								 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"
								 onchange="this.form.submit();">
				    	<html:options	property="value" 
				     					labelProperty="label" 
										collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>"
							/>
					</html:select>
					<html:submit styleId="javascriptButtonID3" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
				</html:form>    
			</td>
		</tr>
	</table>
</logic:present>