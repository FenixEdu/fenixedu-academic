package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.fenixedu.bennu.core.domain.Bennu;

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
                return Bennu.getInstance().getCronScriptStatesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.resource.ResourceResponsibility.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getResourceResponsibilitySet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Lesson.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getLessonsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EquivalencePlanEntry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getEquivalencePlanEntriesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Exemption.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getExemptionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getParkingRequestPeriodsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCycleCourseGroupInformationSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.result.ResultParticipation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getResultParticipationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Role.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getRolesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.BibliographicReference.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getBibliographicReferencesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.StudentGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getStudentGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EnrolmentPeriod.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getEnrolmentPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.resource.Resource.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getResourcesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.NonAffiliatedTeacher.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getNonAffiliatedTeachersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.precedences.Precedence.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getPrecedencesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.transactions.Transaction.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getTransactionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.util.email.Message.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getPendingUtilEmailMessagesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getAdministrativeOfficesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getExpectationEvaluationGroupsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.support.GlossaryEntry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getGlossaryEntrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Coordinator.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCoordinatorsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getStudentInquiriesTeachingResultsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExternalCurricularCourse.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getExternalCurricularCoursesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.project.ProjectParticipation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getProjectParticipationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getOldInquiriesTeachersRessSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionSemester.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getExecutionPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ProjectSubmission.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getProjectSubmissionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getServiceProviderRegimesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.EventEdition.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getEventEditionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularCourseEquivalence.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCurricularCourseEquivalencesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.util.email.Sender.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getUtilEmailSendersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.Test.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getTestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.ExternalActivity.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getExternalActivitysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getTSDProfessorshipsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.CreditNote.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCreditNotesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.candidacy.Candidacy.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCandidaciesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.PublicationsNumber.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getPublicationsNumbersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getReimbursementGuidesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Holiday.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getHolidaysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CompetenceCourse.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCompetenceCoursesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getAccountabilityTypesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.precedences.Restriction.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getRestrictionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.DistributedTest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getDistributedTestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getGroupProposalsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.gesdis.CourseReport.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCourseReportsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getTeachingInquiriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getTSDProcessPhasesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.Seminary.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getSeminarysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.Orientation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getOrientationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularCourseScope.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCurricularCourseScopesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getMasterDegreeThesisDataVersionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EquivalencePlan.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getEquivalencePlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GratuityValues.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getGratuityValuessSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.Context.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getContextsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.library.LibraryCard.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getLibraryCardsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.Accountability.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getAccountabilitysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCandidacysSet();
                    }
                });
        closureAccessMap.put(org.fenixedu.bennu.core.domain.User.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getUsersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.ResearchEvent.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getEventsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MarkSheet.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getMarkSheetsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Professorship.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getProfessorshipsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationProblem.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCardGenerationProblemSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getEnrolmentWrappersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getNotNeedToEnrollInCurricularCoursesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.ScientificJournal.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getScientificJournalsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.Metadata.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getMetadatasSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.OccupationPeriod.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getOccupationPeriodsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Department.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getDepartmentsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.SchoolClass.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getSchoolClasssSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.Party.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getPartysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.support.FAQSection.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getFAQSectionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Evaluation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getEvaluationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingParty.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getParkingPartiesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Guide.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getGuidesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCompetenceCourseLoadsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionCourse.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getExecutionCoursesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewTestGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getTestGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCardGenerationEntriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Alumni.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getAlumnisSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.curricularRules.CurricularRule.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCurricularRulesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.util.email.Message.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getUtilEmailMessagesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.YearDelegateCourseInquiry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getYearDelegateCourseInquiriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EducationArea.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getEducationAreasSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getAcademicCalendarEntriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getTSDTeachersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Branch.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getBranchsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getReimbursementGuideEntrysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.organizationalStructure.Party.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getExternalScholarshipProviderSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.project.Project.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getProjectsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.PostingRule.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getPostingRulesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.contents.Content.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getContentsSet();
            }
        });
        closureAccessMap.put(
                net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getTeacherServiceDistributionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getGroupsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getTSDCoursesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.support.FAQEntry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getFAQEntrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Shift.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getShiftsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Entry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getEntriesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Photograph.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getPhotographsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.LessonPlanning.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getLessonPlanningsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCardGenerationBatchesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getAlumniIdentityRequestSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Receipt.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getReceiptsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.TeacherServiceItem.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getTeacherServiceItemsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewPermissionUnit.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getPermissionUnitsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.EnrolmentEvaluation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getEnrolmentEvaluationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCurriculumModulesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.Credits.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCreditsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCourseEquivalencysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.StudentStatute.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getStudentStatutesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.Career.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCareersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MasterDegreeProofVersion.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getMasterDegreeProofVersionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getResultUnitAssociationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Teacher.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getTeachersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.caseHandling.Process.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getProcessesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.SupportLesson.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getSupportLessonsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingRequest.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getParkingRequestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.ParkingGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getParkingGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.result.ResearchResult.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getResultsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Country.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCountrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.TestQuestion.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getTestQuestionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.PaymentPlan.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getPaymentPlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getTeacherPersonalExpectationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getExternalEnrolmentsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.Question.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getQuestionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getProposalsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GuideSituation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getGuideSituationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Grouping.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getGroupingsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.DegreeCurricularPlan.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getDegreeCurricularPlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.Theme.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getThemesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.log.CurriculumLineLog.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCurriculumLineLogsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getTSDProcessesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Photograph.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getPendingPhotographsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getPersistentGroupMembersSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.AccountingTransaction.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getAccountingTransactionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Summary.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getSummarysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getStudentTestLogsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.resource.ResourceAllocation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getResourceAllocationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getStudentInquiriesCourseResultsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getProfessionalCategoriesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getDegreeModulesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.StudentCurricularPlan.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getStudentCurricularPlansSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getAcademicServiceRequestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getSibsPaymentFileEntrysSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getPunctualRoomsOccupationRequestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.system.CronScriptInvocation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCronScriptInvocationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getRegistrationStatesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.PersonalGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getPersonalGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionInterval.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getExecutionIntervalsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularSemester.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCurricularSemestersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.elections.DelegateElection.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getDelegateElectionsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewCorrector.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCorrectorsSet();
            }
        });
        closureAccessMap.put(
                net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getTeacherDegreeFinalProjectStudentsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Employee.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getEmployeesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.ResearchInterest.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getResearchInterestsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GuideEntry.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getGuideEntrysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.accounting.Event.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getAccountingEventsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CourseLoad.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCourseLoadsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GenericEvent.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getGenericEventsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionDegree.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getExecutionDegreesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.space.RoomClassification.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getRoomClassificationSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Job.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getJobsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.CurricularYear.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCurricularYearsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Attends.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getAttendssSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.Student.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getStudentsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.CaseStudy.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCaseStudysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.space.Blueprint.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getBlueprintsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.thesis.Thesis.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getThesesPendingPublicationSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.MasterDegreeCandidate.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getMasterDegreeCandidatesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.GratuitySituation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getGratuitySituationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.contacts.PartyContact.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getPartyContactsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.RegistrationRegime.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getRegistrationRegimesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ExecutionYear.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getExecutionYearsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Seminaries.Modality.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getModalitysSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.space.SpaceInformation.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getSpaceInformationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.thesis.Thesis.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getThesesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewPresentationMaterial.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getPresentationMaterialsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.student.Registration.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getRegistrationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Qualification.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getQualificationsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.tests.NewTestElement.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getNewTestElementsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ShiftProfessorship.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getShiftProfessorshipsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.ScientificCommission.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getScientificCommissionsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Curriculum.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getCurriculumsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getProjectEventAssociationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Tutorship.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getTutorshipsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.Degree.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getDegreesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getVigilantGroupsSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getVigilantWrappersSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getUnavailablePeriodsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.parking.Vehicle.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getVehiclesSet();
            }
        });
        closureAccessMap.put(
                net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCompetenceCourseInformationChangeRequestsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.vigilancy.Vigilancy.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getVigilanciesSet();
            }
        });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.Cooperation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getCooperationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.JournalIssue.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getJournalIssuesSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.activity.Participation.class.getName(),
                new DomainObjectReader() {
                    @Override
                    public java.util.Set readAllDomainObjects() {
                        return Bennu.getInstance().getParticipationsSet();
                    }
                });
        closureAccessMap.put(net.sourceforge.fenixedu.domain.research.Prize.class.getName(), new DomainObjectReader() {
            @Override
            public java.util.Set readAllDomainObjects() {
                return Bennu.getInstance().getPrizesSet();
            }
        });
    }

}
