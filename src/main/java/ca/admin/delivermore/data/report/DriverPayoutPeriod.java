package ca.admin.delivermore.data.report;

import ca.admin.delivermore.collector.data.Utility;
import ca.admin.delivermore.collector.data.service.TaskDetailRepository;
import ca.admin.delivermore.data.intuit.Intuit;
import ca.admin.delivermore.data.intuit.JournalEntry;
import ca.admin.delivermore.data.entity.DriverAdjustment;
import ca.admin.delivermore.data.entity.DriverCardTip;
import ca.admin.delivermore.collector.data.entity.DriverPayoutEntity;
import ca.admin.delivermore.data.service.DriverAdjustmentRepository;
import ca.admin.delivermore.data.service.Registry;
import ca.admin.delivermore.data.service.intuit.controller.QBOResult;
import com.vaadin.flow.component.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Entity
@IdClass(DriverPayoutPeriodPk.class)
public class DriverPayoutPeriod {

    @Transient
    private Logger log = LoggerFactory.getLogger(DriverPayoutPeriod.class);

    @Id
    private String location;
    @Id
    private LocalDate payoutPeriodStart;

    private LocalDate payoutPeriodEnd;

    private Integer taskCount = 0;
    private Double driverPay = 0.0;
    private Double tip = 0.0;

    private Double cardTip = 0.0;
    private Double driverIncome = 0.0;
    private Double driverCash = 0.0;
    private Double driverAdjustment = 0.0;
    private Double driverPayout = 0.0;
    private Double driverCost = 0.0;

    private File pdfFile = null;
    private File csvFile = null;
    private File journalFile = null;
    @Transient
    private List<JournalEntry> journalEntries = new ArrayList<>();

    @OneToMany
    private List<DriverPayoutWeek> driverPayoutWeekList = new ArrayList<>();
    @Transient
    private Map<Long,DriverPayoutWeek> driverPayoutWeekMap = new TreeMap<>();

    @Transient
    private Map<Long,DriverPayoutWeek> partMonthDriverPayoutWeekMap = new TreeMap<>();
    private Double partMonthDriverPay = 0.0;
    private Double partMonthDriverAdjustments = 0.0;
    private Double partMonthDriverTips = 0.0;
    private Double partMonthDriverCash = 0.0;
    private LocalDate partMonthPayoutPeriodEnd;
    private Boolean partMonth = Boolean.FALSE;

    @OneToMany
    private List<DriverAdjustment> driverAdjustmentList = new ArrayList<>();

    @OneToMany
    private List<DriverCardTip> driverCardTipList = new ArrayList<>();
    @Transient
    private List<Long> fleetIds = new ArrayList<>();

    private File appPath = new File(System.getProperty("user.dir"));
    private File outputDir = new File(appPath,"tozip");

    public DriverPayoutPeriod(String location, LocalDate payoutPeriodStart, LocalDate payoutPeriodEnd) {
        this.location = location;
        this.payoutPeriodStart = payoutPeriodStart;
        this.payoutPeriodEnd = payoutPeriodEnd;

        //determine if split over 2 months
        if(payoutPeriodStart.getMonth().equals(payoutPeriodEnd.getMonth())){
            partMonth = Boolean.FALSE;
            partMonthPayoutPeriodEnd = payoutPeriodEnd;
        }else{
            partMonth = Boolean.TRUE;
            partMonthPayoutPeriodEnd = payoutPeriodStart.with(TemporalAdjusters.lastDayOfMonth());
        }
        buildPeriod();

    }

