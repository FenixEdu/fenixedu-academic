<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<em><bean:message key="title.masterDegree.administrativeOffice"/></em>

<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>

	<p><bean:message key="title.masterDegree.administrativeOffice.chooseExecutionYear" /></p>
</logic:present>
<logic:notPresent name="jspTitle">
	<h2><bean:message key="title.masterDegree.administrativeOffice.chooseExecutionYear" /></h2>
</logic:notPresent>

<p>
<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<bean:define id="executionYearList" name="<%= PresentationConstants.EXECUTION_YEAR_LIST %>" scope="request" />
<logic:present name="<%= PresentationConstants.EXECUTION_DEGREE %>" scope="request" >
	<bean:define id="executionDegree" name="<%= PresentationConstants.EXECUTION_DEGREE %>" scope="request" />
</logic:present>


   <!-- ExecutionYear -->
	<ul>   	
	<logic:iterate id="yearElem" name="executionYearList">
		<li>
	   		<bean:define id="executionYear" name="yearElem" property="value"/>
	   		<bean:define id="executionYearName" name="yearElem" property="label"/>
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
			<%--
			 <td><bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:</td>
	         <td><html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYear" property="executionYear">
	                <html:options collection="executionYearList" property="value" labelProperty="label" />
	             </html:select>
	        </td>
			--%>
		</li>
	</logic:iterate>
	</ul>