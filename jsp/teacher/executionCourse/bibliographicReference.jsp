<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<p>
	<span class="error">
		<html:errors/>
	</span>
</p>

<table width="100%">
	<tr>
		<td class="infoop">
			<bean:message key="label.bibliography.explanation" />
		</td>
	</tr>
</table>

<logic:present name="executionCourse">

	<p>
		<div class="gen-button">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<html:link page="/createBibliographicReference.do?method=prepareCreateBibliographicReference" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="label.insertBibliographicReference"/>                   		     
			</html:link>
		</div>
	</p>

	<h3>
		<bean:message key="message.recommendedBibliography"/>
	</h3>
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
				<bean:define id="url" type="java.lang.String">bibliographicReferenceID=<bean:write name="bibliographicReference" property="idInternal"/></bean:define>
				<html:link page="<%= "/editBibliographicReference.do?method=prepareEditBibliographicReference&amp;" + url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
					<bean:message key="button.edit"/>
				</html:link>
				<html:link page="<%= "/manageExecutionCourse.do?method=deleteBibliographicReference&amp;" + url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
					<bean:message key="button.delete"/>
				</html:link>
				<br/>
				<br/>
			</logic:notEqual>
		</logic:iterate>		
	</blockquote>

	<h3>
		<bean:message key="message.optionalBibliography"/>
	</h3>
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
				<bean:define id="url" type="java.lang.String">bibliographicReferenceID=<bean:write name="bibliographicReference" property="idInternal"/></bean:define>
				<html:link page="<%= "/editBibliographicReference.do?method=prepareEditBibliographicReference&amp;" + url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
					<bean:message key="button.edit"/>
				</html:link>
				<html:link page="<%= "/manageExecutionCourse.do?method=deleteBibliographicReference&amp;" + url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
					<bean:message key="button.delete"/>
				</html:link>
				<br/>
				<br/>
			</logic:equal>
		</logic:iterate>		
	</blockquote>

</logic:present>