    private void buildPeriod(){
        TaskDetailRepository taskDetailRepository = Registry.getBean(TaskDetailRepository.class);
        //build the driverPayoutWeekList here from a query from TaskDetailRepository
        log.info("DriverPayoutPeriod constructor: payoutPeriodStart" + payoutPeriodStart + " payoutPeriodEnd:" + payoutPeriodEnd + " partMonthPayoutPeriodEnd:" + partMonthPayoutPeriodEnd);
        fleetIds = taskDetailRepository.findDistinctFleetIdBetweenDates(payoutPeriodStart.atStartOfDay(),payoutPeriodEnd.atTime(23,59,59));
        //find any fleetIds that only had adjustments - no deliveries
        DriverAdjustmentRepository driverAdjustmentRepository = Registry.getBean(DriverAdjustmentRepository.class);
        List<Long> adjustmentFleetIds = driverAdjustmentRepository.findDistinctFleetIdByAdjustmentDateBetween(payoutPeriodStart,payoutPeriodEnd);
        List<Long> newAdjustmentFleetIds = new ArrayList<>();
        for (Long fleetId: adjustmentFleetIds) {
            if(!fleetIds.contains(fleetId)){
                newAdjustmentFleetIds.add(fleetId);
            }
        }
        fleetIds.addAll(newAdjustmentFleetIds);
        //process all fleetId and build the week payout records
        for (Long fleetId: fleetIds) {
            if(fleetId!=null){
                //log.info("DriverPayoutPeriod constructor: processing fleetId:" + fleetId);
                //TODO:  likely can remove list by using map.values
                driverPayoutWeekList.add(new DriverPayoutWeek(fleetId, payoutPeriodStart, payoutPeriodEnd));
                driverPayoutWeekMap.put(fleetId,new DriverPayoutWeek(fleetId, payoutPeriodStart, payoutPeriodEnd));
                if(partMonth){
                    partMonthDriverPayoutWeekMap.put(fleetId,new DriverPayoutWeek(fleetId, payoutPeriodStart, partMonthPayoutPeriodEnd));
                }
            }
        }

        for (DriverPayoutWeek driverPayoutWeek: driverPayoutWeekList) {
            this.setTaskCount(this.getTaskCount() + driverPayoutWeek.getTaskCount());
            this.setDriverPay(this.getDriverPay() + driverPayoutWeek.getDriverPay());
            this.setTip(this.getTip() + driverPayoutWeek.getTip());
            this.setCardTip(this.getCardTip() + driverPayoutWeek.getCardTip());
            this.setDriverIncome(this.getDriverIncome() + driverPayoutWeek.getDriverIncome());
            this.setDriverCash(this.getDriverCash() + driverPayoutWeek.getDriverCash());
            this.setDriverAdjustment(this.getDriverAdjustment() + driverPayoutWeek.getDriverAdjustment());
            this.setDriverPayout(this.getDriverPayout() + driverPayoutWeek.getDriverPayout());
            this.setDriverCost(this.getDriverCost() + driverPayoutWeek.getDriverCost());
            this.driverAdjustmentList.addAll(driverPayoutWeek.getDriverAdjustmentList());
            //log.info("Adding Driver Card Tip: id:" + driverPayoutWeek.getFleetId() + " cardTip:" + driverPayoutWeek.getCardTip());
            this.driverCardTipList.add(new DriverCardTip(driverPayoutWeek.getFleetId(), driverPayoutWeek.getFleetName(),driverPayoutWeek.getCardTip()));
        }

        if(partMonth){
            for (DriverPayoutWeek driverPayoutWeek: partMonthDriverPayoutWeekMap.values()) {
                partMonthDriverPay = partMonthDriverPay + driverPayoutWeek.getDriverPay();
                partMonthDriverAdjustments = partMonthDriverAdjustments + driverPayoutWeek.getDriverAdjustment();
                partMonthDriverTips = partMonthDriverTips + driverPayoutWeek.getTip();
                partMonthDriverCash = partMonthDriverCash + driverPayoutWeek.getDriverCash();
            }
        }

    }

    public void refresh(){
        //clear all variables
        this.taskCount = 0;
        this.driverPay = 0.0;
        this.tip = 0.0;
        this.cardTip = 0.0;
        this.driverIncome = 0.0;
        this.driverCash = 0.0;
        this.driverAdjustment = 0.0;
        this.driverPayout = 0.0;
        this.driverCost = 0.0;
        this.driverPayoutWeekList.clear();
        this.driverAdjustmentList.clear();
        this.partMonthDriverPayoutWeekMap.clear();
        this.driverPayoutWeekMap.clear();
        this.driverCardTipList.clear();

        this.pdfFile = null;
        this.csvFile = null;
        this.journalFile = null;
        buildPeriod();
    }

