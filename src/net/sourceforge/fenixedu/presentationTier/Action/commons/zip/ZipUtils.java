package net.sourceforge.fenixedu.presentationTier.Action.commons.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionBatch;

public class ZipUtils {

	public void createAndFlushArchive(Set<AcademicServiceRequest> requestsToZip, HttpServletResponse response,
			RectorateSubmissionBatch batch) {
		try {
			Set<String> usedNames = new HashSet<String>();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ZipOutputStream zip = new ZipOutputStream(bout);
			for (AcademicServiceRequest document : requestsToZip) {
				String filename = document.getLastGeneratedDocument().getFilename();
				if (usedNames.contains(filename)) {
					filename = filename + "_1";
				}
				usedNames.add(filename);
				zip.putNextEntry(new ZipEntry(filename));
				zip.write(document.getLastGeneratedDocument().getContents());
				zip.closeEntry();
			}
			zip.close();
			response.setContentType("application/zip");
			response.addHeader("Content-Disposition", "attachment; filename=documentos-" + batch.getRange() + ".zip");
			ServletOutputStream writer = response.getOutputStream();
			writer.write(bout.toByteArray());
			writer.flush();
			writer.close();
			response.flushBuffer();

		} catch (IOException e) {
			throw new DomainException("error.rectorateSubmission.errorGeneratingMetadata", e);
		}
	}
}
