package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile;
import net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile.FileResultPermittedGroupType;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis.ThesisType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import net.sourceforge.fenixedu.domain.thesis.ThesisSite;
import net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.Month;
import pt.utl.ist.fenix.tools.file.DSpaceFileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ApproveThesisDiscussion extends ThesisServiceWithMailNotification {

    private static final String SUBJECT_KEY = "thesis.evaluation.approve.subject";
    private static final String BODY_KEY = "thesis.evaluation.approve.body";

    @Override
    public void process(Thesis thesis) {
	thesis.approveEvaluation();

	if (thesis.isFinalAndApprovedThesis()) {
	    // Evaluated thesis have a public page in
	    // ../dissertacoes/<id_internal>
	    new ThesisSite(thesis);
	    createResult(thesis);
	}
    }

    private void createResult(Thesis thesis) {
	ThesisFile dissertation = thesis.getDissertation();
	Person author = thesis.getStudent().getPerson();

	IFileManager fileManager = DSpaceFileManagerFactory.getFactoryInstance().getSimpleFileManager();
	InputStream stream = fileManager.retrieveFile(dissertation.getExternalStorageIdentification());

	FileDescriptor descriptor = fileManager.saveFile(getVirtualPath(thesis), dissertation.getFilename(), thesis
		.getVisibility().equals(ThesisVisibilityType.INTRANET), getMetadata(thesis), stream);
	final MultiLanguageString title = thesis.getFinalFullTitle();
	String titleForFile = title.getContent(thesis.getLanguage());
	if (titleForFile == null) {
	    titleForFile = title.getContent();
	}
	net.sourceforge.fenixedu.domain.research.result.publication.Thesis publication = new net.sourceforge.fenixedu.domain.research.result.publication.Thesis(
		author,
		ThesisType.Graduation_Thesis,
		titleForFile,
		thesis.getKeywords(),
		RootDomainObject.getInstance().getInstitutionUnit().getName(),
		thesis.getDiscussed().getYear(), // publication year
		getAddress(RootDomainObject.getInstance().getInstitutionUnit()), // address
		thesis.getThesisAbstract(), null, // number of pages
		BundleUtil.getStringFromResourceBundle("resources.EnumerationResources", thesis.getLanguage() == null ? Language.getDefaultLanguage().name() : thesis.getLanguage().name()), // language
		getMonth(thesis), // publication month
		null, // year begin
		null, // month begin
		null); // url

	FileResultPermittedGroupType groupType;
	switch (thesis.getVisibility()) {
	case PUBLIC:
	    groupType = FileResultPermittedGroupType.PUBLIC;
	    break;
	case INTRANET:
	    groupType = FileResultPermittedGroupType.INSTITUTION;
	    break;
	default:
	    groupType = FileResultPermittedGroupType.INSTITUTION;
	    break;
	}

	Group group = ResearchResultDocumentFile.getPermittedGroup(groupType);
	publication.addDocumentFile(descriptor.getFilename(), descriptor.getFilename(), groupType, descriptor.getMimeType(),
		descriptor.getChecksum(), descriptor.getChecksumAlgorithm(), descriptor.getSize(), descriptor.getUniqueId(),
		group);

	publication.setThesis(thesis);
	author.addPersonRoleByRoleType(RoleType.RESEARCHER);
    }

    private String getAddress(Unit institutionUnit) {
	List<PhysicalAddress> addresses = institutionUnit.getPhysicalAddresses();

	if (addresses == null || addresses.isEmpty()) {
	    return null;
	}

	for (PhysicalAddress address : addresses) {
	    String addr = address.getAddress();

	    if (addr != null) {
		return addr;
	    }
	}

	return null;
    }

    private Month getMonth(Thesis thesis) {
	return Month.fromDateTime(thesis.getDiscussed());
    }

    private Collection<FileSetMetaData> getMetadata(Thesis thesis) {
	List<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();

	metaData.add(FileSetMetaData.createAuthorMeta(thesis.getStudent().getPerson().getName()));
	metaData.add(FileSetMetaData.createTitleMeta(thesis.getFinalTitle().getContent(thesis.getDissertation().getLanguage())));

	return metaData;
    }

    private VirtualPath getVirtualPath(Thesis thesis) {
	VirtualPathNode[] nodes = { new VirtualPathNode("Research", "Research"), new VirtualPathNode("Results", "Results"),
		new VirtualPathNode("Publications", "Publications") };

	VirtualPath path = new VirtualPath();
	for (VirtualPathNode node : nodes) {
	    path.addNode(node);
	}

	return path;
    }

    @Override
    protected Collection<Person> getReceivers(Thesis thesis) {
	Person student = thesis.getStudent().getPerson();
	Person president = getPerson(thesis.getPresident());

	Set<Person> persons = personSet(student, president);

	ExecutionYear executionYear = thesis.getEnrolment().getExecutionYear();
	for (ScientificCommission member : thesis.getDegree().getScientificCommissionMembers(executionYear)) {
	    if (member.isContact()) {
		persons.add(member.getPerson());
	    }
	}

	return persons;
    }

    @Override
    protected String getMessage(Thesis thesis) {
	Person currentPerson = AccessControl.getPerson();
	ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

	String title = thesis.getTitle().getContent();
	String year = executionYear.getYear();
	String degreeName = thesis.getDegree().getNameFor(executionYear).getContent();
	String studentName = thesis.getStudent().getPerson().getName();
	String studentNumber = thesis.getStudent().getNumber().toString();

	String date = String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", new Date());
	String currentPersonName = currentPerson.getNickname();

	return getMessage(BODY_KEY, year, degreeName, studentName, studentNumber, date, currentPersonName);
    }

    @Override
    protected String getSubject(Thesis thesis) {
	return getMessage(SUBJECT_KEY, thesis.getTitle().getContent());
    }

}