    public DriverPayoutPeriod() {

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getPayoutPeriodStart() {
        return payoutPeriodStart;
    }

    public void setPayoutPeriodStart(LocalDate payoutPeriodStart) {
        this.payoutPeriodStart = payoutPeriodStart;
    }

    public LocalDate getPayoutPeriodEnd() {
        return payoutPeriodEnd;
    }

    public void setPayoutPeriodEnd(LocalDate payoutPeriodEnd) {
        this.payoutPeriodEnd = payoutPeriodEnd;
    }

    public List<DriverPayoutWeek> getDriverPayoutWeekList() {
        return driverPayoutWeekList;
    }

    public void setDriverPayoutWeekList(List<DriverPayoutWeek> driverPayoutWeekList) {
        this.driverPayoutWeekList = driverPayoutWeekList;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public Double getDriverPay() {
        return Utility.getInstance().round(driverPay,2);
    }

    public void setDriverPay(Double driverPay) {
        this.driverPay = driverPay;
    }

    public Double getTip() {
        return Utility.getInstance().round(tip,2);
    }

    public void setTip(Double tip) {
        this.tip = tip;
    }

    public Double getCardTip() {
        return Utility.getInstance().round(cardTip,2);
    }

    public void setCardTip(Double cardTip) {
        this.cardTip = cardTip;
    }

    public Double getDriverIncome() {
        return Utility.getInstance().round(driverIncome,2);
    }

    public void setDriverIncome(Double driverIncome) {
        this.driverIncome = driverIncome;
    }

    public Double getDriverCash() {
        return Utility.getInstance().round(driverCash,2);
    }

    public void setDriverCash(Double driverCash) {
        this.driverCash = driverCash;
    }

    public Double getDriverAdjustment() {
        return Utility.getInstance().round(driverAdjustment,2);
    }

    public void setDriverAdjustment(Double driverAdjustment) {
        this.driverAdjustment = driverAdjustment;
    }

    public Double getDriverPayout() {
        return Utility.getInstance().round(driverPayout,2);
    }

    public void setDriverPayout(Double driverPayout) {
        this.driverPayout = driverPayout;
    }

    public Double getDriverCost() {
        return Utility.getInstance().round(driverCost,2);
    }

    public void setDriverCost(Double driverCost) {
        this.driverCost = driverCost;
    }

    public File getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    public File getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(File csvFile) {
        this.csvFile = csvFile;
    }

    public File getJournalFile() {
        return journalFile;
    }

    public void setJournalFile(File journalFile) {
        this.journalFile = journalFile;
    }

    @Override
    public String toString() {
        return "DriverPayoutPeriod{" +
                "location='" + location + '\'' +
                ", payoutPeriodStart=" + payoutPeriodStart +
                ", payoutPeriodEnd=" + payoutPeriodEnd +
                ", taskCount=" + taskCount +
                ", driverPay=" + getDriverPay() +
                ", tip=" + getTip() +
                ", driverIncome=" + getDriverIncome() +
                ", driverCash=" + getDriverCash() +
                ", driverAdjustment=" + getDriverAdjustment() +
                ", driverPayout=" + getDriverPayout() +
                ", cardTip=" + getCardTip() +
                ", driverPayoutWeekList=" + driverPayoutWeekList +
                '}';
    }
    public String getDriverPayFmt(){
        return String.format("%.2f",getDriverPay());
    }
    public String getTipFmt(){
        return String.format("%.2f",getTip());
    }
    public String getDriverIncomeFmt(){
        return String.format("%.2f",getDriverIncome());
    }
    public String getDriverCashFmt(){
        return String.format("%.2f",getDriverCash());
    }
    public String getDriverAdjustmentFmt(){
        return String.format("%.2f",getDriverAdjustment());
    }
    public String getDriverPayoutFmt(){
        return String.format("%.2f",getDriverPayout());
    }
    public String getDriverCostFmt(){
        return String.format("%.2f",getDriverCost());
    }

    public void createJournalEntries(){
        journalEntries.clear();
        LocalDate journalEndDate;
        if(partMonth){
            journalEndDate = partMonthPayoutPeriodEnd;
        }else{
            journalEndDate = payoutPeriodEnd;
        }
        String prefix = "DriverPay_";
        String prefixMemo = "Driver payout";
        String prefixFile = "DriverPayoutJournalEntry-";
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setDocNumber(prefix, journalEndDate);
        journalEntry.setTxnDate(journalEndDate);
        journalEntry.setPrivateNote(prefixMemo, payoutPeriodStart,journalEndDate);
        journalEntry.setFileName(prefixFile, payoutPeriodStart,journalEndDate);
        log.info("createJournalEntries: journalNo:" + journalEntry.getDocNumber());
        if(partMonth){
            //build first part of period
            journalEntry.addLine(getPartMonthDriverPay(), JournalEntry.PostingType.Debit,"COGS Driver:Driver Pay","");
            journalEntry.addLine(getPartMonthDriverAdjustments(), JournalEntry.PostingType.Debit,"COGS Driver:Driver Adjustment","");
            journalEntry.addLine(getPartMonthDriverTips(), JournalEntry.PostingType.Debit,"Tips payable","");
            journalEntry.addLine(getPartMonthDriverCash(), JournalEntry.PostingType.Credit,"Cash on hand","");
            for (Long fleetId: fleetIds) {
                if(partMonthDriverPayoutWeekMap.containsKey(fleetId)){
                    String driverName = partMonthDriverPayoutWeekMap.get(fleetId).getFleetName();
                    Double payout = partMonthDriverPayoutWeekMap.get(fleetId).getDriverPayout();
                    if(payout>0.0){
                        journalEntry.addLine(payout, JournalEntry.PostingType.Credit,"Chequing","", Intuit.EntityType.Employee,driverName);
                    }else if(payout<0.0){
                        journalEntry.addLine(Math.abs(payout), JournalEntry.PostingType.Debit,"Chequing","", Intuit.EntityType.Employee,driverName);
                    }
                }
            }
            if(!journalEntry.getErrorProcessing()){  //only add if the journalEntry has no errors during processing
                log.info("createJournalEntries: journalEntry:" + journalEntry);
                journalEntries.add(journalEntry);
            }else{
                log.info("createJournalEntries: failed creating journalEntry:" + journalEntry);
            }

            //build second part of period
            JournalEntry journalEntry2 = new JournalEntry();
            journalEntry2.setDocNumber(prefix, payoutPeriodEnd);
            journalEntry2.setTxnDate(payoutPeriodEnd);
            journalEntry2.setPrivateNote(prefixMemo, partMonthPayoutPeriodEnd.plusDays(1L),payoutPeriodEnd);
            journalEntry2.setFileName(prefixFile, partMonthPayoutPeriodEnd.plusDays(1L),payoutPeriodEnd);
            journalEntry2.addLine(Utility.getInstance().round(getDriverPay()-getPartMonthDriverPay(),2), JournalEntry.PostingType.Debit,"COGS Driver:Driver Pay","");
            journalEntry2.addLine(Utility.getInstance().round(getDriverAdjustment()-getPartMonthDriverAdjustments(),2), JournalEntry.PostingType.Debit,"COGS Driver:Driver Adjustment","");
            journalEntry2.addLine(Utility.getInstance().round(getTip()-getPartMonthDriverTips(),2), JournalEntry.PostingType.Debit,"Tips payable","");
            journalEntry2.addLine(Utility.getInstance().round(getDriverCash()-getPartMonthDriverCash(),2), JournalEntry.PostingType.Credit,"Cash on hand","");
            for (Long fleetId: fleetIds) {
                if(driverPayoutWeekMap.containsKey(fleetId)){
                    String driverName = driverPayoutWeekMap.get(fleetId).getFleetName();
                    Double payout = 0.0;
                    if(partMonthDriverPayoutWeekMap.containsKey(fleetId)){
                        payout = Utility.getInstance().round(driverPayoutWeekMap.get(fleetId).getDriverPayout() - partMonthDriverPayoutWeekMap.get(fleetId).getDriverPayout(),2);
                        //if the split will cause the 2nd part to be negative then make zero and put it in the first part
                    }else{
                        payout = driverPayoutWeekMap.get(fleetId).getDriverPayout();
                    }
                    if(payout>0.0){
                        journalEntry2.addLine(payout, JournalEntry.PostingType.Credit,"Chequing","", Intuit.EntityType.Employee,driverName);
                    }else if(payout<0.0){
                        journalEntry2.addLine(Math.abs(payout), JournalEntry.PostingType.Debit,"Chequing","", Intuit.EntityType.Employee,driverName);
                    }
                }
            }
            if(!journalEntry2.getErrorProcessing()){  //only add if the journalEntry has no errors during processing
                log.info("createJournalEntries: journalEntry2:" + journalEntry2);
                journalEntries.add(journalEntry2);
            }else{
                log.info("createJournalEntries: failed creating journalEntry2:" + journalEntry2);
            }

        }else{
            journalEntry.addLine(getDriverPay(), JournalEntry.PostingType.Debit,"COGS Driver:Driver Pay","");
            log.info("createJournalEntries: getDriverPay:" + getDriverPay());
            journalEntry.addLine(getDriverAdjustment(), JournalEntry.PostingType.Debit,"COGS Driver:Driver Adjustment","");
            log.info("createJournalEntries: getDriverAdjustment:" + getDriverAdjustment());
            journalEntry.addLine(getTip(), JournalEntry.PostingType.Debit,"Tips payable","");
            log.info("createJournalEntries: getTip:" + getTip());
            journalEntry.addLine(getDriverCash(), JournalEntry.PostingType.Credit,"Cash on hand","");
            log.info("createJournalEntries: getDriverCash:" + getDriverCash());
            for (DriverPayoutWeek driverPayoutWeek: getDriverPayoutWeekList()) {
                if(driverPayoutWeek.getDriverPayout()>0.0){
                    journalEntry.addLine(driverPayoutWeek.getDriverPayout(), JournalEntry.PostingType.Credit,"Chequing","", Intuit.EntityType.Employee,driverPayoutWeek.getFleetName());
                    log.info("createJournalEntries: getDriverPayout credit:" + driverPayoutWeek.getDriverPayout());
                }else if(driverPayoutWeek.getDriverPayout()<0.0){
                    journalEntry.addLine(Math.abs(driverPayoutWeek.getDriverPayout()), JournalEntry.PostingType.Debit,"Chequing","", Intuit.EntityType.Employee,driverPayoutWeek.getFleetName());
                    log.info("createJournalEntries: getDriverPayout debit:" + Math.abs(driverPayoutWeek.getDriverPayout()));
                }
            }
            if(!journalEntry.getErrorProcessing()){  //only add if the journalEntry has no errors during processing
                log.info("createJournalEntries: journalEntry:" + journalEntry);
                journalEntries.add(journalEntry);
            }else{
                log.info("createJournalEntries: failed creating journalEntry:" + journalEntry);
            }
        }
    }

    public Double getPartMonthDriverPay() {
        return Utility.getInstance().round(partMonthDriverPay,2);
    }

    public Double getPartMonthDriverAdjustments() {
        return Utility.getInstance().round(partMonthDriverAdjustments,2);
    }

    public Double getPartMonthDriverTips() {
        return Utility.getInstance().round(partMonthDriverTips,2);
    }

    public Double getPartMonthDriverCash() {
        return Utility.getInstance().round(partMonthDriverCash,2);
    }

    public List<PayoutDocument> getPayoutDocuments(){
        List<PayoutDocument> payoutDocumentList = new ArrayList<>();
        if(getCsvFile()==null){
            return payoutDocumentList;
        }

        //create and save a journalEntry
        if(journalEntries.size()==0){
            log.info("getPayoutDocuments: no journalEntries found");
            Notification.show("Failed: no journal entries created. You may need to connect to QBO");
        }else{
            for (JournalEntry journalEntry: journalEntries) {
                String journalFileName = journalEntry.getFileName();
                File journalFile = new File(outputDir,journalFileName);

                QBOResult qboResult = journalEntry.save(journalFile);
                if(qboResult.getSuccess()){
                    payoutDocumentList.add(new PayoutDocument(journalFileName, journalFile, ""));
                }else{
                    log.info("getPayoutDocuments: error saving journalEntry to file:" + journalFileName);
                }
            }
        }

        /*
        if(getJournalFile()!=null){
            payoutDocumentList.add(new PayoutDocument("Driver payout journal entry", getJournalFile(), ""));
        }
         */
        payoutDocumentList.add(new PayoutDocument("All driver payout tasks (Excel)", getCsvFile(), ""));
        payoutDocumentList.add(new PayoutDocument("Driver payout summary statement (PDF)", getPdfFile(),""));
        for (DriverPayoutWeek driverPayoutWeek: getDriverPayoutWeekList()) {
            payoutDocumentList.add(new PayoutDocument("Statement for:" + driverPayoutWeek.getFleetName() + " (PDF)", driverPayoutWeek.getPdfFile(),driverPayoutWeek.getDriver().getEmail()));
        }
        return payoutDocumentList;
    }
    public List<DriverAdjustment> getDriverAdjustmentList() {
        driverAdjustmentList.sort(Comparator.comparing(DriverAdjustment::getFleetName).thenComparing(DriverAdjustment::getAdjustmentDate));
        return driverAdjustmentList;
    }

    public List<DriverCardTip> getDriverCardTipList() {
        return driverCardTipList;
    }

    public List<Long> getFleetIds() {
        List<Long> fleetIds = new ArrayList<>();
        for (DriverPayoutWeek driverPayoutWeek: driverPayoutWeekList) {
            fleetIds.add(driverPayoutWeek.getFleetId());
        }
        return fleetIds;
    }

    public List<DriverPayoutEntity> getTipIssues(){
        TaskDetailRepository taskDetailRepository = Registry.getBean(TaskDetailRepository.class);
        return taskDetailRepository.getDriverPayoutTipIssues(payoutPeriodStart.atStartOfDay(),payoutPeriodEnd.atTime(23,59,59));
    }

    public List<JournalEntry> getJournalEntries() {
        return journalEntries;
    }
}
