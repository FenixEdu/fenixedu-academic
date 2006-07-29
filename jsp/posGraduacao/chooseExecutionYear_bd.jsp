<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
	<bean:message key="title.masterDegree.administrativeOffice.chooseExecutionYear" />
</logic:present>
<logic:notPresent name="jspTitle">
	<h2><bean:message key="title.masterDegree.administrativeOffice.chooseExecutionYear" /></h2>
</logic:notPresent>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<bean:define id="executionYearList" name="<%= SessionConstants.EXECUTION_YEAR_LIST %>" scope="request" />
<logic:present name="<%= SessionConstants.EXECUTION_DEGREE %>" scope="request" >
	<bean:define id="executionDegree" name="<%= SessionConstants.EXECUTION_DEGREE %>" scope="request" />
</logic:present>
<table>
   <!-- ExecutionYear -->
	<logic:iterate id="yearElem" name="executionYearList">
   		<bean:define id="executionYear" name="yearElem" property="value"/>
   		<bean:define id="executionYearName" name="yearElem" property="label"/>
		<tr>
   				<td>
				<logic:present name="jspTitle">
					<html:link page="<%= path + ".do?method=chooseExecutionYear&amp;executionYear=" + executionYearName + "&amp;jspTitle=" + pageContext.findAttribute("jspTitle")+ "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("executionDegree")+ "&amp;executionDegreeID=" + pageContext.findAttribute("executionYear") %>">
						<bean:write name="executionYearName"/>
					</html:link>
				</logic:present>
				<logic:notPresent name="jspTitle">
					<html:link page="<%= path + ".do?method=chooseExecutionYear&amp;executionYear=" + executionYearName + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("executionDegree")+ "&amp;executionDegreeID=" + pageContext.findAttribute("executionYear") %>">
						<bean:write name="executionYearName"/>
					</html:link>
				</logic:notPresent>
       		</td>
<%--         <td><bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:</td>
         <td><html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYear" property="executionYear">
                <html:options collection="executionYearList" property="value" labelProperty="label" />
             </html:select>
         </td> --%>
		</tr>
	</logic:iterate>
</table>