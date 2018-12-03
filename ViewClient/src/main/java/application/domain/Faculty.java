package application.domain;

import javax.persistence.*;

@Entity
@Table(name = "faculty",uniqueConstraints = {
        @UniqueConstraint(columnNames = "faculty_name")})

public class Faculty implements java.io.Serializable{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private int facultyId;

    @Column(name = "faculty_name",nullable = false)
    private String facultyName;


    public Faculty() {};

    public Faculty(String facultyName) {
        this.facultyName = facultyName;
    }

    public int getFacultyId() {
        return facultyId;
    }
    public void setFacultyId(int facultyId) {
        this.facultyId= facultyId;
    }
    public String getFacultyName() {
        return facultyName;
    }
    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    @Override
    public String toString() {
        return "Faculty [id=" + facultyId+ ", facultyName=" + facultyName + "]";
    }

}