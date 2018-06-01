package adminAPU;

import adminAPU.Pages.DocumentsPage;
import adminAPU.Pages.OtherPage;
import adminAPU.Pages.GraduatersPage;
import adminAPU.Pages.ProgramPage;
import adminAPU.Pages.TeachersPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class HeaderPanel extends Panel {

    public HeaderPanel(String id) {
        super(id);

        add(new Link("B11Link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Бакалавриат");
                pageParameters.add("course", "1 курс");
                pageParameters.add("semester", "1 семестр");
                setResponsePage(ProgramPage.class, pageParameters);
            }
        });

        add(new Link("B12Link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Бакалавриат");
                pageParameters.add("course", "1 курс");
                pageParameters.add("semester", "2 семестр");
                setResponsePage(ProgramPage.class, pageParameters);
            }
        });

        add(new Link("B23Link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Бакалавриат");
                pageParameters.add("course", "2 курс");
                pageParameters.add("semester", "3 семестр");
                setResponsePage(ProgramPage.class, pageParameters);
            }
        });

        add(new Link("B24Link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Бакалавриат");
                pageParameters.add("course", "2 курс");
                pageParameters.add("semester", "4 семестр");
                setResponsePage(ProgramPage.class, pageParameters);
            }
        });

        add(new Link("B35Link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Бакалавриат");
                pageParameters.add("course", "3 курс");
                pageParameters.add("semester", "5 семестр");
                setResponsePage(ProgramPage.class, pageParameters);
            }
        });

        add(new Link("B36Link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Бакалавриат");
                pageParameters.add("course", "3 курс");
                pageParameters.add("semester", "6 семестр");
                setResponsePage(ProgramPage.class, pageParameters);
            }
        });

        add(new Link("B47Link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Бакалавриат");
                pageParameters.add("course", "4 курс");
                pageParameters.add("semester", "7 семестр");
                setResponsePage(ProgramPage.class, pageParameters);
            }
        });

        add(new Link("B48Link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Бакалавриат");
                pageParameters.add("course", "4 курс");
                pageParameters.add("semester", "8 семестр");
                setResponsePage(ProgramPage.class, pageParameters);
            }
        });

        add(new Link("M11Link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Магистратура");
                pageParameters.add("course", "5 курс");
                pageParameters.add("semester", "9 семестр");
                setResponsePage(ProgramPage.class, pageParameters);
            }
        });

        add(new Link("M12Link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Магистратура");
                pageParameters.add("course", "5 курс");
                pageParameters.add("semester", "10 семестр");
                setResponsePage(ProgramPage.class, pageParameters);
            }
        });

        add(new Link("M23Link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Магистратура");
                pageParameters.add("course", "6 курс");
                pageParameters.add("semester", "11 семестр");
                setResponsePage(ProgramPage.class, pageParameters);
            }
        });

        add(new Link("M24Link") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Магистратура");
                pageParameters.add("course", "6 курс");
                pageParameters.add("semester", "12 семестр");
                setResponsePage(ProgramPage.class, pageParameters);
            }
        });

        add(new Link("teachersLink") {
            @Override
            public void onClick() {
                setResponsePage(TeachersPage.class);
            }
        });

//        add(new Link("baccalaureateGraduatersLink") {
//            @Override
//            public void onClick() {
//                PageParameters pageParameters = new PageParameters();
//                pageParameters.add("educationLevel", "Бакалавриат");
//                setResponsePage(GraduatersPage.class, pageParameters);
//            }
//        });
//
//        add(new Link("magistracyGraduatersLink") {
//            @Override
//            public void onClick() {
//                PageParameters pageParameters = new PageParameters();
//                pageParameters.add("educationLevel", "Магистратура");
//                setResponsePage(GraduatersPage.class, pageParameters);
//            }
//        });

        add(new Link("baccalaureateDocumentsLink") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Бакалавриат");
                setResponsePage(DocumentsPage.class, pageParameters);
            }
        });

        add(new Link("magistracyDocumentsLink") {
            @Override
            public void onClick() {
                PageParameters pageParameters = new PageParameters();
                pageParameters.add("educationLevel", "Магистратура");
                setResponsePage(DocumentsPage.class, pageParameters);
            }
        });

        add(new Link("otherLink") {
            @Override
            public void onClick() {
                setResponsePage(OtherPage.class);
            }
        });

    }

}