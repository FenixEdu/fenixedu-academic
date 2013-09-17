<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span>

<p><strong><bean:message key="message.public.chooseContext.warning1"/></strong></p>
<p><strong><bean:message key="message.public.chooseContext.warning2"/></strong></p>

<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<bean:parameter id="nextPage" name="nextPage" />
<bean:parameter id="inputPage" name="inputPage" />
<html:form action="<%=path%>" >
	<input alt="input.method" type="hidden" name="method" value="nextPagePublic"/>
	<input alt="input.nextPage" type="hidden" name="nextPage" value="<%= nextPage %>"/>
	<input alt="input.inputPage" type="hidden" name="inputPage" value="<%= inputPage %>"/>
	<html:hidden alt="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" property="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<p class="infoop">
		<bean:message key="message.public.degree.choose"/>
	</p>
	<p>
		<bean:message key="property.context.degree"/>
		:
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.index" property="index" size="1">
			<html:options collection="degreeList" property="value" labelProperty="label"/>
		</html:select>
	</p>
	<p class="infoop">
		<bean:message key="label.chooseYear" />
	</p>
	<p>
	<p>
		<bean:message key="property.context.curricular.year"/>
		:
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.curYear" property="curYear" size="1">
			<html:options collection="curricularYearList" property="value" labelProperty="label"/>
		</html:select>
	</p>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
		<bean:message key="label.next"/>
	</html:submit>
</html:form>