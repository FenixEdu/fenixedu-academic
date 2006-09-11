package pt.utl.ist.domain;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.MetadataManager;

import dml.DmlCompiler;
import dml.DomainClass;
import dml.DomainEntity;
import dml.DomainModel;
import dml.Role;
import dml.Slot;

public class FindMissingOJBMappings {

	private static final Logger logger = Logger.getLogger(DomainDMLOJBVerifier.class);

	private static final Set<String> unmappedAttributes = new TreeSet<String>();

	private static final Set<String> unmappedObjectReferenceAttributes = new TreeSet<String>();

	private static final Set<String> unmappedCollectionReferenceAttributes = new TreeSet<String>();

	public static void main(final String[] args) throws antlr.ANTLRException, Exception {
		DomainModel domainModel = DmlCompiler.getFenixDomainModel(args);
		Map ojbMetadata = getDescriptorTable();

		verifyOJBMappingFromDomainModel(ojbMetadata, domainModel);

		logVerificationResults();
		logger.info("\nVerification complete.");
		System.exit(0);
	}

	protected static Map getDescriptorTable() {
		final MetadataManager metadataManager = MetadataManager.getInstance();
		final DescriptorRepository descriptorRepository = metadataManager.getGlobalRepository();
		return descriptorRepository.getDescriptorTable();
	}

	protected static void verifyOJBMappingFromDomainModel(final Map ojbMetadata, DomainModel model) throws Exception{
		for (final Iterator iterator = model.getClasses(); iterator.hasNext();) {
			final DomainClass domClass = (DomainClass) iterator.next();
			final ClassDescriptor classDescriptor = (ClassDescriptor) ojbMetadata.get(domClass
					.getFullName());

			if (classDescriptor == null) {
				logger.warn("MISSING " + domClass.getFullName() + " from OJB mappings");
			} else {
				verify(classDescriptor, domClass);
			}
		}
	}

	protected static void verify(final ClassDescriptor classDescriptor, final DomainClass domClass) throws Exception {
		DomainEntity domEntity = domClass;
		while (domEntity instanceof DomainClass) {
			DomainClass dClass = (DomainClass) domEntity;

			Class clazz = Class.forName(domEntity.getFullName());

			if (!Modifier.isAbstract(clazz.getModifiers())) {

				// attributes
				Iterator slots = dClass.getSlots();
				while (slots.hasNext()) {
					String slotName = ((Slot) slots.next()).getName();
					if (classDescriptor.getFieldDescriptorByName(slotName) == null) {
						unmappedAttributes.add(domClass.getFullName() + "." + slotName);
					}
				}

				// roles
				Iterator roleSlots = dClass.getRoleSlots();
				while (roleSlots.hasNext()) {
					Role role = (Role) roleSlots.next();
					String roleName = role.getName();
					if (role.getMultiplicityUpper() == 1) {
						// reference descriptors
						if (classDescriptor.getObjectReferenceDescriptorByName(roleName) == null) {
							unmappedObjectReferenceAttributes.add(domClass.getFullName() + "."
									+ roleName);
						}
					} else {
						// collection descriptors
						if (classDescriptor.getCollectionDescriptorByName(roleName) == null) {
							unmappedCollectionReferenceAttributes.add(domClass.getFullName() + "."
									+ roleName);
						}
					}
				}

			}
			domEntity = dClass.getSuperclass();
		}
	}

	protected static void logVerificationResults() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("\nFound ");
		stringBuilder.append(unmappedAttributes.size());
		stringBuilder.append(" attributes not mapped in OJB mapping: \n");
		logCollectionOfStrings(stringBuilder, unmappedAttributes);
		stringBuilder.append("\n");

		stringBuilder.append("\nFound ");
		stringBuilder.append(unmappedObjectReferenceAttributes.size());
		stringBuilder.append(" object reference attributes not mapped in OJB mapping: \n");
		logCollectionOfStrings(stringBuilder, unmappedObjectReferenceAttributes);
		stringBuilder.append("\n");

		stringBuilder.append("\nFound ");
		stringBuilder.append(unmappedCollectionReferenceAttributes.size());
		stringBuilder.append(" colletion reference attributes not mapped in OJB mapping: \n");
		logCollectionOfStrings(stringBuilder, unmappedCollectionReferenceAttributes);
		stringBuilder.append("\n");

		logger.warn(stringBuilder.toString());
	}

	protected static void logCollectionOfStrings(final StringBuilder stringBuilder,
			final Collection collection) {
		for (final Iterator iterator = collection.iterator(); iterator.hasNext();) {
			final String attribute = (String) iterator.next();
			stringBuilder.append(attribute);
			stringBuilder.append("\n");
		}
	}
}
