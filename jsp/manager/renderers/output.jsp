<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- Title and TOC -->
<a name="top"></a>
<div style="margin-bottom: 50px">
    <h2>The first situation: presenting the world</h2>
    
    <ul>
        <li><a href="#warming">Warming up</a></li>
        <li><a href="#present">Presenting an object</a></li>
        <li><a href="#labels">Before schemas what about ...</a></li>
        <li><a href="#schemas">And now schemas for everybody</a></li>
        <li><a href="#properties">Renderers can receive properties</a></li>
        <li><a href="#more-schemas">Schemas have so much more to be said</a></li>
        <li><a href="#layouts">We add new schemas but what about new layouts?</a></li>
        <li><a href="#templates">We can always use templates and all that we already knew</a></li>
    </ul>
</div>

<a name="warming"></a>
<h3>Warming up</h3>

<p>
    As the use of renderers is made through a TagLib, the first thing you need to do is to 
    declare the use of the <code>fenix-renderers.tld</code> description file. The following
    line does the trick:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px;">
    <pre>&lt;%@ taglib uri=&quot;/WEB-INF/fenix-renderers.tld&quot; prefix=&quot;fr&quot; %&gt;</pre>
</div>

<p>
    The TLD is somewhat documented and eclipse is able to show that documentation if you have the Web
    Standard Tools installed. Just copy the <code>fenix-renderers.tld</code> to the 
    <code>jsp/WEB-INF</code> directory and possibly restart eclipse. From this moment you can use the
    beloved <code>Ctrl+Space</code> shortcut to auto-complete tags and see documentation.
</p>

<a name="present"></a>
<h3>Presenting an object</h3>

<p>
    The first thing you would like to know is: 

    <strong>How can I just present something and what can I present?</strong><br/>
</p>

<p>    
    The last part first. You can present any object given that you have a renderer configured to handle
    that object type. Normally there is a renderer for the <code>java.lang.Object</code> type so every
    object is presentable.
</p>

<p>
    Now for the first part. To present an object you can use the <code>view</code> tag. If you have
    Eclipse configured then you can read the documentation for the tag while using it. But the basic
    use of the tag is:
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; scope=&quot;session&quot;/&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:view name="UserView" scope="session"/>
        </div>
    </div>
</div>

<p>
    As you can see, we are using the default presentation for the <code>UserView</code> object available in 
    the session scope. If you ommit the <code>scope</code> attribute then the attribute with the given name 
    will be searched in all scopes starting from the most specific, that is, page scope.
</p>

<p>
    You can also see that the default presentation of a person is simply a link to it's details. More concretly,
    it's a link to an action that presents the person in the tabular layout. You can select the layout directly
    using the <code>layout</code> attribute of the tag.
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; layout=&quot;tabular&quot;/&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:view name="UserView" property="person" layout="tabular"/>
        </div>
    </div>
</div>

<p>
    You may have notice the introduction of the <code>property</code> attribute. This works together with the
    <code>name</code> attribute to further select the target object. It's supposed to behave exactly as in
    other tags available from the Struts project, so you can use a syntax like 
</p>

    <ul>
        <li><code>"person.nationality.code"</code></li>
        <li>or <code>"person.students[0].number"</code></li>
    </ul>
   
<p>    
     as long as all middle elements are not null.
</p>

<p>
    As Fénix domain objects are persistent you have and additional way of refering to a domain object.
    You can specify the object internal id type directly in the tag. If you do this the object will
    be retrieved from the database and displayed as before. So to display the same person as in the
    last example you could write:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px;">
    <bean:define id="personId" name="UserView" property="person.idInternal"/>
    <pre>&lt;fr:view oid=&quot;<%= personId %>&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot; layout=&quot;tabular&quot;/&gt;</pre>
</div>

<p>
    One thing you must notice. The type must be the same as specified in the DML. If you use an
    interface as the type you will get a <code>ClassNotPersistenceCapableException</code>.
</p>

<a name="labels"></a>
<h3>Before schemas what about ...</h3>

<p>
    ... that user friendlly names that appear in the left side of the table?
</p>

