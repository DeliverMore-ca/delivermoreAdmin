package ca.admin.delivermore.data.service;

import ca.admin.delivermore.tookan.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@Service
public class RestClientService implements Serializable {

    private String tookan_api = "53606482f1430f144a136d654e4f2d471ae1cdfd28d4793e5f1506c4";
    private String tookan_baseUrl = "https://api.tookanapp.com/v2";

    /**
     * Returns parsed {@link Task} objects from the REST service.
     *
     * Useful when the response data has a known structure.
     */
    public List<TaskDetail> getAllTasks(LocalDate fromDate, LocalDate toDate) {

        List<TaskDetail> taskDetails = new ArrayList<>();

        String urlExtra = "/get_all_tasks";
        String urlFull = tookan_baseUrl + urlExtra;

        System.out.println("Fetching all Task objects through Tookan REST..");

        //List<Task> tasks = new ArrayList<>();
        Boolean lastPage = Boolean.FALSE;
        Integer currentPage = 1;

        //get all pages of tasks - run at least once
        do {

            // Fetch from Tookan API
            RequestHeadersSpec<?> spec = WebClient.create().post()
                    .uri(urlFull)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(getTaskHeaderBody(fromDate,toDate,currentPage.toString())));

            // do fetch and map result
            System.out.println("taskList: gettingJSON");
            String taskString = spec.retrieve().toEntity(String.class).block().getBody();
            //TODO: check return status
            //System.out.println("taskList String:" + taskString);
            ObjectMapper objectMapper = new ObjectMapper();
            TaskList taskList = null;
            try {
                taskList = objectMapper.readValue(taskString,TaskList.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if(taskList==null){
                System.out.println("taskList: No tasks found");
            }else{
                System.out.println("taskList: processing page " + currentPage + " of " + taskList.getTotalPageCount() + " pages");
                //tasks.addAll(taskList.getData());
                //TODO: get the Task Details for each task
                //List<Long> tempList = new ArrayList<>();
                //tempList.add(436403278L);
                taskDetails.addAll(getTaskDetails(taskList.getTaskIDs()));
                //taskDetails.addAll(getTaskDetails(tempList));

            }
            currentPage++;
            if(currentPage>taskList.getTotalPageCount()){
                lastPage = Boolean.TRUE;
            }

        } while(lastPage.equals(Boolean.FALSE));

        System.out.println(String.format("...processed %d tasks.", taskDetails.size()));

        return taskDetails;
    }

    private String getTaskHeaderBody(LocalDate fromDate, LocalDate toDate, String curPage){

        System.out.println("getTaskHeaderBody: fromDate:" + fromDate.toString() + " toDate:" + toDate.toString());

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode bodyValues = mapper.createObjectNode();

        bodyValues.put("api_key", tookan_api);
        bodyValues.put("job_type", "1");
        //bodyValues.Add("team_id", 1335347);
        //bodyValues.put("job_status", "2"); //2 is success only
        bodyValues.put("start_date", fromDate.toString());
        bodyValues.put("end_date", toDate.toString());
        bodyValues.put("custom_fields", "0");
        bodyValues.put("is_pagination", "1"); //change to 1 to page
        bodyValues.put("requested_page", curPage);

        //System.out.println("getTaskHeaderBody:" + bodyValues.toString());
        return bodyValues.toString();

    }

    /**
     * Returns parsed {@link TaskDetail} objects from the REST service.
     *
     * Useful when the response data has a known structure.
     */
    public List<TaskDetail> getTaskDetails(List<Long> taskIDs) {

        List<TaskDetail> taskDetails = new ArrayList<>();

        String urlExtra = "/get_job_details";
        String urlFull = tookan_baseUrl + urlExtra;

        System.out.println("Fetching Task Details through Tookan REST..");

        WebClient myWebClient = WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build())
                .build();

        // Fetch from Tookan API
        RequestHeadersSpec<?> spec = myWebClient
                .post()
                .uri(urlFull)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(getTaskDetailHeaderBody(taskIDs)));

        // do fetch and map result
        System.out.println("taskDetailList: gettingJSON");
        String taskDetailString = spec.retrieve().toEntity(String.class).block().getBody();
        //TODO: check return status
        System.out.println("taskDetailList String:" + taskDetailString);
        ObjectMapper objectMapper = new ObjectMapper();
        TaskDetailList taskDetailList = null;
        try {
            taskDetailList = objectMapper.readValue(taskDetailString,TaskDetailList.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(taskDetailList==null){

        }else{
            //System.out.println("taskList:" + taskDetailList);
            taskDetails.addAll(taskDetailList.getData());
        }

        System.out.println(String.format("...received %d taskDetails.", taskDetails.size()));

        return taskDetails;
    }

    private String getTaskDetailHeaderBody(List<Long> taskIDs){

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode bodyValues = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();
        for (Long item: taskIDs ) {
            arrayNode.add(item);
        }

        bodyValues.put("api_key", tookan_api);
        bodyValues.put("job_ids", arrayNode);
        bodyValues.put("include_task_history", "1");
        bodyValues.put("job_additional_info", "1");

        //System.out.println("getTaskDetailHeaderBody:" + bodyValues.toString());
        return bodyValues.toString();

    }

    /**
     * Returns parsed {@link Task} objects from the REST service.
     *
     * Useful when the response data has a known structure.
     */
    public List<Driver> getAllDrivers() {

        List<Driver> driverList = new ArrayList<>();

        String urlExtra = "/get_all_fleets";
        String urlFull = tookan_baseUrl + urlExtra;

        System.out.println("Fetching all Drivers through Tookan REST..");

        // Fetch from Tookan API
        RequestHeadersSpec<?> spec = WebClient.create().post()
                .uri(urlFull)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(getDriversHeaderBody()));

        // do fetch and map result
        System.out.println("driversList: gettingJSON");
        String driversString = spec.retrieve().toEntity(String.class).block().getBody();
        //TODO: check return status
        //System.out.println("driversList String:" + driversString);
        ObjectMapper objectMapper = new ObjectMapper();
        Drivers drivers = null;
        try {
            drivers = objectMapper.readValue(driversString,Drivers.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(drivers==null){
            System.out.println("driversList: No drivers found");
        }else{
            driverList.addAll(drivers.getData());
        }

        System.out.println(String.format("...processed %d drivers.", driverList.size()));

        return driverList;
    }

    private String getDriversHeaderBody(){

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode bodyValues = mapper.createObjectNode();

        bodyValues.put("api_key", tookan_api);

        //System.out.println("getDriversHeaderBody:" + bodyValues.toString());
        return bodyValues.toString();

    }


}
