/*
 * Created on Mar 6, 2005
 *
 */
package net.sourceforge.fenixedu.tools;

import java.util.Iterator;
import java.util.Vector;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;

/**
 * @author Luis Cruz
 * 
 */
public class DMLRelationDescriptor
{

    final private ClassDescriptor classDescriptor1;

    final private ObjectReferenceDescriptor objectReferenceDescriptor1;

    final private CollectionDescriptor collectionDescriptor1;

    final private ClassDescriptor classDescriptor2;

    private ObjectReferenceDescriptor objectReferenceDescriptor2 = null;

    private CollectionDescriptor collectionDescriptor2 = null;

    private String key = null;

    public DMLRelationDescriptor(final ClassDescriptor classDescriptor,
            final ObjectReferenceDescriptor objectReferenceDescriptor)
    {
        super();
        this.classDescriptor1 = classDescriptor;
        this.objectReferenceDescriptor1 = objectReferenceDescriptor;
        this.collectionDescriptor1 = null;

        this.classDescriptor2 = getClassDescriptor(objectReferenceDescriptor.getItemClass());

        key = generateKey();
    }

    public DMLRelationDescriptor(final ClassDescriptor classDescriptor,
            final CollectionDescriptor collectionDescriptor)
    {
        super();
        this.classDescriptor1 = classDescriptor;
        this.objectReferenceDescriptor1 = null;
        this.collectionDescriptor1 = collectionDescriptor;

        this.classDescriptor2 = getClassDescriptor(collectionDescriptor.getItemClass());

        key = generateKey();
    }

    protected String generateKey()
    {
        if (objectReferenceDescriptor1 != null)
        {
            getKeyToOtherClass(classDescriptor1, objectReferenceDescriptor1);
        } else if (collectionDescriptor1 != null)
        {
            getKeyToOtherClass(classDescriptor1, collectionDescriptor1);
        } else
        {
            throw new RuntimeException("Class was not properly initialized.");
        }

        if (objectReferenceDescriptor2 != null)
        {
            getKeyToOtherClass(classDescriptor2, objectReferenceDescriptor2);
        } else if (collectionDescriptor2 != null)
        {
            getKeyToOtherClass(classDescriptor2, collectionDescriptor2);
        } else
        {
            //throw new RuntimeException("Class was not properly initialized.");
        }

        if ((objectReferenceDescriptor1 != null || collectionDescriptor1 != null)
                && (objectReferenceDescriptor2 != null || collectionDescriptor2 != null)) {
            final StringBuffer stringBuffer = new StringBuffer();
            if (classDescriptor1.getClassNameOfObject().compareTo(classDescriptor2.getClassNameOfObject()) < 0) {
                stringBuffer.append(classDescriptor1.getClassNameOfObject());
                stringBuffer.append(".");
                if (objectReferenceDescriptor1 != null) {
                    stringBuffer.append(objectReferenceDescriptor1.getAttributeName());
                } else if (collectionDescriptor1 != null) {
                    stringBuffer.append(collectionDescriptor1.getAttributeName());
                }
                stringBuffer.append(":");
                stringBuffer.append(classDescriptor2.getClassNameOfObject());
                stringBuffer.append(".");
                if (objectReferenceDescriptor2 != null) {
                    stringBuffer.append(objectReferenceDescriptor2.getAttributeName());
                } else if (collectionDescriptor2 != null) {
                    stringBuffer.append(collectionDescriptor2.getAttributeName());
                }
            } else {
                stringBuffer.append(classDescriptor2.getClassNameOfObject());
                stringBuffer.append(".");
                if (objectReferenceDescriptor2 != null) {
                    stringBuffer.append(objectReferenceDescriptor2.getAttributeName());
                } else if (collectionDescriptor2 != null) {
                    stringBuffer.append(collectionDescriptor2.getAttributeName());
                }
                stringBuffer.append(":");
                stringBuffer.append(classDescriptor1.getClassNameOfObject());
                stringBuffer.append(".");
                if (objectReferenceDescriptor1 != null) {
                    stringBuffer.append(objectReferenceDescriptor1.getAttributeName());
                } else if (collectionDescriptor1 != null) {
                    stringBuffer.append(collectionDescriptor1.getAttributeName());
                }
            }
            return stringBuffer.toString();
        }

        return null;
    }

