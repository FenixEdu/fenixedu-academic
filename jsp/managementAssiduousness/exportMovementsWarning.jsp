<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.closeExtraMonth" /></h2>

<logic:present name="yearMonthToExport">
	<h3 class="mtop2">
		<fr:view name="yearMonthToExport" schema="show.date">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true" />
			</fr:layout>
		</fr:view>
	</h3>
	<p><span class="warning0"><bean:message key="message.transferToGIAF.warning" bundle="ASSIDUOUSNESS_RESOURCES"/></span></p>
	<p>
		<fr:form action="/monthClosure.do">
			<html:hidden name="parametrizationForm" property="method" value="exportClosedMonthMovementsToGIAF"/> 
			<fr:edit id="yearMonthToExport" name="yearMonthToExport" schema="show.date" visible="false"/>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message key="button.confirm" bundle="ASSIDUOUSNESS_RESOURCES"/>
			</html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='prepareToCloseExtraWorkMonth'">
				<bean:message key="button.cancel" bundle="ASSIDUOUSNESS_RESOURCES"/>
			</html:submit>			
		</fr:form>
	</p>
	
	<logic:notEmpty name="closedMonthDocuments">
		<bean:define id="closedMonthDocuments" name="closedMonthDocuments" toScope="request" />
		<jsp:include page="showClosedMonthDocuments.jsp"/>	
	</logic:notEmpty>
	
</logic:present>