<p>
    Glad you ask. As you won't believe in magic I'm forced to tell you that there is a new resource bundle
    in town: <code>RendererResources</code>. The text that appears in the left side is fetched from this
    resource bundle or from the default resource bundle associated with the module, that is, in the last case
    the <code>ApplicationResources</code> is used. Labels are searched in the resources using a simple
    convention. Each row correspondes to a slot of the object. For example, <code>nome</code> is one of the 
    person's slots because we have the <code>getNome</code> getter. When the label for a slot is needed we 
    search the renderer's resources for a key using the following order:
</p>

    <ol>
      <li><code>label.net.sourceforge.fenixedu.domain.Person.nome</code></li>
      <li><code>label.nome</code></li>
      <li><code>nome</code></li>
    </ol>

<p>
    If no key is present the programmatic name of the slot is shown. <strong>Note</strong> that slots 
    may have more complex names like <code>nationality.code</code>. This does not correspond directly to a getter
    of person but is treated the same way as before.
</p>

<p>
    ... all the other getters that a person has due to all the relations it mantains?
</p>

<p>
    For those you will have to request their display explicitly and decide how they are presented. By default
    only the direct slots of the object are displayed. In the case of domain objects, like the person here,
    those are the ones present in the <code>class</code> definition of the DML.
</p>

<p>
    And why is this so?
</p>

<p>
    Well, direct slots already gain the semantic of "data" in the DML. Relations are more related to business
    logic and domain organization than to data. Each person is related to a country. when you present a person
    you most probably want to show the slot <code>nome</code> but do you wan't to present the country? You may
    wan't to present the country's code or country's name but not all the information of the country because
    then you would have to handle and the country's relations as well. Now consider an external person. Itself
    it has no direct slots, only relations. So when you present a person you want to present it's relation
    with an external institution, an external jury, or an external guider. The need is context specific so the
    decision should also be.
</p>

<p>
    Ah! Other thing. A person has roles. Suppose that in the manager context you wan't to display the details 
    of a role, for instance, the role <code>PERSON</code>. If it's relations were shown by default then the
    manager would get, among other things, a list will several thousand items: all the active persons in the 
    database.
</p>

<a name="schemas"></a>
<h3>And now schemas for everybody</h3>

<p>
    The last example shows a lot of information some of which we may not need. We can control this by
    introducing schemas. The simple vision of schemas is that they declare which slots we are interested
    for a particular type. If, for example, we want to shown only the name, username, and email of 
    a person we could declare the following schema:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;schema name=&quot;person.simple-admin-info&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot;&gt;
    &lt;slot name=&quot;nome&quot;/&gt;
    &lt;slot name=&quot;username&quot;/&gt;
    &lt;slot name=&quot;email&quot;/&gt;
&lt;/schema&gt;</pre>
</div>

<p>
    Schemas are refered by name so the names must be unique. To refer to a schema you can use the
    <code>schema</code> attribute in the <code>view</code> tag. The target object will be shown
    using the given schema, that is, like if it only had the slots declared in the schema.
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; 
         layout=&quot;tabular&quot; schema=&quot;person.simple-admin-info&quot;/&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:view name="UserView" property="person" layout="tabular" schema="person.simple-admin-info"/>
        </div>
    </div>
</div>

<a name="properties"></a>
<h3>Renderers can receive properties</h3>

<p>
    So we now can present a person showing only the information we want and in a tabular layout as intended.
    But sometimes we need to ajust some detail in the we things are presented. One of the things that may
    vary with the context is the style tables and other information is shown. Nevertheless we could need
    other subtle changes.
</p>

<p>
    Most of these changes can be done by passing properties to the renderer. Yes you can pass properties to
    the renderers. If a renderer has methods <code>getFoo</code> and <code>setFoo</code> then it supports
    a property named <code>"foo"</code>.
</p>

<p>
    Ok. Before I show you how to set renderers properties some things must be explained. When you use the
    attribute <code>layout</code> you are actually using a sort of syntactic sugar for a more complex,
    expanded version. The expanded version of the last example is this:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; schema=&quot;person.simple-admin-info&quot;&gt;
    &lt;fr:layout name=&quot;tabular&quot;&gt;
    &lt;/fr:layout&gt;
