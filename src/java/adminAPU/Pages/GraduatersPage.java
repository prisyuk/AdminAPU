package adminAPU.Pages;

import adminAPU.BasePage;
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
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
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

    private Map<String, String> archiveYears;
    private List<String> years;
    private String selectedYear;
    private Map<String, String> archiveGraduaters;
    private List<String> graduatersNames;
    private String selectedGraduaterName;

    public GraduatersPage(PageParameters pageParameters) {

        educationLevel = pageParameters.get("educationLevel").toString();
        selectedYear = pageParameters.get("selectedYear").toString();
        selectedGraduaterName = pageParameters.get("selectedGraduaterName").toString();

        final Archive3ServerConnector archive3ServerConnector = new Archive3ServerConnector("apu.su", 80);
        final Requisite requisite = new Requisite();
        requisite.instanceRichSettings();
        archive3ServerConnector.authenticate("admin", "ApU_36oe", requisite);

        final DataService dataService = archive3ServerConnector.getDataService();
        final AttributeService attributeService = archive3ServerConnector.getAttributeService();

        if (educationLevel.equals("Бакалавриат")) {
            archiveYears = dataService.getChildrenCards(BACCALAUREATE_GRADUATERS_CARD_ID);
        }
        if (educationLevel.equals("Магистратура")) {
            archiveYears = dataService.getChildrenCards(MAGISTRACY_GRADUATERS_CARD_ID);
        }

        years = (List<String>) archiveYears.keySet();
        ListChoice<String> yearsList = new ListChoice<>("yearsList", new PropertyModel<String>(this, "selectedYear"), years);
        yearsList.setMaxRows(5);

        archiveGraduaters = dataService.getChildrenCards(archiveYears.get(selectedYear));
        Map<String, String> sortedArchiveGraduaters = new TreeMap<>(archiveGraduaters);

        graduatersNames = (List<String>) sortedArchiveGraduaters.keySet();
        ListChoice<String> graduatersNamesList = new ListChoice<>("graduatersNamesList", new PropertyModel<String>(this, "selectedGraduaterName"), graduatersNames);
        graduatersNamesList.setMaxRows(15);

        CompleteCard selectedGraduaterCard = retrieveGraduaterCard(dataService, sortedArchiveGraduaters, pageParameters);

        List<AttributeValue> attributesList = selectedGraduaterCard.getAttributes();
        Map<String, List<AttributeValue>> attributeMap = new HashMap(attributesList.stream().collect(Collectors.groupingBy(AttributeValue::getAttributeId)));

        retrieveAttributesValues(attributeMap);

        final TextField<String> graduaterName = new TextField<String>("graduaterName", new PropertyModel<String>(this, "graduaterNameValue"));
        final TextField<String> graduationWorkName = new TextField<String>("graduationWorkName", new PropertyModel<String>(this, "graduationWorkNameValue"));
        final TextField<String> teacherName = new TextField<String>("teacherName", new PropertyModel<String>(this, "teacherNameValue"));
        final TextField<String> altTeacherName = new TextField<String>("altTeacherName", new PropertyModel<String>(this, "altTeacherNameValue"));
        final TextField<String> jobPlaceName = new TextField<String>("jobPlace", new PropertyModel<String>(this, "jobPlaceValue"));
        final TextField<String> position = new TextField<String>("position", new PropertyModel<String>(this, "positionValue"));
        final TextArea<String> shortReference = new TextArea<>("shortReference", new PropertyModel<String>(this, "shortReferenceValue"));

        Form<?> yearsForm = new Form<Void>("yearsForm") {
            @Override
            public void onSubmit() {
                pageParameters.remove("selectedYear");
                pageParameters.add("selectedYear", selectedYear);
                setResponsePage(GraduatersPage.class, pageParameters);
            }
        };

        Form<?> graduatersNamesForm = new Form<Void>("graguatersNamesForm") {
            @Override
            public void onSubmit() {
                pageParameters.remove("selectedGraduaterName");
                pageParameters.add("selectedGraduaterName", selectedGraduaterName);
                setResponsePage(GraduatersPage.class, pageParameters);
            }
        };

        Form<?> selectedGraduaterInfoForm = new Form<Void>("selectedGraduaterInfoForm") {
            @Override
            public void onSubmit() {
            }
        };

        add(yearsForm);
        yearsForm.add(yearsList);

        add(graduatersNamesForm);
        graduatersNamesForm.add(graduatersNamesList);

        add(selectedGraduaterInfoForm);
        selectedGraduaterInfoForm.add(graduaterName);
        selectedGraduaterInfoForm.add(graduationWorkName);
        selectedGraduaterInfoForm.add(teacherName);
        selectedGraduaterInfoForm.add(altTeacherName);
        selectedGraduaterInfoForm.add(jobPlaceName);
        selectedGraduaterInfoForm.add(position);
        selectedGraduaterInfoForm.add(shortReference);

    }

    private CompleteCard retrieveGraduaterCard(final DataService dataService, final Map<String, String> sortedArchiveGraduaters, PageParameters pageParameters) throws ArchiveSystemError {

        CompleteCard selectedGraduaterCard;

        if (selectedGraduaterName != null) {
            selectedGraduaterCard = dataService.getCompleteCard(sortedArchiveGraduaters.get(selectedGraduaterName));
        } else {
            selectedGraduaterName = graduatersNames.get(0);
            pageParameters.add("selectedGraduaterName", selectedGraduaterName);
            selectedGraduaterCard = dataService.getCompleteCard(sortedArchiveGraduaters.get(selectedGraduaterName));
        }

        return selectedGraduaterCard;

    }

    private void retrieveAttributesValues(Map<String, List<AttributeValue>> attributeMap) {

        graduaterNameValue = attributeMap.get(GRADUATER_NAME_ATTR_ID).get(0).getValue();
        graduationWorkNameValue = attributeMap.get(GRADUATION_WORK_NAME_ATTR_ID).get(0).getValue();
        teacherNameValue = attributeMap.get(TEACHER_NAME_ATTR_ID).get(0).getValue();
        altTeacherNameValue = attributeMap.get(ALT_TEACHER_NAME_ATTR_ID).get(0).getValue();
        jobPlaceValue = attributeMap.get(JOB_PLACE_ATTR_ID).get(0).getValue();
        positionValue = attributeMap.get(POSITION_ATTR_ID).get(0).getValue();
        shortReferenceValue = attributeMap.get(SHORT_REFERENCE_ATTR_ID).get(0).getValue();

    }

}
