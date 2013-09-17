package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import pt.ist.fenixframework.DomainObject;

public final class DomainObjectUtil {

    public static final Comparator<DomainObject> COMPARATOR_BY_ID = new Comparator<DomainObject>() {
        @Override
        public int compare(DomainObject o1, DomainObject o2) {
            return o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

    private static final Map<String, DomainObjectReader> closureAccessMap = new HashMap<String, DomainObjectReader>();

    static {
        initAccessClosures();
    }

    private interface DomainObjectReader {
        public java.util.Set readAllDomainObjects();
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
    private static void initAccessClosures() {
        closureAccessMap.put(net.sourceforge.fenixedu.domain.system.CronScriptState.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCronScriptStatesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.resource.ResourceResponsibility.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getResourceResponsibilitySet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Lesson.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getLessonsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EquivalencePlanEntry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getEquivalencePlanEntriesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Exemption.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getExemptionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getParkingRequestPeriodsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCycleCourseGroupInformationSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.result.ResultParticipation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getResultParticipationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Role.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getRolesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.BibliographicReference.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getBibliographicReferencesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.StudentGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getStudentGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EnrolmentPeriod.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getEnrolmentPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.resource.Resource.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getResourcesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.NonAffiliatedTeacher.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getNonAffiliatedTeachersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.precedences.Precedence.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getPrecedencesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.transactions.Transaction.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getTransactionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.util.email.Message.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getPendingUtilEmailMessagesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getAdministrativeOfficesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getExpectationEvaluationGroupsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.support.GlossaryEntry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getGlossaryEntrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Coordinator.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCoordinatorsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getStudentInquiriesTeachingResultsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExternalCurricularCourse.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getExternalCurricularCoursesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.project.ProjectParticipation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getProjectParticipationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getOldInquiriesTeachersRessSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionSemester.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getExecutionPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ProjectSubmission.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getProjectSubmissionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getServiceProviderRegimesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.EventEdition.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getEventEditionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularCourseEquivalence.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCurricularCourseEquivalencesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.util.email.Sender.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getUtilEmailSendersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.Test.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getTestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.ExternalActivity.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getExternalActivitysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getTSDProfessorshipsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.CreditNote.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCreditNotesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.candidacy.Candidacy.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCandidaciesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.PublicationsNumber.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getPublicationsNumbersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getReimbursementGuidesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Holiday.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getHolidaysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CompetenceCourse.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCompetenceCoursesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getAccountabilityTypesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.precedences.Restriction.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getRestrictionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.DistributedTest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getDistributedTestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getGroupProposalsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.gesdis.CourseReport.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCourseReportsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getTeachingInquiriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getTSDProcessPhasesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.Seminary.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getSeminarysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.Orientation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getOrientationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularCourseScope.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCurricularCourseScopesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getMasterDegreeThesisDataVersionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EquivalencePlan.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getEquivalencePlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GratuityValues.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getGratuityValuessSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.Context.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getContextsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.library.LibraryCard.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getLibraryCardsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.Accountability.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getAccountabilitysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCandidacysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LoginAlias.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getLoginAliasSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.User.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getUsersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.ResearchEvent.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getEventsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MarkSheet.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getMarkSheetsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Professorship.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getProfessorshipsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationProblem.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCardGenerationProblemSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getEnrolmentWrappersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getNotNeedToEnrollInCurricularCoursesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.ScientificJournal.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getScientificJournalsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.Metadata.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getMetadatasSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.OccupationPeriod.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getOccupationPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Department.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getDepartmentsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LoginPeriod.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getLoginPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.SchoolClass.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getSchoolClasssSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.Party.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getPartysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.support.FAQSection.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getFAQSectionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Evaluation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getEvaluationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingParty.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getParkingPartiesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Guide.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getGuidesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCompetenceCourseLoadsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionCourse.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getExecutionCoursesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewTestGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getTestGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCardGenerationEntriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Alumni.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getAlumnisSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.curricularRules.CurricularRule.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCurricularRulesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.util.email.Message.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getUtilEmailMessagesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getYearDelegateCourseInquiriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EducationArea.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getEducationAreasSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getAcademicCalendarEntriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getTSDTeachersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Branch.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getBranchsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getReimbursementGuideEntrysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.Party.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getExternalScholarshipProviderSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.project.Project.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getProjectsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.PostingRule.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getPostingRulesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.contents.Content.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getContentsSet();
            }
        });
        closureAccessMap.put(
                net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getTeacherServiceDistributionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getGroupsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getTSDCoursesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.support.FAQEntry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getFAQEntrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Shift.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getShiftsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Entry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getEntriesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Photograph.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getPhotographsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LessonPlanning.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getLessonPlanningsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCardGenerationBatchesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getAlumniIdentityRequestSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Receipt.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getReceiptsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.TeacherServiceItem.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getTeacherServiceItemsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewPermissionUnit.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getPermissionUnitsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EnrolmentEvaluation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getEnrolmentEvaluationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCurriculumModulesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.Credits.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCreditsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCourseEquivalencysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.StudentStatute.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getStudentStatutesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.Career.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCareersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.File.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getFilesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MasterDegreeProofVersion.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getMasterDegreeProofVersionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getResultUnitAssociationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Teacher.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getTeachersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.caseHandling.Process.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getProcessesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.SupportLesson.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getSupportLessonsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingRequest.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getParkingRequestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getParkingGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.result.ResearchResult.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getResultsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Identification.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getIdentificationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Country.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCountrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.TestQuestion.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getTestQuestionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.PaymentPlan.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getPaymentPlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getTeacherPersonalExpectationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getExternalEnrolmentsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.Question.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getQuestionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getProposalsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GuideSituation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getGuideSituationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Grouping.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getGroupingsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.DegreeCurricularPlan.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getDegreeCurricularPlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.Theme.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getThemesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MetaDomainObject.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getMetaDomainObjectsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.log.CurriculumLineLog.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCurriculumLineLogsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getTSDProcessesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Photograph.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getPendingPhotographsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getPersistentGroupMembersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.AccountingTransaction.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getAccountingTransactionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Summary.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getSummarysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getStudentTestLogsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.resource.ResourceAllocation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getResourceAllocationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getStudentInquiriesCourseResultsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getProfessionalCategoriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getDegreeModulesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.StudentCurricularPlan.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getStudentCurricularPlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getAcademicServiceRequestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getSibsPaymentFileEntrysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getPunctualRoomsOccupationRequestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.system.CronScriptInvocation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCronScriptInvocationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getRegistrationStatesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.PersonalGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getPersonalGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionInterval.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getExecutionIntervalsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularSemester.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCurricularSemestersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.elections.DelegateElection.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getDelegateElectionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewCorrector.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCorrectorsSet();
            }
        });
        closureAccessMap.put(
                net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getTeacherDegreeFinalProjectStudentsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Employee.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getEmployeesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.ResearchInterest.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getResearchInterestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GuideEntry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getGuideEntrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Event.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getAccountingEventsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CourseLoad.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCourseLoadsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GenericEvent.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getGenericEventsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionDegree.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getExecutionDegreesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.space.RoomClassification.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getRoomClassificationSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Job.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getJobsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularYear.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCurricularYearsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Attends.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getAttendssSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.Student.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getStudentsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.CaseStudy.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCaseStudysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.space.Blueprint.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getBlueprintsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.thesis.Thesis.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getThesesPendingPublicationSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MasterDegreeCandidate.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getMasterDegreeCandidatesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GratuitySituation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getGratuitySituationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.contacts.PartyContact.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getPartyContactsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.RegistrationRegime.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getRegistrationRegimesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionYear.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getExecutionYearsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.Modality.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getModalitysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.space.SpaceInformation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getSpaceInformationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.thesis.Thesis.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getThesesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getPresentationMaterialsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.Registration.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getRegistrationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Qualification.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getQualificationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewTestElement.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getNewTestElementsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ShiftProfessorship.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getShiftProfessorshipsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ScientificCommission.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getScientificCommissionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Curriculum.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getCurriculumsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getProjectEventAssociationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Tutorship.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getTutorshipsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Degree.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getDegreesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getVigilantGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getVigilantWrappersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getUnavailablePeriodsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.Vehicle.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getVehiclesSet();
            }
        });
        closureAccessMap.put(
                net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCompetenceCourseInformationChangeRequestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.Vigilancy.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getVigilanciesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LoginRequest.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getLoginRequestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.Cooperation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getCooperationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.JournalIssue.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getJournalIssuesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.Participation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return RootDomainObject.getInstance().getParticipationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.Prize.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return RootDomainObject.getInstance().getPrizesSet();
            }
        });
    }

}
