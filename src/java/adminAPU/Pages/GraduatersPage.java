package adminAPU.Pages;

import adminAPU.BasePage;
import adminAPU.Pages.AddPages.AddGraduaterPage;
import com.alee.archive3.Archive3ServerConnector;
import com.alee.archive3.api.data.ArchiveDocument;
import com.alee.archive3.api.data.ArchiveObject;
import com.alee.archive3.api.data.AttributeValue;
import com.alee.archive3.api.data.Card;
import com.alee.archive3.api.data.CompleteCard;
import com.alee.archive3.api.exceptions.ArchiveSystemError;
import com.alee.archive3.api.filetransfer.ArchiveFileUploader;
import com.alee.archive3.api.filetransfer.FileUploadListener;
import com.alee.archive3.api.network.Requisite;
import com.alee.archive3.api.ws.AttributeService;
import com.alee.archive3.api.ws.DataService;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class GraduatersPage extends BasePage {

    private static final String BACCALAUREATE_GRADUATERS_CARD_ID = "ALSFR-c1a1e038-0a06-4dc3-9a4a-e35eadc940ad";
    private static final String MAGISTRACY_GRADUATERS_CARD_ID = "ALSFR-2d8a6711-04db-40e8-a3f2-0d1e8d2293a1";

    private String educationLevel;

    private static final String GRADUATER_NAME_ATTR_ID = "ALSFR-1565d16b-3605-4d5f-b5b9-699425df3491";
    private static final String GRADUATION_WORK_NAME_ATTR_ID = "ALSFR-474fa9c9-b8e0-4c03-8008-6434df409b8a";
    private static final String TEACHER_NAME_ATTR_ID = "ALSFR-d5ed5c97-b216-4036-92fc-d511681ad6e1";
    private static final String ALT_TEACHER_NAME_ATTR_ID = "ALSFR-3e38cc5d-a730-4be2-9a7f-1ed887234c6d";
    private static final String JOB_PLACE_ATTR_ID = "ALSFR-aeb8fbb6-d691-4dce-8215-548951c52632";
    private static final String POSITION_ATTR_ID = "ALSFR-747d24dc-4437-4978-992c-5efbb0fbb70d";
    private static final String SHORT_REFERENCE_ATTR_ID = "ALSFR-02c8d4e0-ce02-4ddb-ad5d-095f0e358ddc";

    private String graduaterNameValue;
    private String graduationWorkNameValue;
    private String teacherNameValue;
    private String altTeacherNameValue;
    private String jobPlaceValue;
    private String positionValue;
    private String shortReferenceValue;

    private String newYearValue;
    private Map<String, String> unsortedArchiveYears;
    private List<String> years;
    private String selectedYear;
    private Map<String, String> unsortedArchiveGraduaters;
    private List<String> graduatersNames;
    private String selectedGraduaterName;

    private List<String> documentsNames;
    private String selectedDocumentName;

    private FileUploadField fileUploadField;

    private Card card;
    private Card defaultNewGraduaterCard;

    public GraduatersPage(PageParameters pageParameters) {

        educationLevel = pageParameters.get("educationLevel").toString();
        selectedYear = pageParameters.get("selectedYear").toString();
        selectedGraduaterName = pageParameters.get("selectedGraduaterName").toString();
        final Label educationLevelLabel = new Label("educationLevelLabel", educationLevel);
        final TextField<String> newYear = new TextField<>("newYear", new PropertyModel<>(this, "newYearValue"));

        final Archive3ServerConnector archive3ServerConnector = new Archive3ServerConnector("apu.su", 80);
        final Requisite requisite = new Requisite();
        requisite.instanceRichSettings();
        archive3ServerConnector.authenticate("admin", "ApU_36oe", requisite);

        final DataService dataService = archive3ServerConnector.getDataService();
        final AttributeService attributeService = archive3ServerConnector.getAttributeService();

        if (educationLevel.equals("Бакалавриат")) {
            unsortedArchiveYears = dataService.getChildrenCards(BACCALAUREATE_GRADUATERS_CARD_ID);
        } else {
            unsortedArchiveYears = dataService.getChildrenCards(MAGISTRACY_GRADUATERS_CARD_ID);
        }

        unsortedArchiveYears = unsortedArchiveYears.entrySet().stream().collect(Collectors.toMap(entry -> entry.getValue(), entry -> entry.getKey()));
        Map<String, String> archiveYears = unsortedArchiveYears.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        years = new ArrayList<>(archiveYears.keySet());

        if (selectedYear == null) {
            selectedYear = years.get(0);
            pageParameters.add("selectedYear", selectedYear);
        }
        final Label selectedYearLabel = new Label("selectedYearLabel", selectedYear);

        ListChoice<String> yearsList = new ListChoice<>("yearsList", new PropertyModel<String>(this, "selectedYear"), years);
        yearsList.setMaxRows(5);

        unsortedArchiveGraduaters = dataService.getChildrenCards(archiveYears.get(selectedYear));

        unsortedArchiveGraduaters = unsortedArchiveGraduaters.entrySet().stream().collect(Collectors.toMap(entry -> entry.getValue(), entry -> entry.getKey()));
        Map<String, String> archiveGraduaters = unsortedArchiveGraduaters.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        graduatersNames = new ArrayList<>(archiveGraduaters.keySet());
        ListChoice<String> graduatersNamesList = new ListChoice<>("graduatersNamesList", new PropertyModel<String>(this, "selectedGraduaterName"), graduatersNames);
        graduatersNamesList.setMaxRows(10);

        CompleteCard selectedGraduaterCard = retrieveGraduaterCard(dataService, archiveGraduaters, pageParameters);

        List<AttributeValue> attributesList = selectedGraduaterCard != null ? selectedGraduaterCard.getAttributes() : new ArrayList<>();
        Map<String, List<AttributeValue>> attributeMap = new HashMap(attributesList.stream().collect(Collectors.groupingBy(AttributeValue::getAttributeId)));

        retrieveAttributesValues(attributeMap);

        List<ArchiveDocument> archiveDocuments = selectedGraduaterCard != null ? selectedGraduaterCard.getDocuments() : new ArrayList<>();
        Collections.sort(archiveDocuments, (ArchiveObject ao1, ArchiveObject ao2) -> ao1.getName().compareTo(ao2.getName()));

        documentsNames = archiveDocuments.stream().map(searchResult -> searchResult.getName()).collect(Collectors.toList());
        if (!documentsNames.isEmpty()) {
            selectedDocumentName = documentsNames.get(0);
        }
        ListChoice<String> documentsNamesList = new ListChoice<>("documentsNamesList", new PropertyModel<String>(this, "selectedDocumentName"), documentsNames);
        documentsNamesList.setMaxRows(4);

        final TextField<String> graduaterName = new TextField<>("graduaterName", new PropertyModel<>(this, "graduaterNameValue"));
        final TextField<String> graduationWorkName = new TextField<>("graduationWorkName", new PropertyModel<>(this, "graduationWorkNameValue"));
        final TextField<String> teacherName = new TextField<>("teacherName", new PropertyModel<>(this, "teacherNameValue"));
        final TextField<String> altTeacherName = new TextField<>("altTeacherName", new PropertyModel<>(this, "altTeacherNameValue"));
        final TextField<String> jobPlace = new TextField<>("jobPlace", new PropertyModel<>(this, "jobPlaceValue"));
        final TextField<String> position = new TextField<>("position", new PropertyModel<>(this, "positionValue"));
        final TextArea<String> shortReference = new TextArea<>("shortReference", new PropertyModel<>(this, "shortReferenceValue"));

        Form<?> addNewYearForm = new Form<Void>("addNewYearForm") {
            @Override
            public void onSubmit() {
                if (!years.contains(newYearValue)) {
                    card = new Card(newYearValue);
                    if (educationLevel.equals("Бакалавриат")) {
                        card.setParentObject(BACCALAUREATE_GRADUATERS_CARD_ID);
                    } else {
                        card.setParentObject(MAGISTRACY_GRADUATERS_CARD_ID);
                    }
                    dataService.createCompleteCard(card, true, new ArrayList<>(), false);
                    pageParameters.remove("selectedGraduaterName");
                    pageParameters.remove("selectedYear");
                    pageParameters.add("selectedYear", newYearValue);
                    archive3ServerConnector.logoff();
                    setResponsePage(GraduatersPage.class, pageParameters);
                }
            }
        };

        Form<?> yearsForm = new Form<Void>("yearsForm") {
            @Override
            public void onSubmit() {
                pageParameters.remove("selectedGraduaterName");
                pageParameters.remove("selectedYear");
                pageParameters.add("selectedYear", selectedYear);
                archive3ServerConnector.logoff();
                setResponsePage(GraduatersPage.class, pageParameters);
            }
        };

        Form<?> deleteYearForm = new Form<Void>("deleteYearForm") {
            @Override
            public void onSubmit() {
                dataService.deleteObjects(archiveYears.get(selectedYear));
                pageParameters.remove("selectedGraduaterName");
                pageParameters.remove("selectedYear");
                archive3ServerConnector.logoff();
                setResponsePage(GraduatersPage.class, pageParameters);
            }
        };

        Form<?> graduatersNamesForm = new Form<Void>("graduatersNamesForm") {
            @Override
            public void onSubmit() {
                pageParameters.remove("selectedGraduaterName");
                pageParameters.add("selectedGraduaterName", selectedGraduaterName);
                archive3ServerConnector.logoff();
                setResponsePage(GraduatersPage.class, pageParameters);
            }
        };

        Form<?> addGraduaterForm = new Form<Void>("addGraduaterForm") {
            @Override
            public void onSubmit() {
                pageParameters.remove("selectedGraduaterName");
                pageParameters.add("parentID", archiveYears.get(selectedYear));
                archive3ServerConnector.logoff();
                setResponsePage(AddGraduaterPage.class, pageParameters);
            }
        };

        Form<?> selectedGraduaterInfoForm = new Form<Void>("selectedGraduaterInfoForm") {
            @Override
            public void onSubmit() {
                dataService.renameObject(selectedGraduaterCard.getId(), graduaterNameValue);

                attributeMap.get(GRADUATER_NAME_ATTR_ID).get(0).setValue(graduaterNameValue);
                attributeMap.get(GRADUATION_WORK_NAME_ATTR_ID).get(0).setValue(graduationWorkNameValue);
                attributeMap.get(TEACHER_NAME_ATTR_ID).get(0).setStringValue(teacherNameValue);
                attributeMap.get(ALT_TEACHER_NAME_ATTR_ID).get(0).setValue(altTeacherNameValue);
                attributeMap.get(JOB_PLACE_ATTR_ID).get(0).setValue(jobPlaceValue);
                attributeMap.get(POSITION_ATTR_ID).get(0).setValue(positionValue);
                attributeMap.get(SHORT_REFERENCE_ATTR_ID).get(0).setValue(shortReferenceValue);

                List<AttributeValue> updatedAttributesList = attributeMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
                attributeService.setAttributeValuesForAnObject(selectedGraduaterCard.getId(), updatedAttributesList);

                pageParameters.remove("selectedGraduaterName");
                pageParameters.add("selectedGraduaterName", graduaterNameValue);
                archive3ServerConnector.logoff();
                setResponsePage(GraduatersPage.class, pageParameters);
            }
        };

        Form<?> documentsForm = new Form<Void>("documentsForm") {
            @Override
            protected void onSubmit() {
                final ArchiveObject selectedDocumentArchiveObject = archiveDocuments.stream().filter(searchResult -> searchResult.getName().equals(selectedDocumentName)).findFirst().orElse(null);
                dataService.deleteObjects(selectedDocumentArchiveObject.getObjectId());
                archive3ServerConnector.logoff();
                setResponsePage(GraduatersPage.class, pageParameters);
            }
        };

        Form<?> uploadDocument = new Form<Void>("uploadDocument") {
            @Override
            protected void onSubmit() {
                final FileUpload fileUpload = fileUploadField.getFileUpload();
                if (fileUpload != null) {
                    File file = new File(fileUpload.getClientFileName());

                    if (file.exists()) {
                        file.delete();
                    }

                    try {
                        file.createNewFile();
                        fileUpload.writeTo(file);
                    } catch (Exception e) {
                        throw new RuntimeException("Error creating new file.", e);
                    }

                    final List<ArchiveDocument> documents = new ArrayList<>();
                    ArchiveDocument document = new ArchiveDocument();
                    document.setName(fileUpload.getClientFileName());
                    document.setParentObject(selectedGraduaterCard.getId());
                    document.setDocumentURL(file.getName());
                    document.setDocumentSize(file.length());
                    document = (ArchiveDocument) archive3ServerConnector.getDataService().createArchiveObject(document);
                    documents.add(document);

                    final String sessionID = archive3ServerConnector.getDocumentService().openSession(documents);
                    final ArchiveFileUploader uploader = new ArchiveFileUploader(archive3ServerConnector);

                    try {
                        uploader.uploadFile(new File(document.getDocumentURL()), document, sessionID, new FileUploadListener() {
                        });
                    } catch (Exception e) {
                        throw new RuntimeException("Error uploading new file.", e);
                    } finally {
                        file.delete();
                    }

                    archive3ServerConnector.logoff();
                    setResponsePage(GraduatersPage.class, pageParameters);
                }
            }
        };

        Form<?> deleteGraduaterForm = new Form<Void>("deleteGraduaterForm") {
            @Override
            public void onSubmit() {
                dataService.deleteObjects(selectedGraduaterCard.getId());
                pageParameters.remove("selectedGraduaterName");
                archive3ServerConnector.logoff();
                setResponsePage(GraduatersPage.class, pageParameters);
            }
        };

        add(addNewYearForm);
        addNewYearForm.add(educationLevelLabel);
        addNewYearForm.add(selectedYearLabel);
        addNewYearForm.add(newYear);

        add(yearsForm);
        yearsForm.add(yearsList);

        add(deleteYearForm);

        add(graduatersNamesForm);
        graduatersNamesForm.add(graduatersNamesList);

        add(addGraduaterForm);

        add(selectedGraduaterInfoForm);
        selectedGraduaterInfoForm.add(graduaterName);
        selectedGraduaterInfoForm.add(graduationWorkName);
        selectedGraduaterInfoForm.add(teacherName);
        selectedGraduaterInfoForm.add(altTeacherName);
        selectedGraduaterInfoForm.add(jobPlace);
        selectedGraduaterInfoForm.add(position);
        selectedGraduaterInfoForm.add(shortReference);

        add(documentsForm);
        documentsForm.add(documentsNamesList);
        add(uploadDocument);
        uploadDocument.add(fileUploadField = new FileUploadField("fileUploadField"));

        add(deleteGraduaterForm);

    }

    private CompleteCard retrieveGraduaterCard(final DataService dataService, final Map<String, String> archiveGraduaters, PageParameters pageParameters) throws ArchiveSystemError {

        CompleteCard selectedGraduaterCard;

        if (archiveGraduaters.isEmpty()) {
            return null;
        }
        if (selectedGraduaterName != null) {
            selectedGraduaterCard = dataService.getCompleteCard(archiveGraduaters.get(selectedGraduaterName));
        } else {
            selectedGraduaterName = graduatersNames.get(0);
            pageParameters.add("selectedGraduaterName", selectedGraduaterName);
            selectedGraduaterCard = dataService.getCompleteCard(archiveGraduaters.get(selectedGraduaterName));
        }

        return selectedGraduaterCard;

    }

    private void retrieveAttributesValues(Map<String, List<AttributeValue>> attributeMap) {

        graduaterNameValue = attributeMap.get(GRADUATER_NAME_ATTR_ID) != null ? attributeMap.get(GRADUATER_NAME_ATTR_ID).get(0).getValue() : new String();
        graduationWorkNameValue = attributeMap.get(GRADUATION_WORK_NAME_ATTR_ID) != null ? attributeMap.get(GRADUATION_WORK_NAME_ATTR_ID).get(0).getValue() : new String();
        teacherNameValue = attributeMap.get(TEACHER_NAME_ATTR_ID) != null ? attributeMap.get(TEACHER_NAME_ATTR_ID).get(0).getStringValue() : new String();
        altTeacherNameValue = attributeMap.get(ALT_TEACHER_NAME_ATTR_ID) != null ? attributeMap.get(ALT_TEACHER_NAME_ATTR_ID).get(0).getValue() : new String();
        jobPlaceValue = attributeMap.get(JOB_PLACE_ATTR_ID) != null ? attributeMap.get(JOB_PLACE_ATTR_ID).get(0).getValue() : new String();
        positionValue = attributeMap.get(POSITION_ATTR_ID) != null ? attributeMap.get(POSITION_ATTR_ID).get(0).getValue() : new String();
        shortReferenceValue = attributeMap.get(SHORT_REFERENCE_ATTR_ID) != null ? attributeMap.get(SHORT_REFERENCE_ATTR_ID).get(0).getValue() : new String();

    }

}