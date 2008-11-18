<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message key="label.internationalrelations.internship.candidacy.remove.title"
	bundle="INTERNATIONAL_RELATIONS_OFFICE" /></h2>

<logic:present name="candidacy">
	<fr:form action="/internship/internshipCandidacy.do">
		<bean:define id="number" name="candidacy" property="studentNumber" />
		<bean:define id="name" name="candidacy" property="name" />
		<p><bean:message key="label.internationalrelations.internship.candidacy.remove.question"
			bundle="INTERNATIONAL_RELATIONS_OFFICE" arg0="<%=number.toString()%>" arg1="<%=name.toString()%>" /></p>
		<fr:edit name="candidacy" visible="false">
		</fr:edit>
		<input type="hidden" name="method" />
		<p><html:submit onclick="this.form.method.value='candidateDelete';">
			<bean:message bundle="COMMON_RESOURCES" key="button.yes" />
		</html:submit> <html:cancel onclick="this.form.method.value='prepareCandidates';">
			<bean:message bundle="COMMON_RESOURCES" key="button.no" />
		</html:cancel></p>
	</fr:form>
</logic:present>