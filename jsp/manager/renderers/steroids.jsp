<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- Title and TOC -->
<a name="top"></a>
<div style="margin-bottom: 50px">
    <h2>The third situation: input on steroids</h2>
    
    <ul>
        <li><a href="#why">Steroids? Why?</a></li>
        <li><a href="#remember">A little flashback about the renderers philosophy</a></li>
        <li><a href="#how">How does this all really works?</a></li>
        <li><a href="#messages">One more thing about messages</a></li>
        <li><a href="#nice-but">Nice, why should I ever go back?</a></li>
        <li><a href="#contexts">More about it: contexts</a></li>
        <li><a href="#creation">Contexts are needed for creation</a></li>
    </ul>
</div>

<a name="why"></a>
<h3>Steroids? Why?</h3>

<p>
    Because the basic renderers philosophy was extended with the objective of making the input
    more similar to the presentation in terms of use in the JSPs and, by that, being more flexible
    and reusable (in the essential parts) ... but there is a catch.
</p>

<p>
    The initial approach consisted in identifing those presentations that were standard through out
    the application, implement them with a renderer, and use it repeatedly with only a few 
    modifications allowed by the renderer's properties. Nevertheless the presentations that were
    identified were always in a macro scale, that is, we would identify standard ways of doing
    the input of all values of an object, or editing all objects in a collection. This does not
    give much control over the details of the presentation.
</p> 

<p> 
    Whenever someone wanted to edit information in a different way it would be limited by strategy 
    being followed. It would either acomodate that specific case in an existing layout or identify 
    the pattern behind that specific case and implement the renderer. 
</p>

<p>
    Since, too many of such cases occur and since the input is not as flexible as the presentation 
    regarding the reuse and composition of available presentation in a JSPs a new strategy was
    developed. This new strategy allows you to specify were each slot's editor will instead of
    choosing the layout for the entire object and having the corresponding renderer decide were 
    each editor goes.
</p>

<p>
    So, why on steroids? Because it teorically gives you much more control over the presentation
    in each JSP. It allows you to control were things are presented, how they are presented, or
    were messages appear. I allows you to edit several distinct objects in a single page or create
    several different types in the same page. The utilization of renderers because much closer to
    what people were used to with struts or JSF but still with a significat difference in philosophy.
</p>

<a name="remember"></a>
<h3>A little flashback about the renderers philosophy</h3>

<p>
    The renderers frameworks is focused on the application data. Whats important is what the
    application wants to show to the user or what information it wants the user to provide.
    Other framewors like JSF or Struts are designed to focus on the interface aspect. The allow
    you to choose what appears in the page and then how you bind it to the application.
</p>

<p>
    We could say that while with renderers we can thing like:
    <em>
        The title is the student's number and here I'm editing the name and email of the
    student.
    </em>
</p>

<p>
    With JSF we would need to think:
    <em>
        The title of the page is a label with the student's number and here I have a form
    with two text fields. The first is bound to the student's name and the second is bound
    to the student's email.
    </em>
</p>

<p>
    Terms like <em>label</em>, <em>form</em>, or <em>text field</em> don't have the same
    importance with renderers than they have with approaches like JSF. Ideally one would
    need to think <em>I need to collect lots of text</em> instead refering to the graphical
    text box component normally used in that situations. Because of this difference in
    philosophy we can't expect to control all aspects of the presentation of a certain piece
    of information. Even with this new approach, were we can choose exactly were a certain
    editor will appear, we just have more flexibility, the philosophy of use is the same. Note
    that I've referred to a generic <em>editor</em> because you can't really tell what the
    final presentation will be just by looking to the JSP. In the page you can track the
    information you want to display (or edit), wich parts of the information are accessible
    (the schema), the intention of that presentation (aka layout), and several pieces of 
    configuration.
</p>

<p>
    In short, this new approach allows you a greater flexibility because now you can edit
    individual slot's of objects, meaning that you can select were each slot's editor appear
    instead of beeing "limited" by a layout that edits alls slot's of the object in a standard
    way. This approach also allows you top skip schemas because now you explicitly indicate each
    slot you need directly in the JSP.
</p>

<a name="how"></a>
<h3>How does this all really works?</h3>

<p>
    So to edit a person you would do something like
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:edit id=&quot;input&quot; name=&quot;UserView&quot; property=&quot;person&quot; schema=&quot;person.simple-edit&quot;/&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
             <fr:edit id="input" name="UserView" property="person" schema="person.simple-edit"/>
        </div>
    </div>
</div>

