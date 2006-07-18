<%@page import="java.io.InputStream"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- Title and TOC -->
<a name="top"></a>
<div style="margin-bottom: 50px">
    <h2>Appendix A: Schemas</h2>
    
    <ul>
        <li><a href="#about">About schemas</a></li>
        <li><a href="#basic">Starting with the basic</a></li>
        <li><a href="#recursion">How to costumize the presentation of slots?</a></li>
        <li><a href="#i18n">I18n in slot's. What does that mean?</a></li>
        <li><a href="#read-only">Read only slots</a></li>
        <li><a href="#validator">Validate input</a></li>
        <li><a href="#converter">Convert user input</a></li>
        <li><a href="#extend">Schemas can extend other schemas</a></li>
        <li><a href="#default">Default values</a></li>
        <li><a href="#setters">Special setters and custom constructor: a better domain integration</a></li>
        <li><a href="#hidden">Declare hidden slots</a></li>
        <li><a href="#dtd">The DTD</a></li>
    </ul>
</div>

<a name="about"></a>
<h3>About schemas</h3>

<p>
    Schemas represent a certain view over an certain type of objects. The schema contains 
    information about which and how each slot should be presented or edited (presented will 
    be used for both situations).
</p>

<a name="basic"></a>
<h3>Starting with the basic</h3>


<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;schema name=&quot;person.simple-admin-info&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot;&gt;
  &lt;slot name=&quot;nome&quot;/&gt;
  &lt;slot name=&quot;username&quot;/&gt;
  &lt;slot name=&quot;email&quot;/&gt;
&lt;/schema&gt;</pre>
</div>

<p>
    Each schema must by assigned a <strong>unique name</strong>. Schemas are referred by name
    from several locations so they must be easily identifiable. Additionaly you also need
    to specify the name of the <strong>target type</strong>. Several functionalities in the
    schema depend on the type of the schema.
</p>

<p>
    The example shows the simples way of using a schema. A set of slots is declared meaning that
    whenever the schema is presented only those slots should be visible. No matter were each
    slot will be presented, the presentation will obey the slot's declared order. This means
    that, according to the example, the slot <code>nome</code> will appear before the slot
    <code>username</code> and this one will appear before <code>email</code>. When editing a single
    object this specifies, for example, the order of the rows and when editing several objects
    this chooses the order of the columns.
</p>

<a name="recursion"></a>
<h3>How to costumize the presentation of slots?</h3>

<p>
    When you want to present an object you choose the schema, layout, properties of that
    layout. The schema reffers the several slots that should be presented so it's possible
    to also specify the same elements for each slot. To do that you msut use the slot's 
    attributes <code>layout</code> and <code>schema</code>, and specify several properties
    for the slot.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;schema name=&quot;person.simple-admin-info.extended&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot;&gt;
  &lt;slot name=&quot;nome&quot;/&gt;
  &lt;slot name=&quot;username&quot; layout=&quot;link&quot;/&gt;
  &lt;slot name=&quot;email&quot;&gt;
      &lt;property name=&quot;link&quot; value=&quot;true&quot;/&gt;
  &lt;/slot&gt;
  &lt;slot name=&quot;pais&quot; schema=&quot;country.short&quot; layout=&quot;values&quot;&gt;
      &lt;property name=&quot;htmlSeparator&quot; value=&quot; - &quot;/&gt;
  &lt;/slot&gt;
&lt;/schema&gt;</pre>
</div>

<p>
    In this example the <code>email</code> and <code>pais</code> slots have their presentation customized.
    In the <code>email</code> slot we are just passing properties to the renderer associated with 
    the default layout for slot's type. Nevertheless, in the <code>pais</code> slot, we are 
    specifying both the <code>schema</code> and <code>attributes</code> and passing a property to
    the renderer associated with the layout <code>values</code>.
</p>

<a name="i18n"></a>
<h3>I18n in slot's. What does that mean?</h3>

<p>
    Each slot has a label that can be shown next to the value or value's editor. This label is 
    the slot's name or a resource message if the key is defined in the module's default bundle.
    The conventions used to determine the slot's label are explained in the
    <html:link page="/renderers/output.do#labels">section about labels in the output examples</html:link>.
