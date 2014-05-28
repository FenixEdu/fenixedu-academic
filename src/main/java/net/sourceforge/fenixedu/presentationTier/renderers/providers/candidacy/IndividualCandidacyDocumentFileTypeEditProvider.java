/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class IndividualCandidacyDocumentFileTypeEditProvider {

    public static class DegreeChangeOrTransferIndividualCandidacyDocumentFileTypeEditProvider implements DataProvider {

        @Override
        public Converter getConverter() {
            return null;
        }

        @Override
        public Object provide(Object source, Object current) {
            List<IndividualCandidacyDocumentFileType> fileTypesList = new ArrayList<IndividualCandidacyDocumentFileType>();

            CandidacyProcessDocumentUploadBean uploadBean = (CandidacyProcessDocumentUploadBean) source;
            IndividualCandidacyProcess individualCandidacyProcess = uploadBean.getIndividualCandidacyProcess();

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(
                    IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.REGISTRATION_CERTIFICATE)
                    .size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.REGISTRATION_CERTIFICATE);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.NO_PRESCRIPTION_CERTIFICATE)
                    .size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.NO_PRESCRIPTION_CERTIFICATE);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(
                    IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_CERTIFICATE).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_CERTIFICATE);
            }

            if (individualCandidacyProcess.getAllFilesForType(
                    IndividualCandidacyDocumentFileType.FOREIGN_INSTITUTION_SCALE_CERTIFICATE).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.FOREIGN_INSTITUTION_SCALE_CERTIFICATE);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.GRADES_DOCUMENT).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.GRADES_DOCUMENT);
            }

            return fileTypesList;
        }
    }

    public static class DegreeCandidacyForGraduatedPersonIndividualCandidacyDocumentFileTypeEditProvider implements DataProvider {

        @Override
        public Converter getConverter() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object provide(Object source, Object current) {
            List<IndividualCandidacyDocumentFileType> fileTypesList = new ArrayList<IndividualCandidacyDocumentFileType>();

            CandidacyProcessDocumentUploadBean uploadBean = (CandidacyProcessDocumentUploadBean) source;
            IndividualCandidacyProcess individualCandidacyProcess = uploadBean.getIndividualCandidacyProcess();

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(
                    IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.DEGREE_CERTIFICATE).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.DEGREE_CERTIFICATE);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(
                    IndividualCandidacyDocumentFileType.FOREIGN_INSTITUTION_SCALE_CERTIFICATE).size() <= 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.FOREIGN_INSTITUTION_SCALE_CERTIFICATE);
            }

            fileTypesList.add(IndividualCandidacyDocumentFileType.REPORT_OR_WORK_DOCUMENT);

            return fileTypesList;
        }

    }

    public static class SecondCycleIndividualCandidacyDocumentFileTypeEditProvider implements DataProvider {

        @Override
        public Converter getConverter() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object provide(Object source, Object current) {
            List<IndividualCandidacyDocumentFileType> fileTypesList = new ArrayList<IndividualCandidacyDocumentFileType>();

            CandidacyProcessDocumentUploadBean uploadBean = (CandidacyProcessDocumentUploadBean) source;
            IndividualCandidacyProcess individualCandidacyProcess = uploadBean.getIndividualCandidacyProcess();

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT).size() < 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(
                    IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT).size() < 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION).size() < 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT).size() < 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT).size() < 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(
                    IndividualCandidacyDocumentFileType.FOREIGN_INSTITUTION_SCALE_CERTIFICATE).size() < 10) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.FOREIGN_INSTITUTION_SCALE_CERTIFICATE);
            }

            fileTypesList.add(IndividualCandidacyDocumentFileType.REPORT_OR_WORK_DOCUMENT);

            return fileTypesList;
        }

    }

    public static class ErasmusIndividualCandidacyDocumentFileTypeEditProvider implements DataProvider {

        @Override
        public Converter getConverter() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            List<IndividualCandidacyDocumentFileType> fileTypesList = new ArrayList<IndividualCandidacyDocumentFileType>();

            fileTypesList.add(IndividualCandidacyDocumentFileType.PHOTO);
            fileTypesList.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
            fileTypesList.add(IndividualCandidacyDocumentFileType.LEARNING_AGREEMENT);
            fileTypesList.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
            fileTypesList.add(IndividualCandidacyDocumentFileType.TRANSCRIPT_OF_RECORDS);
            fileTypesList.add(IndividualCandidacyDocumentFileType.ENGLISH_LEVEL_DECLARATION);

            return fileTypesList;
        }

    }

    public static class PublicErasmusIndividualCandidacyDocumentFileTypeEditProvider implements DataProvider {

        @Override
        public Converter getConverter() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            List<IndividualCandidacyDocumentFileType> fileTypesList = new ArrayList<IndividualCandidacyDocumentFileType>();

            CandidacyProcessDocumentUploadBean uploadBean = (CandidacyProcessDocumentUploadBean) source;
            IndividualCandidacyProcess individualCandidacyProcess = uploadBean.getIndividualCandidacyProcess();

            fileTypesList.add(IndividualCandidacyDocumentFileType.PHOTO);
            fileTypesList.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
            fileTypesList.add(IndividualCandidacyDocumentFileType.LEARNING_AGREEMENT);
            fileTypesList.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
            fileTypesList.add(IndividualCandidacyDocumentFileType.TRANSCRIPT_OF_RECORDS);
            fileTypesList.add(IndividualCandidacyDocumentFileType.ENGLISH_LEVEL_DECLARATION);

            return fileTypesList;
        }

    }

    public static class AllDocumentFileTypeEditProvider implements DataProvider {

        @Override
        public Converter getConverter() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object provide(Object source, Object current) {
            List<IndividualCandidacyDocumentFileType> fileTypesList = new ArrayList<IndividualCandidacyDocumentFileType>();

            CandidacyProcessDocumentUploadBean uploadBean = (CandidacyProcessDocumentUploadBean) source;
            IndividualCandidacyProcess individualCandidacyProcess = uploadBean.getIndividualCandidacyProcess();

            if (individualCandidacyProcess.getActiveFileForType(IndividualCandidacyDocumentFileType.PHOTO) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.PHOTO);
            }

            if (individualCandidacyProcess.getActiveFileForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
            }

            if (individualCandidacyProcess
                    .getActiveFileForType(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
            }

            if (individualCandidacyProcess.getActiveFileForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
            }

            if (individualCandidacyProcess.getActiveFileForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
            }

            if (individualCandidacyProcess.getActiveFileForType(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT);
            }

            if (individualCandidacyProcess.getActiveFileForType(IndividualCandidacyDocumentFileType.DEGREE_CERTIFICATE) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.DEGREE_CERTIFICATE);
            }

            if (individualCandidacyProcess.getActiveFileForType(IndividualCandidacyDocumentFileType.REGISTRATION_CERTIFICATE) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.REGISTRATION_CERTIFICATE);
            }

            if (individualCandidacyProcess.getActiveFileForType(IndividualCandidacyDocumentFileType.NO_PRESCRIPTION_CERTIFICATE) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.NO_PRESCRIPTION_CERTIFICATE);
            }

            if (individualCandidacyProcess
                    .getActiveFileForType(IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_CERTIFICATE) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_CERTIFICATE);
            }

            if (individualCandidacyProcess
                    .getActiveFileForType(IndividualCandidacyDocumentFileType.FOREIGN_INSTITUTION_SCALE_CERTIFICATE) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.FOREIGN_INSTITUTION_SCALE_CERTIFICATE);
            }

            if (individualCandidacyProcess.getActiveFileForType(IndividualCandidacyDocumentFileType.GRADES_DOCUMENT) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.GRADES_DOCUMENT);
            }

            fileTypesList.add(IndividualCandidacyDocumentFileType.REPORT_OR_WORK_DOCUMENT);

            return fileTypesList;
        }

    }

    public static class Over23IndividualCandidacyDocumentFileTypeEditProvider implements DataProvider {

        @Override
        public Converter getConverter() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object provide(Object source, Object current) {
            List<IndividualCandidacyDocumentFileType> fileTypesList = new ArrayList<IndividualCandidacyDocumentFileType>();

            CandidacyProcessDocumentUploadBean uploadBean = (CandidacyProcessDocumentUploadBean) source;
            IndividualCandidacyProcess individualCandidacyProcess = uploadBean.getIndividualCandidacyProcess();

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.PHOTO).size() < 3) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.PHOTO);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT).size() < 3) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(
                    IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT).size() < 3) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION).size() < 3) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
            }

            if (individualCandidacyProcess.getActiveFileForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT) == null) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT).size() < 3) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.VAT_CARD_DOCUMENT);
            }

            if (individualCandidacyProcess.getAllFilesForType(IndividualCandidacyDocumentFileType.DEGREE_CERTIFICATE).size() < 3) {
                fileTypesList.add(IndividualCandidacyDocumentFileType.DEGREE_CERTIFICATE);
            }

            fileTypesList.add(IndividualCandidacyDocumentFileType.REPORT_OR_WORK_DOCUMENT);

            return fileTypesList;
        }

    }

}