<p>
    Now you can do something like:
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;form method=&quot;post&quot; action=&quot;&lt;%= request.getContextPath() + &quot;/manager/renderers/steroids.do&quot; %&gt;&quot;&gt;
    &lt;table border=&quot;1&quot;&gt;
        &lt;tr&gt;
            &lt;td&gt;
                &lt;fr:edit name=&quot;UserView&quot; property=&quot;person&quot; slot=&quot;name&quot;&gt;
                    &lt;fr:layout&gt;
                        &lt;fr:property name=&quot;size&quot; value=&quot;50&quot;/&gt;
                    &lt;/fr:layout&gt;
                &lt;/fr:edit&gt;
            &lt;/td&gt;
            &lt;td&gt;
                &lt;fr:edit name=&quot;UserView&quot; property=&quot;person&quot; slot=&quot;gender&quot;/&gt;
            &lt;/td&gt;
            &lt;td&gt;
                &lt;fr:edit name=&quot;UserView&quot; property=&quot;person&quot; slot=&quot;idDocumentType&quot;/&gt;
            &lt;/td&gt;
        &lt;/tr&gt;
        &lt;tr&gt;
            &lt;td&gt;
                &lt;fr:edit name=&quot;UserView&quot; property=&quot;person&quot; slot=&quot;numeroDocumentoIdentificacao&quot;&gt;
                    &lt;fr:layout&gt;
                        &lt;fr:property name=&quot;size&quot; value=&quot;12&quot;/&gt;
                        &lt;fr:property name=&quot;maxLength&quot; value=&quot;10&quot;/&gt;
                    &lt;/fr:layout&gt;
                &lt;/fr:edit&gt;
            &lt;/td&gt;
            &lt;td&gt;
                &lt;fr:edit name=&quot;UserView&quot; property=&quot;person&quot; slot=&quot;dataValidadeDocumentoIdentificacao&quot;/&gt;
            &lt;/td&gt;
            &lt;td&gt;
                &lt;fr:edit name=&quot;UserView&quot; property=&quot;person&quot; slot=&quot;availablePhoto&quot;/&gt;
            &lt;/td&gt;
        &lt;/tr&gt;
    &lt;/table&gt;        
    
    &lt;input type=&quot;submit&quot;/&gt;
&lt;/form&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <form method="post" action="<%= request.getContextPath() + "/manager/renderers/steroids.do" %>">
                <table border="1">
                    <tr>
                        <td>
                            <fr:edit name="UserView" property="person" slot="name">
                                <fr:layout>
                                    <fr:property name="size" value="50"/>
                                </fr:layout>
                            </fr:edit>
                        </td>
                        <td>
                            <fr:edit name="UserView" property="person" slot="gender"/>
                        </td>
                        <td>
                            <fr:edit name="UserView" property="person" slot="idDocumentType"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <fr:edit name="UserView" property="person" slot="numeroDocumentoIdentificacao">
                                <fr:layout>
                                    <fr:property name="size" value="12"/>
                                    <fr:property name="maxLength" value="10"/>
                                </fr:layout>
                            </fr:edit>
                        </td>
                        <td>
                            <fr:edit name="UserView" property="person" slot="dataValidadeDocumentoIdentificacao"/>
                        </td>
                        <td>
                            <fr:edit name="UserView" property="person" slot="availablePhoto"/>
                        </td>
                    </tr>
                </table>
                
                <input alt="input.input" type="submit"/>
            </form>
        </div>
    </div>
</div>

<p>
    That is a lot to write but it's exactly as someone wants it. Since you have access to the slot directly you can
    place it wherever you want in the layout of the page. The configuration that was defined in the schema is now
    defined directly in the JSP as normal layout properties. You should notice that the <tt>edit</tt> and <tt>create</tt>
    tags now have additional <tt>validator</tt> and <tt>converter</tt> attributes that match the ones available in the
    schema. You also have a <tt>fr:validator</tt> tag that allows you to specify the validator and configure some of the
    validator's properties. Basically you are given the same configuration capabilities that you had in the schema. 
    Nevertheless all that configuration if made in the JSP thought the available tags and attributes.
</p>

<a name="messages"></a>
<h3>One more thing about messages</h3>

<p>
    When you submit a form and something goes wrong (probably a validation fails) the form is redisplayed and 
    the corresponding message appears. This happens in the default layout for editing an object nevertheless for
    every other layout this may not happen. It's the renderer associated with that layout that determines where 
    messages are presented of if they are presented at all. Some layout may not have a good standard way of including
    validation errors next to editors that generated that erros. So they may not include the errors at all.
</p>

