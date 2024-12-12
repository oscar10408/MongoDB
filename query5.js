// Query 5
// Find the oldest friend for each user who has a friend. For simplicity,
// use only year of birth to determine age, if there is a tie, use the
// one with smallest user_id. You may find query 2 and query 3 helpful.
// You can create selections if you want. Do not modify users collection.
// Return a javascript object : key is the user_id and the value is the oldest_friend id.
// You should return something like this (order does not matter):
// {user1:userx1, user2:userx2, user3:userx3,...}

function oldest_friend(dbname) {
    db = db.getSiblingDB(dbname);

    let results = {};
    // TODO: implement oldest friends
    // Use the query 2
    db.users.aggregate([
        { 
            $unwind: "$friends" 
        },
        {
            $project: { 
                user_id: 1, 
                friends: 1,
                _id: 0
            }
        },
        { 
            $out: "flat_users"
        }
    ]);

    // Union the friends with flipped friends to create a full list
    let original = db.flat_users.find({}, { user_id: 1, friends: 1 }).toArray();

    let flipped = db.flat_users.find({}, { friends: 1, user_id: 1 }).toArray().map(doc => {
        return { f1: doc.friends, f2: doc.user_id }; // Change field names accordingly
    });

    let union_friends = original.map(doc => {
        return { f1: doc.user_id, f2: doc.friends }; // Change field names accordingly
    }).concat(flipped);

    let friendsMap = {};

    // Step 1: Create a mapping of user_id to their friends
    union_friends.forEach(pair => {      
        // Initialize the user's friends list if it doesn't exist
        if (!friendsMap[pair.f1]) {
            friendsMap[pair.f1] = [];
        }
        friendsMap[pair.f1].push(pair.f2); // Add friend to the user
    });

    // Loop through each user and find the oldest friend
    Object.keys(friendsMap).forEach(user_id => {
        friends = friendsMap[user_id];
        let oldest_friend_id = null; // user.friends[0];
        let oldest_friend_YOB = Infinity; // db.users.find({user_id: oldest_friend_id}).next().YOB;
        friends.forEach(friend => {
            let friend_YOB = db.users.find({user_id: friend}).next().YOB;
            if (friend_YOB < oldest_friend_YOB || (friend_YOB === oldest_friend_YOB && friend < oldest_friend_id)) {
                oldest_friend_YOB = friend_YOB;
                oldest_friend_id = friend;
            }
        });
        results[user_id] = oldest_friend_id;
    });

    return results;
}
