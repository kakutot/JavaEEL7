package application.websocket;

import application.domain.Faculty;
import java.util.Date;
import java.util.List;

public class FacultyResponse {
    private List<Faculty> facultyList;
    private String formattedDate;

    public FacultyResponse(List<Faculty> facultyList, String formattedDate) {
        this.facultyList = facultyList;
        this.formattedDate = formattedDate;
    }

    public FacultyResponse(){}

    public String getFormatedDate() {
        return formattedDate;
    }

    public void setFormatedDate(String formatedDate) {
        this.formattedDate = formatedDate;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }


    public List<Faculty> getFacultyList() {
        return facultyList;
    }

    public void setFacultyList(List<Faculty> facultyList) {
        this.facultyList = facultyList;
    }
}
