<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	
<logic:present name="executionDegree">
	<html:form action="/chooseExamsContext">

		<html:hidden property="page" value="1"/>
		<html:hidden property="method" value="choose"/>
		<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>

		<table>
			<tr>
				<td colspan="2">
					<html:select property="executionDegreeOID"
								 size="1"
								 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"
								 onchange="document.chooseExamsContextForm.submit();">
				  		<html:options collection="<%= SessionConstants.DEGREES %>"
		    	   					  property="value"
		       						  labelProperty="label"/>
			       </html:select>
				</td>
			</tr>
			<tr>
				<td>
					<html:select property="curricularYear" size="1"
								 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"
								 onchange="document.chooseExamsContextForm.submit();">
			       		<html:options collection="<%= SessionConstants.LABELLIST_CURRICULAR_YEARS %>"
		    	   					  property="value"
		       						  labelProperty="label"/>
			       	</html:select> Ano
			</td>

	</html:form>
	<html:form action="/chooseExamsPeriodContext">
				<td>
		<html:hidden property="method" value="choose"/>
		<html:hidden property="page" value="1"/>
		<html:select property="executionPeriod" size="1"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"
					 onchange="document.chooseExamsExecutionPeriodForm.submit();">
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
