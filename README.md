# yourendolife

##Mongo
###url configuration
```
export MONGO_URL="mongodb://localhost:27017/yourendolife"
```
###2dsphere index
```
radek@radek  ~/repos/yourendolife   master ●  mongo
MongoDB shell version: 2.6.9
connecting to: test
> use yourendolife
switched to db yourendolife
> db.places.ensureIndex({point:"2dsphere"});
{
        "createdCollectionAutomatically" : false,
        "numIndexesBefore" : 1,
        "numIndexesAfter" : 2,
        "ok" : 1
}
> quit()
```
