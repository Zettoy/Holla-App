from twitter_api_creds import get_api


def scrape(tweet_ids):
    api = get_api()
    statuses = api.GetStatuses(tweet_ids)
    return statuses
def transform(status):
    return {
        "id" : status.id_str,
        "username": status.user.screen_name,
        "name": status.user.name,
        "likes" : status.favorite_count,
        "content" : status.text,
        "created_at_epoch" : status.created_at_in_seconds,
        "location" : {
            "coordinates":[],
            "type" : "Point"
        },
        "location_name" : None,
    }
if __name__ == '__main__':
    statuses = scrape([1020082359503212545,
            1019472406388150273
            ])
    transformed = list(map(transform, statuses))
    print("")