</p>

<p>
    You can also override the used convetion and directly specify a <code>key</code> and
    <code>bundle</code> for each slot. This will be used to obtain the label for the slot.
    The schema also has an attribute <code>bundle</code>. This attributes avoids repeating
    the same bundle in all the slot's. So
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;schema ... bundle=&quot;BUNDLE&quot;&gt; ...</pre>
</div>

<p>
    is the same as
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;schema ...&gt;
  &lt;slot ... bundle=&quot;BUNDLE&quot;/&gt;
  &lt;slot ... bundle=&quot;BUNDLE&quot;/&gt;
  ...
&lt;/schema&gt;</pre>
</div>

<p>
    <strong>NOTE</strong>: You can specify the bundle for the whole schema and then override the bundle
    for a particular slot.
</p>


<a name="read-only"></a>
<h3>Read only slots</h3>

<p>
    Slot's can be marked read only. This is only significant when the schema is being used
    in an input context. If the object is only being presented then this information is
    obviously ignored. By default all slot's are writable so you normally you only specify 
    slot's that should not be writeable.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;slot ... read-only=&quot;true&quot;/&gt;</pre>
</div>

<a name="validator"></a>
<h3>Validate input</h3>

<p>
    The input validation is done by validators. Validators are Java classes that extend 
    <code>net.sourceforge.fenixedu.renderers.validators.HtmlValidator</code>. To validate
    a slot you need to specify the class of the validator that will be used.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;slot ... validator=&quot;net.sourceforge.fenixedu.presentationTier...&quot;/&gt;</pre>
</div>

<p>
    Since validators are supposed to be generic and reusable thy can be configured with
    the standard properties system. To pass properties to a validator you need to specify
    the validator as a inner tag.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;slot ...&gt;
    &lt;validator class=&quot;net.sourceforge.fenixedu.renderers.validators.RegexpValidator&quot;/&gt;
        &lt;property name=&quot;regexp&quot; value=&quot;\p{Digit}+&quot;/&gt;
    &lt;/validator&gt;
    ...
&lt;/slot&gt;</pre>
</div>

<a name="converter"></a>
<h3>Convert user input</h3>

<p>
    Sometimes you may need to provide a custom converter for the user input. For example if
    you are accepting an <code>Date</code> but want to convert the user input to an 
    <code>sql.Date</code> or any other subtype. Converters are also Java classes so you also need
    to specify the specific converter to use.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;slot ... converter=&quot;net.sourceforge.fenixedu.presentationTier...&quot;/&gt;</pre>
</div>

<p>
    Converters should only depend on the type and user input so it's not possible to pass
    properties to those converters. For example, associating an <code>sql.Date</code>
    converter to a <code>Date</code> field should understand the user input for that field,
    possibly reuse the conversion already available for the <code>Date</code> type
    and then create a new <code>sql.Date</code>.
</p>

<a name="extend"></a>
<h3>Schemas can extend other schemas</h3>

<p>
    Suppose you define a schema to show the personal details of a person. It could contain about 20 well configured
    and highly tailored slot definitions. Now if you want, in other context, to show the personal information and
    the information about the person's filiation you are in trouble. You need a new schema but how do you reuse
    the last schema? 
</p>

<p>
    Well, if you were thinking Copy&amp;Paste that always works but is not what I want to say.
    If you simply want  to present the information you can create a new schema with the slots for the filiation's
    information an present the person two times: the first with the first schema and the second with the schema
    you just created. Off course you will have all the drawbacs of having two presentations: You can't really mantain
    an uniform presentation of all the information together and you can't use the same strategy to edit that
    information.
</p>

<p>
    You can reuse previous schemas by extending them. The extension mechanism is in some (and few) ways similar to 
    the extension in OO languages. Let's use an example to show how things work:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;schema name=&quot;person.personal&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot;&gt;
  &lt;slot name=&quot;name&quot;/&gt;
  &lt;slot name=&quot;birthdate&quot;/&gt;
  &lt;slot name=&quot;email&quot;/&gt;
&lt;/schema&gt;

