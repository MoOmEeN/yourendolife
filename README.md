# yourendolife
This the research project wich goal was to try [Vaadin](http://vaadin.com).
The idea was to create a web application with minimal possible use of regular front end technologies.
In the end, the app does not contain a single line of HTML, does contain two javascript files (because I used one third party library - not required at all, but use of raw JavaScript code in Vaadin app is really easy) and one SCSS file.

The application itself connects to [Endomondo](http://endomondo.com) services using [Endo2Java](https://github.com/MoOmEeN/endo2java) to retrieve workouts information and does some processing using the data. Unless the domain has expired, application is available here: [yourendolife.com](https://yourendolife.com).

The application uses [Spring Boot](http://projects.spring.io/spring-boot/). It integrates with Vaadin using [Vaadin4Spring](https://github.com/peholmst/vaadin4spring) (at the time I was starting this project, there was no official Vaadin integration with Spring, now there is [one](https://github.com/vaadin/spring)).

Bunch of Vaadin add-ons have been used:
* [PagedTable](https://vaadin.com/directory#!addon/pagedtable) - pagination features for built-in tables
* [GoogleMaps Add-On](https://vaadin.com/directory#!addon/googlemaps-add-on) - integration with Google Maps with no javascript
* [LazyLoadWrapper](https://vaadin.com/directory#!addon/lazyloadwrapper) - to show nice spinner on pages that take more time to load
* [JFreeChartWrapper](https://vaadin.com/directory#!addon/jfreechart-wrapper-for-vaadin) - wrapper for easy use of 
[JFreeChart](http://www.jfree.org/jfreechart/) charts
* [TableExport](https://vaadin.com/directory#!addon/tableexport) - export tables to Excel
* [LoginForm](https://vaadin.com/directory#!addon/loginform) - auto-complete support in login form

App is doing reverse geolocation using [MapQuest Nominatim API](http://open.mapquestapi.com/nominatim/). To improve its performance, results are being cached in Mongo using [geospatial indexes](http://docs.mongodb.org/manual/core/geospatial-indexes/).

## Some configuration tips
### mongo env variable
```
export MONGO_URL="mongodb://localhost:27017/yourendolife"
```
### setting up 2dsphere index
```
radek@radek:~/repos/yourendolife$ mongo
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
