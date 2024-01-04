package pt.uminho.di.chalktyk.services;

import pt.uminho.di.chalktyk.models.miscellaneous.Visibility;
import pt.uminho.di.chalktyk.services.exceptions.NotFoundException;

public interface IExercisesTestsAuthorization {
    boolean canStudentGetExercise(String studentId, Visibility vis, String courseId, String institutionId);
    boolean canStudentGetExercise(String studentId, String exerciseId) throws NotFoundException;

    boolean canStudentAccessExerciseResolution(String studentId, String resolutionId) throws NotFoundException;


    boolean canSpecialistGetExercise(String specialistId, String ownerId, Visibility vis, String courseId, String institutionId);
    boolean canSpecialistGetExercise(String specialistId, String exerciseId) throws NotFoundException;

    boolean canSpecialistAccessExercise(String specialistId, String ownerId, Visibility vis, String courseId, String institutionId);
    boolean canSpecialistAccessExercise(String specialistId, String exerciseId) throws NotFoundException;

    boolean canSpecialistAccessExerciseResolution(String specialistId, String resolutionId) throws NotFoundException;
}