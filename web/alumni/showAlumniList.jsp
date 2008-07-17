<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>

<!-- showAlumniList.jsp -->
<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>


<em><bean:message key="label.portal.alumni" bundle="ALUMNI_RESOURCES" /></em>
<h2><bean:message key="link.search.alumni" bundle="ALUMNI_RESOURCES" /></h2>


<fr:form id="searchForm"
	action="<%="/searchAlumni.do?&method=showAlumniList" %>">
	<fr:edit id="searchAlumniBean" name="searchAlumniBean" visible="false" />

	<table class="tstyle5 thlight thright mbottom05 thmiddle">
		<tr>
			<th><bean:message key="label.name" bundle="ALUMNI_RESOURCES" />:</th>
			<td><fr:edit id="name" name="searchAlumniBean" slot="name" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:layout name="default">
					<fr:property name="size" value="40"/>
				</fr:layout>
			</fr:edit></td>
			<td class="tdclear tderror1"><span><fr:message for="name" /></span></td>
		</tr>
		<tr>
			<th><bean:message key="label.degreeType" bundle="ALUMNI_RESOURCES" />:</th>
			<td><fr:edit id="degreeType" name="searchAlumniBean" slot="degreeType">
				<fr:layout name="default" />
			</fr:edit><span class="color888 smalltxt">(<bean:message key="label.notRequired" bundle="ALUMNI_RESOURCES" />)</span>
			</td>
			<td class="tdclear tderror1"><span><fr:message for="degreeType" /></span></td>
		</tr>
		<tr>
			<th><bean:message key="label.registration.year" bundle="ALUMNI_RESOURCES" />:</th>
			<td>
				<bean:message key="label.date.from" bundle="ALUMNI_RESOURCES" />
				<fr:edit id="firstYear" name="searchAlumniBean" slot="firstExecutionYear">
					<fr:layout name="menu-select">
						<fr:property name="providerClass"
							value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
						<fr:property name="format" value="${year}" />
						<fr:property name="class" value="dinline" />
					</fr:layout>
				</fr:edit>
				<bean:message key="label.date.to" bundle="ALUMNI_RESOURCES" />
				<fr:edit id="finalYear" name="searchAlumniBean" slot="finalExecutionYear">
					<fr:layout name="menu-select">
						<fr:property name="providerClass"
							value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
						<fr:property name="format" value="${year}" />
						<fr:property name="class" value="dinline" />
					</fr:layout>
				</fr:edit>
				<span class="color888 smalltxt">(<bean:message key="label.notRequired" bundle="ALUMNI_RESOURCES" />)</span>
			</td>
			<td class="tdclear tderror1"><span><fr:message for="firstYear"/></span></td>
		</tr>
	</table>
	<p class="mtop05">
		<html:submit>
			<bean:message key="label.filter" bundle="ALUMNI_RESOURCES" />
		</html:submit>
	</p>
</fr:form>

<logic:present name="searchAlumniBean" property="alumni">
	<logic:notEmpty name="searchAlumniBean" property="alumni">
	<bean:define id="bean" name="searchAlumniBean" type="net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniSearchBean"/>
	<p class="mtop15 mbottom05"><bean:message key="label.hitCount" bundle="ALUMNI_RESOURCES" />: <strong><fr:view name="searchAlumniBean" property="totalItems"/></strong></p>
		<fr:view name="searchAlumniBean" property="alumni" schema="alumni.registration.search.list">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle2 mtop05" />
				<fr:property name="columnClasses" value=",,acenter" />
				<fr:property name="sortParameter" value="sort" />
				<fr:property name="sortUrl" value="<%= "/searchAlumni.do?method=showAlumniList" + bean.getSearchElementsAsParameters() %>" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<logic:empty name="searchAlumniBean" property="alumni">
		<bean:message key="label.search.noResultsFound" bundle="ALUMNI_RESOURCES" /> 
	</logic:empty>
</logic:present>
