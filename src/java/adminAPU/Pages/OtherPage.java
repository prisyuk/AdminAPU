package adminAPU.Pages;

import adminAPU.BasePage;
import com.alee.archive3.Archive3ServerConnector;
import com.alee.archive3.api.data.AttributeValue;
import com.alee.archive3.api.data.CompleteCard;
import com.alee.archive3.api.network.Requisite;
import com.alee.archive3.api.ws.AttributeService;
import com.alee.archive3.api.ws.DataService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

public class OtherPage extends BasePage {

    private static final String PULPIT_CONTACTS_CARD_ID = "ALSFR-56c4cc4e-eb87-4e34-b0b9-5d33d2111804";
    private static final String PULPIT_NAME_ATTR_ID = "ALSFR-efd7c323-8956-430a-a5ad-c242787f1ebf";
    private static final String ADDRESS_ATTR_ID = "ALSFR-b9893e8f-6963-474c-9c12-cb64ea44efdf";
    private static final String PHONE_ATTR_ID = "ALSFR-ba69cb7c-31aa-46ab-9536-37fa9dd56c62";
    private static final String EMAIL_ATTR_ID = "ALSFR-e0747554-eada-4b01-a858-32a00d16f130";
    private static final String CONTACT_PERSON_ATTR_ID = "ALSFR-9593ca91-3733-4c3d-a731-2ed5b1db27c1";
    private static final String APPEAL_ATTR_ID = "ALSFR-991fd1c0-898c-4bd1-af5b-9580fadc5851";

    private static final String PULPIT_STATISTICS_CARD_ID = "ALSFR-60b5f60b-5806-43fd-b439-7dd13d9b5be1";
    private static final String STUDENTS_ATTR_ID = "ALSFR-c2812f3d-258b-4441-bc43-24a73c78e078";
    private static final String GRADUATERS_ATTR_ID = "ALSFR-f50f77cc-747f-420d-b436-23b333949067";
    private static final String EXCELLENT_GRADUTERS_ATTR_ID = "ALSFR-54164807-7ffa-4347-9a1b-57743710edaa";

    private String pulpitNameValue;
    private String addressValue;
    private String phoneValue;
    private String emailValue;
    private String contactPersonValue;
    private String appealValue;

    private String studentsValue;
    private String graduatersValue;
    private String excellentGraduatersValue;

    public OtherPage() {

        final Archive3ServerConnector archive3ServerConnector = new Archive3ServerConnector("apu.su", 80);
        final Requisite requisite = new Requisite();
        requisite.instanceRichSettings();
        archive3ServerConnector.authenticate("admin", "ApU_36oe", requisite);

        final DataService dataService = archive3ServerConnector.getDataService();
        final AttributeService attributeService = archive3ServerConnector.getAttributeService();

        CompleteCard completeCardContacts = dataService.getCompleteCard(PULPIT_CONTACTS_CARD_ID);
        List<AttributeValue> attributesListContacts = completeCardContacts.getAttributes();
        Map<String, AttributeValue> attributeMapContacts = new HashMap(attributesListContacts.stream().collect(Collectors.toMap(attribute -> attribute.getAttributeId(), attribute -> attribute)));

        pulpitNameValue = attributeMapContacts.get(PULPIT_NAME_ATTR_ID).getValue();
        addressValue = attributeMapContacts.get(ADDRESS_ATTR_ID).getValue();
        phoneValue = attributeMapContacts.get(PHONE_ATTR_ID).getValue();
        emailValue = attributeMapContacts.get(EMAIL_ATTR_ID).getValue();
        contactPersonValue = attributeMapContacts.get(CONTACT_PERSON_ATTR_ID).getValue();
        appealValue = attributeMapContacts.get(APPEAL_ATTR_ID).getValue();

        CompleteCard completeCardStatistics = dataService.getCompleteCard(PULPIT_STATISTICS_CARD_ID);
        List<AttributeValue> attributesListStatistics = completeCardStatistics.getAttributes();
        Map<String, AttributeValue> attributeMapStatistics = new HashMap(attributesListStatistics.stream().collect(Collectors.toMap(attribute -> attribute.getAttributeId(), attribute -> attribute)));

        studentsValue = attributeMapStatistics.get(STUDENTS_ATTR_ID).getValue();
        graduatersValue = attributeMapStatistics.get(GRADUATERS_ATTR_ID).getValue();
        excellentGraduatersValue = attributeMapStatistics.get(EXCELLENT_GRADUTERS_ATTR_ID).getValue();

        final TextField<String> pulpitName = new TextField<String>("pulpitName", new PropertyModel<String>(this, "pulpitNameValue"));
        final TextField<String> address = new TextField<String>("address", new PropertyModel<String>(this, "addressValue"));
        final TextField<String> phone = new TextField<String>("phone", new PropertyModel<String>(this, "phoneValue"));
        final TextField<String> email = new TextField<String>("email", new PropertyModel<String>(this, "emailValue"));
        final TextField<String> contactPerson = new TextField<String>("contactPerson", new PropertyModel<String>(this, "contactPersonValue"));
        final TextField<String> appeal = new TextField<String>("appeal", new PropertyModel<String>(this, "appealValue"));

        Form<?> contactsForm = new Form<Void>("contactsForm") {
            @Override
            protected void onSubmit() {
                attributeMapContacts.get(PULPIT_NAME_ATTR_ID).setValue(pulpitNameValue);
                attributeMapContacts.get(ADDRESS_ATTR_ID).setValue(addressValue);
                attributeMapContacts.get(PHONE_ATTR_ID).setValue(phoneValue);
                attributeMapContacts.get(EMAIL_ATTR_ID).setValue(emailValue);
                attributeMapContacts.get(CONTACT_PERSON_ATTR_ID).setValue(contactPersonValue);
                attributeMapContacts.get(APPEAL_ATTR_ID).setValue(appealValue);

                List<AttributeValue> updatedAttributesList = new ArrayList<>(attributeMapContacts.values());
                attributeService.setAttributeValuesForAnObject(PULPIT_CONTACTS_CARD_ID, updatedAttributesList);
            }
        };

        final TextField<String> students = new TextField<String>("students", new PropertyModel<String>(this, "studentsValue"));
        final TextField<String> graduaters = new TextField<String>("graduaters", new PropertyModel<String>(this, "graduatersValue"));
        final TextField<String> excellentGraduaters = new TextField<String>("excellentGraduaters", new PropertyModel<String>(this, "excellentGraduatersValue"));

        Form<?> statisticsForm = new Form<Void>("statisticsForm") {
            @Override
            protected void onSubmit() {
                attributeMapStatistics.get(STUDENTS_ATTR_ID).setValue(studentsValue);
                attributeMapStatistics.get(GRADUATERS_ATTR_ID).setValue(graduatersValue);
                attributeMapStatistics.get(EXCELLENT_GRADUTERS_ATTR_ID).setValue(excellentGraduatersValue);

                List<AttributeValue> updatedAttributesList = new ArrayList<>(attributeMapStatistics.values());
                attributeService.setAttributeValuesForAnObject(PULPIT_STATISTICS_CARD_ID, updatedAttributesList);
            }
        };

        add(contactsForm);
        contactsForm.add(pulpitName);
        contactsForm.add(address);
        contactsForm.add(phone);
        contactsForm.add(email);
        contactsForm.add(contactPerson);
        contactsForm.add(appeal);

        add(statisticsForm);
        statisticsForm.add(students);
        statisticsForm.add(graduaters);
        statisticsForm.add(excellentGraduaters);

        archive3ServerConnector.logoff();

    }

}