// Query 6
// Find the average friend count per user.
// Return a decimal value as the average user friend count of all users in the users collection.

function find_average_friendcount(dbname) {
    db = db.getSiblingDB(dbname);

    // TODO: calculate the average friend count
    let total_friend_count = 0;
    let user_count = 0;
    db.users.find().forEach(user => {
        let friend_count = user.friends.length;
        total_friend_count += friend_count;
        user_count++;
    });

    return total_friend_count / user_count;
}
