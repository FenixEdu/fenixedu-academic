<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message key="message.recommendedBibliography"/>
</h2>

<blockquote>
	<logic:iterate id="competenceCourse" name="executionCourse" property="competenceCourses">
		<logic:present name="competenceCourse" property="competenceCourseInformation">
			<logic:present name="competenceCourse" property="competenceCourseInformation.bibliographicReferences">
				<logic:iterate id="bibliographicReference" name="competenceCourse" property="competenceCourseInformation.bibliographicReferences.bibliographicReferencesSortedByOrder">
					<logic:equal name="bibliographicReference" property="type" value="MAIN">
						<logic:empty name="bibliographicReference" property="url">
							<bean:write name="bibliographicReference" property="title" filter="false"/>
						</logic:empty>
						<logic:notEmpty name="bibliographicReference" property="url">
							<html:link name="bibliographicReference" property="url">
								<bean:write name="bibliographicReference" property="title" filter="false"/>
							</html:link>>
						</logic:notEmpty>
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

<h2>
	<bean:message key="message.optionalBibliography"/>
</h2>
<blockquote>
	<logic:iterate id="competenceCourse" name="executionCourse" property="competenceCourses">
		<logic:present name="competenceCourse" property="competenceCourseInformation">
			<logic:present name="competenceCourse" property="competenceCourseInformation.bibliographicReferences">
				<logic:iterate id="bibliographicReference" name="competenceCourse" property="competenceCourseInformation.bibliographicReferences.bibliographicReferencesSortedByOrder">
					<logic:equal name="bibliographicReference" property="type" value="SECONDARY">
						<logic:empty name="bibliographicReference" property="url">
							<bean:write name="bibliographicReference" property="title" filter="false"/>
						</logic:empty>
						<logic:notEmpty name="bibliographicReference" property="url">
							<html:link name="bibliographicReference" property="url">
								<bean:write name="bibliographicReference" property="title" filter="false"/>
							</html:link>>
						</logic:notEmpty>
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
