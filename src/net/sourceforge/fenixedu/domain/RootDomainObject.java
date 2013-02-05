package net.sourceforge.fenixedu.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.Transaction;

public class RootDomainObject extends RootDomainObject_Base {

    private volatile static RootDomainObject instance = null;

    // private interface DomainObjectReader {
    // public DomainObject readDomainObjectByOID();
    // }

    public static synchronized void init() {
        if (instance == null) {
            Transaction.withTransaction(new jvstm.TransactionalCommand() {
                @Override
                public void doIt() {
                    instance = FenixFramework.getRoot();
                    instance.initAccessClosures();
                }
            });
        }
    }

    public static RootDomainObject getInstance() {
        if (instance == null) {
            init();
        }
        return instance;
    }

    public RootDomainObject() {
        checkIfIsSingleton();
    }

    private void checkIfIsSingleton() {
        if (FenixFramework.getRoot() != null && FenixFramework.getRoot() != this) {
            throw new Error("There can only be one! (instance of MyOrg)");
        }
    }

    public static void initTests() {
        instance = new RootDomainObject();
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
        return this;
    }

    private interface DomainObjectReader {
        public DomainObject readDomainObjectByOID(final Integer idInternal);

        public java.util.Set readAllDomainObjects();
    }

    private static final java.util.Map<String, DomainObjectReader> closureAccessMap =
            new java.util.HashMap<String, DomainObjectReader>();

    @Deprecated
    public static DomainObject readDomainObjectByOID(final Class domainClass, final Integer idInternal) {
        if (domainClass != null) {
            final DomainObjectReader domainObjectReader = closureAccessMap.get(domainClass.getName());
            if (domainObjectReader != null) {
                return domainObjectReader.readDomainObjectByOID(idInternal);
            } else if (domainClass != Object.class && domainClass != DomainObject.class) {
                return readDomainObjectByOID(domainClass.getSuperclass(), idInternal);
            }
        }
        return null;
    }

    @Deprecated
    public static java.util.Set readAllDomainObjects(final Class domainClass) {
        final java.util.Set domainObjects = readAllDomainObjectsAux(domainClass);
        final java.util.Set resultSet = new java.util.HashSet();
        if (domainObjects != null) {
            for (final Object object : domainObjects) {
                if (domainClass.isInstance(object)) {
                    resultSet.add(object);
                }
            }
        }
        return resultSet;
    }

