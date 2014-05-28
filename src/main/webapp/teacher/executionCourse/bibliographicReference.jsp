<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="link.bibliography" /></h2>

<p>
	<span class="error0"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>

<div class="infoop2">
	<bean:message key="label.bibliography.explanation" />
</div>

<logic:present name="executionCourse">
	<bean:define id="professorship" name="executionCourse" property="professorshipForCurrentUser"/>
	<bean:define id="professorshipPermissions" name="professorship" property="permissions"/>
	
	<logic:equal name="professorshipPermissions" property="bibliografy" value="true">
	<p>
		<div class="gen-button">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<html:link page="/manageBibliographicReference.do?method=prepareCreateBibliographicReference" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="label.insertBibliographicReference"/>                   		     
			</html:link>
		</div>
		<div class="gen-button">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
			<html:link page="/manageBibliographicReference.do?method=prepareImportBibliographicReferences&amp;page=0" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.import.bibliographicReferences"/>
			</html:link>
		</div>
	</p>
	</logic:equal>

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
                                <bean:define id="refUrl" name="bibliographicReference" property="url"/>
	                            <html:link href="<%= "" + refUrl %>">
	                                <bean:write name="bibliographicReference" property="title" filter="false"/><br/>
	                            </html:link>
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
	        				<bean:define id="url" type="java.lang.String">bibliographicReferenceID=<bean:write name="bibliographicReference" property="externalId"/></bean:define>
	        				<html:link page="<%= "/manageBibliographicReference.do?method=prepareEditBibliographicReference&amp;" + url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
	        					<bean:message key="button.edit"/>
	        				</html:link>, 
	        				<html:link page="<%= "/manageBibliographicReference.do?method=deleteBibliographicReference&amp;" + url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
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
                    <html:link page="/manageBibliographicReference .do?method=prepareSortBibliography" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
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
                                <bean:define id="refUrl" name="bibliographicReference" property="url"/>
	                            <html:link href="<%= "" + refUrl %>">
		                                <bean:write name="bibliographicReference" property="title" filter="false"/><br/>
		                            </html:link>
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
		        				<bean:define id="url" type="java.lang.String">bibliographicReferenceID=<bean:write name="bibliographicReference" property="externalId"/></bean:define>
		        				<html:link page="<%= "/manageBibliographicReference.do?method=prepareEditBibliographicReference&amp;" + url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
		        					<bean:message key="button.edit"/>
		        				</html:link>, 
		        				<html:link page="<%= "/manageBibliographicReference.do?method=deleteBibliographicReference&amp;" + url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
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
                    <html:link page="/manageBibliographicReference.do?method=prepareSortBibliography&amp;optional=true" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
                        <bean:message key="message.sortOptionalBibliography"/>
                    </html:link>
                </p>
            </logic:greaterThan>
        </logic:notEmpty>


</logic:present>