package net.sourceforge.fenixedu.domain.documents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

/**
 * {@link GeneratedDocument}s are output files resulting of some process of the
 * application, all generated files are stored in dspace.
 * 
 * Generated documents have 3 core relations:
 * <ul>
 * <li>Addressee: Someone or something that is the interested party of the document, like a person who receives some certificate
 * document.</li>
 * <li>Operator: An employee who processes the document.</li>
 * <li>Source: A domain entity who feeds the information presented in the document. This one may not make sense in all cases, it
 * is only used when needed.</li>
 * </ul>
 * 
 * @author Pedro Santos (pmrsa)
 */
public abstract class GeneratedDocument extends GeneratedDocument_Base {
    protected static final String CONFIG_DSPACE_DOCUMENT_STORE = "dspace.generated.document.store";

    private static final String ROOT_DIR_DESCRIPTION = "Generated Documents";

    private static final String ROOT_DIR = "GeneratedDocuments";

    private static final String NO_ADDRESSEE_NODE = "NoAddressee";

    private static final String NO_ADDRESSEE_NODE_DESCRIPTION = "No Addressee";

    public GeneratedDocument() {
        super();
    }

    protected void init(GeneratedDocumentType type, Party addressee, Person operator, String filename, byte[] content) {
        setType(type);
        setAddressee(addressee);
        setOperator(operator);
        init(getVirtualPath(), filename, filename, createMetaData(operator, filename), content, computePermittedGroup());
    }

    @Override
    public void delete() {
        removeAddressee();
        removeOperator();
        super.delete();
    }

    @Override
    public boolean isPersonAllowedToAccess(Person person) {
        if (person == null) {
            return false;
        }
        if (person.equals(getOperator())) {
            return true;
        }
        if (person.equals(getAddressee())) {
            return true;
        }
        return super.isPersonAllowedToAccess(person);
    }

    protected VirtualPath getVirtualPath() {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode(ROOT_DIR, ROOT_DIR_DESCRIPTION));
        if (getAddressee() != null) {
            filePath.addNode(new VirtualPathNode(getAddressee().getExternalId().toString(), getAddressee().getName()));
        } else {
            filePath.addNode(new VirtualPathNode(NO_ADDRESSEE_NODE, NO_ADDRESSEE_NODE_DESCRIPTION));
        }
        filePath.addNode(new VirtualPathNode(getType().name(), getType().name()));
        return filePath;
    }

    protected abstract Group computePermittedGroup();

    private Collection<FileSetMetaData> createMetaData(Person operator, String filename) {
        List<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();
        metaData.add(FileSetMetaData.createAuthorMeta(operator == null ? "script" : operator.getName()));
        metaData.add(FileSetMetaData.createTitleMeta(filename));
        return metaData;
    }

    public static final Comparator<GeneratedDocument> COMPARATOR_BY_UPLOAD_TIME = new Comparator<GeneratedDocument>() {

        @Override
        public int compare(GeneratedDocument o1, GeneratedDocument o2) {
            return o1.getUploadTime().compareTo(o2.getUploadTime());
        }

    };
}
