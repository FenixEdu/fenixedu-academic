package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Filtro.SiteManagerScormFileAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileContentCreationBean.EducationalResourceType;
import pt.ist.fenixframework.Atomic;
import pt.linkare.scorm.utils.ScormMetaDataHash;

public class CreateScormFile extends CreateFileContent {

    public static class CreateScormFileItemForItemArgs {
        private Site site;

        private Container container;

        private File file;

        private String originalFilename;

        private String displayName;

        private Group permittedGroup;

        private ScormMetaDataHash scormParameters;

        private Person person;

        private EducationalResourceType educationalResourceType;

        public CreateScormFileItemForItemArgs(Site site, Container container, File file, String originalFilename,
                String displayName, Group permittedGroup, ScormMetaDataHash scormParameters, Person person,
                EducationalResourceType educationalResourceType) {
            this.site = site;
            this.container = container;
            this.file = file;
            this.originalFilename = originalFilename;
            this.displayName = displayName;
            this.permittedGroup = permittedGroup;
            this.scormParameters = scormParameters;
            this.person = person;
            this.educationalResourceType = educationalResourceType;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public Container getContainer() {
            return container;
        }

        public void setContainer(Container container) {
            this.container = container;
        }

        public String getOriginalFilename() {
            return originalFilename;
        }

        public void setOriginalFilename(String originalFilename) {
            this.originalFilename = originalFilename;
        }

        public Group getPermittedGroup() {
            return permittedGroup;
        }

        public void setPermittedGroup(Group permittedGroup) {
            this.permittedGroup = permittedGroup;
        }

        public ScormMetaDataHash getScormParameters() {
            return scormParameters;
        }

        public void setScormParameters(ScormMetaDataHash scormParameters) {
            this.scormParameters = scormParameters;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public EducationalResourceType getEducationalResourceType() {
            return educationalResourceType;
        }

        public void setEducationalResourceType(EducationalResourceType resourceType) {
            this.educationalResourceType = resourceType;
        }

        public Site getSite() {
            return site;
        }

        public void setSite(Site site) {
            this.site = site;
        }

    }

    private ThreadLocal<ScormMetaDataHash> extraScormParam = null;

    protected void run(CreateScormFileItemForItemArgs args) throws FenixServiceException, DomainException, IOException {
        extraScormParam = new ThreadLocal<ScormMetaDataHash>();
        extraScormParam.set(args.getScormParameters());

        super.run(args.getSite(), args.getContainer(), args.getFile(), args.getOriginalFilename(), args.getDisplayName(),
                args.getPermittedGroup(), args.getPerson(), args.getEducationalResourceType());
    }

    // Service Invokers migrated from Berserk

    private static final CreateScormFile serviceInstance = new CreateScormFile();

    @Atomic
    public static void runCreateScormFile(CreateScormFileItemForItemArgs args) throws FenixServiceException, DomainException,
            IOException, NotAuthorizedException {
        SiteManagerScormFileAuthorizationFilter.instance.execute(args);
        serviceInstance.run(args);
    }

}