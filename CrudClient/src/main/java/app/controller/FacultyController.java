package app.controller;

import app.model.Faculty;
import app.repo.FacultyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value="/faculties")
public class FacultyController {
    @Autowired
    private FacultyRepo facultyRepo;
    @Autowired
    private SocketMessengerService socketMessengerService;

    @GetMapping()
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


    @GetMapping(value = "/delete_faculty")
    public String deleteFaculty(@RequestParam(name="facultyId") String id){
        facultyRepo.deleteById(new Integer(id));
        socketMessengerService.sendNewDeleteMessage(id);

        return "redirect:/faculties";
    }
    @PostMapping(value = "/update_faculty")
    public String updateFaculty(Faculty faculty){
        facultyRepo.save(faculty);

        if(facultyRepo.findById(faculty.getFacultyId()).isPresent()){
            socketMessengerService.sendNewUpdateMessage(String.valueOf(faculty.getFacultyId()));
        }
        else{
            socketMessengerService.sendNewInsertMessage(String.valueOf(faculty.getFacultyId()));
        }


        return "redirect:/faculties";
    }

    @GetMapping("/findOne")
    @ResponseBody
    public Faculty findOne(@RequestParam int id ) {
        return facultyRepo.findById(id).orElse(new Faculty("undef"));
    }
}
