/*
 * Created on Feb 27, 2005
 *
 */
package net.sourceforge.fenixedu.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;

/**
 * @author Luis Cruz
 * 
 */
public class DMLClassDescriptor
{

    private final ClassDescriptor classDescriptor;

    private final Map attributeDescriptors;

    private final Map objectReferenceDescriptors;

    private final Map collectionDescriptors;

    private final Set relations;

    public class DMLAttributeDescriptor
    {

        private FieldDescriptor fieldDescriptor;

        public DMLAttributeDescriptor(final FieldDescriptor fieldDescriptor)
        {
            this.fieldDescriptor = fieldDescriptor;
        }

        public void appendDMLDescription(final StringBuffer stringBuffer)
        {
            final Class fieldType = fieldDescriptor.getPersistentField().getType();
            if (isSupportedDMLField(fieldType))
            {
                stringBuffer.append("\n\t");
                stringBuffer.append(getSimpleClassName(fieldType));
                stringBuffer.append(" ");
                stringBuffer.append(getAttributeName());
                stringBuffer.append(";");
            }
        }

        protected String getSimpleClassName(final Class fieldType)
        {
            // If using java 1.5 this will work:
            // return fieldType.getSimpleName();
            // Otherwise, some string manipulation must be used:
            final String className = fieldType.getName();
            final String packageName = fieldType.getPackage().getName();
            return className.substring(packageName.length() + 1, className.length());
        }

        protected boolean isSupportedDMLField(final Class fieldType)
        {
            return fieldType.getName().startsWith("java.lang.") || fieldType.getName().equals("java.util.Date");
        }

        public String getAttributeName()
        {
            return fieldDescriptor.getAttributeName();
        }
    }

    public class DMLObjectReferenceDescriptor
    {

        private ObjectReferenceDescriptor objectReferenceDescriptor;

        public DMLObjectReferenceDescriptor(final ObjectReferenceDescriptor objectReferenceDescriptor)
        {
            this.objectReferenceDescriptor = objectReferenceDescriptor;
        }

        public void appendDMLDescription(final StringBuffer stringBuffer)
        {
            final String attributeName = objectReferenceDescriptor.getAttributeName();
            final Class fieldType = objectReferenceDescriptor.getPersistentField().getType();

            stringBuffer.append("\n\nrelation ");
            stringBuffer.append("???");
            stringBuffer.append(" {");

            stringBuffer.append("\n\t");
            stringBuffer.append(getClassName());
            stringBuffer.append(" playsRole ");
            stringBuffer.append("???");
            stringBuffer.append(";");

            stringBuffer.append("\n\t");
            stringBuffer.append(fieldType.getName());
            stringBuffer.append(" playsRole ");
            stringBuffer.append(attributeName);
            stringBuffer.append(";");

            stringBuffer.append("\n}");
        }

    }

    public class DMLCollectionDescriptor
    {

        private CollectionDescriptor collectionDescriptor;

        public DMLCollectionDescriptor(final CollectionDescriptor collectionDescriptor)
        {
            this.collectionDescriptor = collectionDescriptor;
        }

        public void appendDMLDescription(final StringBuffer stringBuffer)
        {
            final String attributeName = collectionDescriptor.getAttributeName();
            final Class fieldType = collectionDescriptor.getPersistentField().getType();

            stringBuffer.append("\n\nrelation ");
            stringBuffer.append("???");
            stringBuffer.append(" {");

            stringBuffer.append("\n\t");
            stringBuffer.append(getClassName());
            stringBuffer.append(" playsRole ");
            stringBuffer.append("???");
            stringBuffer.append(" {\n\t\tnmultiplicity *;\n\t}");

            stringBuffer.append("\n\t");
            stringBuffer.append(fieldType.getName());
            stringBuffer.append(" playsRole ");
            stringBuffer.append(attributeName);
            stringBuffer.append(";");

            stringBuffer.append("\n}");
        }

    }

