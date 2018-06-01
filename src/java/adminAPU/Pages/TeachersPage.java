package adminAPU.Pages;

import adminAPU.BasePage;
import adminAPU.Pages.AddPages.AddTeacherPage;
import com.alee.archive3.Archive3ServerConnector;
import com.alee.archive3.api.data.ArchiveDocument;
import com.alee.archive3.api.data.ArchiveObject;
import com.alee.archive3.api.data.AttributeValue;
import com.alee.archive3.api.data.CompleteCard;
import com.alee.archive3.api.exceptions.ArchiveSystemError;
import com.alee.archive3.api.exceptions.UnknownServiceException;
import com.alee.archive3.api.filetransfer.ArchiveFileUploader;
import com.alee.archive3.api.filetransfer.FileUploadListener;
import com.alee.archive3.api.network.Requisite;
import com.alee.archive3.api.search.AdvSearchableField;
import com.alee.archive3.api.search.FieldType;
import com.alee.archive3.api.search.MatchType;
import com.alee.archive3.api.search.SearchRequest;
import com.alee.archive3.api.ws.AttributeService;
import com.alee.archive3.api.ws.DataService;
import com.alee.archive3.api.ws.SearchService;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class TeachersPage extends BasePage {

    private static final String TYPE_OF_CARD_ATTR_ID = "ALSFR-41175b28-2f62-430c-8f35-a3f3f01a55a2";
    private static final String TEACHER_ID = "ALSFR-77f68663-6166-49e9-b949-be91a45f7be6";

    private static final String TEACHER_NAME_ATTR_ID = "ALSFR-40afdf23-e1fb-4e99-a59d-5bd5526172ca";
    private static final String POSITION_ATTR_ID = "ALSFR-ea86c66f-327c-4930-b226-b252406ac21b";
    private static final String ACADEMIC_DEGREE_ATTR_ID = "ALSFR-b74e7d7b-8bf6-45d1-90b9-2bfac5ea164b";
    private static final String EDUCATION_LEVEL_ATTR_ID = "ALSFR-259f88b7-5fd3-4f04-bba9-8081026a29d6";
    private static final String PHONE_ATTR_ID = "ALSFR-9adef902-62c5-4f10-a09b-6124247aae0e";
    private static final String EMAIL_ATTR_ID = "ALSFR-2c56c9c9-3ec0-448d-b19c-29671f150ae2";
    private static final String SHORT_DESCRIPTION_ATTR_ID = "ALSFR-75c837fd-6557-417d-9971-8fe8f7af13b8";

    private String teacherNameValue;
    private String positionValue;
    private String academicDegreeValue;
    private List<String> educationLevelsValues;
    private String phoneValue;
    private String emailValue;
    private String shortDescriptionValue;

    private static final String DOCTOR_OF_TECHNICAL_SCIENCES_ID = "ALSFR-b8ad7bbc-4708-4031-8b53-1fccb3927af1";
    private static final String CANDIDATE_OF_TECHNICAL_SCIENCES_ID = "ALSFR-88134f22-f9f8-422f-b4a9-95dbf7bc237f";

    private static final List<String> academicDegreesList = Arrays.asList("Доктор технических наук", "Кандидат технических наук", "Нет");

    private static final String BACCALAUREATE_ID = "ALSFR-9fc82a1a-fa4d-4ab0-a53e-3e5d538161dd";
    private static final String MAGISTRACY_ID = "ALSFR-1d6ba1bf-5472-488b-934b-98b81b82eb1a";

    private boolean baccalaureateCheckboxValue;
    private boolean magistracyCheckboxValue;

    private List<String> teachersNames;
    private String selectedTeacherName;
    private String selectedAcademicDegree;
    private List<String> documentsNames;
    private String selectedDocumentName;

    private ArchiveObject selectedTeacherArchiveObject;

    private FileUploadField fileUploadField;

    public TeachersPage(PageParameters pageParameters) {

        selectedTeacherName = pageParameters.get("selectedTeacherName").toString();

        final Archive3ServerConnector archive3ServerConnector = new Archive3ServerConnector("apu.su", 80);
        final Requisite requisite = new Requisite();
        requisite.instanceRichSettings();
        archive3ServerConnector.authenticate("admin", "ApU_36oe", requisite);

        final DataService dataService = archive3ServerConnector.getDataService();
        final AttributeService attributeService = archive3ServerConnector.getAttributeService();

        List<ArchiveObject> archiveTeachers = findTeachers(archive3ServerConnector);
        Collections.sort(archiveTeachers, (ArchiveObject ao1, ArchiveObject ao2) -> ao1.getName().compareTo(ao2.getName()));

        teachersNames = archiveTeachers.stream().map(searchResult -> searchResult.getName()).collect(Collectors.toList());

        ListChoice<String> teachersNamesList = new ListChoice<>("teachersNamesList", new PropertyModel<String>(this, "selectedTeacherName"), teachersNames);
        teachersNamesList.setMaxRows(15);

        CompleteCard selectedTeacherCard = retrieveTeacherCard(dataService, archiveTeachers, pageParameters);

        List<AttributeValue> attributesList = selectedTeacherCard.getAttributes();
        Map<String, List<AttributeValue>> attributeMap = new HashMap(attributesList.stream().collect(Collectors.groupingBy(AttributeValue::getAttributeId)));

        retrieveAttributesValues(attributeMap);

        List<ArchiveDocument> archiveDocuments = selectedTeacherCard.getDocuments();
        Collections.sort(archiveDocuments, (ArchiveObject ao1, ArchiveObject ao2) -> ao1.getName().compareTo(ao2.getName()));

        documentsNames = archiveDocuments.stream().map(searchResult -> searchResult.getName()).collect(Collectors.toList());
        if (!documentsNames.isEmpty()) {
        selectedDocumentName = documentsNames.get(0);
        }
        ListChoice<String> documentsNamesList = new ListChoice<>("documentsNamesList", new PropertyModel<String>(this, "selectedDocumentName"), documentsNames);
        documentsNamesList.setMaxRows(2);

        final TextField<String> teacherName = new TextField<String>("teacherName", new PropertyModel<String>(this, "teacherNameValue"));
        final TextField<String> position = new TextField<String>("position", new PropertyModel<String>(this, "positionValue"));
        final RadioChoice<String> academicDegrees = new RadioChoice<String>("academicDegrees", new PropertyModel<String>(this, "selectedAcademicDegree"), academicDegreesList);
        final CheckBox baccalaureateCheckbox = new CheckBox("baccalaureateCheckbox", new PropertyModel<>(this, "baccalaureateCheckboxValue"));
        final CheckBox magistracyCheckbox = new CheckBox("magistracyCheckbox", new PropertyModel<>(this, "magistracyCheckboxValue"));
        final TextField<String> phone = new TextField<String>("phone", new PropertyModel<String>(this, "phoneValue"));
        final TextField<String> email = new TextField<String>("email", new PropertyModel<String>(this, "emailValue"));
        final TextArea<String> shortDescription = new TextArea<String>("shortDescription", new PropertyModel<String>(this, "shortDescriptionValue"));

        Form<?> teachersNamesForm = new Form<Void>("teachersNamesForm") {
            @Override
            public void onSubmit() {
                archive3ServerConnector.logoff();
                pageParameters.remove("selectedTeacherName");
                pageParameters.add("selectedTeacherName", selectedTeacherName);
                setResponsePage(TeachersPage.class, pageParameters);
            }
        };

        Form<?> addTeacherForm = new Form<Void>("addTeacherForm") {
            @Override
            public void onSubmit() {
                archive3ServerConnector.logoff();
                pageParameters.remove("selectedTeacherName");
                pageParameters.add("parentID", selectedTeacherArchiveObject.getParentObject());
                setResponsePage(AddTeacherPage.class, pageParameters);
            }
        };

        Form<?> selectedTeacherInfoForm = new Form<Void>("selectedTeacherInfoForm") {
            @Override
            public void onSubmit() {
                dataService.renameObject(selectedTeacherCard.getId(), teacherNameValue);

                attributeMap.get(TEACHER_NAME_ATTR_ID).get(0).setValue(teacherNameValue);
                attributeMap.get(POSITION_ATTR_ID).get(0).setValue(positionValue);

                if ("Доктор технических наук".equals(selectedAcademicDegree)) {
                    attributeMap.get(ACADEMIC_DEGREE_ATTR_ID).get(0).setValue(DOCTOR_OF_TECHNICAL_SCIENCES_ID);
                    attributeMap.get(ACADEMIC_DEGREE_ATTR_ID).get(0).setStringValue("Доктор технических наук");
                }
                if ("Кандидат технических наук".equals(selectedAcademicDegree)) {
                    attributeMap.get(ACADEMIC_DEGREE_ATTR_ID).get(0).setValue(CANDIDATE_OF_TECHNICAL_SCIENCES_ID);
                    attributeMap.get(ACADEMIC_DEGREE_ATTR_ID).get(0).setStringValue("Кандидат технических наук");
                }
                if ("Нет".equals(selectedAcademicDegree)) {
                    attributeMap.get(ACADEMIC_DEGREE_ATTR_ID).get(0).setValue(null);
                }

                attributeMap.put(EDUCATION_LEVEL_ATTR_ID, new ArrayList<AttributeValue>());
                if (baccalaureateCheckboxValue) {
                    AttributeValue baccalaureateAttribute = new AttributeValue(EDUCATION_LEVEL_ATTR_ID, BACCALAUREATE_ID);
                    baccalaureateAttribute.setStringValue("Бакалавриат");
                    attributeMap.get(EDUCATION_LEVEL_ATTR_ID).add(baccalaureateAttribute);
                }
                if (magistracyCheckboxValue) {
                    AttributeValue magistracyAttribute = new AttributeValue(EDUCATION_LEVEL_ATTR_ID, MAGISTRACY_ID);
                    magistracyAttribute.setStringValue("Магистратура");
                    attributeMap.get(EDUCATION_LEVEL_ATTR_ID).add(magistracyAttribute);
                }

                attributeMap.get(PHONE_ATTR_ID).get(0).setValue(phoneValue);
                attributeMap.get(EMAIL_ATTR_ID).get(0).setValue(emailValue);
                attributeMap.get(SHORT_DESCRIPTION_ATTR_ID).get(0).setValue(shortDescriptionValue);

                List<AttributeValue> updatedAttributesList = attributeMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
                attributeService.setAttributeValuesForAnObject(selectedTeacherCard.getId(), updatedAttributesList);

                archive3ServerConnector.logoff();
                pageParameters.remove("selectedTeacherName");
                pageParameters.add("selectedTeacherName", teacherNameValue);
                setResponsePage(TeachersPage.class, pageParameters);
            }
        };

        Form<?> documentsForm = new Form<Void>("documentsForm") {
            @Override
            protected void onSubmit() {
                final ArchiveObject selectedDocumentArchiveObject = archiveDocuments.stream().filter(searchResult -> searchResult.getName().equals(selectedDocumentName)).findFirst().orElse(null);
                dataService.deleteObjects(selectedDocumentArchiveObject.getObjectId());
                archive3ServerConnector.logoff();
                setResponsePage(TeachersPage.class, pageParameters);
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
                    document.setParentObject(selectedTeacherCard.getId());
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
                    setResponsePage(TeachersPage.class, pageParameters);
                }
            }
        };

        Form<?> deleteTeacherForm = new Form<Void>("deleteTeacherForm") {
            @Override
            public void onSubmit() {
                dataService.deleteObjects(selectedTeacherCard.getId());
                archive3ServerConnector.logoff();
                pageParameters.remove("selectedTeacherName");
                setResponsePage(TeachersPage.class, pageParameters);
            }
        };

        add(teachersNamesForm);
        teachersNamesForm.add(teachersNamesList);

        add(addTeacherForm);

        add(selectedTeacherInfoForm);
        selectedTeacherInfoForm.add(teacherName);
        selectedTeacherInfoForm.add(position);
        selectedTeacherInfoForm.add(academicDegrees);
        selectedTeacherInfoForm.add(baccalaureateCheckbox);
        selectedTeacherInfoForm.add(magistracyCheckbox);
        selectedTeacherInfoForm.add(phone);
        selectedTeacherInfoForm.add(email);
        selectedTeacherInfoForm.add(shortDescription);

        add(documentsForm);
        documentsForm.add(documentsNamesList);
        add(uploadDocument);
        uploadDocument.add(fileUploadField = new FileUploadField("fileUploadField"));

        add(deleteTeacherForm);

    }

    private List<ArchiveObject> findTeachers(final Archive3ServerConnector archive3ServerConnector) throws UnknownServiceException {

        final SearchService searchService = archive3ServerConnector.getSearchService();
        final SearchRequest searchRequest = new SearchRequest();

        final ArrayList<AdvSearchableField> advSearchableFields = new ArrayList<>();
        final AdvSearchableField asf = new AdvSearchableField();
        asf.setFieldType(FieldType.ATTRIBUTE);
        asf.setMatchType(MatchType.EXACT);
        asf.setFieldName(TYPE_OF_CARD_ATTR_ID);
        asf.setFieldConditions(Collections.singletonList(TEACHER_ID));
        advSearchableFields.add(asf);

        searchRequest.setFieldList(advSearchableFields);
        final List<ArchiveObject> archiveTeachers = searchService.performSearch(searchRequest);
        return archiveTeachers;

    }

    private CompleteCard retrieveTeacherCard(final DataService dataService, final List<ArchiveObject> archiveTeachers, PageParameters pageParameters) throws ArchiveSystemError {

        CompleteCard selectedTeacherCard;

        if (selectedTeacherName != null) {
            selectedTeacherArchiveObject = archiveTeachers.stream().filter(searchResult -> searchResult.getName().equals(selectedTeacherName)).findFirst().orElse(null);
            selectedTeacherCard = dataService.getCompleteCard(selectedTeacherArchiveObject.getObjectId());
        } else {
            selectedTeacherName = archiveTeachers.get(0).getName();
            pageParameters.add("selectedTeacherName", selectedTeacherName);
            selectedTeacherArchiveObject = archiveTeachers.get(0);
            selectedTeacherCard = dataService.getCompleteCard(selectedTeacherArchiveObject.getObjectId());
        }

        return selectedTeacherCard;

    }

    private void retrieveAttributesValues(Map<String, List<AttributeValue>> attributeMap) {

        teacherNameValue = attributeMap.get(TEACHER_NAME_ATTR_ID).get(0).getValue();
        positionValue = attributeMap.get(POSITION_ATTR_ID).get(0).getValue();
        academicDegreeValue = attributeMap.get(ACADEMIC_DEGREE_ATTR_ID).get(0).getValue();
        educationLevelsValues = attributeMap.get(EDUCATION_LEVEL_ATTR_ID).stream().map(AttributeValue::getValue).collect(Collectors.toList());
        phoneValue = attributeMap.get(PHONE_ATTR_ID).get(0).getValue();
        emailValue = attributeMap.get(EMAIL_ATTR_ID).get(0).getValue();
        shortDescriptionValue = attributeMap.get(SHORT_DESCRIPTION_ATTR_ID).get(0).getValue();

        if (academicDegreeValue.equals(DOCTOR_OF_TECHNICAL_SCIENCES_ID)) {
            selectedAcademicDegree = "Доктор технических наук";
        }
        if (academicDegreeValue.equals(CANDIDATE_OF_TECHNICAL_SCIENCES_ID)) {
            selectedAcademicDegree = "Кандидат технических наук";
        }
        if (academicDegreeValue.isEmpty()) {
            selectedAcademicDegree = "Нет";
        }

        if (educationLevelsValues.contains(BACCALAUREATE_ID)) {
            baccalaureateCheckboxValue = true;
        }
        if (educationLevelsValues.contains(MAGISTRACY_ID)) {
            magistracyCheckboxValue = true;
        }

    }

}