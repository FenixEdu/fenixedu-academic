/**
 * 
 */
package net.sourceforge.fenixedu.tools;

import java.io.FileNotFoundException;
import java.lang.reflect.Modifier;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.framework.FenixPersistentField;
import net.sourceforge.fenixedu.stm.OJBFunctionalSetWrapper;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;
import org.apache.ojb.broker.metadata.fieldaccess.PersistentField;

import dml.DmlCompiler;
import dml.DomainClass;
import dml.DomainEntity;
import dml.DomainModel;
import dml.Role;
import dml.Slot;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class OJBMetadataGenerator {

    private static final Map<String, String> java2JdbcConversion = new HashMap<String, String>();
    
    private static final Map<String, String> primitiveJavaType2DataTypeConversor = new HashMap<String, String>();
    
    private static final Set<String> unmappedObjectReferenceAttributesInDML = new TreeSet<String>();

    private static final Set<String> unmappedCollectionReferenceAttributesInDML = new TreeSet<String>();

    private static final Set<String> unmappedObjectReferenceAttributesInOJB = new TreeSet<String>();

    private static final Set<String> unmappedCollectionReferenceAttributesInOJB = new TreeSet<String>();

    private static String classToDebug = null;

    private static Formatter changeTablesCommands = null;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String[] dmlFilesArray = { args[0] };
        if (args.length == 2) {
            classToDebug = args[1];
        }
        




        DomainModel domainModel = DmlCompiler.getDomainModel(dmlFilesArray);
        Map ojbMetadata = MetadataManager.getInstance().getGlobalRepository().getDescriptorTable();

        updateOJBMappingFromDomainModel(ojbMetadata, domainModel);

        printUnmmapedAttributes(unmappedObjectReferenceAttributesInDML,
                "UnmappedObjectReferenceAttributes in DML:");
        printUnmmapedAttributes(unmappedCollectionReferenceAttributesInDML,
                "UnmappedCollectionReferenceAttributes in DML:");
        printUnmmapedAttributes(unmappedObjectReferenceAttributesInOJB,
                "UnmappedObjectReferenceAttributes in OJB:");
        printUnmmapedAttributes(unmappedCollectionReferenceAttributesInOJB,
                "UnmappedCollectionReferenceAttributes in OJB:");

//        if (changeTablesCommands != null) {
//            changeTablesCommands.flush();
//        }

        System.exit(0);

    }



   

    private static void printUnmmapedAttributes(Set<String> unmappedAttributesSet, String title) {
        if (!unmappedAttributesSet.isEmpty()) {
            System.out.println();
            System.out.println(title);
            for (String objectReference : unmappedAttributesSet) {
                System.out.println(objectReference);
            }
        }
    }

    public static void updateOJBMappingFromDomainModel(Map ojbMetadata, DomainModel domainModel)
            throws Exception {

        initJava2JdbcConversionMap();
        
        initPrimitiveJavaType2DataTypeConversorMap();
        
//        for (final Iterator iterator = domainModel.getClasses(); iterator.hasNext();) {
//            final DomainClass domClass = (DomainClass) iterator.next();
//            final Class clazz = Class.forName(domClass.getFullName());
//            
//            if (!Modifier.isAbstract(clazz.getModifiers()) && !domClass.getFullName().equals("net.sourceforge.fenixedu.domain.RootDomainObject")) {
//                final ClassDescriptor classDescriptor = (ClassDescriptor) ojbMetadata.get(domClass
//                        .getFullName());
//                if (classDescriptor != null) {
//                    // add keyRootDomainObject field
//                    addKeyRootDomainObjectField(clazz, classDescriptor);
//                }
//            }
//            
//        }        
        
        for (final Iterator iterator = domainModel.getClasses(); iterator.hasNext();) {
            final DomainClass domClass = (DomainClass) iterator.next();

            final ClassDescriptor classDescriptor = (ClassDescriptor) ojbMetadata.get(domClass
                    .getFullName());

            if (classDescriptor != null) {
                final Class clazz = Class.forName(domClass.getFullName());
                updateFields(classDescriptor, domClass, ojbMetadata, clazz);
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    updateRelations(classDescriptor, domClass, ojbMetadata, clazz);
                }
            }

            if (classToDebug != null && classDescriptor.getClassNameOfObject().contains(classToDebug)) {
                System.out.println(classDescriptor.toXML());
            }
        }
        
    }

    private static void addKeyRootDomainObjectField(final Class clazz,
            final ClassDescriptor classDescriptor) throws FileNotFoundException {

        if (classDescriptor.getFieldDescriptorByName("keyRootDomainObject") == null) {
            int maxFieldID = 0;
            for (FieldDescriptor descriptor : classDescriptor.getFieldDescriptions()) {
                if (descriptor.getColNo() > maxFieldID) {
                    maxFieldID = descriptor.getColNo();
                }
            }
            FieldDescriptor rootDomainObjectFieldDescriptor = new FieldDescriptor(classDescriptor, ++maxFieldID);
            rootDomainObjectFieldDescriptor.setColumnName("KEY_ROOT_DOMAIN_OBJECT");
            rootDomainObjectFieldDescriptor.setColumnType("INTEGER");
            rootDomainObjectFieldDescriptor.setAccess("readwrite");
            PersistentField persistentField = new FenixPersistentField(clazz, "keyRootDomainObject");
            rootDomainObjectFieldDescriptor.setPersistentField(persistentField);
            classDescriptor.addFieldDescriptor(rootDomainObjectFieldDescriptor);

//            if (changeTablesCommands == null) {
//                changeTablesCommands = new Formatter(new File("addRootDomainObjectKeys.sql"));
//            }
//            changeTablesCommands.format(
//                    "alter table %s add column KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1;\n",
//                    classDescriptor.getFullTableName());
        }
    }

    protected static void updateFields(final ClassDescriptor classDescriptor, final DomainClass domClass,
            Map ojbMetadata, Class persistentFieldClass) throws Exception {
        
        DomainEntity domEntity = domClass;
        int fieldID = 1;
        while (domEntity instanceof DomainClass) {
            DomainClass dClass = (DomainClass) domEntity;

            Iterator<Slot> slots = dClass.getSlots();
            while (slots.hasNext()) {
                Slot slot = slots.next();
                String slotName = slot.getName();
                
                FieldDescriptor fieldDescriptor = new FieldDescriptor(classDescriptor, fieldID++);
                fieldDescriptor.setColumnName(StringFormatter.convertToDBStyle(slotName));
//                System.out.println(">> " + slot.getType());
//                System.out.println("\t" + java2JdbcConversion.get(slot.getType()));
                fieldDescriptor.setColumnType(java2JdbcConversion.get(slot.getType()));
                fieldDescriptor.setAccess("readwrite");
                if(primitiveJavaType2DataTypeConversor.get(slot.getType()) != null){
                    fieldDescriptor.setFieldConversionClassName(primitiveJavaType2DataTypeConversor.get(slot.getType()));
                }
                if(slotName.equals("idInternal")){
                    fieldDescriptor.setPrimaryKey(true);
                    fieldDescriptor.setAutoIncrement(true);
                }
                PersistentField persistentField = new FenixPersistentField(persistentFieldClass, slotName);
                fieldDescriptor.setPersistentField(persistentField);
                classDescriptor.addFieldDescriptor(fieldDescriptor);

            }
            
            domEntity = dClass.getSuperclass();
        }
        
    }
    
    protected static void updateRelations(final ClassDescriptor classDescriptor, final DomainClass domClass,
            Map ojbMetadata, Class persistentFieldClass) throws Exception {
        
        DomainEntity domEntity = domClass;
        while (domEntity instanceof DomainClass) {
            DomainClass dClass = (DomainClass) domEntity;
            
            // roles
            Iterator roleSlots = dClass.getRoleSlots();
            while (roleSlots.hasNext()) {
                Role role = (Role) roleSlots.next();
                String roleName = role.getName();

                if (domClass.getFullName().equals("net.sourceforge.fenixedu.domain.RootDomainObject")
                		&& roleName != null && (roleName.equals("rootDomainObject") || roleName.equals("rootDomainObjects"))) {
                	continue;
                }

                if (role.getMultiplicityUpper() == 1) {

                    // reference descriptors
                    if (classDescriptor.getObjectReferenceDescriptorByName(roleName) == null) {

                        String foreignKeyField = "key" + StringUtils.capitalize(roleName);

                        if (findSlotByName(dClass, foreignKeyField) == null) {
                            unmappedObjectReferenceAttributesInDML.add(foreignKeyField + " -> "
                                    + dClass.getName());
                            continue;
                        }

                        if (classDescriptor.getFieldDescriptorByName(foreignKeyField) == null) {
                            Class classToVerify = Class.forName(dClass.getFullName());
                            if (!Modifier.isAbstract(classToVerify.getModifiers())) {
                                unmappedObjectReferenceAttributesInOJB.add(foreignKeyField + " -> "
                                        + dClass.getName());
                                continue;
                            }
                        }

                        generateReferenceDescriptor(classDescriptor, persistentFieldClass, role,
                                roleName, foreignKeyField);

                    }
                } else {

                    // collection descriptors
                    if (classDescriptor.getCollectionDescriptorByName(roleName) == null) {

                        CollectionDescriptor collectionDescriptor = new CollectionDescriptor(
                                classDescriptor);

                        if (role.getOtherRole().getMultiplicityUpper() == 1) {

                            String foreignKeyField = "key"
                                    + StringUtils.capitalize(role.getOtherRole().getName());

                            if (findSlotByName((DomainClass) role.getType(), foreignKeyField) == null) {
                                unmappedCollectionReferenceAttributesInDML.add(foreignKeyField + " | "
                                        + ((DomainClass) role.getType()).getName() + " -> "
                                        + dClass.getName());
                                continue;
                            }

                            ClassDescriptor otherClassDescriptor = (ClassDescriptor) ojbMetadata
                                    .get(((DomainClass) role.getType()).getFullName());
                            
                            if(otherClassDescriptor == null){
                                System.out.println("Ignoring " + ((DomainClass) role.getType()).getFullName());
                                continue;
                            }
                            
                            
//                            if (otherClassDescriptor.getFieldDescriptorByName(foreignKeyField) == null) {
//                                Class classToVerify = Class.forName(otherClassDescriptor
//                                        .getClassNameOfObject());
//                                if (!Modifier.isAbstract(classToVerify.getModifiers())) {
//                                    unmappedCollectionReferenceAttributesInOJB.add(foreignKeyField
//                                            + " -> " + otherClassDescriptor.getClassNameOfObject()
//                                            + " | " + classDescriptor.getClassNameOfObject());
//                                    continue;
//                                }
//
//                            }

                            generateOneToManyCollectionDescriptor(collectionDescriptor, foreignKeyField);

                        } else {
                            generateManyToManyCollectionDescriptor(collectionDescriptor, role);

                        }
                        updateCollectionDescriptorWithCommonSettings(classDescriptor,
                                persistentFieldClass, role, roleName, collectionDescriptor);
                    }
                }
            }

            domEntity = dClass.getSuperclass();
        }
    }

    private static void updateCollectionDescriptorWithCommonSettings(
            final ClassDescriptor classDescriptor, Class persistentFieldClass, Role role,
            String roleName, CollectionDescriptor collectionDescriptor) throws ClassNotFoundException {
        collectionDescriptor.setItemClass(Class.forName(role.getType().getFullName()));
        collectionDescriptor.setPersistentField(persistentFieldClass, roleName);
        collectionDescriptor.setRefresh(false);
        collectionDescriptor.setCollectionClass(OJBFunctionalSetWrapper.class);
        collectionDescriptor.setCascadeRetrieve(false);
        collectionDescriptor.setLazy(false);
        classDescriptor.addCollectionDescriptor(collectionDescriptor);
    }

    private static void generateManyToManyCollectionDescriptor(
            CollectionDescriptor collectionDescriptor, Role role) {

        String indirectionTableName = StringFormatter.splitCamelCaseString(role.getRelation().getName())
                .replace(' ', '_').toUpperCase();
        String fkToItemClass = "KEY_"
                + StringFormatter.splitCamelCaseString(role.getType().getName()).replace(' ', '_')
                        .toUpperCase();
        String fkToThisClass = "KEY_"
                + StringFormatter.splitCamelCaseString(role.getOtherRole().getType().getName()).replace(
                        ' ', '_').toUpperCase();

        if (fkToItemClass.equals(fkToThisClass)) {
            fkToItemClass = fkToItemClass
                    + "_"
                    + StringFormatter.splitCamelCaseString(role.getName()).replace(' ', '_')
                            .toUpperCase();
            fkToThisClass = fkToThisClass
                    + "_"
                    + StringFormatter.splitCamelCaseString(role.getOtherRole().getName()).replace(' ',
                            '_').toUpperCase();
        }

        collectionDescriptor.setIndirectionTable(indirectionTableName);
        collectionDescriptor.addFkToItemClass(fkToItemClass);
        collectionDescriptor.addFkToThisClass(fkToThisClass);
        collectionDescriptor.setCascadingStore(ObjectReferenceDescriptor.CASCADE_NONE);
        collectionDescriptor.setCascadingDelete(ObjectReferenceDescriptor.CASCADE_NONE);
    }

    private static void generateOneToManyCollectionDescriptor(CollectionDescriptor collectionDescriptor,
            String foreignKeyField) {
        collectionDescriptor.setCascadingStore(ObjectReferenceDescriptor.CASCADE_NONE);
        collectionDescriptor.addForeignKeyField(foreignKeyField);
    }

    private static void generateReferenceDescriptor(final ClassDescriptor classDescriptor,
            Class persistentFieldClass, Role role, String roleName, String foreignKeyField)
            throws ClassNotFoundException {
        ObjectReferenceDescriptor referenceDescriptor = new ObjectReferenceDescriptor(classDescriptor);
        referenceDescriptor.setItemClass(Class.forName(role.getType().getFullName()));
        referenceDescriptor.addForeignKeyField(foreignKeyField);
        referenceDescriptor.setPersistentField(persistentFieldClass, roleName);
        referenceDescriptor.setCascadeRetrieve(false);
        referenceDescriptor.setCascadingStore(ObjectReferenceDescriptor.CASCADE_LINK);
        referenceDescriptor.setLazy(false);

        classDescriptor.addObjectReferenceDescriptor(referenceDescriptor);
    }

    private static Slot findSlotByName(DomainClass domainClass, String slotName) {
        DomainClass domainClassIter = domainClass;
        while (domainClassIter != null) {

            for (Iterator<Slot> slotsIter = domainClassIter.getSlots(); slotsIter.hasNext();) {
                Slot slot = (Slot) slotsIter.next();
                if (slot.getName().equals(slotName)) {
                    return slot;
                }
            }

            domainClassIter = (DomainClass) domainClassIter.getSuperclass();
        }

        return null;
    }

    private static void initJava2JdbcConversionMap() {
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.research.result.PatentType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.GradeScale", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences", "LONGVARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.tests.CorrectionAvailability", "INTEGER");
        java2JdbcConversion.put("java.lang.Character", "CHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.research.project.ProjectType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.research.result.PatentStatus", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.ShiftType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.tests.CorrectionFormula", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.branch.BranchType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.LegalRegimenType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.util.LogicOperators", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.PublicationArea", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.DiaSemana", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.QualificationType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.research.project.ProjectParticipation.ProjectParticipationType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.EnrolmentAction", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.transactions.PaymentType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.person.IDDocumentType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.StudentType", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.ProviderRegimeType", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.GraduationType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.tests.TestType", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.ContentType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.MarkType", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.Season", "INTEGER");
