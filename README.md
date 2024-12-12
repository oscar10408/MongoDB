# Project 3: MongoDB

## Introduction

In Project 3, I worked with MongoDB, a NoSQL database system, to explore its capabilities using a dataset similar to the one from Project 2. The project is divided into two parts:

- **Part A:** I extracted data from tables in the Fakebook database and exported a JSON file (`output.json`) containing information about users.
- **Part B:** I imported `output.json` (or a provided sample file) into MongoDB and created a collection of users. I then wrote seven queries to interact with the MongoDB collection.

# Part A: Export Oracle Database to JSON

## Introduction to JSON

JSON (JavaScript Object Notation) is a lightweight data-interchange format that represents data as key-value pairs, similar to a `std::map` in C++. However, JSON values can have varying data types within the same object. Below is an example of how JSON objects can be initialized and accessed in JavaScript:

```javascript
var student1 = { "Name" : "John Doe", "Age" : 21, "Major" : ["CS", "Math"] }
student1["Name"]; // returns "John Doe"
```
You can also create JSON arrays, which are collections of JSON objects:
```javascript
var students = [
  { "Name" : "John Doe", "Age" : 21, "Major" : ["CS", "Math"] },
  { "Name" : "Richard Roe", "Age" : 22, "Major" : ["CS"] },
  { "Name" : "Joe Public", "Age" : 21, "Major" : ["CE"] }
];
students[0]["Name"]; // returns "John Doe"
```

## Export to JSON

In **Part A** of Project 3, the task is to query the **Project 3 Fakebook Oracle database** to export comprehensive information about each user into a **JSON array**. The dataset will contain **800 JSON objects**, each representing a user, and the output should be stored in a `JSONArray`. Each object will include detailed user information such as personal details, friends, current location, and hometown.

## Data Fields

Each JSON object representing a user should contain the following fields:

- **user_id**: (int) The unique identifier for the user.
- **first_name**: (string) The first name of the user.
- **last_name**: (string) The last name of the user.
- **gender**: (string) The gender of the user.
- **YOB**: (int) The year of birth of the user.
- **MOB**: (int) The month of birth of the user.
- **DOB**: (int) The day of birth of the user.
- **friends**: (JSONArray) A list containing the user IDs of the user's friends who have a larger user ID than the current user. The friends' relationships are assumed to be symmetric, so if user 700 is friends with user 25, it will only appear in the list for user 25 and not for user 700.
- **current**: (JSONObject) Contains information about the user's current location:
  - **city**: (string) The current city of the user.
  - **state**: (string) The current state of the user.
  - **country**: (string) The current country of the user.
- **hometown**: (JSONObject) Contains information about the user's hometown:
  - **city**: (string) The hometown city of the user.
  - **state**: (string) The hometown state of the user.
  - **country**: (string) The hometown country of the user.

### Special Considerations:
- If a user has no friends, the `friends` key should contain an empty `JSONArray` (`[]`).
- If the user has no `current` or `hometown` information, those keys should contain empty JSON objects (`{}`).

## Example JSON Output

Below is an example of one user element in the resulting JSON array:
```javascript
 {
 "MOB": 10,
 "hometown": {
 "country": "Middle Earth",
 "city": "Linhir",
 "state": "Gondor"
 },
 "current": {
 "country": "Middle Earth",
 "city": "Caras Galadhon",
 "state": "Lothlorien"
 },
 "gender": "female",
 "user_id": 744,
 "DOB": 14,
 "last_name": "MARTINEZ",
 "first_name": "Lily",
 "YOB": 516,
 "friends": [754, 760, 772, 782]
 }
```