    public DMLClassDescriptor(final String className, final ClassDescriptor classDescriptor)
    {
        this.classDescriptor = classDescriptor;

        final FieldDescriptor[] fieldDescriptors = classDescriptor.getFieldDescriptions();

        int numberFields = (fieldDescriptors != null) ? fieldDescriptors.length : 0;

        this.attributeDescriptors = new HashMap(numberFields);

        for (int i = 0; i < numberFields; i++)
        {
            addAttributeDescriptor(fieldDescriptors[i]);
        }

        relations = new HashSet();

        final Vector objectReferenceDescriptors = classDescriptor.getObjectReferenceDescriptors();

        this.objectReferenceDescriptors = new HashMap(objectReferenceDescriptors.size());

        for (final Iterator iterator = objectReferenceDescriptors.iterator(); iterator.hasNext();)
        {
            final ObjectReferenceDescriptor objectReferenceDescriptor = (ObjectReferenceDescriptor) iterator
                    .next();
            final DMLRelationDescriptor dmlRelationDescriptor = new DMLRelationDescriptor(classDescriptor, objectReferenceDescriptor);
            addObjectReferenceDescriptor(objectReferenceDescriptor);
            relations.add(dmlRelationDescriptor);
        }

        final Vector collectionDescriptors = classDescriptor.getCollectionDescriptors();

        this.collectionDescriptors = new HashMap(collectionDescriptors.size());

        for (final Iterator iterator = collectionDescriptors.iterator(); iterator.hasNext();)
        {
            final CollectionDescriptor collectionDescriptor = (CollectionDescriptor) iterator.next();
            final DMLRelationDescriptor dmlRelationDescriptor = new DMLRelationDescriptor(classDescriptor, collectionDescriptor);
            addCollectionDescriptor(collectionDescriptor);
            relations.add(dmlRelationDescriptor);
        }
    }

    public void appendDMLDescription(final StringBuffer stringBuffer)
    {
        stringBuffer.append("\n\nclass ");
        stringBuffer.append(getClassName());
        stringBuffer.append(" {");
        for (final Iterator iterator = attributeDescriptors.values().iterator(); iterator.hasNext();)
        {
            final DMLAttributeDescriptor attributeDescriptor = (DMLAttributeDescriptor) iterator.next();
            attributeDescriptor.appendDMLDescription(stringBuffer);
        }
        stringBuffer.append("\n}");

//        for (final Iterator iterator = objectReferenceDescriptors.values().iterator(); iterator
//                .hasNext();)
//        {
//            final DMLObjectReferenceDescriptor dmlObjectReferenceDescriptor = (DMLObjectReferenceDescriptor) iterator
//                    .next();
//            dmlObjectReferenceDescriptor.appendDMLDescription(stringBuffer);
//        }
//
//        for (final Iterator iterator = collectionDescriptors.values().iterator(); iterator.hasNext();)
//        {
//            final DMLCollectionDescriptor dmlCollectionDescriptor = (DMLCollectionDescriptor) iterator
//                    .next();
//            dmlCollectionDescriptor.appendDMLDescription(stringBuffer);
//        }
    }

    protected void addAttributeDescriptor(final FieldDescriptor fieldDescriptor)
    {
        final DMLAttributeDescriptor dmlAttributeDescriptor = new DMLAttributeDescriptor(fieldDescriptor);

        final String attributeName = fieldDescriptor.getAttributeName();

        attributeDescriptors.put(attributeName, dmlAttributeDescriptor);
    }

    protected void addObjectReferenceDescriptor(final ObjectReferenceDescriptor objectReferenceDescriptor)
    {
        final DMLObjectReferenceDescriptor dmlObjectReferenceDescriptor = new DMLObjectReferenceDescriptor(
                objectReferenceDescriptor);

        final String attributeName = objectReferenceDescriptor.getAttributeName();

        this.objectReferenceDescriptors.put(attributeName, dmlObjectReferenceDescriptor);
    }

    public String getClassName()
    {
        return classDescriptor.getClassNameOfObject();
    }

    protected void addCollectionDescriptor(final CollectionDescriptor collectionDescriptor)
    {
        final DMLCollectionDescriptor dmlCollectionDescriptor = new DMLCollectionDescriptor(
                collectionDescriptor);

        final String attributeName = collectionDescriptor.getAttributeName();

        this.collectionDescriptors.put(attributeName, dmlCollectionDescriptor);
    }

    public Map getAttributeDescriptors()
    {
        return attributeDescriptors;
    }
    public Map getCollectionDescriptors()
    {
        return collectionDescriptors;
    }
    public Map getObjectReferenceDescriptors()
    {
        return objectReferenceDescriptors;
    }
    public Set getRelations()
    {
        return relations;
    }
}