    @Deprecated
    public static java.util.Set readAllDomainObjectsAux(final Class domainClass) {
        if (domainClass != null) {
            final DomainObjectReader domainObjectReader = closureAccessMap.get(domainClass.getName());
            if (domainObjectReader != null) {
                return domainObjectReader.readAllDomainObjects();
            } else if (domainClass != Object.class && domainClass != DomainObject.class) {
                return readAllDomainObjectsAux(domainClass.getSuperclass());
            }
        }
        return null;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.system.CronScriptState readCronScriptStateByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.system.CronScriptState domainObject =
                (net.sourceforge.fenixedu.domain.system.CronScriptState) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.system.CronScriptState.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.resource.ResourceResponsibility readResourceResponsibilityByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.resource.ResourceResponsibility domainObject =
                (net.sourceforge.fenixedu.domain.resource.ResourceResponsibility) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.resource.ResourceResponsibility.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Lesson readLessonByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Lesson domainObject =
                (net.sourceforge.fenixedu.domain.Lesson) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Lesson.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.grant.contract.GrantContract readGrantContractByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.grant.contract.GrantContract domainObject =
                (net.sourceforge.fenixedu.domain.grant.contract.GrantContract) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.grant.contract.GrantContract.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.EquivalencePlanEntry readEquivalencePlanEntryByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.EquivalencePlanEntry domainObject =
                (net.sourceforge.fenixedu.domain.EquivalencePlanEntry) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.EquivalencePlanEntry.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.accounting.Exemption readExemptionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.accounting.Exemption domainObject =
                (net.sourceforge.fenixedu.domain.accounting.Exemption) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.accounting.Exemption.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod readParkingRequestPeriodByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod domainObject =
                (net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation readCycleCourseGroupInformationByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation domainObject =
                (net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.research.result.ResultParticipation readResultParticipationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.result.ResultParticipation domainObject =
                (net.sourceforge.fenixedu.domain.research.result.ResultParticipation) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.research.result.ResultParticipation.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Role readRoleByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Role domainObject =
                (net.sourceforge.fenixedu.domain.Role) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Role.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.BibliographicReference readBibliographicReferenceByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.BibliographicReference domainObject =
                (net.sourceforge.fenixedu.domain.BibliographicReference) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.BibliographicReference.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.StudentGroup readStudentGroupByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.StudentGroup domainObject =
                (net.sourceforge.fenixedu.domain.StudentGroup) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.StudentGroup.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.EnrolmentPeriod readEnrolmentPeriodByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.EnrolmentPeriod domainObject =
                (net.sourceforge.fenixedu.domain.EnrolmentPeriod) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.EnrolmentPeriod.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.resource.Resource readResourceByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.resource.Resource domainObject =
                (net.sourceforge.fenixedu.domain.resource.Resource) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.resource.Resource.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.NonAffiliatedTeacher readNonAffiliatedTeacherByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.NonAffiliatedTeacher domainObject =
                (net.sourceforge.fenixedu.domain.NonAffiliatedTeacher) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.NonAffiliatedTeacher.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.precedences.Precedence readPrecedenceByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.precedences.Precedence domainObject =
                (net.sourceforge.fenixedu.domain.precedences.Precedence) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.precedences.Precedence.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.transactions.Transaction readTransactionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.transactions.Transaction domainObject =
                (net.sourceforge.fenixedu.domain.transactions.Transaction) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.transactions.Transaction.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity readGrantPaymentEntityByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity domainObject =
                (net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.util.email.Message readMessageByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.util.email.Message domainObject =
                (net.sourceforge.fenixedu.domain.util.email.Message) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.util.email.Message.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice readAdministrativeOfficeByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice domainObject =
                (net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup readExpectationEvaluationGroupByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup domainObject =
                (net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.support.GlossaryEntry readGlossaryEntryByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.support.GlossaryEntry domainObject =
                (net.sourceforge.fenixedu.domain.support.GlossaryEntry) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.support.GlossaryEntry.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Coordinator readCoordinatorByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Coordinator domainObject =
                (net.sourceforge.fenixedu.domain.Coordinator) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Coordinator.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult readStudentInquiriesTeachingResultByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult domainObject =
                (net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.ExternalCurricularCourse readExternalCurricularCourseByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.ExternalCurricularCourse domainObject =
                (net.sourceforge.fenixedu.domain.ExternalCurricularCourse) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.ExternalCurricularCourse.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.research.project.ProjectParticipation readProjectParticipationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.project.ProjectParticipation domainObject =
                (net.sourceforge.fenixedu.domain.research.project.ProjectParticipation) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.research.project.ProjectParticipation.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes readOldInquiriesTeachersResByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes domainObject =
                (net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.ExecutionSemester readExecutionSemesterByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.ExecutionSemester domainObject =
                (net.sourceforge.fenixedu.domain.ExecutionSemester) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.ExecutionSemester.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.ProjectSubmission readProjectSubmissionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.ProjectSubmission domainObject =
                (net.sourceforge.fenixedu.domain.ProjectSubmission) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.ProjectSubmission.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime readServiceProviderRegimeByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime domainObject =
                (net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.research.activity.EventEdition readEventEditionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.activity.EventEdition domainObject =
                (net.sourceforge.fenixedu.domain.research.activity.EventEdition) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.research.activity.EventEdition.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.CurricularCourseEquivalence readCurricularCourseEquivalenceByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.CurricularCourseEquivalence domainObject =
                (net.sourceforge.fenixedu.domain.CurricularCourseEquivalence) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.CurricularCourseEquivalence.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.util.email.Sender readSenderByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.util.email.Sender domainObject =
                (net.sourceforge.fenixedu.domain.util.email.Sender) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.util.email.Sender.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.onlineTests.Test readTestByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.onlineTests.Test domainObject =
                (net.sourceforge.fenixedu.domain.onlineTests.Test) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.onlineTests.Test.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacher.ExternalActivity readExternalActivityByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacher.ExternalActivity domainObject =
                (net.sourceforge.fenixedu.domain.teacher.ExternalActivity) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.teacher.ExternalActivity.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship readTSDProfessorshipByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship domainObject =
                (net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.accounting.CreditNote readCreditNoteByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.accounting.CreditNote domainObject =
                (net.sourceforge.fenixedu.domain.accounting.CreditNote) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.accounting.CreditNote.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.candidacy.Candidacy readCandidacyByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.candidacy.Candidacy domainObject =
                (net.sourceforge.fenixedu.domain.candidacy.Candidacy) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.candidacy.Candidacy.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacher.PublicationsNumber readPublicationsNumberByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacher.PublicationsNumber domainObject =
                (net.sourceforge.fenixedu.domain.teacher.PublicationsNumber) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.teacher.PublicationsNumber.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide readReimbursementGuideByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide domainObject =
                (net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Holiday readHolidayByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Holiday domainObject =
                (net.sourceforge.fenixedu.domain.Holiday) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Holiday.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.CompetenceCourse readCompetenceCourseByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.CompetenceCourse domainObject =
                (net.sourceforge.fenixedu.domain.CompetenceCourse) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.CompetenceCourse.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType readAccountabilityTypeByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType domainObject =
                (net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.precedences.Restriction readRestrictionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.precedences.Restriction domainObject =
                (net.sourceforge.fenixedu.domain.precedences.Restriction) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.precedences.Restriction.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.onlineTests.DistributedTest readDistributedTestByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.onlineTests.DistributedTest domainObject =
                (net.sourceforge.fenixedu.domain.onlineTests.DistributedTest) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.onlineTests.DistributedTest.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal readGroupProposalByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal domainObject =
                (net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.gesdis.CourseReport readCourseReportByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.gesdis.CourseReport domainObject =
                (net.sourceforge.fenixedu.domain.gesdis.CourseReport) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.gesdis.CourseReport.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry readTeachingInquiryByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry domainObject =
                (net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase readTSDProcessPhaseByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase domainObject =
                (net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Seminaries.Seminary readSeminaryByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Seminaries.Seminary domainObject =
                (net.sourceforge.fenixedu.domain.Seminaries.Seminary) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Seminaries.Seminary.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacher.Orientation readOrientationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacher.Orientation domainObject =
                (net.sourceforge.fenixedu.domain.teacher.Orientation) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.teacher.Orientation.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.CurricularCourseScope readCurricularCourseScopeByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.CurricularCourseScope domainObject =
                (net.sourceforge.fenixedu.domain.CurricularCourseScope) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.CurricularCourseScope.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion readMasterDegreeThesisDataVersionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion domainObject =
                (net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.EquivalencePlan readEquivalencePlanByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.EquivalencePlan domainObject =
                (net.sourceforge.fenixedu.domain.EquivalencePlan) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.EquivalencePlan.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.GratuityValues readGratuityValuesByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.GratuityValues domainObject =
                (net.sourceforge.fenixedu.domain.GratuityValues) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.GratuityValues.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.degreeStructure.Context readContextByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.degreeStructure.Context domainObject =
                (net.sourceforge.fenixedu.domain.degreeStructure.Context) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.degreeStructure.Context.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.library.LibraryCard readLibraryCardByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.library.LibraryCard domainObject =
                (net.sourceforge.fenixedu.domain.library.LibraryCard) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.library.LibraryCard.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.organizationalStructure.Accountability readAccountabilityByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.organizationalStructure.Accountability domainObject =
                (net.sourceforge.fenixedu.domain.organizationalStructure.Accountability) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.organizationalStructure.Accountability.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy readSeminaryCandidacyByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy domainObject =
                (net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.LoginAlias readLoginAliasByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.LoginAlias domainObject =
                (net.sourceforge.fenixedu.domain.LoginAlias) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.LoginAlias.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.User readUserByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.User domainObject =
                (net.sourceforge.fenixedu.domain.User) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.User.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.research.activity.ResearchEvent readResearchEventByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.activity.ResearchEvent domainObject =
                (net.sourceforge.fenixedu.domain.research.activity.ResearchEvent) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.research.activity.ResearchEvent.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.MarkSheet readMarkSheetByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.MarkSheet domainObject =
                (net.sourceforge.fenixedu.domain.MarkSheet) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.MarkSheet.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Professorship readProfessorshipByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Professorship domainObject =
                (net.sourceforge.fenixedu.domain.Professorship) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Professorship.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationProblem readCardGenerationProblemByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationProblem domainObject =
                (net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationProblem) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationProblem.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper readEnrolmentWrapperByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper domainObject =
                (net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse readNotNeedToEnrollInCurricularCourseByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse domainObject =
                (net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse.class
                                        .getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.research.activity.ScientificJournal readScientificJournalByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.activity.ScientificJournal domainObject =
                (net.sourceforge.fenixedu.domain.research.activity.ScientificJournal) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.research.activity.ScientificJournal.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.onlineTests.Metadata readMetadataByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.onlineTests.Metadata domainObject =
                (net.sourceforge.fenixedu.domain.onlineTests.Metadata) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.onlineTests.Metadata.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.OccupationPeriod readOccupationPeriodByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.OccupationPeriod domainObject =
                (net.sourceforge.fenixedu.domain.OccupationPeriod) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.OccupationPeriod.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Department readDepartmentByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Department domainObject =
                (net.sourceforge.fenixedu.domain.Department) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Department.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.LoginPeriod readLoginPeriodByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.LoginPeriod domainObject =
                (net.sourceforge.fenixedu.domain.LoginPeriod) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.LoginPeriod.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.SchoolClass readSchoolClassByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.SchoolClass domainObject =
                (net.sourceforge.fenixedu.domain.SchoolClass) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.SchoolClass.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.organizationalStructure.Party readPartyByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.organizationalStructure.Party domainObject =
                (net.sourceforge.fenixedu.domain.organizationalStructure.Party) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.organizationalStructure.Party.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.support.FAQSection readFAQSectionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.support.FAQSection domainObject =
                (net.sourceforge.fenixedu.domain.support.FAQSection) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.support.FAQSection.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Evaluation readEvaluationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Evaluation domainObject =
                (net.sourceforge.fenixedu.domain.Evaluation) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Evaluation.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.parking.ParkingParty readParkingPartyByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.parking.ParkingParty domainObject =
                (net.sourceforge.fenixedu.domain.parking.ParkingParty) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.parking.ParkingParty.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Guide readGuideByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Guide domainObject =
                (net.sourceforge.fenixedu.domain.Guide) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Guide.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad readCompetenceCourseLoadByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad domainObject =
                (net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.ExecutionCourse readExecutionCourseByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.ExecutionCourse domainObject =
                (net.sourceforge.fenixedu.domain.ExecutionCourse) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.ExecutionCourse.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.tests.NewTestGroup readNewTestGroupByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.tests.NewTestGroup domainObject =
                (net.sourceforge.fenixedu.domain.tests.NewTestGroup) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.tests.NewTestGroup.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry readCardGenerationEntryByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry domainObject =
                (net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Alumni readAlumniByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Alumni domainObject =
                (net.sourceforge.fenixedu.domain.Alumni) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Alumni.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.curricularRules.CurricularRule readCurricularRuleByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.curricularRules.CurricularRule domainObject =
                (net.sourceforge.fenixedu.domain.curricularRules.CurricularRule) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.curricularRules.CurricularRule.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry readYearDelegateCourseInquiryByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry domainObject =
                (net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.EducationArea readEducationAreaByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.EducationArea domainObject =
                (net.sourceforge.fenixedu.domain.EducationArea) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.EducationArea.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry readAcademicCalendarEntryByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry domainObject =
                (net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher readTSDTeacherByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher domainObject =
                (net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Branch readBranchByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Branch domainObject =
                (net.sourceforge.fenixedu.domain.Branch) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Branch.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry readReimbursementGuideEntryByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry domainObject =
                (net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.research.project.Project readProjectByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.project.Project domainObject =
                (net.sourceforge.fenixedu.domain.research.project.Project) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.research.project.Project.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.accounting.PostingRule readPostingRuleByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.accounting.PostingRule domainObject =
                (net.sourceforge.fenixedu.domain.accounting.PostingRule) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.accounting.PostingRule.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.contents.Content readContentByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.contents.Content domainObject =
                (net.sourceforge.fenixedu.domain.contents.Content) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.contents.Content.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution readTeacherServiceDistributionByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution domainObject =
                (net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution.class
                                        .getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup readFinalDegreeWorkGroupByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup domainObject =
                (net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse readTSDCourseByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse domainObject =
                (net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.support.FAQEntry readFAQEntryByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.support.FAQEntry domainObject =
                (net.sourceforge.fenixedu.domain.support.FAQEntry) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.support.FAQEntry.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Shift readShiftByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Shift domainObject =
                (net.sourceforge.fenixedu.domain.Shift) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Shift.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.accounting.Entry readEntryByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.accounting.Entry domainObject =
                (net.sourceforge.fenixedu.domain.accounting.Entry) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.accounting.Entry.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Photograph readPhotographByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Photograph domainObject =
                (net.sourceforge.fenixedu.domain.Photograph) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Photograph.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.LessonPlanning readLessonPlanningByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.LessonPlanning domainObject =
                (net.sourceforge.fenixedu.domain.LessonPlanning) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.LessonPlanning.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch readCardGenerationBatchByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch domainObject =
                (net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest readAlumniIdentityCheckRequestByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest domainObject =
                (net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.accounting.Receipt readReceiptByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.accounting.Receipt domainObject =
                (net.sourceforge.fenixedu.domain.accounting.Receipt) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.accounting.Receipt.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacher.TeacherServiceItem readTeacherServiceItemByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacher.TeacherServiceItem domainObject =
                (net.sourceforge.fenixedu.domain.teacher.TeacherServiceItem) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.teacher.TeacherServiceItem.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.tests.NewPermissionUnit readNewPermissionUnitByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.tests.NewPermissionUnit domainObject =
                (net.sourceforge.fenixedu.domain.tests.NewPermissionUnit) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.tests.NewPermissionUnit.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.EnrolmentEvaluation readEnrolmentEvaluationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.EnrolmentEvaluation domainObject =
                (net.sourceforge.fenixedu.domain.EnrolmentEvaluation) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.EnrolmentEvaluation.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule readCurriculumModuleByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule domainObject =
                (net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.studentCurriculum.Credits readCreditsByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.studentCurriculum.Credits domainObject =
                (net.sourceforge.fenixedu.domain.studentCurriculum.Credits) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.studentCurriculum.Credits.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency readCourseEquivalencyByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency domainObject =
                (net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.student.StudentStatute readStudentStatuteByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.student.StudentStatute domainObject =
                (net.sourceforge.fenixedu.domain.student.StudentStatute) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.student.StudentStatute.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacher.Career readCareerByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacher.Career domainObject =
                (net.sourceforge.fenixedu.domain.teacher.Career) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.teacher.Career.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.File readFileByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.File domainObject =
                (net.sourceforge.fenixedu.domain.File) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.File.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.MasterDegreeProofVersion readMasterDegreeProofVersionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.MasterDegreeProofVersion domainObject =
                (net.sourceforge.fenixedu.domain.MasterDegreeProofVersion) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.MasterDegreeProofVersion.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation readResultUnitAssociationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation domainObject =
                (net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Teacher readTeacherByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Teacher domainObject =
                (net.sourceforge.fenixedu.domain.Teacher) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Teacher.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.caseHandling.Process readProcessByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.caseHandling.Process domainObject =
                (net.sourceforge.fenixedu.domain.caseHandling.Process) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.caseHandling.Process.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.SupportLesson readSupportLessonByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.SupportLesson domainObject =
                (net.sourceforge.fenixedu.domain.SupportLesson) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.SupportLesson.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.parking.ParkingRequest readParkingRequestByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.parking.ParkingRequest domainObject =
                (net.sourceforge.fenixedu.domain.parking.ParkingRequest) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.parking.ParkingRequest.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.parking.ParkingGroup readParkingGroupByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.parking.ParkingGroup domainObject =
                (net.sourceforge.fenixedu.domain.parking.ParkingGroup) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.parking.ParkingGroup.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.research.result.ResearchResult readResearchResultByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.result.ResearchResult domainObject =
                (net.sourceforge.fenixedu.domain.research.result.ResearchResult) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.research.result.ResearchResult.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Identification readIdentificationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Identification domainObject =
                (net.sourceforge.fenixedu.domain.Identification) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Identification.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Country readCountryByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Country domainObject =
                (net.sourceforge.fenixedu.domain.Country) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Country.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.onlineTests.TestQuestion readTestQuestionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.onlineTests.TestQuestion domainObject =
                (net.sourceforge.fenixedu.domain.onlineTests.TestQuestion) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.onlineTests.TestQuestion.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.accounting.PaymentPlan readPaymentPlanByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.accounting.PaymentPlan domainObject =
                (net.sourceforge.fenixedu.domain.accounting.PaymentPlan) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.accounting.PaymentPlan.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation readTeacherPersonalExpectationByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation domainObject =
                (net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment readExternalEnrolmentByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment domainObject =
                (net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.onlineTests.Question readQuestionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.onlineTests.Question domainObject =
                (net.sourceforge.fenixedu.domain.onlineTests.Question) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.onlineTests.Question.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal readProposalByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal domainObject =
                (net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.GuideSituation readGuideSituationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.GuideSituation domainObject =
                (net.sourceforge.fenixedu.domain.GuideSituation) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.GuideSituation.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Grouping readGroupingByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Grouping domainObject =
                (net.sourceforge.fenixedu.domain.Grouping) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Grouping.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.DegreeCurricularPlan readDegreeCurricularPlanByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.DegreeCurricularPlan domainObject =
                (net.sourceforge.fenixedu.domain.DegreeCurricularPlan) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.DegreeCurricularPlan.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.grant.contract.GrantType readGrantTypeByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.grant.contract.GrantType domainObject =
                (net.sourceforge.fenixedu.domain.grant.contract.GrantType) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.grant.contract.GrantType.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Seminaries.Theme readThemeByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Seminaries.Theme domainObject =
                (net.sourceforge.fenixedu.domain.Seminaries.Theme) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Seminaries.Theme.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.protocols.Protocol readProtocolByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.protocols.Protocol domainObject =
                (net.sourceforge.fenixedu.domain.protocols.Protocol) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.protocols.Protocol.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.MetaDomainObject readMetaDomainObjectByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.MetaDomainObject domainObject =
                (net.sourceforge.fenixedu.domain.MetaDomainObject) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.MetaDomainObject.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.log.CurriculumLineLog readCurriculumLineLogByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.log.CurriculumLineLog domainObject =
                (net.sourceforge.fenixedu.domain.log.CurriculumLineLog) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.log.CurriculumLineLog.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess readTSDProcessByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess domainObject =
                (net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers readPersistentGroupMembersByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers domainObject =
                (net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.grant.contract.GrantPart readGrantPartByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.grant.contract.GrantPart domainObject =
                (net.sourceforge.fenixedu.domain.grant.contract.GrantPart) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.grant.contract.GrantPart.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.accounting.AccountingTransaction readAccountingTransactionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.accounting.AccountingTransaction domainObject =
                (net.sourceforge.fenixedu.domain.accounting.AccountingTransaction) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.accounting.AccountingTransaction.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Summary readSummaryByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Summary domainObject =
                (net.sourceforge.fenixedu.domain.Summary) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Summary.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog readStudentTestLogByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog domainObject =
                (net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.resource.ResourceAllocation readResourceAllocationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.resource.ResourceAllocation domainObject =
                (net.sourceforge.fenixedu.domain.resource.ResourceAllocation) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.resource.ResourceAllocation.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult readStudentInquiriesCourseResultByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult domainObject =
                (net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory readProfessionalCategoryByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory domainObject =
                (net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule readDegreeModuleByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule domainObject =
                (net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.StudentCurricularPlan readStudentCurricularPlanByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.StudentCurricularPlan domainObject =
                (net.sourceforge.fenixedu.domain.StudentCurricularPlan) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.StudentCurricularPlan.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest readAcademicServiceRequestByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest domainObject =
                (net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry readSibsPaymentFileEntryByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry domainObject =
                (net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest readPunctualRoomsOccupationRequestByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest domainObject =
                (net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.system.CronScriptInvocation readCronScriptInvocationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.system.CronScriptInvocation domainObject =
                (net.sourceforge.fenixedu.domain.system.CronScriptInvocation) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.system.CronScriptInvocation.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime readGrantContractRegimeByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime domainObject =
                (net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState readRegistrationStateByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState domainObject =
                (net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.PersonalGroup readPersonalGroupByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.PersonalGroup domainObject =
                (net.sourceforge.fenixedu.domain.PersonalGroup) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.PersonalGroup.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.ExecutionInterval readExecutionIntervalByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.ExecutionInterval domainObject =
                (net.sourceforge.fenixedu.domain.ExecutionInterval) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.ExecutionInterval.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.CurricularSemester readCurricularSemesterByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.CurricularSemester domainObject =
                (net.sourceforge.fenixedu.domain.CurricularSemester) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.CurricularSemester.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.elections.DelegateElection readDelegateElectionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.elections.DelegateElection domainObject =
                (net.sourceforge.fenixedu.domain.elections.DelegateElection) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.elections.DelegateElection.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.tests.NewCorrector readNewCorrectorByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.tests.NewCorrector domainObject =
                (net.sourceforge.fenixedu.domain.tests.NewCorrector) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.tests.NewCorrector.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent readTeacherDegreeFinalProjectStudentByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent domainObject =
                (net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent.class
                                        .getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Employee readEmployeeByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Employee domainObject =
                (net.sourceforge.fenixedu.domain.Employee) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Employee.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.research.ResearchInterest readResearchInterestByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.ResearchInterest domainObject =
                (net.sourceforge.fenixedu.domain.research.ResearchInterest) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.research.ResearchInterest.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.GuideEntry readGuideEntryByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.GuideEntry domainObject =
                (net.sourceforge.fenixedu.domain.GuideEntry) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.GuideEntry.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.accounting.Event readEventByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.accounting.Event domainObject =
                (net.sourceforge.fenixedu.domain.accounting.Event) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.accounting.Event.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.CourseLoad readCourseLoadByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.CourseLoad domainObject =
                (net.sourceforge.fenixedu.domain.CourseLoad) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.CourseLoad.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.GenericEvent readGenericEventByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.GenericEvent domainObject =
                (net.sourceforge.fenixedu.domain.GenericEvent) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.GenericEvent.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.ExecutionDegree readExecutionDegreeByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.ExecutionDegree domainObject =
                (net.sourceforge.fenixedu.domain.ExecutionDegree) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.ExecutionDegree.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.space.RoomClassification readRoomClassificationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.space.RoomClassification domainObject =
                (net.sourceforge.fenixedu.domain.space.RoomClassification) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.space.RoomClassification.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Job readJobByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Job domainObject =
                (net.sourceforge.fenixedu.domain.Job) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Job.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.CurricularYear readCurricularYearByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.CurricularYear domainObject =
                (net.sourceforge.fenixedu.domain.CurricularYear) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.CurricularYear.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Attends readAttendsByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Attends domainObject =
                (net.sourceforge.fenixedu.domain.Attends) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Attends.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.student.Student readStudentByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.student.Student domainObject =
                (net.sourceforge.fenixedu.domain.student.Student) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.student.Student.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Seminaries.CaseStudy readCaseStudyByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Seminaries.CaseStudy domainObject =
                (net.sourceforge.fenixedu.domain.Seminaries.CaseStudy) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Seminaries.CaseStudy.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement readGrantContractMovementByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement domainObject =
                (net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.space.Blueprint readBlueprintByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.space.Blueprint domainObject =
                (net.sourceforge.fenixedu.domain.space.Blueprint) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.space.Blueprint.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.grant.owner.GrantOwner readGrantOwnerByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.grant.owner.GrantOwner domainObject =
                (net.sourceforge.fenixedu.domain.grant.owner.GrantOwner) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.grant.owner.GrantOwner.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.thesis.Thesis readThesisByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.thesis.Thesis domainObject =
                (net.sourceforge.fenixedu.domain.thesis.Thesis) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.thesis.Thesis.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.MasterDegreeCandidate readMasterDegreeCandidateByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.MasterDegreeCandidate domainObject =
                (net.sourceforge.fenixedu.domain.MasterDegreeCandidate) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.MasterDegreeCandidate.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance readGrantInsuranceByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance domainObject =
                (net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.GratuitySituation readGratuitySituationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.GratuitySituation domainObject =
                (net.sourceforge.fenixedu.domain.GratuitySituation) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.GratuitySituation.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.contacts.PartyContact readPartyContactByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.contacts.PartyContact domainObject =
                (net.sourceforge.fenixedu.domain.contacts.PartyContact) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.contacts.PartyContact.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.student.RegistrationRegime readRegistrationRegimeByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.student.RegistrationRegime domainObject =
                (net.sourceforge.fenixedu.domain.student.RegistrationRegime) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.student.RegistrationRegime.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.ExecutionYear readExecutionYearByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.ExecutionYear domainObject =
                (net.sourceforge.fenixedu.domain.ExecutionYear) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.ExecutionYear.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Seminaries.Modality readModalityByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Seminaries.Modality domainObject =
                (net.sourceforge.fenixedu.domain.Seminaries.Modality) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Seminaries.Modality.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.space.SpaceInformation readSpaceInformationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.space.SpaceInformation domainObject =
                (net.sourceforge.fenixedu.domain.space.SpaceInformation) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.space.SpaceInformation.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial readNewPresentationMaterialByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial domainObject =
                (net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.student.Registration readRegistrationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.student.Registration domainObject =
                (net.sourceforge.fenixedu.domain.student.Registration) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.student.Registration.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher readGrantOrientationTeacherByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher domainObject =
                (net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Qualification readQualificationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Qualification domainObject =
                (net.sourceforge.fenixedu.domain.Qualification) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Qualification.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.tests.NewTestElement readNewTestElementByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.tests.NewTestElement domainObject =
                (net.sourceforge.fenixedu.domain.tests.NewTestElement) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.tests.NewTestElement.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.ShiftProfessorship readShiftProfessorshipByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.ShiftProfessorship domainObject =
                (net.sourceforge.fenixedu.domain.ShiftProfessorship) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.ShiftProfessorship.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.ScientificCommission readScientificCommissionByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.ScientificCommission domainObject =
                (net.sourceforge.fenixedu.domain.ScientificCommission) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.ScientificCommission.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Curriculum readCurriculumByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Curriculum domainObject =
                (net.sourceforge.fenixedu.domain.Curriculum) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Curriculum.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation readProjectEventAssociationByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation domainObject =
                (net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy readGrantSubsidyByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy domainObject =
                (net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Tutorship readTutorshipByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Tutorship domainObject =
                (net.sourceforge.fenixedu.domain.Tutorship) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Tutorship.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public net.sourceforge.fenixedu.domain.Degree readDegreeByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.Degree domainObject =
                (net.sourceforge.fenixedu.domain.Degree) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.Degree.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    @Deprecated
    public void initAccessClosures() {
        closureAccessMap.put(net.sourceforge.fenixedu.domain.system.CronScriptState.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCronScriptStateByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCronScriptStatesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.resource.ResourceResponsibility.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readResourceResponsibilityByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getResourceResponsibilitySet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Lesson.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readLessonByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getLessonsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.grant.contract.GrantContract.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readGrantContractByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getGrantContractsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EquivalencePlanEntry.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readEquivalencePlanEntryByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getEquivalencePlanEntriesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Exemption.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readExemptionByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getExemptionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readParkingRequestPeriodByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getParkingRequestPeriodsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readCycleCourseGroupInformationByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCycleCourseGroupInformationSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.result.ResultParticipation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readResultParticipationByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getResultParticipationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Role.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readRoleByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getRolesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.BibliographicReference.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readBibliographicReferenceByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getBibliographicReferencesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.StudentGroup.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readStudentGroupByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getStudentGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EnrolmentPeriod.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readEnrolmentPeriodByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getEnrolmentPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.resource.Resource.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readResourceByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getResourcesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.NonAffiliatedTeacher.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readNonAffiliatedTeacherByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getNonAffiliatedTeachersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.precedences.Precedence.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readPrecedenceByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getPrecedencesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.transactions.Transaction.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readTransactionByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getTransactionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readGrantPaymentEntityByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getGrantPaymentEntitysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.util.email.Message.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readMessageByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getPendingUtilEmailMessagesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readAdministrativeOfficeByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAdministrativeOfficesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readExpectationEvaluationGroupByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getExpectationEvaluationGroupsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.support.GlossaryEntry.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readGlossaryEntryByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getGlossaryEntrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Coordinator.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCoordinatorByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCoordinatorsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readStudentInquiriesTeachingResultByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getStudentInquiriesTeachingResultsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExternalCurricularCourse.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readExternalCurricularCourseByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getExternalCurricularCoursesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.project.ProjectParticipation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readProjectParticipationByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getProjectParticipationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readOldInquiriesTeachersResByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getOldInquiriesTeachersRessSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionSemester.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readExecutionSemesterByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getExecutionPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ProjectSubmission.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readProjectSubmissionByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getProjectSubmissionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readServiceProviderRegimeByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getServiceProviderRegimesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.EventEdition.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readEventEditionByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getEventEditionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularCourseEquivalence.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readCurricularCourseEquivalenceByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCurricularCourseEquivalencesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.util.email.Sender.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readSenderByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getUtilEmailSendersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.Test.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readTestByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getTestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.ExternalActivity.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readExternalActivityByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getExternalActivitysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readTSDProfessorshipByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTSDProfessorshipsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.CreditNote.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCreditNoteByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCreditNotesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.candidacy.Candidacy.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCandidacyByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCandidaciesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.PublicationsNumber.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readPublicationsNumberByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getPublicationsNumbersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readReimbursementGuideByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getReimbursementGuidesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Holiday.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readHolidayByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getHolidaysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CompetenceCourse.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCompetenceCourseByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCompetenceCoursesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readAccountabilityTypeByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAccountabilityTypesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.precedences.Restriction.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readRestrictionByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getRestrictionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.DistributedTest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readDistributedTestByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getDistributedTestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readGroupProposalByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getGroupProposalsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.gesdis.CourseReport.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCourseReportByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCourseReportsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readTeachingInquiryByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTeachingInquiriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readTSDProcessPhaseByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTSDProcessPhasesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.Seminary.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readSeminaryByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getSeminarysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.Orientation.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readOrientationByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getOrientationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularCourseScope.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCurricularCourseScopeByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCurricularCourseScopesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readMasterDegreeThesisDataVersionByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getMasterDegreeThesisDataVersionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EquivalencePlan.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readEquivalencePlanByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getEquivalencePlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GratuityValues.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readGratuityValuesByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getGratuityValuessSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.Context.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readContextByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getContextsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.library.LibraryCard.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readLibraryCardByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getLibraryCardsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.Accountability.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readAccountabilityByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAccountabilitysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readSeminaryCandidacyByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCandidacysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LoginAlias.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readLoginAliasByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getLoginAliasSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.User.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readUserByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getUsersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.ResearchEvent.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readResearchEventByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getEventsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MarkSheet.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readMarkSheetByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getMarkSheetsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Professorship.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readProfessorshipByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getProfessorshipsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationProblem.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readCardGenerationProblemByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCardGenerationProblemSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readEnrolmentWrapperByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getEnrolmentWrappersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readNotNeedToEnrollInCurricularCourseByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getNotNeedToEnrollInCurricularCoursesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.ScientificJournal.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readScientificJournalByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getScientificJournalsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.Metadata.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readMetadataByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getMetadatasSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.OccupationPeriod.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readOccupationPeriodByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getOccupationPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Department.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readDepartmentByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getDepartmentsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LoginPeriod.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readLoginPeriodByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getLoginPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.SchoolClass.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readSchoolClassByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getSchoolClasssSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.Party.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readPartyByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getPartysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.support.FAQSection.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readFAQSectionByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getFAQSectionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Evaluation.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readEvaluationByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getEvaluationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingParty.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readParkingPartyByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getParkingPartiesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Guide.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readGuideByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getGuidesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readCompetenceCourseLoadByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCompetenceCourseLoadsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionCourse.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readExecutionCourseByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getExecutionCoursesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewTestGroup.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readNewTestGroupByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getTestGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readCardGenerationEntryByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCardGenerationEntriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Alumni.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readAlumniByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getAlumnisSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.curricularRules.CurricularRule.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readCurricularRuleByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCurricularRulesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.util.email.Message.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readMessageByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getUtilEmailMessagesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readYearDelegateCourseInquiryByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getYearDelegateCourseInquiriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EducationArea.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readEducationAreaByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getEducationAreasSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readAcademicCalendarEntryByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAcademicCalendarEntriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readTSDTeacherByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTSDTeachersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Branch.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readBranchByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getBranchsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readReimbursementGuideEntryByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getReimbursementGuideEntrysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.Party.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readPartyByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getExternalScholarshipProviderSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.project.Project.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readProjectByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getProjectsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.PostingRule.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readPostingRuleByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getPostingRulesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.contents.Content.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readContentByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getContentsSet();
            }
        });
        closureAccessMap.put(
                net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readTeacherServiceDistributionByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTeacherServiceDistributionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readFinalDegreeWorkGroupByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getGroupsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readTSDCourseByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTSDCoursesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.support.FAQEntry.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readFAQEntryByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getFAQEntrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Shift.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readShiftByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getShiftsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Entry.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readEntryByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getEntriesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Photograph.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readPhotographByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getPhotographsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LessonPlanning.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readLessonPlanningByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getLessonPlanningsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readCardGenerationBatchByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCardGenerationBatchesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readAlumniIdentityCheckRequestByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAlumniIdentityRequestSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Receipt.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readReceiptByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getReceiptsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.TeacherServiceItem.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readTeacherServiceItemByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTeacherServiceItemsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewPermissionUnit.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readNewPermissionUnitByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getPermissionUnitsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EnrolmentEvaluation.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readEnrolmentEvaluationByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getEnrolmentEvaluationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readCurriculumModuleByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCurriculumModulesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.Credits.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCreditsByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCreditsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readCourseEquivalencyByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCourseEquivalencysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.StudentStatute.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readStudentStatuteByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getStudentStatutesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.Career.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCareerByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCareersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.File.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readFileByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getFilesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MasterDegreeProofVersion.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readMasterDegreeProofVersionByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getMasterDegreeProofVersionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readResultUnitAssociationByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getResultUnitAssociationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Teacher.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readTeacherByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getTeachersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.caseHandling.Process.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readProcessByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getProcessesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.SupportLesson.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readSupportLessonByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getSupportLessonsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingRequest.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readParkingRequestByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getParkingRequestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingGroup.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readParkingGroupByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getParkingGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.result.ResearchResult.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readResearchResultByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getResultsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Identification.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readIdentificationByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getIdentificationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Country.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCountryByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCountrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.TestQuestion.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readTestQuestionByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getTestQuestionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.PaymentPlan.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readPaymentPlanByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getPaymentPlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readTeacherPersonalExpectationByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTeacherPersonalExpectationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readExternalEnrolmentByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getExternalEnrolmentsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.Question.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readQuestionByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getQuestionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readProposalByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getProposalsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GuideSituation.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readGuideSituationByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getGuideSituationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Grouping.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readGroupingByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getGroupingsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.DegreeCurricularPlan.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readDegreeCurricularPlanByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getDegreeCurricularPlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.grant.contract.GrantType.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readGrantTypeByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getGrantTypesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.Theme.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readThemeByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getThemesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.protocols.Protocol.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readProtocolByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getProtocolsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MetaDomainObject.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readMetaDomainObjectByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getMetaDomainObjectsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.log.CurriculumLineLog.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCurriculumLineLogByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCurriculumLineLogsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readTSDProcessByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTSDProcessesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Photograph.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readPhotographByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getPendingPhotographsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readPersistentGroupMembersByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getPersistentGroupMembersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.grant.contract.GrantPart.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readGrantPartByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getGrantPartsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.AccountingTransaction.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readAccountingTransactionByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAccountingTransactionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Summary.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readSummaryByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getSummarysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readStudentTestLogByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getStudentTestLogsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.resource.ResourceAllocation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readResourceAllocationByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getResourceAllocationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readStudentInquiriesCourseResultByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getStudentInquiriesCourseResultsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readProfessionalCategoryByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getProfessionalCategoriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readDegreeModuleByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getDegreeModulesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.StudentCurricularPlan.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readStudentCurricularPlanByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getStudentCurricularPlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readAcademicServiceRequestByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAcademicServiceRequestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readSibsPaymentFileEntryByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getSibsPaymentFileEntrysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readPunctualRoomsOccupationRequestByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getPunctualRoomsOccupationRequestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.system.CronScriptInvocation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readCronScriptInvocationByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCronScriptInvocationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readGrantContractRegimeByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getGrantContractRegimesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readRegistrationStateByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getRegistrationStatesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.PersonalGroup.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readPersonalGroupByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getPersonalGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionInterval.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readExecutionIntervalByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getExecutionIntervalsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularSemester.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCurricularSemesterByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCurricularSemestersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.elections.DelegateElection.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readDelegateElectionByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getDelegateElectionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewCorrector.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readNewCorrectorByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCorrectorsSet();
            }
        });
        closureAccessMap.put(
                net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readTeacherDegreeFinalProjectStudentByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTeacherDegreeFinalProjectStudentsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Employee.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readEmployeeByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getEmployeesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.ResearchInterest.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readResearchInterestByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getResearchInterestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GuideEntry.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readGuideEntryByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getGuideEntrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Event.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readEventByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getAccountingEventsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CourseLoad.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCourseLoadByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCourseLoadsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GenericEvent.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readGenericEventByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getGenericEventsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionDegree.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readExecutionDegreeByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getExecutionDegreesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.space.RoomClassification.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readRoomClassificationByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getRoomClassificationSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Job.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readJobByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getJobsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularYear.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCurricularYearByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCurricularYearsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Attends.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readAttendsByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getAttendssSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.Student.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readStudentByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getStudentsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.CaseStudy.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCaseStudyByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCaseStudysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readGrantContractMovementByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getGrantContractMovementsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.space.Blueprint.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readBlueprintByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getBlueprintsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.grant.owner.GrantOwner.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readGrantOwnerByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getGrantOwnersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.thesis.Thesis.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readThesisByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getThesesPendingPublicationSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MasterDegreeCandidate.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readMasterDegreeCandidateByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getMasterDegreeCandidatesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readGrantInsuranceByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getGrantInsurancesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GratuitySituation.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readGratuitySituationByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getGratuitySituationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.contacts.PartyContact.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readPartyContactByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getPartyContactsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.RegistrationRegime.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readRegistrationRegimeByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getRegistrationRegimesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionYear.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readExecutionYearByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getExecutionYearsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.Modality.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readModalityByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getModalitysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.space.SpaceInformation.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readSpaceInformationByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getSpaceInformationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.thesis.Thesis.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readThesisByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getThesesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readNewPresentationMaterialByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getPresentationMaterialsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.Registration.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readRegistrationByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getRegistrationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readGrantOrientationTeacherByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getGrantOrientationTeachersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Qualification.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readQualificationByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getQualificationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewTestElement.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readNewTestElementByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getNewTestElementsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ShiftProfessorship.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readShiftProfessorshipByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getShiftProfessorshipsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ScientificCommission.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readScientificCommissionByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getScientificCommissionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Curriculum.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readCurriculumByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getCurriculumsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readProjectEventAssociationByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getProjectEventAssociationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readGrantSubsidyByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getGrantSubsidysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Tutorship.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readTutorshipByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getTutorshipsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Degree.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readDegreeByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getDegreesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readVigilantGroupByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getVigilantGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readVigilantWrapperByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getVigilantWrappersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readUnavailablePeriodByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getUnavailablePeriodsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.Vehicle.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readVehicleByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getVehiclesSet();
            }
        });
        closureAccessMap.put(
                net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readCompetenceCourseInformationChangeRequestByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCompetenceCourseInformationChangeRequestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.Vigilancy.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readVigilancyByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getVigilanciesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LoginRequest.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readLoginRequestByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getLoginRequestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.Cooperation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readCooperationByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCooperationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.JournalIssue.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readJournalIssueByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getJournalIssuesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.Participation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public DomainObject readDomainObjectByOID(final Integer idInternal) {
                        return readParticipationByOID(idInternal);
                    }

                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getParticipationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.Prize.class.getName(), new DomainObjectReader() {
            @Override
            public DomainObject readDomainObjectByOID(final Integer idInternal) {
                return readPrizeByOID(idInternal);
            }

            @Override
            public java.util.Set readAllDomainObjects() {
                return getPrizesSet();
            }
        });
    }

    public net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup readVigilantGroupByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup domainObject =
                (net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    public net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper readVigilantWrapperByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper domainObject =
                (net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    public net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod readUnavailablePeriodByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod domainObject =
                (net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    public net.sourceforge.fenixedu.domain.parking.Vehicle readVehicleByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.parking.Vehicle domainObject =
                (net.sourceforge.fenixedu.domain.parking.Vehicle) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.parking.Vehicle.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    public net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest readCompetenceCourseInformationChangeRequestByOID(
            Integer idInternal) {
        final net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest domainObject =
                (net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(
                                net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest.class
                                        .getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    public net.sourceforge.fenixedu.domain.vigilancy.Vigilancy readVigilancyByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.vigilancy.Vigilancy domainObject =
                (net.sourceforge.fenixedu.domain.vigilancy.Vigilancy) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.vigilancy.Vigilancy.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    public net.sourceforge.fenixedu.domain.LoginRequest readLoginRequestByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.LoginRequest domainObject =
                (net.sourceforge.fenixedu.domain.LoginRequest) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.LoginRequest.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    public net.sourceforge.fenixedu.domain.research.activity.Cooperation readCooperationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.activity.Cooperation domainObject =
                (net.sourceforge.fenixedu.domain.research.activity.Cooperation) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.research.activity.Cooperation.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    public net.sourceforge.fenixedu.domain.research.activity.JournalIssue readJournalIssueByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.activity.JournalIssue domainObject =
                (net.sourceforge.fenixedu.domain.research.activity.JournalIssue) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.research.activity.JournalIssue.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    public net.sourceforge.fenixedu.domain.research.activity.Participation readParticipationByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.activity.Participation domainObject =
                (net.sourceforge.fenixedu.domain.research.activity.Participation) pt.ist.fenixframework.pstm.Transaction
                        .readDomainObject(net.sourceforge.fenixedu.domain.research.activity.Participation.class.getName(),
                                idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

    public net.sourceforge.fenixedu.domain.research.Prize readPrizeByOID(Integer idInternal) {
        final net.sourceforge.fenixedu.domain.research.Prize domainObject =
                (net.sourceforge.fenixedu.domain.research.Prize) pt.ist.fenixframework.pstm.Transaction.readDomainObject(
                        net.sourceforge.fenixedu.domain.research.Prize.class.getName(), idInternal);
        return (domainObject == null || domainObject.getRootDomainObject() == null) ? null : domainObject;
    }

}