&lt;/fr:view&gt;</pre>
</div>

<p>
    That inner <code>layout</code> tag allows you to both select and configure the layout. To set renderer's
    properties you can user the tag <code>property</code> that i to be used inside the tag <code>layout</code>.
    The tag <code>property</code> can have two attributes: name and value. And now an example:
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; schema=&quot;person.simple-admin-info&quot;&gt;
    &lt;fr:layout name=&quot;tabular&quot;&gt;
        &lt;fr:property name=&quot;classes&quot; value=&quot;style1&quot;/&gt;
        &lt;fr:property name=&quot;columnClasses&quot; value=&quot;listClasses,&quot;/&gt;
    &lt;/fr:layout&gt;
&lt;/fr:view&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:view name="UserView" property="person" schema="person.simple-admin-info">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="style1"/>
                    <fr:property name="columnClasses" value="listClasses,"/>
                </fr:layout>
            </fr:view>            
        </div>
    </div>
</div>

<p>
    In this example we are setting the <code>classes</code> and <code>columnClasses</code> properties
    of the renderer. As the renderer generates a table these properties are used to costumize the
    relevant parts of it. You could also specify the properties <code>rowClasses</code> and
    <code>headerClasses</code>. Note that the value of <code>columnClasses</code> is 
    <code>"listClasses,&lt;blanck&gt;"</code> meaning that the first column as the specified class and
    the second has no class.
</p>

<p>
    You can also ommit the <tt>value</tt> attribute. In this case the value of the property will be the
    body of the property tag. This can be used and is usefull for complex values that span over multiple lines.
    It is also usefull when combined with <tt>CDATA</tt> blocks. Note that you can use other tags inside the
    property tag. They will be evaluated and the resulting text is used as the property value. The following
    example is hipotetical as the renderer associated with the tabular layout for the Person domain object does 
    not support the given property.
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; schema=&quot;person.simple-admin-info&quot;&gt;
    &lt;fr:layout name=&quot;tabular&quot;&gt;
        &lt;fr:property name=&quot;description&quot;&gt;
            This is a description that includes &lt;acronym title=&quot;Hypertext Markup Language&quot;&gt;HTML&lt;/acronym&gt;
            and the use of inner &lt;html:link href=&quot;http://java.sun.com/products/jsp/syntax/1.2/syntaxref1211.html&quot;&gt;TagLibs&lt;/html:link&gt;.
        &lt;fr:property/&gt;
    &lt;/fr:layout&gt;
&lt;/fr:view&gt;</pre>
    </div>
</div>

<p>
    You must note that the body of the property is only evaluated once before any object is renderered
    so if you are presenting several objects you can't include logic in the property value and expect it
    to be evaluated when each object is being presented.
</p>

<a name="more-schemas"></a>
<h3>Schemas have so much more to be said</h3>

<p>
    We saw the simple use of schemas: to limit the presented slots. But schemas can contain much more 
    information for each slot. You can, for instance, indicate the layout, schema, a properties that
    will be used to present the value of a slot. This makes the renderers adopt a more recursive and
    flexible style.
</p>

<p>
    The following example extends the schema previously defined and uses it as before:
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Schema -->
    <div>
        <p><strong>Schemas</strong></p>
        <pre>&lt;schema name=&quot;person.simple-admin-info.extended&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot;&gt;
    &lt;slot name=&quot;nome&quot;/&gt;
    &lt;slot name=&quot;username&quot; layout=&quot;link&quot;/&gt;
    &lt;slot name=&quot;email&quot;&gt;
        &lt;property name=&quot;link&quot; value=&quot;true&quot;/&gt;
    &lt;/slot&gt;
    &lt;slot name=&quot;nationality&quot; schema=&quot;country.short&quot; layout=&quot;values&quot;&gt;
        &lt;property name=&quot;htmlSeparator&quot; value=&quot; - &quot;/&gt;
    &lt;/slot&gt;
&lt;/schema&gt;

&lt;schema name=&quot;country.short&quot; type=&quot;net.sourceforge.fenixedu.domain.Country&quot;&gt;
    &lt;slot name=&quot;code&quot;/&gt;
    &lt;slot name=&quot;nationality&quot;/&gt;
