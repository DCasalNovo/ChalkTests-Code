
package pt.uminho.di.chalktyk.apis;

import pt.uminho.di.chalktyk.apis.to_be_removed_models_folder.InlineResponse2001;
import pt.uminho.di.chalktyk.models.miscellaneous.Visibility;
import pt.uminho.di.chalktyk.models.tests.Test;
import pt.uminho.di.chalktyk.models.tests.TestResolution;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

@Validated
public interface TestsApi {

    @Operation(summary = "Retrieve tests.", description = "Retrieves tests that match the given filters.", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Successful retrieval.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Test.class)))),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized operation. Example: When trying to access tests from a specific course that the user does not belong to.") })
    @RequestMapping(value = "",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Test>> getTests(@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "page", required = true) Integer page
, @NotNull @Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema(allowableValues={ "1", "50" }, minimum="1", maximum="50"
)) @Valid @RequestParam(value = "itemsPerPage", required = true) Integer itemsPerPage
, @Parameter(in = ParameterIn.QUERY, description = "Array of identifiers from the tags that will be used to filter the tests." ,schema=@Schema( defaultValue="[]")) @Valid @RequestParam(value = "tags", required = false, defaultValue="[]") List<String> tags
, @Parameter(in = ParameterIn.QUERY, description = "Value that defines if the exercise must have all the given tags to be retrieved." ,schema=@Schema( defaultValue="false")) @Valid @RequestParam(value = "matchAllTags", required = false, defaultValue="false") Boolean matchAllTags
, @Parameter(in = ParameterIn.QUERY, description = "Describes the type of visibility that the tests must have.  This parameter must be paired with the parameter 'visibilityTarget'  when the value is either 'institution' or 'course'. " ,schema=@Schema(allowableValues={ "public", "institution", "course" }
, defaultValue="public")) @Valid @RequestParam(value = "visibilityType", required = false, defaultValue="public") String visibilityType
, @Parameter(in = ParameterIn.QUERY, description = "Identifier of the visibility target. For example, if visibilityType='institution',  then this parameter is the identifier of the institution. " ,schema=@Schema()) @Valid @RequestParam(value = "visibilityTarget", required = false) String visibilityTarget
, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "specialistId", required = false) String specialistId
, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "courseId", required = false) String courseId
, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "institutionId", required = false) String institutionId
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Create a test", description = "", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Success.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        
        @ApiResponse(responseCode = "400", description = "Bad input."),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized operation.") })
    @RequestMapping(value = "",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<String> createTest(@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "visibility", required = true) Visibility visibility
, @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Test body
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Get test resolution using its id.", description = "", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Successful retrieval.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TestResolution.class))),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized operation."),
        
        @ApiResponse(responseCode = "404", description = "Test resolution not found.") })
    @RequestMapping(value = "/resolutions/{resolutionId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<TestResolution> getTestResolutionById(@Parameter(in = ParameterIn.PATH, description = "Test resolution identifier", required=true, schema=@Schema()) @PathVariable("resolutionId") String resolutionId
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Delete test by its id.", description = "", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Test deleted successfully."),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized operation."),
        
        @ApiResponse(responseCode = "404", description = "Test not found.") })
    @RequestMapping(value = "/{testId}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteTestById(@Parameter(in = ParameterIn.PATH, description = "Test identifier", required=true, schema=@Schema()) @PathVariable("testId") String testId
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Duplicates the test using its identifier.", description = "", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Successful duplication.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized operation."),
        
        @ApiResponse(responseCode = "404", description = "Test with the given id does not exist.") })
    @RequestMapping(value = "/{testId}/duplicate",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<String> duplicateTestById(@Parameter(in = ParameterIn.PATH, description = "Test identifier", required=true, schema=@Schema()) @PathVariable("testId") String testId
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Update a test", description = "This method is used to update an existing test. Check the schema", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "204", description = "Test updated successfully."),
        
        @ApiResponse(responseCode = "400", description = "Bad input."),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized operation."),
        
        @ApiResponse(responseCode = "404", description = "Test not found.") })
    @RequestMapping(value = "/{testId}",
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> updateTest(@Parameter(in = ParameterIn.PATH, description = "Test identifier", required=true, schema=@Schema()) @PathVariable("testId") String testId
, @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Test body
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Issue the automatic correction of the test resolutions.", description = "", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Success."),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized operation."),
        
        @ApiResponse(responseCode = "404", description = "Test not found.") })
    @RequestMapping(value = "/{testId}/resolutions/correction",
        method = RequestMethod.PUT)
    ResponseEntity<Void> automaticCorrection(@Parameter(in = ParameterIn.PATH, description = "Test identifier", required=true, schema=@Schema()) @PathVariable("testId") String testId
, @Parameter(in = ParameterIn.QUERY, description = "Type of correction. The correction can either be automatic or done by AI. When using AI correction, the AI will only be used to correct questions that cannot be corrected automatically, i.e., by using the solution. " ,schema=@Schema(allowableValues={ "auto", "ai" }
)) @Valid @RequestParam(value = "correctionType", required = false) String correctionType
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Retrieves the number of students that submitted a resolution for a specific test.", description = "- Retrieves the number of students that submitted a resolution for a specific test.  The total number of submissions can be obtained by setting the 'total' query parameter to 'true'. ", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Successful retrieval.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
        
        @ApiResponse(responseCode = "404", description = "Test not found.") })
    @RequestMapping(value = "/{testId}/resolutions/count",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Integer> countStudentsTestResolutions(@Parameter(in = ParameterIn.PATH, description = "Test identifier", required=true, schema=@Schema()) @PathVariable("testId") String testId
, @Parameter(in = ParameterIn.QUERY, description = "'false' to count the number of students that made a submission. 'true' to count the total number of submissions." ,schema=@Schema( defaultValue="false")) @Valid @RequestParam(value = "total", required = false, defaultValue="false") Boolean total
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Get all test resolutions.", description = "", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Successful retrieval.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = InlineResponse2001.class)))),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized operation."),
        
        @ApiResponse(responseCode = "404", description = "Test not found.") })
    @RequestMapping(value = "/{testId}/resolutions",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse2001>> getTestResolutions(@Parameter(in = ParameterIn.PATH, description = "Test identifier", required=true, schema=@Schema()) @PathVariable("testId") String testId
, @NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "page", required = true) Integer page
, @NotNull @Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema(allowableValues={ "1", "50" }, minimum="1", maximum="50"
)) @Valid @RequestParam(value = "itemsPerPage", required = true) Integer itemsPerPage
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Create a test resolution", description = "", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Test resolution created successfully."),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized operation."),
        
        @ApiResponse(responseCode = "404", description = "Could not find any test with the given id.") })
    @RequestMapping(value = "/{testId}/resolutions",
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Void> createTestResolution(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("testId") String testId
, @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody TestResolution body
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Allows to check if the student can submit a resolution for the test.", description = "", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Successful retrieval.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
        
        @ApiResponse(responseCode = "404", description = "Test not found.") })
    @RequestMapping(value = "/{testId}/resolutions/{studentId}/can-submit",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Boolean> canStudentSubmitResolution(@Parameter(in = ParameterIn.PATH, description = "Test identifier", required=true, schema=@Schema()) @PathVariable("testId") String testId
, @Parameter(in = ParameterIn.PATH, description = "student identifier", required=true, schema=@Schema()) @PathVariable("studentId") String studentId
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Retrieves the number of (resolution) submissions a student has made for a specific test.", description = "", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Successful retrieval.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
        
        @ApiResponse(responseCode = "404", description = "Test not found.") })
    @RequestMapping(value = "/{testId}/resolutions/{studentId}/count",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Integer> countStudentSubmissionsForTest(@Parameter(in = ParameterIn.PATH, description = "Test identifier", required=true, schema=@Schema()) @PathVariable("testId") String testId
, @Parameter(in = ParameterIn.PATH, description = "student identifier", required=true, schema=@Schema()) @PathVariable("studentId") String studentId
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Get the list of identifiers of the student's resolutions for the given test.", description = "", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Success.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized operation.") })
    @RequestMapping(value = "/{testId}/resolutions/{studentId}/ids",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<String>> getStudentTestResolutionsIds(@Parameter(in = ParameterIn.PATH, description = "Test identifier", required=true, schema=@Schema()) @PathVariable("testId") String testId
, @Parameter(in = ParameterIn.PATH, description = "student identifier", required=true, schema=@Schema()) @PathVariable("studentId") String studentId
, @RequestHeader("chalkauthtoken") String jwt);


    @Operation(summary = "Get latest test resolution made by the student.", description = "", tags={ "tests" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Success.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TestResolution.class))),
        
        @ApiResponse(responseCode = "401", description = "Unauthorized operation."),
        
        @ApiResponse(responseCode = "404", description = "Not found.") })
    @RequestMapping(value = "/{testId}/resolutions/{studentId}/last",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<TestResolution> getStudentLastResolution(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("testId") String testId
, @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("studentId") String studentId
, @RequestHeader("chalkauthtoken") String jwt);

}
