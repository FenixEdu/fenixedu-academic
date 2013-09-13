<%@ page language="java" %> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.research.result.publication.PreferredPublicationPriority"%>
<html:xhtml/>

    <logic:notEmpty name="books">
        <p id='books' class="mtop15 mbottom0"><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Books"/></span></strong></p>
        <bean:define id="currentSchema" value="result.publication.presentation.Book" toScope="request"/>
        <bean:define id="results" name="books" toScope="request"/>
        <jsp:include page="publicationsResume.jsp"/>
    </logic:notEmpty>

    <logic:notEmpty name="inbooks">
        <p id='inbooks' class="mtop15 mbottom0"><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.BookParts"/></span></strong></p>
        <bean:define id="currentSchema" value="result.publication.presentation.BookPart" toScope="request"/>
        <bean:define id="results" name="inbooks" toScope="request"/>
        <jsp:include page="publicationsResume.jsp"/>
    </logic:notEmpty>
    
    <bean:define id="hasArticles" value="false" toScope="request"/>
    <logic:notEmpty name="national-articles"> 
        <bean:define id="hasArticles" value="true" toScope="request"/>
    </logic:notEmpty>
    <logic:notEmpty name="international-articles"> 
        <bean:define id="hasArticles" value="true" toScope="request"/>
    </logic:notEmpty>   
    
    <logic:equal name="hasArticles" value="true">
        <p id='books' class="mvert0"></p>
        <bean:define id="currentSchema" value="result.publication.presentation.Article" toScope="request"/>

        <logic:notEmpty name="international-articles">
        <p class="mtop15 mbottom0"><strong><bean:message key="label.internationalArticles" bundle="RESEARCHER_RESOURCES"/></strong></p>
        <bean:define id="results" name="international-articles" toScope="request"/>
        <jsp:include page="publicationsResume.jsp"/>
        </logic:notEmpty>
        
        <logic:notEmpty name="national-articles">
        <p class="mtop15 mbottom0"><strong><bean:message key="label.nationalArticles" bundle="RESEARCHER_RESOURCES"/></strong></p>
        <bean:define id="results" name="national-articles" toScope="request"/>
        <jsp:include page="publicationsResume.jsp"/>
        </logic:notEmpty>
    </logic:equal>
    
    <logic:notEmpty name="international-inproceedings">
        <p id='inproceedings' class="mtop15 mbottom0"/><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.InternationalInproceedings"/></span></strong></p>
        <bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
        <bean:define id="results" name="international-inproceedings" toScope="request"/>
        <jsp:include page="publicationsResume.jsp"/>
    </logic:notEmpty>
    
    <logic:notEmpty name="national-inproceedings">
        <p id='inproceedings' class="mtop15 mbottom0"/><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.NationalInproceedings"/></span></strong></p>
        <bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
        <bean:define id="results" name="national-inproceedings" toScope="request"/>
        <jsp:include page="publicationsResume.jsp"/>
    </logic:notEmpty>
    
    <logic:notEmpty name="proceedings">
        <p id='proceedings' class="mtop15 mbottom0"/><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Proceedings"/></span></strong></p>
        <bean:define id="currentSchema" value="result.publication.presentation.Proceedings" toScope="request"/>
        <bean:define id="results" name="proceedings" toScope="request"/>
        <jsp:include page="publicationsResume.jsp"/>
    </logic:notEmpty>
    
    <logic:notEmpty name="theses">
        <p id='theses' class="mtop15 mbottom0"/><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Theses"/></span></strong></p>
        <bean:define id="currentSchema" value="result.publication.presentation.Thesis" toScope="request"/>
        <bean:define id="results" name="theses" toScope="request"/>
        <jsp:include page="publicationsResume.jsp"/>
    </logic:notEmpty>
    
    <logic:notEmpty name="manuals">
        <p id='manuals' class="mtop15 mbottom0"/><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Manuals"/></span></strong></p>
        <bean:define id="currentSchema" value="result.publication.presentation.Manual" toScope="request"/>
        <bean:define id="results" name="manuals" toScope="request"/>
        <jsp:include page="publicationsResume.jsp"/>
    </logic:notEmpty>
    
    <logic:notEmpty name="technicalReports">
        <p id='technicalReports' class="mtop15 mbottom0"/><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.TechnicalReports"/></span></strong></p>
        <bean:define id="currentSchema" value="result.publication.presentation.TechnicalReport" toScope="request"/>
        <bean:define id="results" name="technicalReports" toScope="request"/>
        <jsp:include page="publicationsResume.jsp"/>
    </logic:notEmpty>
    
    <logic:notEmpty name="otherPublications">
        <p id='otherPublications' class="mtop15 mbottom0"/><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.OtherPublications"/></span></strong></p>
        <bean:define id="currentSchema" value="result.publication.presentation.OtherPublication" toScope="request"/>
        <bean:define id="results" name="otherPublications" toScope="request"/>
        <jsp:include page="publicationsResume.jsp"/>
    </logic:notEmpty>
    
    <logic:notEmpty name="unstructureds">
        <p id='unstructureds' class="mtop15 mbottom0"/><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Unstructureds"/></span></strong></p>
        
        <p class="color888">
            <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Unstructureds.description"/>
        </p>
        
        <bean:define id="results" name="unstructureds" toScope="request"/>
        <table class="publications mtop1">
            <logic:iterate id="result" name="results" scope="request">
                <bean:define id="resultId" name="result" property="externalId"/>
                <tr>
			        <td class="priority">
			        <logic:present name="preferredSetting">
			            <fr:edit name="result" slot="preferredLevel" nested="true">
			                <fr:layout>
			                    <fr:property name="defaultOptionHidden" value="true" />
			                </fr:layout>
			            </fr:edit>
			        </logic:present>
				        <logic:notPresent name="preferredSetting">
				            <bean:define id="level" name="result" property="preferredLevel.name" type="java.lang.String" />
				            <span title='<bean:message bundle="ENUMERATION_RESOURCES" key="<%= PreferredPublicationPriority.class.getSimpleName() + "." + level + ".description"  %>"/>'>
				               <fr:view name="result" property="preferredLevel" />
				            </span>
				        </logic:notPresent>
			        </td>
			        <td>
	                    <fr:view name="result" layout="values" schema="result.publication.presentation.Unstructured">
	                        <fr:layout>
	                            <fr:property name="htmlSeparator" value=", "/>
	                            <fr:property name="indentation" value="false"/>
	                        </fr:layout>
	                    </fr:view>
	                    <p class="mtop025">
	                    <html:link page="<%="/resultPublications/prepareEditData.do?resultId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.convertUnstructured" /></html:link>, 
	                    <html:link page="<%="/resultPublications/prepareDelete.do?&resultId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.delete" /></html:link>
	                    </p>
                    </td>
                </tr>
            </logic:iterate>
        </table>
    </logic:notEmpty>