&lt;schema name=&quot;person.filiation&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot; extends=&quot;person.personal&quot;&gt;
  &lt;remove name=&quot;email&quot;/&gt;
  &lt;slot name=&quot;nameOfFather&quot;/&gt;
  &lt;slot name=&quot;nameOfMother&quot;/&gt;
&lt;/schema&gt;

&lt;schema name=&quot;person.filiation.edit&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot; extends=&quot;person.filiation&quot;&gt;
  &lt;slot name=&quot;name&quot;      read-only=&quot;true&quot;/&gt;
  &lt;slot name=&quot;birthdate&quot; read-only=&quot;true&quot;/&gt;
  &lt;slot name=&quot;email&quot;     read-only=&quot;true&quot;/&gt;
&lt;/schema&gt;</pre>
</div>

<p>
    In this example the <code>person.filiation</code> schema is extending the <code>person.personal</code>
    schema. Additionally we are removing the <code>email</code> slot in the <code>person.filiation</code>
    schema. This means that the schema will have 4 slots in the order <code>name</code>, <code>birthdate</code>,
    <code>nameOfFather</code>, and <code>nameOfMother</code>.
</p>

<p>
    The remove tag indicates slots that you don't want to include/inherit. This is preformed before any other
    thing is considered in the schema that is extending, in this case <code>person.filiation</code>.
    You can also "override" slot's definitions. If you declare a slot with a name that exists in the 
    parent then you are overriding it. This means that the slot will remain in the position it was first declared
    but will only have the properties of it's last declaration. In the last schema, named <code>person.filiation.edit</code>,
    we are extending the <code>person.filiation</code> schema and indicating that the slot's included/inhertited
    from the <code>person.personal</code> schema are read only. Nevertheless this schema contains all the
    slot's in the same order as <code>person.filiation</code>. All the new slot's are included after existing
    slot's.
</p>

<p>
    <strong>A note about problems of reuse</strong>. Schemas are intended to be reused in several presentation 
    through out the application. So, if you change a schema check the schemas that are extending that one. If your
    changes are too big you can choose to make a new schema or change the name and check wich dependencies
    fail (schemas can only extend existing schemas and an errors will be reported).
</p>

<a name="default"></a>
<h3>Default values</h3>

<p>
    The slot's attribute <code>default</code> can be used to specify the default value for the
    slot when no value is available, as in this case. The specified value will be converted to
    the type of the slot and then presented. From example, for a enumeration you can specify as
    the default value the name of the enum. Here is an example with some default values:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>(...)
    &lt;slot name=&quot;username&quot; default=&quot;pxxxxx&quot; (...)
    &lt;slot name=&quot;idDocumentType&quot; default=&quot;IDENTITY_CARD&quot; (...)
    &lt;slot name=&quot;maritalStatus&quot; default=&quot;UNKNOWN&quot; (...)
(...)</pre>
</div>

<p>
    Default values are simple and are directly tied to the schema, that is, to the place were 
    you tell how you want the object to be presented. The default value,
    as presented before, allows you to statically specify values that are independent from the
    context were the schema is being used. This works reasonably well for enums or some
    strings but is less good for complex values that cannot be easily represented as a string.
    Check the <html:link page="/renderers/input.do#dynamic-defaults">the dynamic values</html:link>
    section of the input example for more information.
</p>

<a name="setters"></a>
<h3>Special setters and custom constructor: a better domain integration</h3>

<p>
    Normally the renderers architecture use the standard Java Beans notation. Objects that need
    to be created are created using the default constructor and each slot's value is obtained
    with the standard getter and altered with the standard setter.
</p>

<p>
    Nevertheless when we need to ensure some consistency in the changed or created objects we
    need to invoke more complex constructors or invoke setters that take multiple arguments and
    do verifications that can only be done correctly if all the values are available at the 
    same time.
</p>

