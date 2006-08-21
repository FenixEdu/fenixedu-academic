<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="competenceCoursesInformations" name="executionCourse" property="competenceCoursesInformations"/>

<h2>
	<bean:message key="message.recommendedBibliography"/>
</h2>

<blockquote>
	<logic:iterate id="competenceCourseInformation" name="competenceCoursesInformations">
		<logic:present name="competenceCourseInformation" property="bibliographicReferences">
			<logic:iterate id="bibliographicReference" name="competenceCourseInformation" property="bibliographicReferences.bibliographicReferencesSortedByOrder">
				<logic:equal name="bibliographicReference" property="type" value="MAIN">
					<logic:notPresent name="bibliographicReference" property="url">
						<bean:write name="bibliographicReference" property="title" filter="false"/>
					</logic:notPresent>
					<logic:present name="bibliographicReference" property="url">
						<bean:define id="url" type="java.lang.String" name="bibliographicReference" property="url"/>
						<html:link href="<%= url %>">
							<bean:write name="bibliographicReference" property="title" filter="false"/>
						</html:link>
					</logic:present>
					<br/>
					<bean:write name="bibliographicReference" property="authors" filter="false" />
					<br/>
					<bean:write name="bibliographicReference" property="reference"  filter="false" />
					<br/>
					<bean:write name="bibliographicReference" property="year" filter="false" />
					<br/>
					<br/>
				</logic:equal>
			</logic:iterate>
		</logic:present>
	</logic:iterate>
	<logic:iterate id="bibliographicReference" name="executionCourse" property="associatedBibliographicReferences">
		<logic:notEqual name="bibliographicReference" property="optional" value="true">
			<bean:write name="bibliographicReference" property="title" filter="false"/>
			<br/>
			<bean:write name="bibliographicReference" property="authors" filter="false" />
			<br/>
			<bean:write name="bibliographicReference" property="reference"  filter="false" />
			<br/>
			<bean:write name="bibliographicReference" property="year" filter="false" />
			<br/>
			<br/>
		</logic:notEqual>
	</logic:iterate>		
</blockquote>

<logic:equal name="executionCourse" property="hasAnySecondaryBibliographicReference" value="true">
	<h2>
		<bean:message key="message.optionalBibliography"/>
	</h2>
	<blockquote>
		<logic:iterate id="competenceCourseInformation" name="competenceCoursesInformations">
			<logic:present name="competenceCourseInformation" property="bibliographicReferences">
				<logic:iterate id="bibliographicReference" name="competenceCourseInformation" property="bibliographicReferences.bibliographicReferencesSortedByOrder">
					<logic:equal name="bibliographicReference" property="type" value="SECONDARY">
						<logic:notPresent name="bibliographicReference" property="url">
							<bean:write name="bibliographicReference" property="title" filter="false"/>
						</logic:notPresent>
						<logic:present name="bibliographicReference" property="url">
							<bean:define id="url" type="java.lang.String" name="bibliographicReference" property="url"/>
							<html:link href="<%= url %>">
								<bean:write name="bibliographicReference" property="title" filter="false"/>
							</html:link>
						</logic:present>
						<br/>
						<bean:write name="bibliographicReference" property="authors" filter="false" />
						<br/>
						<bean:write name="bibliographicReference" property="reference"  filter="false" />
						<br/>
						<bean:write name="bibliographicReference" property="year" filter="false" />
						<br/>
						<br/>
					</logic:equal>
				</logic:iterate>
			</logic:present>
		</logic:iterate>
		<logic:iterate id="bibliographicReference" name="executionCourse" property="associatedBibliographicReferences">
			<logic:equal name="bibliographicReference" property="optional" value="true">
				<bean:write name="bibliographicReference" property="title" filter="false"/>
				<br/>
				<bean:write name="bibliographicReference" property="authors" filter="false" />
				<br/>
				<bean:write name="bibliographicReference" property="reference"  filter="false" />
				<br/>
				<bean:write name="bibliographicReference" property="year" filter="false" />
				<br/>
				<br/>
			</logic:equal>
		</logic:iterate>		
	</blockquote>
</logic:equal>