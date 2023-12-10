print("START")


db = db.getSiblingDB('product-service')

db.createUser(
    {
     user: "rootadmin",
     password: "M4hy4r_1",
     roles: [{role:'readWrite', db: 'product-service'}]
    }
);

db.createCollection("user");

print("END")