&lt;/schema&gt;
</pre>
    </div>

    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; schema=&quot;person.simple-admin-info.extended&quot;&gt;
    &lt;fr:layout name=&quot;tabular&quot;&gt;
        &lt;fr:property name=&quot;classes&quot; value=&quot;style1&quot;/&gt;
        &lt;fr:property name=&quot;columnClasses&quot; value=&quot;listClasses,&quot;/&gt;
    &lt;/fr:layout&gt;
&lt;/fr:view&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:view name="UserView" property="person" schema="person.simple-admin-info.extended">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="style1"/>
                    <fr:property name="columnClasses" value="listClasses,"/>
                </fr:layout>
            </fr:view>            
        </div>
    </div>
</div>

<a name="layouts"></a>
<h3>We add new schemas but what about new layouts?</h3>

<p>
    First, remember that a layout is a combination of a name, a renderer and a set of properties. Layouts are defined in the
    configuration file and may use existing renderes. Of course, some layouts seem to have a one-to-one mapping with a
    renderer but that's not necessary.
</p>

<p>
    Lets use the last example to add two new layouts. First lets suppose that the type of table used &#0151; with all that
    nice colors and borders &#0151; is frequently used. We could use the well known Copy&amp;Paste to reuse the layout and
    it's properties. The same for the inline presentation of the country, that is, presenting several values separated
    by a simple <code>" - "</code>.
</p>

<p>
    So we could do some sort of refactoring and create a layouts to represents those patterns. How do we do this? We add
    new entries to the renderers' configuration. Here is an example of what can be added:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;renderer type=&quot;java.lang.Object&quot; layout=&quot;nice-details-table&quot; 
          class=&quot;net.sourceforge.fenixedu.renderers.StandardObjectRenderer&quot;&gt;
    &lt;property name=&quot;classes&quot; value=&quot;style1&quot;/&gt;
    &lt;property name=&quot;columnClasses&quot; value=&quot;listClasses,&quot;/&gt;
&lt;/renderer&gt;
    
&lt;renderer type=&quot;java.lang.Object&quot; layout=&quot;values-dash&quot; 
          class=&quot;net.sourceforge.fenixedu.renderers.ValuesRenderer&quot;&gt;
    &lt;property name=&quot;htmlSeparator&quot; value=&quot; - &quot;/&gt;
&lt;/renderer&gt;</pre>
</div>

<p>
    Two new layous named <code>nide-details-table</code> and <code>values-dash</code> were defined. So now we can ommit the
    properties from the previous example and use the new names for the layout.
</p>
    
<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Schema -->
    <div>
        <p><strong>Schemas</strong></p>
        <pre>(...)
    &lt;slot name=&quot;country&quot; schema=&quot;country.short&quot; layout=&quot;values-dash&quot;/&gt;
(...)
</pre>
    </div>

    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; schema=&quot;person.simple-admin-info.extended&quot; layout=&quot;nice-details-table&quot;/&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:view name="UserView" property="person" schema="person.simple-admin-info.extended" layout="nice-details-table"/>
        </div>
    </div>
</div>

<a name="templates"></a>
<h3>We can always use templates and all that we already knew</h3>

<p>
    Suppose that you need to present a person in a very exotic manner. It would probably be much easier
    to create a JSP &#0151; probably with a powerfull web page designer &#0151; and simply introduce
    a couple of <code>bean:write</code> tags to display the information you need. But obviously you
    don't want to abandon the power provided by the renderers so there is a possibility to mix
    both approaches.
</p>

<p>
    One of the predefined renderers is the <code>TemplateRenderer</code>. You can configure the 
    <code>template</code> property of this renderer to refer one JSP. What the renderer does is
    setup the environment for the JSP and then delegating the presentation to it. You can consider
    that the JSP is included in the place were you used the <code>view</code> tag. 
</p>

<p>
    First, here is an example of how to delegate the presentation of a person to a template:
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot;&gt;
    &lt;fr:layout name=&quot;template&quot;&gt;
        &lt;fr:property name=&quot;template&quot; value=&quot;/manager/renderers/template.jsp&quot;/&gt;
    &lt;/fr:layout&gt;
