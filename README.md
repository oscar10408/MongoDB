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


