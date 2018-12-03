package application.controller;

import application.domain.Faculty;
import application.socket.CommonModel;
import application.socket.SocketChannelClient;
import application.websocket.FacultyRequest;
import application.websocket.FacultyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import application.repo.FacultyRepo;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class FacultyController {
    @Autowired
    private FacultyRepo facultyRepo;

    @GetMapping("/faculties")
    public String  faculties(@RequestParam(name = "filter",required = false)String filter,Model model) {
        Iterable result = facultyRepo.findAll();
        model.addAttribute("faculties",result);
        model.addAttribute("faculty",new Faculty());
        Iterable<Faculty> faculties;
        if(filter!=null && !filter.isEmpty()){
            faculties = facultyRepo.findByFacultyNameContaining(filter);
        }
        else {
            faculties = facultyRepo.findAll();
            filter = "";
        }
        model.addAttribute("filter",filter);
        model.addAttribute("faculties",faculties);
        return "faculties";
    }


    @GetMapping("/faculties/findOne")
    @ResponseBody
    public Faculty findOne(@RequestParam int id ) {
        return facultyRepo.findById(id).orElse(new Faculty("undef"));
    }

    @MessageMapping("/faculties/hello")
    @SendTo("/topic/faculties")
    public FacultyResponse facultyResponse(FacultyRequest facultyRequest) throws Exception {
        System.out.println("Websocket request : " + facultyRequest);
        CommonModel result;
        while ((result = SocketChannelClient.getCurrentMessage()) == null){
            //System.out.println("No data available");
        }
        System.out.println("New Response from server : " + result);
        List<Faculty> faculties = StreamSupport.stream(facultyRepo.findAll()
                                                .spliterator(),false)
                                                .collect(Collectors.toList());

        String formattedTime = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss")
                .format(new Date(Long.valueOf(result.getTime())));

        return new FacultyResponse(faculties,formattedTime);
    }
}
