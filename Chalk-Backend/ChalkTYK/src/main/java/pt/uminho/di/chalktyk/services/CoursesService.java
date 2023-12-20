package pt.uminho.di.chalktyk.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.uminho.di.chalktyk.models.courses.Course;
import pt.uminho.di.chalktyk.models.institutions.Institution;
import pt.uminho.di.chalktyk.models.users.Specialist;
import pt.uminho.di.chalktyk.models.users.Student;
import pt.uminho.di.chalktyk.repositories.CourseDAO;
import pt.uminho.di.chalktyk.services.exceptions.BadInputException;
import pt.uminho.di.chalktyk.services.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoursesService implements ICoursesService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final CourseDAO courseDAO;
    private final IInstitutionsService institutionsService;
    private final ISpecialistsService specialistsService;
    private final IStudentsService studentsService;

    @Autowired
    public CoursesService(EntityManager entityManager, CourseDAO courseDAO, IInstitutionsService institutionsService, ISpecialistsService specialistsService, IStudentsService studentsService) {
        this.entityManager = entityManager;
        this.courseDAO = courseDAO;
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
        // TODO: get owner
        /* 
        String ownerId = course.getOwnerId();
        if(ownerId == null)
            throw new BadInputException("Cannot create course: A course must have an owner.");
        if(!specialistsService.existsSpecialistById(ownerId))
            throw new BadInputException("Cannot create course: A course must have a valid owner.");

        // get owner's institution id
        String institutionId = null;
        try {
            var inst = institutionsService.getSpecialistInstitution(ownerId);
            if(inst != null)
                institutionId = inst.getName();
        }catch (NotFoundException ignored){}
        course.setInstituitionId(institutionId);
        */

        // persists the course in sql database
        // TODO: get institution
        Institution inst = new Institution();//institutionId != null ? entityManager.getReference(Institution.class, institutionId) : null;
        // TODO: get specialist
        Specialist spec = new Specialist("name", inst);//entityManager.getReference(Specialist.class, ownerId);
        Course newCourse = new Course(null, name, course.getDescription(), null, inst);
        courseDAO.save(newCourse);

        return course.getId();
    }

    /**
     * Retrieves course using its identifier
     * @param courseId course identifier
     * @return course that has the given identifier
     * @throws NotFoundException if the course does not exist
     */
    public Course getCourseById(String courseId) throws NotFoundException {
        Course c = courseDAO.findById(courseId).orElse(null);
        if(c == null)
            throw new NotFoundException("Could not get course: there is no course with the given identifier.");
        return c;
    }

    @Override
    public boolean existsCourseById(String courseId) {
        return courseDAO.existsById(courseId);
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
        if(!courseDAO.existsById(courseId))
            throw new NotFoundException("Could not add specialists: course not found.");
        for(String specialistId : specialistsIds){
            if(specialistsService.existsSpecialistById(specialistId))
                courseDAO.addSpecialistToCourse(specialistId, courseId);
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
        if(!courseDAO.existsById(courseId))
            throw new NotFoundException("Could not add students: course not found.");
        for(String studentId : studentsIds){
            if(studentsService.existsStudentById(studentId))
                courseDAO.addStudentToCourse(studentId, courseId);
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
        if(!courseDAO.existsById(courseId))
            throw new NotFoundException("Could not remove specialists: course not found.");
        for(String specialistId : specialistsIds){
            if(specialistsService.existsSpecialistById(specialistId))
                courseDAO.removeSpecialistFromCourse(specialistId, courseId);
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
        if(!courseDAO.existsById(courseId))
            throw new NotFoundException("Could not remove students: course not found.");
        for(String studentId : studentsIds){
            if(studentsService.existsStudentById(studentId))
                courseDAO.removeStudentFromCourse(studentId, courseId);
        }
    }

    /**
     * Update course basic information. Owner cannot be updated here.
     *
     * @param course basic course information
     */
    @Override
    @Transactional
    public void updateCourse(Course course) throws BadInputException, NotFoundException {
        if(course == null)
            throw new BadInputException("Cannot update course: course is null.");

        if(course.getId() == null)
            throw new BadInputException("Cannot update course: identifier of the course is null");

        Course origCourse = courseDAO.findById(course.getId()).orElse(null);
        if(origCourse == null)
            throw new NotFoundException("Cannot update course: course does not exist.");

        // check name
        String name = course.getName();
        if(name == null || name.isEmpty())
            throw new BadInputException("Cannot update course: A name of a course cannot be empty or null.");
        boolean nameUpdated = origCourse.getName().equals(name);

        // update course fields
        origCourse.setName(course.getName());
        origCourse.setDescription(course.getDescription());

        // update the course in nosql database
        origCourse = courseDAO.save(origCourse);

        // Update sql information
        if(nameUpdated) {
            var courseSQL = courseDAO.findById(origCourse.getId()).orElse(null);
            assert courseSQL != null;
            courseSQL.setName(origCourse.getName());
            courseDAO.save(courseSQL);
        }
    }

    /**
     * Get list of students that are associated with a specific course.
     *
     * @param courseId     course identifier
     * @param page         index of the page
     * @param itemsPerPage number of items each page has
     * @return list of students that are associated with a specific course.
     * @throws NotFoundException if the course does not exist
     */
    @Override
    public List<Student> getCourseStudents(String courseId, int page, int itemsPerPage) throws NotFoundException {
        if(!courseDAO.existsById(courseId))
            throw new NotFoundException("Could not get course students: the course does not exist.");
        // TODO: maybe retornar página diretamente
        return pageToListStudents(courseDAO.getCourseStudents(courseId, PageRequest.of(page, itemsPerPage)));
    }

    /**
     * Get list of specialists that are associated with a specific course.
     *
     * @param courseId     course identifier
     * @param page         index of the page
     * @param itemsPerPage number of items each page has
     * @return list of specialists that are associated with a specific course.
     * @throws NotFoundException if the course does not exist
     */
    @Override
    public List<Specialist> getCourseSpecialists(String courseId, int page, int itemsPerPage) throws NotFoundException {
        if(!courseDAO.existsById(courseId))
            throw new NotFoundException("Could not get course specialists: the course does not exist.");
        // TODO: maybe retornar página diretamente
        return pageToListSpecialists(courseDAO.getCourseSpecialists(courseId, PageRequest.of(page, itemsPerPage)));
    }


    @Override
    public boolean checkSpecialistInCourse(String courseId, String specialistId) throws NotFoundException {
        if(!courseDAO.existsById(courseId))
            throw new NotFoundException("Could not get course specialists: the course does not exist.");
        return courseDAO.isCourseSpecialist(courseId, specialistId);
    }

    /**
     * Check if studentId belongs to a course
     *
     * @param courseId  course identifier
     * @param studentId student identifier
     * @return 'true' or 'false' depending on whether the student belongs to a course or not
     */
    @Override
    public boolean checkStudentInCourse(String courseId, String studentId) throws NotFoundException {
        if(!courseDAO.existsById(courseId))
            throw new NotFoundException("Could not get course students: the course does not exist.");
        return courseDAO.isCourseStudent(courseId, studentId);
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
    public List<Course> getStudentCourses(String studentId, int page, int itemsPerPage) throws NotFoundException {
        if(!studentsService.existsStudentById(studentId))
            throw new NotFoundException("Could not get student courses: student does not exist.");
        // TODO: maybe retornar página diretamente
        return pageToListCourses(courseDAO.getStudentCourses(studentId, PageRequest.of(page, itemsPerPage)));
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
    public List<Course> getSpecialistCourses(String specialistId, int page, int itemsPerPage) throws NotFoundException {
        if(!specialistsService.existsSpecialistById(specialistId))
            throw new NotFoundException("Could not get specialist courses: specialist does not exist.");
        // TODO: maybe retornar página diretamente
        return pageToListCourses(courseDAO.getSpecialistCourses(specialistId, PageRequest.of(page, itemsPerPage)));
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
    public List<Course> getInstitutionCourses(String institutionId, int page, int itemsPerPage) throws NotFoundException {
        if(!institutionsService.existsInstitutionById(institutionId))
            throw new NotFoundException("Could not get institution courses: institution does not exist.");
        // TODO: maybe retornar página diretamente
        return pageToListCourses(courseDAO.getStudentCourses(institutionId, PageRequest.of(page, itemsPerPage)));
    }

    /* ***  Auxiliary Methods  *** */
    private List<Student> pageToListStudents(Page<Student> studentPage) throws NotFoundException {
        List<Student> studentList = new ArrayList<>();
        for(Student student : studentPage)
            ;
            // TODO: change this
            //studentList.add(studentsService.getStudentById(student.getId()));
        return studentList;
    }

    private List<Specialist> pageToListSpecialists(Page<Specialist> specialistPage) throws NotFoundException {
        List<Specialist> specialistList = new ArrayList<>();
        for(Specialist specialist : specialistPage)
            ;
            // TODO: change this
            //specialistList.add(specialistsService.getSpecialistById(specialist.getId()));
        return specialistList;
    }

    private List<Course> pageToListCourses(Page<Course> coursePage) throws NotFoundException {
        List<Course> courseList = new ArrayList<>();
        for(Course course : coursePage)
            ;
            // TODO: change this
            //courseList.add(this.getCourseById(course.getId()));
        return courseList;
    }
}