//        java2JdbcConversion.put("java.util.Date", "TIME");
//        java2JdbcConversion.put("java.util.Date", "TIMESTAMP");
        java2JdbcConversion.put("java.util.Date", "DATE");
        java2JdbcConversion.put("org.joda.time.YearMonthDay", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.PeriodState", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.DocumentType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.teacher.AdviseType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState", "VARCHAR");
//        java2JdbcConversion.put("java.lang.Double", "INTEGER");
        java2JdbcConversion.put("java.lang.Double", "DOUBLE");
//        java2JdbcConversion.put("java.lang.Double", "VARCHAR");
//        java2JdbcConversion.put("java.lang.Double", "FLOAT");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.ContractType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.DelegateYearType", "INTEGER");
        java2JdbcConversion.put("java.lang.Integer", "INTEGER");
//        java2JdbcConversion.put("java.lang.Integer", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.PublicationType", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.teacher.ServiceExemptionType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.research.event.EventType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.gratuity.ExemptionGratuityType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.EntryPhase", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.sms.SmsDeliveryType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.OldPublicationType", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.RegimenType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.PeriodToApplyRestriction", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.research.result.ProductType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.person.Gender", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.curriculum.EnrollmentState", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.FileItemPermittedGroupType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.OrientationType", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.tools.enrollment.AreaType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.masterDegree.GuideRequester", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.EnrolmentEvaluationState", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.person.MaritalStatus", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.transactions.TransactionType", "VARCHAR");
//        java2JdbcConversion.put("java.lang.Boolean", "INTEGER");
        java2JdbcConversion.put("java.lang.Boolean", "BIT");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.CurricularCourseExecutionScope", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.accessControl.Group", "BLOB");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState", "VARCHAR");
        java2JdbcConversion.put("java.lang.String", "LONGVARCHAR");
