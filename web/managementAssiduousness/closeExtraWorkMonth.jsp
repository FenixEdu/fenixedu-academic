<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.closeExtraMonth" /></h2>

<p class="mtop2">
	<span class="error0 mtop0">
		<html:messages id="message" message="true" property="message">
			<bean:write name="message" filter="false"/>
		</html:messages>
	</span>
	<span class="highlight1 mtop0">
		<html:messages id="message" message="true" property="sucess">
			<bean:write name="message" filter="false"/>
		</html:messages>
	</span>
</p>	

<logic:present name="yearMonthToExport">
	<h3 class="mtop2">
		<fr:view name="yearMonthToExport" schema="show.date">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true" />
			</fr:layout>
		</fr:view>
	</h3>
	<p>
	<fr:form action="/monthClosure.do?method=verifyExportClosedaMonthMovements">
		<fr:edit id="yearMonthToExport" name="yearMonthToExport" schema="show.date" visible="false"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
			<bean:message key="button.exportMovementsToGIAF" />
		</html:submit>
	</fr:form>
	</p>
	<p>
	<fr:form action="/monthClosure.do?method=openExtraWorkMonth">
		<fr:edit id="yearMonthToOpen" name="yearMonthToExport" schema="show.date" visible="false"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.open" styleClass="invisible">
			<bean:message key="button.open" />
		</html:submit>
	</fr:form>
	</p>
	<p>
	<fr:form action="/monthClosure.do?method=updateExtraWorkAmounts">
		<fr:edit id="yearMonthToUpdate" name="yearMonthToExport" schema="show.date" visible="false"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.open" styleClass="invisible">
			<bean:message key="button.updateExtraWorkAmounts" />
		</html:submit>
	</fr:form>
	</p>

	<logic:notEmpty name="closedMonthDocuments">
		<bean:define id="closedMonthDocuments" name="closedMonthDocuments" toScope="request" />
		<jsp:include page="showClosedMonthDocuments.jsp"/>	
	</logic:notEmpty>

</logic:present>

<logic:present name="yearMonth">
	<h3 class="mtop2">
		<fr:view name="yearMonth" schema="show.date">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true" />
			</fr:layout>
		</fr:view>
	</h3>
	<logic:notEqual name="yearMonth" property="isThisYearMonthClosedForExtraWork" value="true">
		<fr:form action="/monthClosure.do?method=closeExtraWorkMonth">
			<fr:edit id="yearMonth" name="yearMonth" schema="show.date" visible="false"/>
			<bean:message key="message.closeMonthConfirmation" bundle="ASSIDUOUSNESS_RESOURCES"/>
			<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
				<bean:message key="button.confirm" />
			</html:submit></p>
		</fr:form>
	</logic:notEqual>
	<logic:equal name="yearMonth" property="isThisYearMonthClosedForExtraWork" value="true">
		<bean:message key="message.cantCloseMonth" bundle="ASSIDUOUSNESS_RESOURCES"/>
	</logic:equal>
</logic:present>