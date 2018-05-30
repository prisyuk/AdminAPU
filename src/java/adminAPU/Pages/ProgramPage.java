package adminAPU.Pages;

import adminAPU.BasePage;
import adminAPU.Pages.AddPages.AddSubjectPage;
import com.alee.archive3.Archive3ServerConnector;
import com.alee.archive3.api.data.ArchiveObject;
import com.alee.archive3.api.data.AttributeValue;
import com.alee.archive3.api.data.CompleteCard;
import com.alee.archive3.api.exceptions.ArchiveSystemError;
import com.alee.archive3.api.exceptions.UnknownServiceException;
import com.alee.archive3.api.network.Requisite;
import com.alee.archive3.api.search.AdvSearchableField;
import com.alee.archive3.api.search.FieldType;
import com.alee.archive3.api.search.MatchType;
import com.alee.archive3.api.search.SearchRequest;
import com.alee.archive3.api.ws.AttributeService;
import com.alee.archive3.api.ws.DataService;
import com.alee.archive3.api.ws.SearchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ProgramPage extends BasePage {

    private static final String TYPE_OF_CARD_ATTR_ID = "ALSFR-41175b28-2f62-430c-8f35-a3f3f01a55a2";
    private static final String SUBJECT_ID = "ALSFR-f9d5db5f-aa4b-4e24-8827-d9ed9b7ee940";
    private static final String EDUCATION_LEVEL_ATTR_ID = "ALSFR-df7e6510-ee01-4f6e-9835-ee5694745218";
    private static final String BACCALAUREATE_ID = "ALSFR-9fc82a1a-fa4d-4ab0-a53e-3e5d538161dd";
    private static final String MAGISTRACY_ID = "ALSFR-1d6ba1bf-5472-488b-934b-98b81b82eb1a";
    private static final String COURSE_ATTR_ID = "ALSFR-782a1dba-fb8e-4baa-ac4b-fe10e3d3bf1c";
    private static final String SEMESTER_ATTR_ID = "ALSFR-9e82551f-e16e-40c1-ad36-16d75a3856e9";

    private String educationLevel;
    private String course;
    private String semester;

    private static final String SUBJECT_NAME_ATTR_ID = "ALSFR-baf2df7f-82ad-4098-88b2-5c11c0689118";
    private static final String PULPIT_NAME_ATTR_ID = "ALSFR-d1c3a9ea-f67c-4337-b8b1-81341f33e818";
    private static final String CYCLE_ATTR_ID = "ALSFR-f734d9c9-be9c-44ef-a3d5-046219d94d96";
    private static final String FORM_OF_PASSING_ATTR_ID = "ALSFR-e012081d-3411-494e-8af9-0aeac88ec64e";
    private static final String CLASS_HOURS_ATTR_ID = "ALSFR-0df2c762-4ed6-4f83-a734-7e02d3c95282";
    private static final String SELF_HOURS_ATTR_ID = "ALSFR-268b9c2c-682e-48eb-89bb-955a0f49ad1b";
    private static final String TOTAL_DURATION_ATTR_ID = "ALSFR-fafeacfc-41eb-496a-92d9-26dfa61ea369";
    private static final String LECTURE_HOURS_PER_WEEK_ATTR_ID = "ALSFR-b60c4591-308f-41f0-8808-4e774575a2ea";
    private static final String PRACTICAL_HOURS_PER_WEEK_ATTR_ID = "ALSFR-196b98b5-0258-4246-a585-9bce4939473d";
    private static final String LABORATORY_HOURS_PER_WEEK_ATTR_ID = "ALSFR-333b0c66-27fc-4f8b-ac22-e6a76e7897fb";
    private static final String TOTAL_HOURS_PER_WEEK_ATTR_ID = "ALSFR-5a6e6bc4-c068-463e-a138-6050c6aaf7d3";
    private static final String SHORT_DESCRIPTION_ATTR_ID = "ALSFR-f06eed7b-1e86-4b5e-8c7e-987c94318509";

    private String subjectNameValue;
    private String pulpitNameValue;
    private String cycleValue;
    private List<String> formsOfPassingValues;
    private String classHoursValue;
    private String selfHoursValue;
    private String totalDurationValue;
    private String lectureHoursPerWeekValue;
    private String practicalHoursPerWeekValue;
    private String laboratoryHoursPerWeekValue;
    private String totalHoursPerWeekValue;
    private String shortDescriptionValue;

    private static final String TECHNOLOGICAL_CYCLE_ID = "ALSFR-fe184f45-4362-41d8-b2f2-ab0b523d299c";
    private static final String PROGRAM_ENGINEERING_ID = "ALSFR-05852842-b74c-4909-a3b4-45aa76051031";
    private static final String INFORMATION_SYSTEM_ID = "ALSFR-85a7f3f2-07f6-4a65-acdc-a9a2c1dfc877";
    private static final String PROJECT_MANAGEMENT_ID = "ALSFR-b8d2c78e-2e0e-42c2-850f-498f66b99312";
    private static final String SAFETY_AND_PROTECTION_ID = "ALSFR-0bfaddaa-f635-4f17-a5fe-ed20a60408d3";
    private static final String SYSTEM_ANALYSIS_ID = "ALSFR-8b2bd0bd-c00b-46bc-aa51-b1dec61c25ec";
    private static final String PRODUCT_MANAGEMENT_ID = "ALSFR-2dab8641-eaa5-4dea-a9d8-5efbfc092f1a";
    private static final String DESIGNING_THE_SYSTEM_ID = "ALSFR-41af0fd4-c5be-4db9-92b0-6a5380352dac";

    private static final List<String> cyclesList = Arrays.asList("Технологический цикл",
            "Программная инженерия",
            "Информационная система",
            "Управление проектом",
            "Безопасность и защита",
            "Системный анализ",
            "Управление продуктом",
            "Проектирование системы",
            "Другое");

    private static final String DIFFERENTIATED_CREDIT_ID = "ALSFR-63bec0ab-ecd9-47e8-8deb-6d396029f2f4";
    private static final String COURSE_PROJECT_ID = "ALSFR-026e25b8-2b0d-4bbc-afbc-c25bcf9ad132";
    private static final String COURSE_WORK_ID = "ALSFR-c398d05c-6cf1-45fb-8c2b-dcab353b23fe";
    private static final String EXAM_ID = "ALSFR-844ce3e0-ebec-4f42-946b-d26741f0a6ee";

    private boolean differentiatedCreditCheckboxValue;
    private boolean courseProjectCheckboxValue;
    private boolean courseWorkCheckboxValue;
    private boolean examCheckboxValue;

    private List<String> subjectsNames;
    private String selectedSubjectName;
    private String selectedCycle;

    public ProgramPage(PageParameters pageParameters) {

        selectedSubjectName = pageParameters.get("selectedSubjectName").toString();

        educationLevel = pageParameters.get("educationLevel").toString();
        course = pageParameters.get("course").toString();
        semester = pageParameters.get("semester").toString();
        final Label educationLevelLabel = new Label("educationLevelLabel", educationLevel);
        final Label courseLabel = new Label("courseLabel", course);
        final Label semesterLabel = new Label("semesterLabel", semester);

        final Archive3ServerConnector archive3ServerConnector = new Archive3ServerConnector("apu.su", 80);
        final Requisite requisite = new Requisite();
        requisite.instanceRichSettings();
        archive3ServerConnector.authenticate("admin", "ApU_36oe", requisite);

        final DataService dataService = archive3ServerConnector.getDataService();
        final AttributeService attributeService = archive3ServerConnector.getAttributeService();

        List<ArchiveObject> archiveSubjects = findSubjects(archive3ServerConnector);
        Collections.sort(archiveSubjects, new Comparator<ArchiveObject>() {
            public int compare(ArchiveObject ao1, ArchiveObject ao2) {
                return ao1.getName().compareTo(ao2.getName());
            }
        });

        subjectsNames = archiveSubjects.stream().map(searchResult -> searchResult.getName()).collect(Collectors.toList());
        ListChoice<String> subjectsNamesList = new ListChoice<>("subjectsNamesList", new PropertyModel<String>(this, "selectedSubjectName"), subjectsNames);
        subjectsNamesList.setMaxRows(10);

        CompleteCard selectedSubjectCard = retrieveSubjectCard(dataService, archiveSubjects, pageParameters);

        List<AttributeValue> attributesList = selectedSubjectCard.getAttributes();
        Map<String, List<AttributeValue>> attributeMap = new HashMap(attributesList.stream().collect(Collectors.groupingBy(AttributeValue::getAttributeId)));

        retrieveAttributesValues(attributeMap);

        final TextField<String> subjectName = new TextField<>("subjectName", new PropertyModel<String>(this, "subjectNameValue"));
        final TextField<String> pulpitName = new TextField<>("pulpitName", new PropertyModel<String>(this, "pulpitNameValue"));
        final RadioChoice<String> cycles = new RadioChoice<String>("cycles", new PropertyModel<String>(this, "selectedCycle"), cyclesList);
        final CheckBox differentiatedCreditCheckbox = new CheckBox("differentiatedCreditCheckbox", new PropertyModel<>(this, "differentiatedCreditCheckboxValue"));
        final CheckBox courseProjectCheckbox = new CheckBox("courseProjectCheckbox", new PropertyModel<>(this, "courseProjectCheckboxValue"));
        final CheckBox courseWorkCheckbox = new CheckBox("courseWorkCheckbox", new PropertyModel<>(this, "courseWorkCheckboxValue"));
        final CheckBox examCheckbox = new CheckBox("examCheckbox", new PropertyModel<>(this, "examCheckboxValue"));
        final TextField<String> classHours = new TextField<>("classHours", new PropertyModel<String>(this, "classHoursValue"));
        final TextField<String> selfHours = new TextField<>("selfHours", new PropertyModel<String>(this, "selfHoursValue"));
        final TextField<String> totalDuration = new TextField<>("totalDuration", new PropertyModel<String>(this, "totalDurationValue"));
        final TextField<String> lectureHoursPerWeek = new TextField<>("lectureHoursPerWeek", new PropertyModel<String>(this, "lectureHoursPerWeekValue"));
        final TextField<String> practicalHoursPerWeek = new TextField<>("practicalHoursPerWeek", new PropertyModel<String>(this, "practicalHoursPerWeekValue"));
        final TextField<String> laboratoryHoursPerWeek = new TextField<>("laboratoryHoursPerWeek", new PropertyModel<String>(this, "laboratoryHoursPerWeekValue"));
        final TextField<String> totalHoursPerWeek = new TextField<>("totalHoursPerWeek", new PropertyModel<String>(this, "totalHoursPerWeekValue"));
        final TextArea<String> shortDescription = new TextArea<>("shortDescription", new PropertyModel<String>(this, "shortDescriptionValue"));

        Form<?> subjectsNamesForm = new Form<Void>("subjectsNamesForm") {
            @Override
            public void onSubmit() {
                pageParameters.remove("selectedSubjectName");
                pageParameters.add("selectedSubjectName", selectedSubjectName);
                setResponsePage(ProgramPage.class, pageParameters);
            }
        };

        Form<?> selectedSubjectInfoForm = new Form<Void>("selectedSubjectInfoForm") {
            @Override
            public void onSubmit() {
                dataService.renameObject(selectedSubjectCard.getId(), subjectNameValue);

                attributeMap.get(SUBJECT_NAME_ATTR_ID).get(0).setValue(subjectNameValue);
                attributeMap.get(PULPIT_NAME_ATTR_ID).get(0).setValue(pulpitNameValue);

                if ("Технологический цикл".equals(selectedCycle)) {
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setValue(TECHNOLOGICAL_CYCLE_ID);
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setStringValue("Технологический цикл");
                }
                if ("Программная инженерия".equals(selectedCycle)) {
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setValue(PROGRAM_ENGINEERING_ID);
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setStringValue("Программная инженерия");
                }
                if ("Информационная система".equals(selectedCycle)) {
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setValue(INFORMATION_SYSTEM_ID);
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setStringValue("Информационная система");
                }
                if ("Управление проектом".equals(selectedCycle)) {
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setValue(PROJECT_MANAGEMENT_ID);
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setStringValue("Управление проектом");
                }
                if ("Безопасность и защита".equals(selectedCycle)) {
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setValue(SAFETY_AND_PROTECTION_ID);
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setStringValue("Безопасность и защита");
                }
                if ("Системный анализ".equals(selectedCycle)) {
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setValue(SYSTEM_ANALYSIS_ID);
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setStringValue("Системный анализ");
                }
                if ("Управление продуктом".equals(selectedCycle)) {
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setValue(PRODUCT_MANAGEMENT_ID);
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setStringValue("Управление продуктом");
                }
                if ("Проектирование системы".equals(selectedCycle)) {
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setValue(DESIGNING_THE_SYSTEM_ID);
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setStringValue("Проектирование системы");
                }
                if ("Другое".equals(selectedCycle)) {
                    attributeMap.get(CYCLE_ATTR_ID).get(0).setValue(null);
                }

                attributeMap.put(FORM_OF_PASSING_ATTR_ID, new ArrayList<AttributeValue>());
                if (differentiatedCreditCheckboxValue) {
                    AttributeValue differentiatedCreditAttribute = new AttributeValue(FORM_OF_PASSING_ATTR_ID, DIFFERENTIATED_CREDIT_ID);
                    differentiatedCreditAttribute.setStringValue("Дифф.зачет");
                    attributeMap.get(FORM_OF_PASSING_ATTR_ID).add(differentiatedCreditAttribute);
                }
                if (courseProjectCheckboxValue) {
                    AttributeValue courseProjectAttribute = new AttributeValue(FORM_OF_PASSING_ATTR_ID, COURSE_PROJECT_ID);
                    courseProjectAttribute.setStringValue("Курсовой проект");
                    attributeMap.get(FORM_OF_PASSING_ATTR_ID).add(courseProjectAttribute);
                }
                if (courseWorkCheckboxValue) {
                    AttributeValue courseWorkAttribute = new AttributeValue(FORM_OF_PASSING_ATTR_ID, COURSE_WORK_ID);
                    courseWorkAttribute.setStringValue("Курсовая работа");
                    attributeMap.get(FORM_OF_PASSING_ATTR_ID).add(courseWorkAttribute);
                }
                if (examCheckboxValue) {
                    AttributeValue examAttribute = new AttributeValue(FORM_OF_PASSING_ATTR_ID, EXAM_ID);
                    examAttribute.setStringValue("Экзамен");
                    attributeMap.get(FORM_OF_PASSING_ATTR_ID).add(examAttribute);
                }

                attributeMap.get(CLASS_HOURS_ATTR_ID).get(0).setValue(classHoursValue);
                attributeMap.get(SELF_HOURS_ATTR_ID).get(0).setValue(selfHoursValue);
                attributeMap.get(TOTAL_DURATION_ATTR_ID).get(0).setValue(totalDurationValue);
                attributeMap.get(LECTURE_HOURS_PER_WEEK_ATTR_ID).get(0).setValue(lectureHoursPerWeekValue);
                attributeMap.get(PRACTICAL_HOURS_PER_WEEK_ATTR_ID).get(0).setValue(practicalHoursPerWeekValue);
                attributeMap.get(LABORATORY_HOURS_PER_WEEK_ATTR_ID).get(0).setValue(laboratoryHoursPerWeekValue);
                attributeMap.get(TOTAL_HOURS_PER_WEEK_ATTR_ID).get(0).setValue(totalHoursPerWeekValue);
                attributeMap.get(SHORT_DESCRIPTION_ATTR_ID).get(0).setValue(shortDescriptionValue);

                List<AttributeValue> updatedAttributesList = attributeMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
                attributeService.setAttributeValuesForAnObject(selectedSubjectCard.getId(), updatedAttributesList);
            }
        };

        Form<?> deleteSubjectForm = new Form<Void>("deleteSubjectForm") {
            @Override
            public void onSubmit() {
                dataService.deleteObjects(selectedSubjectCard.getId());
            }
        };

        Form<?> addSubjectForm = new Form<Void>("addSubjectForm") {
            @Override
            public void onSubmit() {
                pageParameters.remove("selectedSubjectName");
                setResponsePage(AddSubjectPage.class, pageParameters);
            }
        };

        add(subjectsNamesForm);
        subjectsNamesForm.add(educationLevelLabel);
        subjectsNamesForm.add(courseLabel);
        subjectsNamesForm.add(semesterLabel);
        subjectsNamesForm.add(subjectsNamesList);

        add(addSubjectForm);
        add(selectedSubjectInfoForm);
        selectedSubjectInfoForm.add(subjectName);
        selectedSubjectInfoForm.add(pulpitName);
        selectedSubjectInfoForm.add(cycles);
        selectedSubjectInfoForm.add(differentiatedCreditCheckbox);
        selectedSubjectInfoForm.add(courseProjectCheckbox);
        selectedSubjectInfoForm.add(courseWorkCheckbox);
        selectedSubjectInfoForm.add(examCheckbox);
        selectedSubjectInfoForm.add(classHours);
        selectedSubjectInfoForm.add(selfHours);
        selectedSubjectInfoForm.add(totalDuration);
        selectedSubjectInfoForm.add(lectureHoursPerWeek);
        selectedSubjectInfoForm.add(practicalHoursPerWeek);
        selectedSubjectInfoForm.add(laboratoryHoursPerWeek);
        selectedSubjectInfoForm.add(totalHoursPerWeek);
        selectedSubjectInfoForm.add(shortDescription);
        add(deleteSubjectForm);

    }

    private List<ArchiveObject> findSubjects(final Archive3ServerConnector archive3ServerConnector) throws UnknownServiceException {

        final SearchService searchService = archive3ServerConnector.getSearchService();
        final SearchRequest searchRequest = new SearchRequest();

        final ArrayList<AdvSearchableField> advSearchableFields = new ArrayList<>();

        final AdvSearchableField asf1 = new AdvSearchableField();
        asf1.setFieldType(FieldType.ATTRIBUTE);
        asf1.setMatchType(MatchType.EXACT);
        asf1.setFieldName(TYPE_OF_CARD_ATTR_ID);
        asf1.setFieldConditions(Collections.singletonList(SUBJECT_ID));
        advSearchableFields.add(asf1);

        final AdvSearchableField asf2 = new AdvSearchableField();
        asf2.setFieldType(FieldType.ATTRIBUTE);
        asf2.setMatchType(MatchType.EXACT);
        asf2.setFieldName(EDUCATION_LEVEL_ATTR_ID);
        if (educationLevel.equals("Бакалавриат")) {
            asf2.setFieldConditions(Collections.singletonList(BACCALAUREATE_ID));
        } else {
            asf2.setFieldConditions(Collections.singletonList(MAGISTRACY_ID));
        }
        advSearchableFields.add(asf2);

        final AdvSearchableField asf3 = new AdvSearchableField();
        asf3.setFieldType(FieldType.ATTRIBUTE);
        asf3.setMatchType(MatchType.EXACT);
        asf3.setFieldName(COURSE_ATTR_ID);
        asf3.setFieldConditions(Collections.singletonList(course.substring(0, 1)));
        advSearchableFields.add(asf3);

        final AdvSearchableField asf4 = new AdvSearchableField();
        asf4.setFieldType(FieldType.ATTRIBUTE);
        asf4.setMatchType(MatchType.EXACT);
        asf4.setFieldName(SEMESTER_ATTR_ID);
        if (semester.length() == 10) {
            asf4.setFieldConditions(Collections.singletonList(semester.substring(0, 2)));
        } else {
            asf4.setFieldConditions(Collections.singletonList(semester.substring(0, 1)));
        }
        advSearchableFields.add(asf4);

        searchRequest.setFieldList(advSearchableFields);
        final List<ArchiveObject> archiveSubjects = searchService.performSearch(searchRequest);
        return archiveSubjects;

    }

    private CompleteCard retrieveSubjectCard(final DataService dataService, final List<ArchiveObject> archiveSubjects, PageParameters pageParameters) throws ArchiveSystemError {

        CompleteCard selectedSubjectCard;

        if (selectedSubjectName != null) {
            final ArchiveObject selectedSubjectArchiveObject = archiveSubjects.stream().filter(searchResult -> searchResult.getName().equals(selectedSubjectName)).findFirst().orElse(null);
            selectedSubjectCard = dataService.getCompleteCard(selectedSubjectArchiveObject.getObjectId());
        } else {
            selectedSubjectName = archiveSubjects.get(0).getName();
            pageParameters.add("selectedSubjectName", selectedSubjectName);
            final ArchiveObject selectedSubjectArchiveObject = archiveSubjects.get(0);
            selectedSubjectCard = dataService.getCompleteCard(selectedSubjectArchiveObject.getObjectId());
        }

        return selectedSubjectCard;

    }

    private void retrieveAttributesValues(Map<String, List<AttributeValue>> attributeMap) {

        subjectNameValue = attributeMap.get(SUBJECT_NAME_ATTR_ID).get(0).getValue();
        pulpitNameValue = attributeMap.get(PULPIT_NAME_ATTR_ID).get(0).getValue();
        cycleValue = attributeMap.get(CYCLE_ATTR_ID).get(0).getValue();
        formsOfPassingValues = attributeMap.get(FORM_OF_PASSING_ATTR_ID).stream().map(AttributeValue::getValue).collect(Collectors.toList());
        classHoursValue = attributeMap.get(CLASS_HOURS_ATTR_ID).get(0).getValue();
        selfHoursValue = attributeMap.get(SELF_HOURS_ATTR_ID).get(0).getValue();
        totalDurationValue = attributeMap.get(TOTAL_DURATION_ATTR_ID).get(0).getValue();
        lectureHoursPerWeekValue = attributeMap.get(LECTURE_HOURS_PER_WEEK_ATTR_ID).get(0).getValue();
        practicalHoursPerWeekValue = attributeMap.get(PRACTICAL_HOURS_PER_WEEK_ATTR_ID).get(0).getValue();
        laboratoryHoursPerWeekValue = attributeMap.get(LABORATORY_HOURS_PER_WEEK_ATTR_ID).get(0).getValue();
        totalHoursPerWeekValue = attributeMap.get(TOTAL_HOURS_PER_WEEK_ATTR_ID).get(0).getValue();
        shortDescriptionValue = attributeMap.get(SHORT_DESCRIPTION_ATTR_ID).get(0).getValue();

        if (cycleValue.equals(TECHNOLOGICAL_CYCLE_ID)) {
            selectedCycle = "Технологический цикл";
        }
        if (cycleValue.equals(PROGRAM_ENGINEERING_ID)) {
            selectedCycle = "Программная инженерия";
        }
        if (cycleValue.equals(INFORMATION_SYSTEM_ID)) {
            selectedCycle = "Информационная система";
        }
        if (cycleValue.equals(PROJECT_MANAGEMENT_ID)) {
            selectedCycle = "Управление проектом";
        }
        if (cycleValue.equals(SAFETY_AND_PROTECTION_ID)) {
            selectedCycle = "Безопасность и защита";
        }
        if (cycleValue.equals(SYSTEM_ANALYSIS_ID)) {
            selectedCycle = "Системный анализ";
        }
        if (cycleValue.equals(PRODUCT_MANAGEMENT_ID)) {
            selectedCycle = "Управление продуктом";
        }
        if (cycleValue.equals(DESIGNING_THE_SYSTEM_ID)) {
            selectedCycle = "Проектирование системы";
        }
        if (cycleValue.isEmpty()) {
            selectedCycle = "Другое";
        }

        if (formsOfPassingValues.contains(DIFFERENTIATED_CREDIT_ID)) {
            differentiatedCreditCheckboxValue = true;
        }
        if (formsOfPassingValues.contains(COURSE_PROJECT_ID)) {
            courseProjectCheckboxValue = true;
        }
        if (formsOfPassingValues.contains(COURSE_WORK_ID)) {
            courseWorkCheckboxValue = true;
        }
        if (formsOfPassingValues.contains(EXAM_ID)) {
            examCheckboxValue = true;
        }

    }

}