//        java2JdbcConversion.put("java.lang.String", "VARCHAR");
//        java2JdbcConversion.put("java.lang.String", "CHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.State", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.ProposalState", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.SituationName", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.MultiLanguageString", "LONGVARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.StudentState", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType", "VARCHAR");
        java2JdbcConversion.put("org.joda.time.Partial", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.SecretaryEnrolmentStudentReason", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.degreeStructure.RegimeType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.WeekDay", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.degree.DegreeType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.ByteArray", "BLOB");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.person.RoleType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.TipoSala", "INTEGER");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.GuideState", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.research.result.FormatType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.research.event.EventParticipation.EventParticipationRole", "VARCHAR");
        java2JdbcConversion.put("org.joda.time.DateTime", "TIMESTAMP");
        java2JdbcConversion.put("org.joda.time.TimeOfDay", "TIME");        
        java2JdbcConversion.put("net.sourceforge.fenixedu.util.HourMinuteSecond", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.MarkSheetType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.MarkSheetState", "VARCHAR");
        java2JdbcConversion.put("java.math.BigDecimal", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation.ProjectEventAssociationRole", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLevel", "VARCHAR");
        java2JdbcConversion.put("org.joda.time.Duration", "BIGINT");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.assiduousness.WorkWeek", "LONGVARCHAR");    
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.assiduousness.util.CardType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.assiduousness.util.DayType", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup", "VARCHAR");
        java2JdbcConversion.put("net.sourceforge.fenixedu.domain.assiduousness.util.CardState", "VARCHAR");
    }
    
    private static void initPrimitiveJavaType2DataTypeConversorMap() {
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.research.result.PatentType", "net.sourceforge.fenixedu.persistenceTier.Conversores.PatentType2SqlPatentTypeConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.GradeScale", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaGradeScale2SqlGradeScaleFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaBibliographicReferences2SqlBibliographicReferencesFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.tests.CorrectionAvailability", "net.sourceforge.fenixedu.persistenceTier.Conversores.CorrectionAvailability2EnumCorrectionAvailabilityFieldConversion");
        primitiveJavaType2DataTypeConversor.put("java.lang.Character", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaCharacter2SqlCharFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaCurricularRuleType2SqlCurricularRuleTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.research.project.ProjectType", "net.sourceforge.fenixedu.persistenceTier.Conversores.ProjectType2SqlProjectTypeConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.research.result.PatentStatus", "net.sourceforge.fenixedu.persistenceTier.Conversores.PatentStatus2SqlPatentStatusConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.ShiftType", "net.sourceforge.fenixedu.persistenceTier.Conversores.TipoAula2EnumTipoAulaFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaMasterDegreeClassification2SQLMasterDegreeClassificationFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.tests.CorrectionFormula", "net.sourceforge.fenixedu.persistenceTier.Conversores.CorrectionFormula2EnumCorrectionFormulaFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum", "net.sourceforge.fenixedu.persistenceTier.Conversores.PartyType2SQLPartyTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.branch.BranchType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaBranchType2SqlBranchTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.LegalRegimenType", "net.sourceforge.fenixedu.persistenceTier.Conversores.LegalRegimenType2SqlLegalRegimenTypeConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.util.LogicOperators", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaLogicOperators2SqlLogicOperatorsFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaFinalDegreeWorkProposalStatus2SqlFinalDegreeWorkProposalStatusFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaCurricularCourseType2SqlCurricularCourseTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.PublicationArea", "net.sourceforge.fenixedu.persistenceTier.Conversores.PublicationArea2SqlPublicationAreaFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.DiaSemana", "net.sourceforge.fenixedu.persistenceTier.Conversores.DiaSemana2EnumDiaSemanaFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.QualificationType", "net.sourceforge.fenixedu.persistenceTier.Conversores.QualificationType2SqlQualificationType");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.research.project.ProjectParticipation.ProjectParticipationType", "net.sourceforge.fenixedu.persistenceTier.Conversores.ProjectParticipationType2SqlProjectParticipationTypeConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.EnrolmentAction", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaEnrollmentLogAction2SqlEnrollmentLogActionFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.transactions.PaymentType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaPaymentType2SqlPaymentTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.person.IDDocumentType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaTipoDocId2SqlTipoDocIdFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaBolonhaDegreeType2SqlBolonhaDegreeTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaCurricularStage2SqlCurricularStageFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.StudentType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaStudentKind2SqlStudentKindFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum", "net.sourceforge.fenixedu.persistenceTier.Conversores.AccountabilityType2SQLAccountabilityTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.ProviderRegimeType", "net.sourceforge.fenixedu.persistenceTier.Conversores.ProviderRegimeTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.GraduationType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaGraduationType2SqlGraduationTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.tests.TestType", "net.sourceforge.fenixedu.persistenceTier.Conversores.TestType2EnumTestTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.ContentType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaContentType2SqlContentTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.MarkType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaMarkType2SqlMarkTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.Season", "net.sourceforge.fenixedu.persistenceTier.Conversores.Season2EnumSeasonFieldConversion");
