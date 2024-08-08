package ca.admin.delivermore.views.drivers;

import ca.admin.delivermore.collector.data.Utility;
import ca.admin.delivermore.collector.data.service.DriversRepository;
import ca.admin.delivermore.collector.data.tookan.Driver;
import ca.admin.delivermore.data.entity.DriverAdjustment;
import ca.admin.delivermore.data.report.DriverPayoutDay;
import ca.admin.delivermore.data.report.DriverPayoutWeek;
import ca.admin.delivermore.data.report.PayoutDocument;
import ca.admin.delivermore.security.AuthenticatedUser;
import ca.admin.delivermore.views.MainLayout;
import com.vaadin.componentfactory.DateRange;
import com.vaadin.componentfactory.EnhancedDateRangePicker;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.vaadin.olli.FileDownloadWrapper;

import javax.annotation.security.RolesAllowed;
import java.io.*;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipOutputStream;

@PageTitle("Driver Report")
@Route(value = "driverreport", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class DriverReportView extends Main {

    private Logger log = LoggerFactory.getLogger(DriverReportView.class);
    LocalDate startDate;
    LocalDate endDate;

    private File appPath = new File(System.getProperty("user.dir"));
    private File outputDir = new File(appPath,"tozip");

    @Value("classpath:PayStatement_Template.docx")
    private Resource resourcePayStatementTemplate;

    Optional<Driver> signedInDriver;
    private EnhancedDateRangePicker rangeDatePicker = new EnhancedDateRangePicker("Select period:");
    private VerticalLayout detailsLayout = new VerticalLayout();

    private AuthenticatedUser authenticatedUser;

    private DriversRepository driversRepository;

    private ComboBox<Driver> selectedDialogDriver = new ComboBox<>("Driver");

    private DriverPayoutWeek driverPayoutWeek;

    List<PayoutDocument> selectedPayoutDocuments = new ArrayList<>();

    public DriverReportView(@Autowired AuthenticatedUser authenticatedUser, @Autowired DriversRepository driversRepository) {
        this.authenticatedUser = authenticatedUser;
        this.driversRepository = driversRepository;

        signedInDriver = getSignedInDriver();
        log.info("signedInDriver:" + signedInDriver);

        configureDatePicker();
        configureDriverList();
        startDate = rangeDatePicker.getValue().getStartDate();
        endDate = rangeDatePicker.getValue().getEndDate();

        buildDriverPayoutDetails();
        add(getToolbar(), getContent());
    }

    private Optional<Driver> getSignedInDriver() {
        return authenticatedUser.get();
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout(rangeDatePicker, selectedDialogDriver);
        toolbar.setPadding(true);
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.addClassName("toolbar");

        Button zipDocumentsButton = new Button("Zip documents");
        zipDocumentsButton.setDisableOnClick(false);
        zipDocumentsButton.setEnabled(true);

        FileDownloadWrapper zipDocumentsButtonWrapper = new FileDownloadWrapper(
                new StreamResource("DriverPayoutFiles.zip", () -> {
                    try {
                        File zippedFile = getZippedFileforFileList(selectedPayoutDocuments);
                        zipDocumentsButton.setEnabled(true);
                        return new ByteArrayInputStream(Files.readAllBytes( zippedFile.toPath()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
        zipDocumentsButtonWrapper.wrapComponent(zipDocumentsButton);
        toolbar.add(zipDocumentsButtonWrapper);


        return toolbar;
    }

    private void configureDatePicker() {
        LocalDate defaultDate = LocalDate.parse("2022-08-14");

        //get lastWeek as the default for the range picker
        LocalDate nowDate = LocalDate.now();
        LocalDate prevSun = nowDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        prevSun = nowDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate startOfLastWeek = prevSun.minusWeeks(1);
        LocalDate endOfLastWeek = startOfLastWeek.plusDays(6);
        LocalDate startOfThisWeek = prevSun;
        LocalDate endOfThisWeek = nowDate;

        rangeDatePicker.setMin(defaultDate);
        rangeDatePicker.setValue(new DateRange(startOfThisWeek,endOfThisWeek));
        rangeDatePicker.addValueChangeListener(e -> {
            startDate = rangeDatePicker.getValue().getStartDate();
            endDate = rangeDatePicker.getValue().getEndDate();
            buildDriverPayoutDetails();
        });
    }

    private void configureDriverList(){
        selectedDialogDriver.setItems(driversRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
        selectedDialogDriver.setItemLabelGenerator(Driver::getName);
        selectedDialogDriver.setReadOnly(false);
        selectedDialogDriver.setPlaceholder("Select driver");
        selectedDialogDriver.addValueChangeListener(item -> {

            buildDriverPayoutDetails();
        });
    }

    private Component getContent() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.add(detailsLayout);
        detailsLayout.setSizeFull();
        return mainLayout;
    }

    private void buildDriverPayoutDetails() {
        detailsLayout.removeAll();
        if(selectedDialogDriver.isEmpty()){
            log.info("buildDriverPayoutDetails: No user signed in. Cannot continue");
            detailsLayout.add(new Html("<p>No driver selected.  Cannot continue.</p>"));
            return;
        }
        //display payout info
        driverPayoutWeek = new DriverPayoutWeek(selectedDialogDriver.getValue().getFleetId(), startDate, endDate);
        detailsLayout.add(driverPayoutWeek.getDetails(true));

        selectedPayoutDocuments.clear();
        createStatements();
        selectedPayoutDocuments.add(new PayoutDocument("Statement for:" + driverPayoutWeek.getFleetName() + " (PDF)", driverPayoutWeek.getPdfFile(),driverPayoutWeek.getDriver().getEmail()));

        String htmlNotes = "<p><b>Note:</b> <i>the above information is provided as a convenience and is not to be used as a statement of pay. Recent tasks may be missing due to processing delays and adjustments may be made prior to a payout statement being processed.</i></p>";
        Html notes = new Html(htmlNotes);
        notes.getElement().getStyle().set("font-size", "10px");
        detailsLayout.add(notes);


    }

    private void createStatements() {
        //create a folder to hold all the temp files.  Empty the folder of previous runs
        String outputFileExt = ".pdf";
        Utility.emptyDir(outputDir);
        try {
            Files.createDirectory(outputDir.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("createPDFStatements: week:" + driverPayoutWeek.toString());

        String outputFileName = "PayStatement-" + driverPayoutWeek.getFleetName() + driverPayoutWeek.getPayoutDate() + "-" + driverPayoutWeek.getWeekEndDate();
        File outputFile = new File(outputDir,outputFileName + outputFileExt);
        driverPayoutWeek.setPdfFile(outputFile);

        try {
            // 1) Load Docx file by filling Velocity template engine and cache it to the registry
            InputStream in = resourcePayStatementTemplate.getInputStream();
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);

            // 2) Create context Java model
            IContext context = report.createContext();
            //Project project = new Project("XDocReport");
            context.put("driverPayoutWeek", driverPayoutWeek);

            List<DriverPayoutDay> driverDays = driverPayoutWeek.getDriverPayoutDayList();
            // 2) Create fields metadata to manage lazy loop (#forech velocity)
            // for table row.
            FieldsMetadata metadata = new FieldsMetadata();
            metadata.addFieldAsList("driverDays.getPayoutDate()");
            metadata.addFieldAsList("driverDays.getTaskCount()");
            metadata.addFieldAsList("driverDays.getDriverPayFmt()");
            metadata.addFieldAsList("driverDays.getTipFmt()");
            metadata.addFieldAsList("driverDays.getDriverIncomeFmt()");
            metadata.addFieldAsList("driverDays.getDriverCashFmt()");
            metadata.addFieldAsList("driverDays.getDriverPayoutFmt()");
            report.setFieldsMetadata(metadata);

            context.put("driverDays", driverDays);

            List<DriverAdjustment> driverAdjustments = driverPayoutWeek.getDriverAdjustmentList();
            // 2) Create fields metadata to manage lazy loop (#forech velocity)
            // for table row.
            metadata.addFieldAsList("driverAdjustments.getAdjustmentDate()");
            metadata.addFieldAsList("driverAdjustments.getAdjustmentNote()");
            metadata.addFieldAsList("driverAdjustments.getAdjustmentAmountFmt()");

            context.put("driverAdjustments", driverAdjustments);

            // 3) Generate report by merging Java model with the Docx
            //To PDF
            OutputStream out = new FileOutputStream(outputFile);
            Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
            report.convert(context, options, out);
        } catch (IOException e) {
            log.info("createPDFStatements: FAILED week:" + driverPayoutWeek.toString() + " ERROR:" + e.toString());
            e.printStackTrace();
        } catch (XDocReportException e) {
            log.info("createPDFStatements: FAILED2 week:" + driverPayoutWeek.toString() + " ERROR:" + e.toString());
            e.printStackTrace();
        }

    }

    private File getZippedFileforFileList(List<PayoutDocument> docList){
        log.info("***Start of ZIP process***");
        String zipFileName = "Week " + startDate;
        File zippedFile = new File(appPath, zipFileName + ".zip");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(zipFileName + ".zip");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (PayoutDocument doc: docList) {
            try {
                Utility.zipFile(doc.getFile(), doc.getFile().getName(), zipOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            zipOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("***END of ZIP process***");

        return zippedFile;

    }



}
