<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

	
<logic:present name="executionDegree">
	<html:form action="/chooseExamsContext">

		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD_OID %>" property="<%= PresentationConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>

		<table>
			<tr>
				<td colspan="2">
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeOID" property="executionDegreeOID"
								 size="1"
								 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"
								 onchange="this.form.submit();">
				  		<html:options collection="<%= PresentationConstants.DEGREES %>"
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
			       		<html:options collection="<%= PresentationConstants.LABELLIST_CURRICULAR_YEARS %>"
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
							collection="<%= PresentationConstants.LABELLIST_EXECUTIONPERIOD%>"
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