//        primitiveJavaType2DataTypeConversor.put("java.util.Date", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaDate2SqlDateConversion");
//        primitiveJavaType2DataTypeConversor.put("java.util.Date", "org.apache.ojb.broker.accesslayer.conversions.JavaDate2SqlDateFieldConversion");
//        primitiveJavaType2DataTypeConversor.put("java.util.Date", "net.sourceforge.fenixedu.persistenceTier.Conversores.TimeStamp2DateFieldConversion");
//        primitiveJavaType2DataTypeConversor.put("java.util.Date", "net.sourceforge.fenixedu.persistenceTier.Conversores.Time2DateFieldConversion");
//        primitiveJavaType2DataTypeConversor.put("java.util.Date", "net.sourceforge.fenixedu.persistenceTier.Conversores.Date2TimeFieldConversion");
//        primitiveJavaType2DataTypeConversor.put("java.util.Date", "org.apache.ojb.broker.accesslayer.conversions.FieldConversionDefaultImpl");
        primitiveJavaType2DataTypeConversor.put("org.joda.time.YearMonthDay", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaYearMonthDay2SqlStringFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.PeriodState", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaPeriodState2SqlPeriodState");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.DocumentType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaDocumentType2SqlDocumentTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.teacher.AdviseType", "net.sourceforge.fenixedu.persistenceTier.Conversores.AdviseType2SqlAdviseTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaDegreeCurricularPlanState2SqlDegreeCurricularPlanStateFieldConversion");
