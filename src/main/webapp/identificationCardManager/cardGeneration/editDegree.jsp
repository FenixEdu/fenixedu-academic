<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em>Cartões de Identificação</em>
<h2><bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.edit.degree" /></h2>
<p><html:link page="/manageCardGeneration.do?method=showDegreeCodesAndLabels">« Voltar</html:link></p>

<bean:define id="url" type="java.lang.String">/manageCardGeneration.do?method=showDegreeCodesAndLabels&amp;degreeType=<bean:write name="degree" property="degreeType.name"/></bean:define>
<fr:edit id="net.sourceforge.fenixedu.domain.Degree.card.generation.edit"
		 name="degree"
		 schema="net.sourceforge.fenixedu.domain.Degree.card.generation.edit"
		 type="net.sourceforge.fenixedu.domain.Degree"
		 action="<%= url %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop1"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
</fr:edit>