<p>
    When you start editing slots directly this is always the case, that is, the editor never includes any space for
    showing the messages associated with the slot. So you have to include them explicitly in the page. Once again this
    has the advantage of allowing you to place the message wherever you want in the page. To include the message
    you need to use the <tt>fr:message</tt> tag. This tag has several attributes but the <tt>for</tt> attributes
    is the one that binds the tag to correct input context by specifying the id of that context. Whever there is a 
    message for that context it is displayed in the place where the tag was inserted.
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;form method=&quot;post&quot; action=&quot;&lt;%= request.getContextPath() + &quot;/manager/renderers/steroids.do&quot; %&gt;&quot;&gt;
    &lt;table border=&quot;1&quot;&gt;
        &lt;tr&gt;
            &lt;td&gt;
                &lt;fr:edit id=&quot;person-name-validated&quot; name=&quot;UserView&quot; property=&quot;person&quot; slot=&quot;name&quot;
                         validator=&quot;net.sourceforge.fenixedu.renderers.validators.RequiredValidator&quot;&gt;
                    &lt;fr:layout&gt;
                        &lt;fr:property name=&quot;size&quot; value=&quot;50&quot;/&gt;
                    &lt;/fr:layout&gt;
                &lt;/fr:edit&gt;
            &lt;/td&gt;
            &lt;td&gt;
                &lt;fr:message for=&quot;person-name-validated&quot;/&gt;
            &lt;/td&gt;
        &lt;/tr&gt;
    &lt;/table&gt;
    
    &lt;input type=&quot;submit&quot;/&gt;
&lt;/form&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <form method="post" action="<%= request.getContextPath() + "/manager/renderers/steroids.do" %>">
                <table border="1">
                    <tr>
                        <td>
                            <fr:edit id="person-name-validated" name="UserView" property="person" slot="name"
                                     validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator">
                                <fr:layout>
                                    <fr:property name="size" value="50"/>
                                </fr:layout>
                            </fr:edit>
                        </td>
                        <td>
                            <fr:message for="person-name-validated"/>
                        </td>
                    </tr>
                </table>
                
                <input alt="input.input" type="submit"/>
            </form>
        </div>
    </div>
</div>

<p>
    An input context for a slot can only have one message. This message is either a validation message or a 
    conversion error message. In other words, if validation fails for the slot then the message from the
    validation will be available. When validation passes but the value could not be converter to the final
    value then the convertion message will be available instead.
</p>

<a name="contexts"></a>
<h3>More about it: contexts</h3>

<p>
    We have already mentioned contexts but have never propertly defined what a context is. When you use one of
    the <tt>fr:view</tt>, <tt>fr:edit</tt>, or <tt>fr:create</tt> tags you are implicitly defining a presentation
    context (or a context). For the case of the <tt>fr:view</tt> tag the context is not important because no state
    needs to be propagated. But for the other tags a ViewState is associated with each tag. 
</p>

<p>
    More. When you're using the "old" approach and edit an entire object, the input context is the same for the
    the entire presentation and thus for all the slots. When the "new" approach is used there is an input
    context for each slot. This is mostly inefficient because much more hidden information is included in the 
    page than is really needed.
</p>

<p>
    You can group all the independent contexts. The contexts will still be independent. There will be one ViewState
    per context. Nevertheless the representation in the page will be much more efficient.
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:context&gt;
    (...)
&lt;/fr:context&gt;</pre>
    </div>
</div>

<p>
    All the inner contexts, that is, all the contexts from <tt>fr:edit</tt> or <tt>fr:create</tt> tags will be
    grouped and encoded togheter. Nevertheless this tag does not generate a form. It still need to be enclosed
    in an external form like the one provided by <tt>html:form</tt> from <tt>struts-html</tt> taglib. 
</p>

<p>
    If you really want a simple form that acts as a context aggregator you can use the <tt>fr:form</tt> tag. This
    tag only allows you to specify the <tt>action</tt> attribute so, as you can see, it's very simple and does not
    force you to declare a form bean for the action like the <tt>html:form</tt> does.
</p>

<a name="creation"></a>
<h3>Contexts are needed for creation</h3>

<p>
    Everything that was said is fine for the <tt>fr:edit</tt>. Neverthless you need to know a little more about the
    <tt>fr:create</tt> tag. When you create an object with the "new" approach the <tt>fr:context</tt> or <tt>fr:form</tt>
    tags are required around all the <tt>fr:create</tt> tags that are used to create the object. If all contexts 
    were left really independant they you would create an object for each <tt>fr:create</tt> tag present in the
    page. If all those context are aggregated then they will all colaborate in the creation of the same object and everything
    works as expected.
</p>

<p>
    One other note. Currently you can only create one direct object per page. One direct object means that all <tt>fr:create</tt>
    will either create as much objects as there are tags (you will probably never want that) or create only one object. Of course
    you can create objects implicitly as mentioned <html:link page="/renderers/input.do" anchor="create-multiple">before</html:link>.
</p>

<p style="margin-top: 50px; padding-top: 10px; border-top: 1px solid #AAAAAA">
    <span style="float: left;"><a href="#top">Top</a></span>
    <span style="float: right;">
        Next: <html:link page="/renderers/actions.do">The fourth situation: renderers meet actions</html:link>
    </span>
</p>


