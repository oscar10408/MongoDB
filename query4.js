// Query 4
// Find user pairs (A,B) that meet the following constraints:
// i) user A is male and user B is female
// ii) their Year_Of_Birth difference is less than year_diff
// iii) user A and B are not friends
// iv) user A and B are from the same hometown city
// The following is the schema for output pairs:
// [
//      [user_id1, user_id2],
//      [user_id1, user_id3],
//      [user_id4, user_id2],
//      ...
//  ]
// user_id is the field from the users collection. Do not use the _id field in users.
// Return an array of arrays.

function suggest_friends(year_diff, dbname) {
    db = db.getSiblingDB(dbname);

    let pairs = [];
    // TODO: implement suggest friends
    db.users.find().forEach(user_A => {
        db.users.find().forEach(user_B => {
            if (user_A.user_id !== user_B.user_id) {
                if (
                    user_A.gender == "male" &&
                    user_B.gender == "female" &&
                    Math.abs(user_A.YOB - user_B.YOB) < year_diff &&
                    user_A.friends.indexOf(user_B.user_id) == -1 && // user_B is not a friend of user_A
                    user_B.friends.indexOf(user_A.user_id) == -1 && // user_A is not a friend of user_B
                    user_A.hometown.city == user_B.hometown.city
                ) {
                    pairs.push([user_A.user_id, user_B.user_id]);
                }
            }
        });
    });

    return pairs;
}