    protected String getKeyToOtherClass(final ClassDescriptor classDescriptor,
            final ObjectReferenceDescriptor objectReferenceDescriptor)
    {
        final Vector vector = objectReferenceDescriptor.getForeignKeyFields();
        for (final Iterator iterator = vector.iterator(); iterator.hasNext();)
        {
            final Object foreignKeyField = iterator.next();
            final String columnName;
            if (foreignKeyField instanceof String)
            {
                columnName = (String) foreignKeyField;
            } else if (foreignKeyField instanceof Integer)
            {
                final Integer columnIndex = (Integer) foreignKeyField;
                final FieldDescriptor fieldDescriptor = classDescriptor
                        .getFieldDescriptorByIndex(columnIndex.intValue());
                columnName = fieldDescriptor.getColumnName();
            } else
            {
                throw new RuntimeException("Unsupported foreign key field type: "
                        + foreignKeyField.getClass().getName());
            }

            final CollectionDescriptor collectionDescriptor = findInverseCollectionDescriptor(objectReferenceDescriptor, foreignKeyField);
            if (collectionDescriptor != null) {
                collectionDescriptor2 = collectionDescriptor;
            } else {
                System.out.println("No inverse reference found for: "
                        + classDescriptor1.getFullTableName()
                        + "." + objectReferenceDescriptor.getAttributeName()
                        + " to " + classDescriptor2.getFullTableName());
            }

            return columnName;
        }
        throw new RuntimeException("No foreignKeyField for objectReferenceDescriptor");
    }

    protected CollectionDescriptor findInverseCollectionDescriptor(final ObjectReferenceDescriptor objectReferenceDescriptor, final Object foreignKeyField)
    {
        //System.out.println(classDescriptor1.getFullTableName() + "." + objectReferenceDescriptor.getAttributeName());
        final Vector collectionDescriptors = classDescriptor2.getCollectionDescriptors();
        //System.out.println("   collectionDescriptors.size= " + collectionDescriptors.size());
        for (final Iterator iterator = collectionDescriptors.iterator(); iterator.hasNext(); ) {
            final CollectionDescriptor collectionDescriptor = (CollectionDescriptor) iterator.next();
            //System.out.println("   collectionDescriptors.getAttributeName()= " + collectionDescriptor.getAttributeName());
            Vector foreignKeyFields = collectionDescriptor.getForeignKeyFields();
            for (final Iterator fkIiterator = foreignKeyFields.iterator(); fkIiterator.hasNext(); ) {
                Object fk = fkIiterator.next();
                if (foreignKeyField.equals(fk) && collectionDescriptor.getItemClassName().equals(objectReferenceDescriptor.getClassDescriptor().getClassOfObject())) {
                    return collectionDescriptor;
                }
            }
        }

        if (foreignKeyField instanceof String) {
            return classDescriptor2.getCollectionDescriptorByName((String) foreignKeyField);
        } else if (foreignKeyField instanceof Integer) {
            throw new RuntimeException("Unwaranted usage of integer as foreign key field: " + foreignKeyField); 
        } else {
            throw new RuntimeException("Unsupported foreign key type.");
        }
    }

    protected String getKeyToOtherClass(final ClassDescriptor classDescriptor,
            final CollectionDescriptor collectionDescriptor)
    {
        if (collectionDescriptor.getIndirectionTable() != null)
        {
            String[] foreignKeysToItemClass = collectionDescriptor.getFksToItemClass();
            if (foreignKeysToItemClass.length == 1)
            {
                return foreignKeysToItemClass[0];
            } else if (foreignKeysToItemClass.length == 0)
            {
                return null;
            } else
            {
                throw new RuntimeException("Multiple foreign key fields to item class: ");
            }
        }
        return null;
    }

    protected ClassDescriptor getClassDescriptor(Class klass)
    {
        final MetadataManager metadataManager = MetadataManager.getInstance();
        final DescriptorRepository descriptorRepository = metadataManager.getGlobalRepository();
        return descriptorRepository.getDescriptorFor(klass);
    }

    public String getKey()
    {
        return key;
    }

    public void appendDMLDescription(final StringBuffer stringBuffer)
    {
        stringBuffer.append("\n\nrelation ");
        stringBuffer.append("???");
        stringBuffer.append(" {");

        stringBuffer.append("\n\t");
        stringBuffer.append(classDescriptor1.getClassNameOfObject());
        stringBuffer.append(" playsRole ");
        if (objectReferenceDescriptor2 != null) {
            stringBuffer.append(objectReferenceDescriptor2.getAttributeName());
            stringBuffer.append(";");
        } else if (collectionDescriptor2 != null) {
            stringBuffer.append(collectionDescriptor2.getAttributeName());
            stringBuffer.append(" {\n\t\tmultiplicity *;\n\t}");
        } else {
            stringBuffer.append("???");
            stringBuffer.append(" {\n\t\tmultiplicity ???;\n\t}");
        }

        stringBuffer.append("\n\t");
        stringBuffer.append(classDescriptor2.getClassNameOfObject());
        stringBuffer.append(" playsRole ");
        if (objectReferenceDescriptor1 != null) {
            stringBuffer.append(objectReferenceDescriptor1.getAttributeName());
            stringBuffer.append(";");
        } else if (collectionDescriptor1 != null) {
            stringBuffer.append(collectionDescriptor1.getAttributeName());
            stringBuffer.append(" {\n\t\tmultiplicity *;\n\t}");
        } else {
            stringBuffer.append("???");
            stringBuffer.append(" {\n\t\tmultiplicity ???;\n\t}");
        }

        stringBuffer.append("\n}");
    }

}