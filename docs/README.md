# Theater Booking System - Backend

This is the backend of a Theater Booking System that handles performances and actors using Javalin, JPA, and Hibernate for database persistence. The system supports CRUD operations for performances, actors, and allows fetching props based on the genre of performances.

__Requirements__\
Java 17 or above.\
Javalin for building the REST API.\
JPA / Hibernate for database interaction.\
PostgreSQL for database storage.


API Endpoints
1. Get all performances\
   GET /api/performances\
   Description: Returns a list of all performances.
   Allows `actorId` to be `null`. Does not return actor info
```[
  {
    "id": 1,
    "startTime": "2025-05-10T20:00:00",
    "endTime": "2025-05-10T22:30:00",
    "title": "Phantom of the Theater (Updated)",
    "ticketPrice": 250.0,
    "latitude": 55.7,
    "longitude": 12.6,
    "genre": "MUSICAL",
    "actorId": 4
  },
  {
    "id": 2,
    ...
    ...
```

2. Get a performance by ID\
   GET /api/performances/{id}\
   Description: Returns details of a performance by its ID. Also 
   Gives actor information. If no actor is attached, will simply have `actorId` be `null` and not include info.
   I did this through constructoer overloading in `PerformanceDTO`

```
{
  "id": 2,
  "startTime": "2025-04-27T13:05:37.680721",
  "endTime": "2025-04-28T13:05:37.680721",
  "title": "Dramatic Night",
  "ticketPrice": 110.0,
  "latitude": 56.0,
  "longitude": 13.0,
  "genre": "DRAMA",
  "actorId": 2,
  "actor": {
    "id": 2,
    "firstName": "Bob",
    "lastName": "Johnson",
    "email": "bob@example.com",
    "phone": 87654321,
    "yearsOfExperience": 10
  }
}
```

3. Create a new performance\
   POST /api/performances\
   Describtion: Creates a new performance... that about it. Does not set the `actorId`

```
{
"id": 11,
"startTime": "2025-05-10T19:00:00",
"endTime": "2025-05-10T21:00:00",
"title": "Phantom of the Theater",
"ticketPrice": 220.0,
"latitude": 55.7,
"longitude": 12.6,
"genre": "MUSICAL",
"actorId": null
}
```

4. Update a performance\
   PUT /api/performances/{id}\
   Description: Updates but keeps `actorId` the same

```
HTTP/1.1 200 OK
Date: Fri, 25 Apr 2025 11:45:01 GMT
Content-Type: text/plain
Content-Length: 0

<Response body is empty>
```


5. Delete a performance\
   DELETE /api/performances/{id}\
   Description: Deletes a performance by ID.
```
HTTP/1.1 204 No Content
Date: Fri, 25 Apr 2025 11:48:28 GMT
Content-Type: text/plain

<Response body is empty>
```


6. Add an actor to a performance
   PUT /api/performances/{performance_id}/actors/{actor_id}
```
HTTP/1.1 204 No Content
Date: Fri, 25 Apr 2025 11:49:29 GMT
Content-Type: text/plain

<Response body is empty>
```


7. Get performances by actor\
   GET /api/performances/actor/{actor_id}\
   Description: Get a list of all performances by a specific actor.

```
[
    {
        "id": 3,
        "startTime": "2025-04-28T13:05:37.681767",
        "endTime": "2025-04-29T13:05:37.681767",
        "title": "Musical Mayhem",
        "ticketPrice": 120.0,
        "latitude": 57.0,
        "longitude": 14.0,
        "genre": "MUSICAL",
        "actorId": 3
    },
    {
        "id": 1,
        "startTime": "2025-05-10T20:00:00",
        "endTime": "
        ...
        ...
```


8. Filter performances by genre\
   GET /api/performances/genre/{genre}
```
   {
       "type": "warning",
       "message": "No performances found for genre: COMEDY",
       "status": 404,
       "path": "/api/performances/genre/comedy"
   }
```

9. Get total revenue per actor\
   GET /api/performances/actor-revenue

```
[
    {
        "actorId": 1,
        "totalRevenue": 250.0
    },
    {
        "actorId": 2,
        "totalRevenue": 0.0
    },
    ...
    ...
```

10. Populate the database
    POST /api/performances/populate
    Description: Will wipe the database and repopulate it with sample data.

11. Get props for a perfprmance
    GET /api/performances/{id}/props

```
[
    {
        "name": "Feather Boa",
        "description": "Colorful prop often used in musical dance numbers.",
        "genre": "musical",
        "costEstimate": 45,
        "createdAt": "2024-11-30T17:44:58.547Z",
        "updatedAt": "2024-10-30T17:44:58.547Z"
    },
    {
        "name": "Tap Shoes",
        "description": "Essential footwear for musical tap routines.",
        ...
        ...
```
12. Get the total cost estimate of props for a performance\
    GET /api/performances/{id}/props/total-cost

```
{
    "totalPropCost": 250,
    "performance_id": 1
}
```

13. Get props for a performance by ID with additional performance details\
    GET /api/performances/{id}/with-props\
    Description: 
```
{
  "performance": {
    "id": 1,
    "startTime": "2025-05-10T20:00:00",
    "endTime": "2025-05-10T22:30:00",
    "title": "Phantom of the Theater (Updated)",
    "ticketPrice": 250.0,
    "latitude": 55.7,
    "longitude": 12.6,
    "genre": "MUSICAL",
    "actorId": 3,
    "actor": {
      "id": 3,
      "firstName": "Clara",
      "lastName": "Andersen",
      "email": "clara@example.com",
      "phone": 44442222,
      "yearsOfExperience": 3
    }
  },
  "props": [
    {
      "name": "Feather Boa",
      "description": "Colorful prop often used in musical dance numbers.",
      "genre": "musical",
      "costEstimate": 45,
      "createdAt": "2024-11-30T17:44:58.547Z",
      "updatedAt": "2024-10-30T17:44:58.547Z"
    },
    ...
    ...
    ...
```

## Theoretical questions
1. Checked exceptions are exceptions that are checked at compiletime. You can handle these exceptions with try/catch for example
2. Queries are for intereacting with the DB, while steams are for actions on data that is already in hand

3. PUT will have the same effect no matter how many times you call it with the same data. it doesnt create new objects (used to update data)
   PATCH will update only the provided fields, but also not create new objects

{
"type": "error",
"message": "Performance with id 9 not found",
"status": 404,
"path": "/api/performances/9"
}