<p>
    Schemas were extended to allow you to specify that information when needed. For each schema
    you can indicate wich constructor is called instead of the default construtor and wich 
    special setters replace the standard setters. Here is an generic example:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;schema ... constructor=&quot;s1,s3:java.sql.Date,s2:Integer&quot;&gt;
  &lt;slot name=&quot;s1&quot;/&gt;
  &lt;slot name=&quot;s2&quot;/&gt;
  &lt;slot name=&quot;s3&quot;/&gt;
  &lt;slot name=&quot;s4&quot;/&gt;
  &lt;slot name=&quot;s5&quot;/&gt;
  &lt;slot name=&quot;s6&quot;/&gt;
    
  &lt;setter signature=&quot;setSomething(s4,s5)&quot;/&gt;
&lt;/schema&gt;</pre>
</div>

<p>
    This example is generic to exercise all the possibilities of this mechanism. The constructor
    for the object is specified as the <code>constructor</code> attribute of the schema. You can
    only specify one constructor for that schema. Special setters are declared using the 
    <code>setter</code> inner tag. You can declare multiple setters. For both the constructor
    and setter you specify a signature. The main difference between them is that you don't (can't)
    specify the name in the constructor. Nevertheless the way that signature identifies the 
    correct method/constructor is the same.
</p>

<p>
    Each signature consists of a name (as said only valid in the setter signature) and a list of
    parameters separated by commans. Each parameter consists of slot name and can be accompanied 
    by a type declaration. If the type is not given then it's assumed to be the type of the slot.
    The type can be given to force the parameter type to a subclass of the slots type. For instance
    if slot <code>s3</code> is of type <code>java.lang.Date</code> then, in the example, we are 
    forcing the parameter to the subtype <code>java.sql.Date</code>. 
</p>

<p>
    Explaning the example, we can see that whever we use this schema to create an object then
    the object will be created using a constructor that has 3 parameters with the types
</p>

<ol>
  <li>type of slot <code>s2</code></li>
  <li><code>java.sql.Date</code></li>
  <li><code>java.lang.Integer</code></li>
</ol>

<p>
    and passing as arguments the values of the slot <code>s1</code>, <code>s3</code>, and <code>s2</code>
    respectively. After creating the object the slots used in the constructor are considered seted
    so we are left with the slots <code>s4</code> to <code>s6</code> to set. The slots
    <code>s4</code> and <code>s5</code> are referred in a special setter. so this setter is used.
    This means that after creating the object the setter <code>setSomething</code> is called
    passing as arguments the values of the slots <code>s4</code> and <code>s5</code>. This slots
    are considered seted so the only slot left is <code>s6</code>. This slot was not used in
    the constructor neither was he used in a special setter so it will be seted using the standard
    setter <code>setS6(s6)</code>.
</p>

<a name="hidden"></a>
<h3>Declare hidden slots</h3>

<p>
    In the constructor and setters signature you can only reffer slots defined in the schema.
    The problem is that in most cases values that you need to specify in the constructor, for 
    example, are context dependant, that is, they are passed as hidden slots from the JSP.
</p>

<p>
    To overcome this problem you can declare those slots also in the schema and mark them as 
    hidden with the <code>hidden</code> atrribute. These slots will not be considered in the
    presentation but will be available to the constructor and setter declaration. Off course that
    if no hidden slots is really passed in the JSP the constructor will be passed the null value
    for that slot. See more in the <html:link page="/renderers/input.do#hidden">hidden slots 
    example</html:link>.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre>&lt;slot ... hidden=&quot;true&quot;/&gt;</pre>
</div>

<a name="dtd"></a>
<h3>The DTD</h3>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
    <pre><%
    
    String dtdLocation = "/WEB-INF/schemas/fenix-renderers-schemas.dtd";
    InputStream stream = pageContext.getServletContext().getResourceAsStream(dtdLocation);
    
    if (stream == null) {
        out.write("Could not find DTD in " + dtdLocation);
    }
    else {
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader buffered = new BufferedReader(reader);
        
        String line = null;
        while ((line = buffered.readLine()) != null) {
            out.write(
                    line.replaceAll("&", "&amp;")
                        .replaceAll("<", "&lt;") 
                        .replaceAll(">", "&gt;")
                        .replaceAll("\"", "&quot;")
                        + "\n");
        }
    }

%></pre>
</div>

<p style="margin-top: 50px; padding-top: 10px; border-top: 1px solid #AAAAAA">
    <span style="float: left;"><a href="#top">Top</a></span>
</p>

