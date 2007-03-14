<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<h2><bean:message key="title.exportGroupProperties"/></h2>

<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>


<div class="infoop2">
	<bean:message key="label.teacher.exportGroupProperties.description" />
</div>


<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<bean:parameter id="nextPage" name="nextPage" />
<bean:parameter id="inputPage" name="inputPage" />
<html:form action="<%=path%>" method="get">
	<input alt="input.method" type="hidden" name="method" value="nextPagePublic"/>
	<input alt="input.nextPage" type="hidden" name="nextPage" value="<%= nextPage %>"/>
	<input alt="input.inputPage" type="hidden" name="inputPage" value="<%= inputPage %>"/>
	<html:hidden alt="<%=SessionConstants.EXECUTION_PERIOD_OID%>" property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<%--
	<div class="infoop">
		<bean:message key="message.public.degree.choose"/>
	</div>
--%>



<%-- 
	<div class="infoop">
		<bean:message key="label.chooseYear" />
	</div>
--%>


	<table class="tstyle5 thlight thright">
		<tr>
			<th>
				<bean:message key="property.context.degree"/>:
			</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.index" property="index" size="1">
					<html:options collection="degreeList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<th><bean:message key="property.context.curricular.year"/>:</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.curYear" property="curYear" size="1">
					<html:options collection="curricularYearList" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
	</table>

	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
			<bean:message key="label.next"/>
		</html:submit>
	</p>

</html:form>