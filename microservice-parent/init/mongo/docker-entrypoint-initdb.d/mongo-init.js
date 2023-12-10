print('START')

db = db.getSiblingDB('product-service');

db.createUser(
    {
        user: 'rootadmin',
        password: 'lunamwah',
        roles: [{role: 'readWrite', db: 'product-service'}]
    }

)

db.createCollection('user');


print('END')