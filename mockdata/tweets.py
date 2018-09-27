class ToScrape:
    def __init__(self, tweet_id, loc, loc_name=None):
        self.tweet_id = tweet_id
        self.location = loc
        self.location_name = loc_name


def Location(lat, long):
    return {
        "coordinates": [lat, long],
        "type": "Point"
    }


tweets_to_scrape = {
    "csesoc": [
        ToScrape(1043790559129743360, Location(-33.918839, 151.231189), "UNSW CSE"),
        ToScrape(1038722009373372428, Location(-33.918839, 151.231189), "UNSW CSE"),
    ],
    "unsw": [
        ToScrape(1020082359503212545, Location(-33.917188, 151.231277), "UNSW Quadrangle" ),
        ToScrape(1039409059332411392, Location(-33.917192, 151.233487), "UNSW Library"),
        ToScrape(992238394364186624,Location(-33.916862, 151.227716), "UNSW Law"),
    ],
    "kingsford_food": [
        ToScrape(1039544144652382216, Location(-33.919801, 151.227446), "McDonald's Kingsford"),

    ]
}
