<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="competenceCoursesInformations" name="executionCourse" property="competenceCoursesInformations"/>

<h2>Bibliografia</h2>


<logic:equal name="executionCourse" property="hasAnyMainBibliographicReference" value="true">
	<h3 class="mtop2 mbottom1 greytxt">
		<bean:message key="message.recommendedBibliography"/>
	</h3>
	
	<ul>
		<logic:iterate id="competenceCourseInformation" name="competenceCoursesInformations">
			<logic:present name="competenceCourseInformation" property="bibliographicReferences">
				<logic:iterate id="bibliographicReference" name="competenceCourseInformation" property="bibliographicReferences.bibliographicReferencesSortedByOrder">
					<logic:equal name="bibliographicReference" property="type" value="MAIN">
					<li>
						<em>
							<logic:notPresent name="bibliographicReference" property="url">
								<bean:write name="bibliographicReference" property="title" filter="false"/>
							</logic:notPresent>
							
							<logic:present name="bibliographicReference" property="url">
								<bean:define id="url" type="java.lang.String" name="bibliographicReference" property="url"/>
								<html:link href="<%= url %>">
									<bean:write name="bibliographicReference" property="title" filter="false"/>
								</html:link>
							</logic:present>
						</em>, 
						<span><bean:write name="bibliographicReference" property="authors" filter="false" /></span>, 
						<span><bean:write name="bibliographicReference" property="year" filter="false" /></span>, 
						<span><bean:write name="bibliographicReference" property="reference"  filter="false" /></span>
					</li>
					</logic:equal>
				</logic:iterate>
			</logic:present>
		</logic:iterate>
		<logic:iterate id="bibliographicReference" name="executionCourse" property="orderedBibliographicReferences">
			<logic:notEqual name="bibliographicReference" property="optional" value="true">
			<li>
				<em><bean:write name="bibliographicReference" property="title" filter="false"/></em>, 
				<span><bean:write name="bibliographicReference" property="authors" filter="false" /></span>, 
				<span><bean:write name="bibliographicReference" property="year" filter="false" /></span>, 
				<span><bean:write name="bibliographicReference" property="reference"  filter="false" /></span>
			</li>
			</logic:notEqual>
		</logic:iterate>		
	</ul>
</logic:equal>

<logic:equal name="executionCourse" property="hasAnySecondaryBibliographicReference" value="true">
	<h3 class="mtop2 mbottom1 greytxt">
		<bean:message key="message.optionalBibliography"/>
	</h3>
	
	<ul>
		<logic:iterate id="competenceCourseInformation" name="competenceCoursesInformations">
			<logic:present name="competenceCourseInformation" property="bibliographicReferences">
				<logic:iterate id="bibliographicReference" name="competenceCourseInformation" property="bibliographicReferences.bibliographicReferencesSortedByOrder">
					<logic:equal name="bibliographicReference" property="type" value="SECONDARY">
					<li>
						<em>
							<logic:notPresent name="bibliographicReference" property="url">
								<bean:write name="bibliographicReference" property="title" filter="false"/>
							</logic:notPresent>
							
							<logic:present name="bibliographicReference" property="url">
								<bean:define id="url" type="java.lang.String" name="bibliographicReference" property="url"/>
								<html:link href="<%= url %>">
									<bean:write name="bibliographicReference" property="title" filter="false"/>
								</html:link>
							</logic:present>
						</em>, 
						<span><bean:write name="bibliographicReference" property="authors" filter="false" /></span>, 
						<span><bean:write name="bibliographicReference" property="year" filter="false" /></span>, 
						<span><bean:write name="bibliographicReference" property="reference"  filter="false" /></span>
					</li>
					</logic:equal>
				</logic:iterate>
			</logic:present>
		</logic:iterate>
		<logic:iterate id="bibliographicReference" name="executionCourse" property="orderedBibliographicReferences">
			<logic:equal name="bibliographicReference" property="optional" value="true">
				<li>
					<em><bean:write name="bibliographicReference" property="title" filter="false"/></em>, 
					<span><bean:write name="bibliographicReference" property="authors" filter="false" /></span>, 
					<span><bean:write name="bibliographicReference" property="year" filter="false" /></span>, 
					<span><bean:write name="bibliographicReference" property="reference"  filter="false" /></span>
				</li>
			</logic:equal>
		</logic:iterate>		
	</ul>
</logic:equal>