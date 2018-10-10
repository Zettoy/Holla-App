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

## POST LOCATION SEARCH
POST /posts/search/location
### body expects json :
	{
	  "location" : {
		"type" : "Point",
		"coordinates":[-33.912383,151.223480]
	  }
	}

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
        "location_name": "UNSW Roundhouse",
	"distance": 500
    }]
## POST SEARCH BY POST ID
POST /posts/search/postid

### body expects json :
	{ 
  	"id" : "5bba12f6053a101f009c7c11"
	}
sample response:
[

    {
        "id": "5bbc9f31c97bed00134516bf",
        "content": "test my by",
        "author": "UNSWroundhouse",
        "date": "2018-10-09T12:29:37.209Z",
        "location": {
            "coordinates": [
                -33.859998,
                151.1999827
            ],
            "type": "Point"
        },
        "location_name": ""
    }
]
	
## POST SEARCH BY USER ID
POST /posts/search/userid

### body expects json :
	{ 
  	"id" : "5b92a535e44fa8130cb5cf4e"
	}
sample response (distance is not given when searching by id):
[

    {
        "id": "5bbc9f31c97bed00134516bf",
        "content": "test my by",
        "author": "UNSWroundhouse",
        "date": "2018-10-09T12:29:37.209Z",
        "location": {
            "coordinates": [
                -33.859998,
                151.1999827
            ],
            "type": "Point"
        },
        "location_name": ""
    }
]
## COMMENT CREATION
POST /comments/create

### body expects json :
	{ 
  	"post" : "5bba12f6053a101f009c7c11",
  	"content" : "Trivia sux !!!"
	}

## COMMENT SEARCH BY POST
POST /comments/search/post
### body expects json :
	{
	"post" : "5bba12f6053a101f009c7c11"
	}

sample response : 

	[
	    {
		"id": "5bbb1bb8cd4cd02d68930614",
		"content": "Get ahead of the Weekly Trivia game at Unibar ❓Don't water-fail this week 🌊t",
		"author": "UNSWroundhouse",
		"date": "2018-10-08T08:56:24.446Z",
		"post": "5bba12f6053a101f009c7c11"
	    }
	]
   
