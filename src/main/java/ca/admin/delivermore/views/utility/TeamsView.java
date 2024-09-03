package ca.admin.delivermore.views.utility;

import ca.admin.delivermore.collector.data.service.RestClientService;
import ca.admin.delivermore.collector.data.service.TeamsRepository;
import ca.admin.delivermore.collector.data.tookan.Team;
import ca.admin.delivermore.gridexporter.ButtonsAlignment;
import ca.admin.delivermore.gridexporter.GridExporter;
import ca.admin.delivermore.views.MainLayout;
import ca.admin.delivermore.views.UIUtilities;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.security.RolesAllowed;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@PageTitle("Locations")
@Route(value = "locations", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class TeamsView extends VerticalLayout {

    private Logger log = LoggerFactory.getLogger(TeamsView.class);

    private List<Team> teamList;

    private List<Team> selectedTeams = new ArrayList<>();

    RestClientService restClientService;

    TeamsRepository teamsRepository;

    private VerticalLayout mainLayout = UIUtilities.getVerticalLayout();
    private Grid<Team> grid = new Grid<>();

    private Button activeToggle = new Button("Toggle Active");

    public TeamsView(RestClientService restClientService, TeamsRepository teamsRepository) {
        this.restClientService = restClientService;
        this.teamsRepository = teamsRepository;

        mainLayout.add(getToolbar());
        mainLayout.add(getGrid());
        setSizeFull();
        mainLayout.setSizeFull();
        add(mainLayout);

    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = UIUtilities.getHorizontalLayout(true,true,false );
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.addClassName("toolbar");
        Button refreshFromTookanButton = new Button("Refresh from Tookan");
        refreshFromTookanButton.setDisableOnClick(true);
        refreshFromTookanButton.addClickListener(event -> {
            refreshTeams();
            grid.getDataProvider().refreshAll();
            refreshFromTookanButton.setEnabled(true);
        });

        activeToggle.setDisableOnClick(true);
        activeToggle.setEnabled(false);
        activeToggle.addClickListener(event -> {
            toggleActiveForSelectedTeams();
            grid.getDataProvider().refreshAll();
            activeToggle.setEnabled(true);
        });
        toolbar.add(refreshFromTookanButton, activeToggle);
        return toolbar;

    }

    private void toggleActiveForSelectedTeams() {
        for (Team team: selectedTeams ) {
            team.setActive(!team.getActive());
            teamsRepository.save(team);
        }
        teamList = teamsRepository.findByOrderByTeamNameAsc();
    }

    private VerticalLayout getGrid() {
        VerticalLayout gridLayout = UIUtilities.getVerticalLayout();
        gridLayout.setWidthFull();
        gridLayout.setHeightFull();
        teamList = teamsRepository.findByOrderByTeamNameAsc();
        grid.setItems(teamList);
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        GridExporter<Team> exporter = GridExporter.createFor(grid);

        exporter.createExportColumn(grid.addColumn(Team::getTeamName).setFlexGrow(1).setSortable(true),true,"Name");
        String statusWidth = "50px";
        exporter.createExportColumn(grid.addComponentColumn(team -> UIUtilities.createStatusIcon(team.getActive())).setWidth(statusWidth).setComparator(Team::getActive),true,"Active",grid.addColumn(Team::getActive));
        exporter.createExportColumn(grid.addColumn(Team::getTeamId).setFlexGrow(0).setSortable(true),true,"Id");
        exporter.createExportColumn(grid.addColumn(Team::getAddress).setFlexGrow(1).setSortable(false),true,"Address");
        exporter.createExportColumn(grid.addColumn(Team::getLatitude).setFlexGrow(0).setSortable(false),true,"Latitude");
        exporter.createExportColumn(grid.addColumn(Team::getLongitude).setFlexGrow(0).setSortable(false),true,"Longitude");

        exporter.setFileName("DriverExport" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
        exporter.setButtonsAlignment(ButtonsAlignment.LEFT);

        gridLayout.add(grid);
        gridLayout.setFlexGrow(1,grid);

        grid.addSelectionListener(e -> {
            selectedTeams.clear();
            selectedTeams.addAll(e.getAllSelectedItems());
            if(selectedTeams.size()>0){
                activeToggle.setEnabled(true);
            }else{
                activeToggle.setEnabled(false);
            }
        });

        return gridLayout;
    }


    private void refreshTeams() {
        //refresh the teams from tookan in case they have changed
        List<Team> currentTeamsList = restClientService.getAllTeams();
        //update existing tookan data or save new
        for (Team team: currentTeamsList ) {
            Team foundTeam = teamsRepository.findByTeamId(team.getTeamId());
            if(foundTeam==null){ //new
                log.info("refreshDrivers: saving new team:" + team.getTeamName());
                teamsRepository.save(team);
            }else{ //update
                log.info("refreshDrivers: updating tookan fields for team:" + team.getTeamName());
                foundTeam.updateTeamTookanOnly(team);
                teamsRepository.save(foundTeam);
            }
        }
        teamList = teamsRepository.findByOrderByTeamNameAsc();
    }


}
