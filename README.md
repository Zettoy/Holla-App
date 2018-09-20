client side of Holla app - android application

# API Reference

## POST CREATION
POST /posts/create

### body expects json :

	{ 
	"location" : {
		"type" : "Point",
		"coordinates":[-33.912383,151.223480]
	  },
	  "location_name": "UNSW roundhouse" ,
	  "content" : "Get ahead of the Weekly Trivia game at Unibar ❓Don't water-fail this week 🌊t"
	}
required : all but location_name
### body expect cookies : (cookies should identify user etc. google oauth, facebook login)

## GET LOCATION SEARCH
GET /posts/search/location
### body expects json : (currently no parameters such as location taken, just outputs list of posts)

sample response : 

    [{
        "id": "5b9e1123da2d4e1a90b8b669",
        "content": "Who will be crowned The Big Cheese?🍔 Earn bragging rights and be the next BNOC by winning The Big Cheese | Burger Eating Contest (https://www.eventbrite.com.au/e/the-big-cheese-competitor-registration-tickets-49161660851). Sign up now! Competitors will pick up a sweet limited edition Big Cheese t-shirt for competing 👕",
        "author": "UNSWroundhouse",
        "date": "2018-09-16T08:15:31.626Z",
        "location": {
            "coordinates": [
                -33.912383,
                151.22348
            ],
            "type": "Point"
        },
        "location_name": "UNSW Roundhouse"
    }]
   
