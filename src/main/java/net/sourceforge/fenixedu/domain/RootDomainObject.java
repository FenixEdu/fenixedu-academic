package net.sourceforge.fenixedu.domain;

import pt.ist.fenixframework.DomainObject;
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

    protected RootDomainObject getRootDomainObject() {
        return this;
    }

    private interface DomainObjectReader {
        public java.util.Set readAllDomainObjects();
    }

    private static final java.util.Map<String, DomainObjectReader> closureAccessMap =
            new java.util.HashMap<String, DomainObjectReader>();

    @Deprecated
    public static <T> java.util.Set<T> readAllDomainObjects(final Class<T> domainClass) {
        final java.util.Set<T> domainObjects = readAllDomainObjectsAux(domainClass);
        final java.util.Set<T> resultSet = new java.util.HashSet<T>();
        if (domainObjects != null) {
            for (final T object : domainObjects) {
                if (domainClass.isInstance(object)) {
                    resultSet.add(object);
                }
            }
        }
        return resultSet;
    }

    @Deprecated
    private static java.util.Set readAllDomainObjectsAux(final Class domainClass) {
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
    public void initAccessClosures() {
        closureAccessMap.put(net.sourceforge.fenixedu.domain.system.CronScriptState.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCronScriptStatesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.resource.ResourceResponsibility.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getResourceResponsibilitySet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Lesson.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getLessonsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EquivalencePlanEntry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getEquivalencePlanEntriesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Exemption.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getExemptionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getParkingRequestPeriodsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCycleCourseGroupInformationSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.result.ResultParticipation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getResultParticipationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Role.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getRolesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.BibliographicReference.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getBibliographicReferencesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.StudentGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getStudentGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EnrolmentPeriod.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getEnrolmentPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.resource.Resource.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getResourcesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.NonAffiliatedTeacher.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getNonAffiliatedTeachersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.precedences.Precedence.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getPrecedencesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.transactions.Transaction.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getTransactionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.util.email.Message.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getPendingUtilEmailMessagesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAdministrativeOfficesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getExpectationEvaluationGroupsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.support.GlossaryEntry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getGlossaryEntrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Coordinator.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCoordinatorsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getStudentInquiriesTeachingResultsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExternalCurricularCourse.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getExternalCurricularCoursesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.project.ProjectParticipation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getProjectParticipationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getOldInquiriesTeachersRessSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionSemester.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getExecutionPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ProjectSubmission.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getProjectSubmissionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getServiceProviderRegimesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.EventEdition.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getEventEditionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularCourseEquivalence.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCurricularCourseEquivalencesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.util.email.Sender.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getUtilEmailSendersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.Test.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getTestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.ExternalActivity.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getExternalActivitysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTSDProfessorshipsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.CreditNote.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCreditNotesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.candidacy.Candidacy.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCandidaciesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.PublicationsNumber.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getPublicationsNumbersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getReimbursementGuidesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Holiday.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getHolidaysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CompetenceCourse.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCompetenceCoursesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAccountabilityTypesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.precedences.Restriction.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getRestrictionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.DistributedTest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getDistributedTestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getGroupProposalsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.gesdis.CourseReport.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCourseReportsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTeachingInquiriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTSDProcessPhasesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.Seminary.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getSeminarysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.Orientation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getOrientationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularCourseScope.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCurricularCourseScopesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getMasterDegreeThesisDataVersionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EquivalencePlan.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getEquivalencePlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GratuityValues.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getGratuityValuessSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.Context.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getContextsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.library.LibraryCard.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getLibraryCardsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.Accountability.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAccountabilitysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCandidacysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LoginAlias.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getLoginAliasSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.User.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getUsersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.ResearchEvent.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getEventsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MarkSheet.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getMarkSheetsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Professorship.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getProfessorshipsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationProblem.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCardGenerationProblemSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getEnrolmentWrappersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getNotNeedToEnrollInCurricularCoursesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.ScientificJournal.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getScientificJournalsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.Metadata.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getMetadatasSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.OccupationPeriod.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getOccupationPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Department.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getDepartmentsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LoginPeriod.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getLoginPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.SchoolClass.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getSchoolClasssSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.Party.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getPartysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.support.FAQSection.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getFAQSectionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Evaluation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getEvaluationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingParty.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getParkingPartiesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Guide.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getGuidesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCompetenceCourseLoadsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionCourse.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getExecutionCoursesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewTestGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getTestGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCardGenerationEntriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Alumni.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getAlumnisSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.curricularRules.CurricularRule.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCurricularRulesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.util.email.Message.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getUtilEmailMessagesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getYearDelegateCourseInquiriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EducationArea.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getEducationAreasSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAcademicCalendarEntriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTSDTeachersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Branch.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getBranchsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getReimbursementGuideEntrysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.Party.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getExternalScholarshipProviderSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.project.Project.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getProjectsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.PostingRule.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getPostingRulesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.contents.Content.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getContentsSet();
            }
        });
        closureAccessMap.put(
                net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTeacherServiceDistributionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getGroupsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTSDCoursesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.support.FAQEntry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getFAQEntrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Shift.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getShiftsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Entry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getEntriesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Photograph.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getPhotographsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LessonPlanning.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getLessonPlanningsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCardGenerationBatchesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAlumniIdentityRequestSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Receipt.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getReceiptsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.TeacherServiceItem.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTeacherServiceItemsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewPermissionUnit.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getPermissionUnitsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EnrolmentEvaluation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getEnrolmentEvaluationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCurriculumModulesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.Credits.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCreditsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCourseEquivalencysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.StudentStatute.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getStudentStatutesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.Career.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCareersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.File.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getFilesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MasterDegreeProofVersion.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getMasterDegreeProofVersionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getResultUnitAssociationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Teacher.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getTeachersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.caseHandling.Process.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getProcessesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.SupportLesson.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getSupportLessonsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingRequest.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getParkingRequestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getParkingGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.result.ResearchResult.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getResultsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Identification.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getIdentificationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Country.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCountrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.TestQuestion.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getTestQuestionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.PaymentPlan.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getPaymentPlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTeacherPersonalExpectationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getExternalEnrolmentsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.Question.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getQuestionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getProposalsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GuideSituation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getGuideSituationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Grouping.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getGroupingsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.DegreeCurricularPlan.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getDegreeCurricularPlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.Theme.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getThemesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MetaDomainObject.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getMetaDomainObjectsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.log.CurriculumLineLog.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCurriculumLineLogsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTSDProcessesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Photograph.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getPendingPhotographsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getPersistentGroupMembersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.AccountingTransaction.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAccountingTransactionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Summary.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getSummarysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getStudentTestLogsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.resource.ResourceAllocation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getResourceAllocationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getStudentInquiriesCourseResultsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getProfessionalCategoriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getDegreeModulesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.StudentCurricularPlan.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getStudentCurricularPlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getAcademicServiceRequestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getSibsPaymentFileEntrysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getPunctualRoomsOccupationRequestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.system.CronScriptInvocation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCronScriptInvocationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getRegistrationStatesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.PersonalGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getPersonalGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionInterval.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getExecutionIntervalsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularSemester.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCurricularSemestersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.elections.DelegateElection.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getDelegateElectionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewCorrector.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCorrectorsSet();
            }
        });
        closureAccessMap.put(
                net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getTeacherDegreeFinalProjectStudentsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Employee.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getEmployeesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.ResearchInterest.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getResearchInterestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GuideEntry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getGuideEntrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Event.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getAccountingEventsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CourseLoad.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCourseLoadsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GenericEvent.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getGenericEventsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionDegree.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getExecutionDegreesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.space.RoomClassification.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getRoomClassificationSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Job.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getJobsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularYear.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCurricularYearsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Attends.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getAttendssSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.Student.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getStudentsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.CaseStudy.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCaseStudysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.space.Blueprint.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getBlueprintsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.thesis.Thesis.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getThesesPendingPublicationSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MasterDegreeCandidate.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getMasterDegreeCandidatesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GratuitySituation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getGratuitySituationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.contacts.PartyContact.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getPartyContactsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.RegistrationRegime.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getRegistrationRegimesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionYear.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getExecutionYearsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.Modality.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getModalitysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.space.SpaceInformation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getSpaceInformationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.thesis.Thesis.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getThesesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getPresentationMaterialsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.Registration.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getRegistrationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Qualification.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getQualificationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewTestElement.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getNewTestElementsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ShiftProfessorship.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getShiftProfessorshipsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ScientificCommission.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getScientificCommissionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Curriculum.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getCurriculumsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getProjectEventAssociationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Tutorship.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getTutorshipsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Degree.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getDegreesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getVigilantGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getVigilantWrappersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getUnavailablePeriodsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.Vehicle.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getVehiclesSet();
            }
        });
        closureAccessMap.put(
                net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCompetenceCourseInformationChangeRequestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.Vigilancy.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getVigilanciesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LoginRequest.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getLoginRequestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.Cooperation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getCooperationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.JournalIssue.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getJournalIssuesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.Participation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return getParticipationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.Prize.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return getPrizesSet();
            }
        });
    }

}
