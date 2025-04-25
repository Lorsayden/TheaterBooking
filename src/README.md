Task Overview
Task 1: Setup

    Java + Maven project using Javalin and Hibernate

    README.md created to document the project

Task 2: JPA and DAOs

    SkiLesson and Instructor entities implemented using JPA

    SkiLessonDAO and InstructorDAO created

    DTOs used for data transfer

    Populator class adds initial data to the database

Task 3: REST API with Javalin

    Routes:

        GET /skilessons

        GET /skilessons/{id}

        POST /skilessons

        PUT /skilessons/{id}

        DELETE /skilessons/{id}

        PUT /skilessons/{lessonId}/instructor/{instructorId}

        POST /skilessons/populate
        This endpoint is only for testing/demo purposes and should be removed or 
        protected in production.

Task 3: Responses

    GET http://localhost:7070/api/skilessons/1


Task 3: Theoretical question

    Why PUT for assigning instructors?
    The differences between PUT and POST is as following:
    
    POST:
    Use when you're creating a new thing (like a new lesson or user).
    If you send it twice, you’ll probably get two new things.
    
    PUT:
    Use when you're changing an existing thing.
    If you send the same PUT request twice, nothing changes after the first one.
    
    We are not interested in POST in this example, because it would create duplicate 
    lessons, instead of updating an already existing lesson


Task 4: REST Error Handling

    Custom JSON errors returned:

        404 for missing ski lesson

        400/404 for delete on non-existent lesson

Task 5: Streams and Queries

    GET /skilessons/level/{level} – filters lessons by level

    GET /instructors/totalprice – returns total price per instructor

    [
      { "instructorId": 1, "totalPrice": 1050 },
      { "instructorId": 2, "totalPrice": 800 }
    ]

Task 6: External API Data

    Consumed external API for instruction data:
    https://apiprovider.cphbusinessapps.dk/skilesson/{level}

    Instructions added when calling GET /skilessons/{id}

    Added endpoint:

        GET /instructors/{id}/instruction-duration

        Returns: { "totalInstructionMinutes": 90 }

Task 7: Testing REST Endpoints

    REST Assured used

    @BeforeAll starts Javalin and DB

    @BeforeEach populates DB

    Tests cover all endpoints except instruction-duration due to API timing/data mismatch