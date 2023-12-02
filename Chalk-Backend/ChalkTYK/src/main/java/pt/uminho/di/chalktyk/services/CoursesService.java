package pt.uminho.di.chalktyk.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.uminho.di.chalktyk.models.nonrelational.courses.Course;
import pt.uminho.di.chalktyk.models.relational.Institution;
import pt.uminho.di.chalktyk.models.relational.Specialist;
import pt.uminho.di.chalktyk.models.relational.Student;
import pt.uminho.di.chalktyk.repositories.nonrelational.CourseDAO;
import pt.uminho.di.chalktyk.repositories.relational.CourseSqlDAO;
import pt.uminho.di.chalktyk.services.exceptions.BadInputException;
import pt.uminho.di.chalktyk.services.exceptions.NotFoundException;

import java.util.List;
import java.util.Set;

@Service
public class CoursesService implements ICoursesService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final CourseDAO courseDAO;
    private final CourseSqlDAO courseSqlDAO;
    private final IInstitutionsService institutionsService;
    private final ISpecialistsService specialistsService;
    private final IStudentsService studentsService;

    @Autowired
    public CoursesService(EntityManager entityManager, CourseDAO courseDAO, CourseSqlDAO courseSqlDAO, IInstitutionsService institutionsService, ISpecialistsService specialistsService, IStudentsService studentsService) {
        this.entityManager = entityManager;
        this.courseDAO = courseDAO;
        this.courseSqlDAO = courseSqlDAO;
        this.institutionsService = institutionsService;
        this.specialistsService = specialistsService;
        this.studentsService = studentsService;
    }

    /**
     * Creates a course
     *
     * @param course basic properties of the course
     * @return identifier of the course
     * @throws BadInputException when the course is null, or when a property of the course is not valid, like empty name,
     *                           invalid institution id (if given), or an invalid (specialist) owner id
     */
    @Override
    @Transactional
    public String createCourse(Course course) throws BadInputException {
        if(course == null)
            throw new BadInputException("Cannot create course: course is null.");

        course.setId(null); // to prevent overwrite attacks

        // check name
        String name = course.getName();
        if(name == null || name.isEmpty())
            throw new BadInputException("Cannot create course: A name of a course cannot be empty or null.");

        // check owner (specialist) id
        String ownerId = course.getOwnerId();
        if(ownerId == null)
            throw new BadInputException("Cannot create course: A course must have an owner.");
        if(!specialistsService.existsSpecialistById(ownerId))
            throw new BadInputException("Cannot create course: A course must have a valid owner.");

        // check institution id
        String institutionId = course.getInstituitionId();
        if(institutionId != null){
            if(!institutionsService.existsInstitutionById(institutionId))
                throw new BadInputException("Cannot create course: To associate a course to an institution," +
                                            " the institution id must be valid.");

            // check if the owner of the course belongs to the given institution
            if(!institutionsService.isSpecialistOfInstitution(ownerId, institutionId))
                throw new BadInputException("Cannot create course: The institution of the course does not" +
                                            " match the institution of the course's owner.");
        }

        // persist the course in nosql database
        course = courseDAO.save(course);

        // persists the course in sql database
        Institution inst = entityManager.getReference(Institution.class, institutionId);
        Specialist spec = entityManager.getReference(Specialist.class, ownerId);
        var courseSQL = new pt.uminho.di.chalktyk.models.relational.Course(course.getId(), inst, course.getName(), Set.of(spec));
        courseSqlDAO.save(courseSQL);

        return course.getId();
    }

    /**
     * Add specialists to course
     *
     * @param courseId       identifier of the course
     * @param specialistsIds list of specialists identifiers
     */
    @Transactional
    @Override
    public void addSpecialistsToCourse(String courseId, List<String> specialistsIds) throws NotFoundException {
        if(!courseSqlDAO.existsById(courseId))
            throw new NotFoundException("Could not add specialists: course not found.");
        for(String specialistId : specialistsIds){
            if(specialistsService.existsSpecialistById(specialistId))
                courseSqlDAO.addSpecialistToCourse(specialistId, courseId);
        }
    }

    /**
     * Add students to course.
     *
     * @param courseId    identifier of the course
     * @param studentsIds list of students identifiers
     */
    @Transactional
    @Override
    public void addStudentsToCourse(String courseId, List<String> studentsIds) throws NotFoundException {
        if(!courseSqlDAO.existsById(courseId))
            throw new NotFoundException("Could not add students: course not found.");
        for(String studentId : studentsIds){
            if(studentsService.existsStudentById(studentId))
                courseSqlDAO.addStudentToCourse(studentId, courseId);
        }
    }

    /**
     * Remove specialists from course
     *
     * @param courseId       identifier of the course
     * @param specialistsIds list of specialists identifiers
     */
    @Transactional
    @Override
    public void removeSpecialistsFromCourse(String courseId, List<String> specialistsIds) throws NotFoundException {
        if(!courseSqlDAO.existsById(courseId))
            throw new NotFoundException("Could not remove specialists: course not found.");
        for(String specialistId : specialistsIds){
            if(specialistsService.existsSpecialistById(specialistId))
                courseSqlDAO.removeSpecialistFromCourse(specialistId, courseId);
        }
    }

    /**
     * Remove students from course.
     *
     * @param courseId    identifier of the course
     * @param studentsIds list of students identifiers
     */
    @Transactional
    @Override
    public void removeStudentsFromCourse(String courseId, List<String> studentsIds) throws NotFoundException {
        if(!courseSqlDAO.existsById(courseId))
            throw new NotFoundException("Could not remove students: course not found.");
        for(String studentId : studentsIds){
            if(studentsService.existsStudentById(studentId))
                courseSqlDAO.removeStudentFromCourse(studentId, courseId);
        }
    }

    /**
     * Update course basic information
     *
     * @param course basic course information
     */
    @Override
    public void updateCourse(Course course) {
        //TODO
    }

    /**
     * Get list of students that are associated with a specific course.
     *
     * @param courseId     course identifier
     * @param page         index of the page
     * @param itemsPerPage number of items each page has
     * @return list of students that are associated with a specific course.
     */
    @Override
    public Page<Student> getCourseStudents(String courseId, int page, int itemsPerPage) {
        return null;        //TODO
    }

    /**
     * Get list of specialists that are associated with a specific course.
     *
     * @param courseId     course identifier
     * @param page         index of the page
     * @param itemsPerPage number of items each page has
     * @return list of specialists that are associated with a specific course.
     */
    @Override
    public Page<Specialist> getCourseSpecialists(String courseId, int page, int itemsPerPage) {
        return null;        //TODO
    }

    /**
     * Get list of courses that a specific student is associated with
     *
     * @param studentId    identifier of the student
     * @param page         index of the page
     * @param itemsPerPage number of courses each page has
     * @return list of courses that a specific student is associated with
     */
    @Override
    public Page<Course> getStudentCourses(String studentId, int page, int itemsPerPage) {
        return null;        //TODO
    }

    /**
     * Get list of courses that a specific specialist is associated with
     *
     * @param specialistId identifier of the specialist
     * @param page         index of the page
     * @param itemsPerPage number of courses each page has
     * @return list of courses that a specific specialist is associated with
     */
    @Override
    public Page<Course> getSpecialistCourses(String specialistId, int page, int itemsPerPage) {
        return null;        //TODO
    }

    /**
     * Get list of courses that a specific institution is associated with
     *
     * @param institutionId identifier of the institution
     * @param page          index of the page
     * @param itemsPerPage  number of courses each page has
     * @return list of courses that a specific institution is associated with
     */
    @Override
    public Page<Course> getInstitutionCourses(String institutionId, int page, int itemsPerPage) {
        return null;        //TODO
    }
}
