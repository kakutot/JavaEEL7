package application.repo;


import application.domain.Faculty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface  FacultyRepo  extends CrudRepository<Faculty,Integer> {
    List<Faculty> findByFacultyNameContaining(String facultyName);
    Faculty getDistinctByFacultyName(String facultyName);
}