//        primitiveJavaType2DataTypeConversor.put("java.lang.Double", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaString2FloatFieldConversion");
//        primitiveJavaType2DataTypeConversor.put("java.lang.Double", "org.apache.ojb.broker.accesslayer.conversions.FieldConversionDefaultImpl");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.ContractType", "net.sourceforge.fenixedu.persistenceTier.Conversores.ContractType2SqlContractTypeConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.DelegateYearType", "net.sourceforge.fenixedu.persistenceTier.Conversores.DelegateYearTypeFieldConversion");
//        primitiveJavaType2DataTypeConversor.put("java.lang.Integer", "org.apache.ojb.broker.accesslayer.conversions.FieldConversionDefaultImpl");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.PublicationType", "net.sourceforge.fenixedu.persistenceTier.Conversores.PublicationTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.teacher.ServiceExemptionType", "net.sourceforge.fenixedu.persistenceTier.Conversores.ServiceExemptionTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.research.event.EventType", "net.sourceforge.fenixedu.persistenceTier.Conversores.ResearchEventType2SqlResearchEventTypeConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.gratuity.ExemptionGratuityType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaExemptionGratuityType2SqlExemptionGratuityTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.EntryPhase", "net.sourceforge.fenixedu.persistenceTier.Conversores.EntryPhase2SqlEntryPhaseFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.sms.SmsDeliveryType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaSmsDeliveryType2SqlSmsDeliveryTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.OldPublicationType", "net.sourceforge.fenixedu.persistenceTier.Conversores.OldPublicationTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.RegimenType", "net.sourceforge.fenixedu.persistenceTier.Conversores.RegimenType2SqlRegimenTypeConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.PeriodToApplyRestriction", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaPeriodToApplyRestriction2SqlPeriodToApplyRestrictionFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.research.result.ProductType", "net.sourceforge.fenixedu.persistenceTier.Conversores.ProductType2SqlProductTypeConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.person.Gender", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaSex2SqlSexFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaStudentCurricularPlanState2SqlStudentCurricularPlanStateFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.curriculum.EnrollmentState", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaEnrollmentState2SqlEnrollmentStateFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.FileItemPermittedGroupType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaFileItemPermittedGroupType2SqlFileItemPermittedGroupTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.OrientationType", "net.sourceforge.fenixedu.persistenceTier.Conversores.OrientationTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.tools.enrollment.AreaType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaAreaType2SqlAreaTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.masterDegree.GuideRequester", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaGuideRequester2SqlGuideRequesterFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.EnrolmentEvaluationState", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaEnrolmentEvaluationState2SqlEnrolmentEvaluationStateFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.person.MaritalStatus", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaMaritalStatus2SqlMaritalStatusFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.transactions.TransactionType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaTransactionType2SqlTransactionTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("java.lang.Boolean", "org.apache.ojb.broker.accesslayer.conversions.Boolean2IntFieldConversion");
//        primitiveJavaType2DataTypeConversor.put("java.lang.Boolean", "net.sourceforge.fenixedu.persistenceTier.Conversores.Int2BooleanFieldConversion");
//        primitiveJavaType2DataTypeConversor.put("java.lang.Boolean", "org.apache.ojb.broker.accesslayer.conversions.FieldConversionDefaultImpl");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.CurricularCourseExecutionScope", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaCurricularCourseExecutionScope2SqlCurricularCourseExecutionScopeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaEnrolmentEvaluationType2SqlEnrolmentEvaluationTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.accessControl.Group", "org.apache.ojb.broker.accesslayer.conversions.Object2ByteArrFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaReimbursementGuideState2SqlReimbursementGuideStateFieldConversion");
//        primitiveJavaType2DataTypeConversor.put("java.lang.String", "org.apache.ojb.broker.accesslayer.conversions.FieldConversionDefaultImpl");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaEnrollmentCondition2SqlEnrollmentConditionFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.State", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaState2SQLStateFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.ProposalState", "net.sourceforge.fenixedu.persistenceTier.Conversores.ProposalState2EnumProposalStateFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.SituationName", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaSituationName2SqlSituationNameFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaSibsPaymentStatus2SQLSibsPaymentStatusFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.MultiLanguageString", "net.sourceforge.fenixedu.persistenceTier.Conversores.MultiLanguageString2SqlMultiLanguageStringConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaSibsPaymentType2SQLSibsPaymentTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.StudentState", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaStudentState2SqlStudentStateFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType", "net.sourceforge.fenixedu.persistenceTier.Conversores.FunctionType2SqlFunctionTypeConversion");
        primitiveJavaType2DataTypeConversor.put("org.joda.time.Partial", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaYearMonth2SqlStringFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.SecretaryEnrolmentStudentReason", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaSecretaryEnrolmentStudentReason2sqlSecretaryEnrolmentStudentReasonFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.degreeStructure.RegimeType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaRegimeType2SqlRegimeTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice", "net.sourceforge.fenixedu.persistenceTier.Conversores.StudentPersonalDataAuthorizationChoice2sqlStudentPersonalDataAuthorizationChoiceFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaEnrolmentGroupPolicyType2SqlEnrolmentGroupPolicyTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.WeekDay", "net.sourceforge.fenixedu.persistenceTier.Conversores.WeekDay2SqlWeekDayFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaCurricularPeriodType2SqlCurricularPeriodTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.degree.DegreeType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaTipoCurso2SqlTipoCursoFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.ByteArray", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaByteArray2SqlByteArrayFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.person.RoleType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaRoleType2SqlRoleTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.TipoSala", "net.sourceforge.fenixedu.persistenceTier.Conversores.TipoSala2EnumTipoSalaFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.GuideState", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaGuideSituation2SqlGuideSituationFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.research.result.FormatType", "net.sourceforge.fenixedu.persistenceTier.Conversores.FormatType2SqlFormatTypeConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.research.event.EventParticipation.EventParticipationRole", "net.sourceforge.fenixedu.persistenceTier.Conversores.EventParticipationRole2SqlEventParticipationRoleConversion");
        primitiveJavaType2DataTypeConversor.put("org.joda.time.DateTime", "net.sourceforge.fenixedu.persistenceTier.Conversores.TimeStamp2DateTimeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.util.HourMinuteSecond", "net.sourceforge.fenixedu.persistenceTier.Conversores.HourMinuteSecond2TimeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaSpecialization2SqlSpecializationFieldConversion");        
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.MarkSheetState", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaMarkSheetState2SqlMarkSheetStateFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.MarkSheetType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaMarkSheetType2SqlMarkSheetTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType", "net.sourceforge.fenixedu.persistenceTier.Conversores.JustificationType2SqlVarcharConverter");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState", "net.sourceforge.fenixedu.persistenceTier.Conversores.AssiduousnessStatusState2SqlVarcharConverter");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation.ProjectEventAssociationRole", "net.sourceforge.fenixedu.persistenceTier.Conversores.ProjectParticipationType2SqlProjectParticipationTypeConversion");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLevel", "net.sourceforge.fenixedu.persistenceTier.Conversores.JavaCompetenceCourseLevelType2SqlCompetenceCourseLevelTypeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("org.joda.time.TimeOfDay", "net.sourceforge.fenixedu.persistenceTier.Conversores.TimeOfDay2TimeFieldConversion");
        primitiveJavaType2DataTypeConversor.put("org.joda.time.Duration", "net.sourceforge.fenixedu.persistenceTier.Conversores.Duration2SqlBigIntConverter");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.assiduousness.WorkWeek", "net.sourceforge.fenixedu.persistenceTier.Conversores.WorkWeek2SqlVarcharConverter");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.assiduousness.util.CardType", "net.sourceforge.fenixedu.persistenceTier.Conversores.CardType2SqlVarcharConverter");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.assiduousness.util.DayType", "net.sourceforge.fenixedu.persistenceTier.Conversores.DayType2SqlVarcharConverter");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup", "net.sourceforge.fenixedu.persistenceTier.Conversores.JustificationGroup2SqlVarcharConverter");
        primitiveJavaType2DataTypeConversor.put("net.sourceforge.fenixedu.domain.assiduousness.util.CardState", "net.sourceforge.fenixedu.persistenceTier.Conversores.CardState2SqlVarcharConverter");
    }

    
}