&lt;/fr:view&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
            <fr:view name="UserView" property="person">
                <fr:layout name="template">
                    <fr:property name="template" value="/manager/renderers/template.jsp"/>
                </fr:layout>
            </fr:view>
        </div>
    </div>
</div>

<p>
    As this is a little too much to write just to specify the template that should be used, the 
    <code>view</code> tag supports an abbreviation for this case. In the same way that the 
    <code>layout</code> attribute allows to ommit the inner <code>fr:layout</code> tag, the 
    <code>template</code> attribute allows you to ommit all the inner tags.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>&lt;fr:view name=&quot;UserView&quot; property=&quot;person&quot; template=&quot;/manager/renderers/template.jsp&quot;&gt;</pre>
</div>

<p>
    But how do we access the object to be presented from the template?
</p>

<p>
    There is an additional taglib that can only be used in templates: <code>fenix-template.tld</code>.
    This taglib provides tags very similar to those we have been using but reference the
    object to be presented implicitly. Let's look at the template used before. You can easily see how 
    each element was generated by looking at the <code>(n)</code> notation.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>&lt;%@ taglib uri=&quot;/WEB-INF/fenix-template.tld&quot; prefix=&quot;ft&quot; %&gt;

&lt;!-- Defines a page attribute with the value of the property &quot;nome&quot; of the shown object --&gt;
&lt;ft:define id=&quot;personName&quot; property=&quot;nome&quot;/&gt;

&lt;table style=&quot;border-bottom: 1px solid gray&quot;&gt;
    &lt;thead&gt;
        &lt;tr&gt;
            &lt;th colspan=&quot;5&quot; style=&quot;border-bottom: 1px solid gray&quot;&gt;(1) &lt;%= personName %&gt;&lt;/td&gt;
        &lt;/tr&gt;
    &lt;/thead&gt;
    &lt;tbody&gt;
        &lt;tr&gt;
            &lt;td&gt;(2) &lt;ft:label property=&quot;gender&quot;/&gt;&lt;/td&gt;
            &lt;td&gt;(3) &lt;ft:view property=&quot;gender&quot;/&gt;&lt;/td&gt;
            
            &lt;!-- separator --&gt;
            &lt;td width=&quot;100px&quot; rowspan=&quot;2&quot;&gt;&lt;/td&gt;
            
            &lt;td&gt;(4) &lt;ft:label property=&quot;dateOfBirthYearMonthDay&quot;/&gt;&lt;/td&gt;
            &lt;td&gt;(5)
                &lt;ft:view property=&quot;dateOfBirthYearMonthDay&quot;&gt;
                    &lt;ft:layout&gt;
                        &lt;ft:property name=&quot;format&quot; value=&quot;dd MMMM yyyy&quot;/&gt;
                    &lt;/ft:layout&gt;
                &lt;/ft:view&gt;
            &lt;/td&gt;
        &lt;/tr&gt;
        &lt;tr&gt;
            &lt;td&gt;(6) &lt;ft:label property=&quot;username&quot;/&gt;&lt;/td&gt;
            &lt;td&gt;(7) &lt;ft:view property=&quot;username&quot;/&gt;&lt;/td&gt;

            &lt;td&gt;(8) &lt;ft:label property=&quot;nationality.nationality&quot;/&gt;&lt;/td&gt;
            &lt;td&gt;(9) &lt;ft:view property=&quot;nationality.nationality&quot;/&gt; &lt;/td&gt;
        &lt;/tr&gt;
    &lt;/tbody&gt;
&lt;/table&gt;</pre>
</div>

<p>
    The two entirely new tags are <code>define</code> and <code>label</code>. The first tag
    is very similar to the <code>bean:define</code> tag. It defines a new attribute in the 
    page with the value of the given property from the presented object.  The <code>label</code> 
    tag allows you to search for the label defined in the renderers' resources using the 
    renderer's conventions for labels.
</p>

<p style="margin-top: 50px; padding-top: 10px; border-top: 1px solid #AAAAAA">
    <span style="float: left;"><a href="#top">Top</a></span>
    <span style="float: right;">
        Next: <html:link page="/renderers/input.do">The second situation: give me input</html:link>
    </span>
</p>

