<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<html:form action="/manageCreditsNotes">
	<html:hidden property="method" value="editNote"/>
	<html:hidden property="executionPeriodId"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="noteType"/>
	
	<span class="error"><html:errors/></span>
	<html:messages id="message" message="true">
		<span class="error">
			<bean:write name="message"/>
		</span>
	</html:messages>
	
	<p><h2><bean:message key="label.notes"/></h2></p>	
	<br/>
	
	<bean:define id="noteType" name="creditsNotesForm" property="noteType" type="java.lang.String" />
	<html:textarea cols="60" rows="8" property="<%= noteType %>" styleClass="mbottom05"/>	
	
	<br/>	
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:submit styleClass="inputbutton" onclick="this.form.method.value='cancel';this.form.page.value='0'">
		<bean:message key="button.cancel"/>
	</html:submit>
</html:form>

