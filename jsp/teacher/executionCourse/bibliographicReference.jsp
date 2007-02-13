<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="link.bibliography" /></h2>

<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>

<div class="section1">
	<bean:message key="label.bibliography.explanation" />
</div>

<logic:present name="executionCourse">

	<p>
		<div class="gen-button">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<html:link page="/createBibliographicReference.do?method=prepareCreateBibliographicReference" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="label.insertBibliographicReference"/>                   		     
			</html:link>
		</div>
		<div class="gen-button">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<html:link page="/manageExecutionCourse.do?method=prepareImportBibliographicReferences&amp;page=0" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.import.bibliographicReferences"/>
			</html:link>
		</div>
	</p>

	<h3 class="mtop2">
		<bean:message key="message.recommendedBibliography"/>
	</h3>
	

        <%-- Recommended bibliography from competence course --%>
        <logic:equal name="executionCourse" property="compentenceCourseMainBibliographyAvailable" value="false">
	        <logic:empty name="executionCourse" property="orderedBibliographicReferences">
	           	<em><bean:message key="label.noSecondaryBibliographicReference"/>.</em>
	        </logic:empty>
        </logic:equal>
        
        <logic:iterate id="competenceCourseInformation" name="executionCourse" property="competenceCoursesInformations">
            <logic:present name="competenceCourseInformation" property="bibliographicReferences">
                <logic:iterate id="bibliographicReference" name="competenceCourseInformation" property="bibliographicReferences.bibliographicReferencesSortedByOrder">
                    <logic:equal name="bibliographicReference" property="type" value="MAIN">
                    <ul class="list4">
	                    <li>
	                        <logic:empty name="bibliographicReference" property="url">
	                            <bean:write name="bibliographicReference" property="title" filter="false"/><br/>
	                        </logic:empty>
	                        <logic:notEmpty name="bibliographicReference" property="url">
	                            <html:link name="bibliographicReference" property="url">
	                                <bean:write name="bibliographicReference" property="title" filter="false"/><br/>
	                            </html:link>>
	                        </logic:notEmpty>
	                        <bean:write name="bibliographicReference" property="authors" filter="false" /><br/>
	                        <bean:write name="bibliographicReference" property="reference"  filter="false" /><br/>
	                        <bean:write name="bibliographicReference" property="year" filter="false" /><br/>
                        </li>
					</ul>
                    </logic:equal>
                </logic:iterate>
            </logic:present>
        </logic:iterate>

        <%-- Recommended bibliography from execution course --%>

        <logic:notEmpty name="executionCourse" property="mainBibliographicReferences">
        		<logic:iterate id="bibliographicReference" name="executionCourse" property="orderedBibliographicReferences">
        			<logic:notEqual name="bibliographicReference" property="optional" value="true">
        			<ul class="list4">
	        			<li>
	        				<bean:write name="bibliographicReference" property="title" filter="false"/><br/>
	        				<bean:write name="bibliographicReference" property="authors" filter="false"/><br/>
	        				<bean:write name="bibliographicReference" property="reference"  filter="false"/><br/>
	        				<bean:write name="bibliographicReference" property="year" filter="false" /><br/>
	        				<bean:define id="url" type="java.lang.String">bibliographicReferenceID=<bean:write name="bibliographicReference" property="idInternal"/></bean:define>
	        				<html:link page="<%= "/editBibliographicReference.do?method=prepareEditBibliographicReference&amp;" + url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	        					<bean:message key="button.edit"/>
	        				</html:link>, 
	        				<html:link page="<%= "/manageExecutionCourse.do?method=deleteBibliographicReference&amp;" + url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	        					<bean:message key="button.delete"/>
	        				</html:link>
        				</li>
        			</ul>
        			</logic:notEqual>
        		</logic:iterate>

            <bean:size id="referencesSize" name="executionCourse" property="mainBibliographicReferences"/>
            <logic:greaterThan name="referencesSize" value="1">
                <p>
                    <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
                    <html:link page="/manageExecutionCourse.do?method=prepareSortBibliography" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
                        <bean:message key="message.sortRecommendedBibliography"/>
                    </html:link>
                </p>
            </logic:greaterThan>
        </logic:notEmpty>



	<h3 class="mtop2">
		<bean:message key="message.optionalBibliography"/>
	</h3>


        <%-- Secondary bibliography from competence course --%>
        <logic:equal name="executionCourse" property="compentenceCourseSecondaryBibliographyAvailable" value="false">
	        <logic:empty name="executionCourse" property="secondaryBibliographicReferences">
				<em><bean:message key="label.noPrimaryBibliographicReference"/>.</em>
	        </logic:empty>
        </logic:equal>
        
        <logic:iterate id="competenceCourseInformation" name="executionCourse" property="competenceCoursesInformations">
            <logic:present name="competenceCourseInformation" property="bibliographicReferences">
                <logic:iterate id="bibliographicReference" name="competenceCourseInformation" property="bibliographicReferences.bibliographicReferencesSortedByOrder">
                    <logic:equal name="bibliographicReference" property="type" value="SECONDARY">
						<ul class="list4">
							<li>
		                        <logic:empty name="bibliographicReference" property="url">
		                            <bean:write name="bibliographicReference" property="title" filter="false"/><br/>
		                        </logic:empty>
		                        <logic:notEmpty name="bibliographicReference" property="url">
		                            <html:link name="bibliographicReference" property="url">
		                                <bean:write name="bibliographicReference" property="title" filter="false"/><br/>
		                            </html:link>>
		                        </logic:notEmpty>
		                        <bean:write name="bibliographicReference" property="authors" filter="false" /><br/>
		                        <bean:write name="bibliographicReference" property="reference"  filter="false" /><br/>
		                        <bean:write name="bibliographicReference" property="year" filter="false" /><br/>
	                        </li>
                        </ul>
                    </logic:equal>
                </logic:iterate>
            </logic:present>
        </logic:iterate>
    
        <%-- Secondary bibliography from execution course --%>
        
        <logic:notEmpty name="executionCourse" property="secondaryBibliographicReferences">
        		<logic:iterate id="bibliographicReference" name="executionCourse" property="orderedBibliographicReferences">
        			<logic:equal name="bibliographicReference" property="optional" value="true">
        				<ul class="list4">
	        				<li>
		        				<bean:write name="bibliographicReference" property="title" filter="false"/><br/>
		        				<bean:write name="bibliographicReference" property="authors" filter="false" /><br/>
		        				<bean:write name="bibliographicReference" property="reference"  filter="false" /><br/>
		        				<bean:write name="bibliographicReference" property="year" filter="false" /><br/>
		        				<bean:define id="url" type="java.lang.String">bibliographicReferenceID=<bean:write name="bibliographicReference" property="idInternal"/></bean:define>
		        				<html:link page="<%= "/editBibliographicReference.do?method=prepareEditBibliographicReference&amp;" + url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
		        					<bean:message key="button.edit"/>
		        				</html:link>, 
		        				<html:link page="<%= "/manageExecutionCourse.do?method=deleteBibliographicReference&amp;" + url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
		        					<bean:message key="button.delete"/>
		        				</html:link>
		       				</li>
        				</ul>
        			</logic:equal>
        		</logic:iterate>		

            <bean:size id="referencesSize" name="executionCourse" property="secondaryBibliographicReferences"/>
            <logic:greaterThan name="referencesSize" value="1">
                <p>
                    <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
                    <html:link page="/manageExecutionCourse.do?method=prepareSortBibliography&amp;optional=true" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
                        <bean:message key="message.sortOptionalBibliography"/>
                    </html:link>
                </p>
            </logic:greaterThan>
        </logic:notEmpty>


</logic:present>