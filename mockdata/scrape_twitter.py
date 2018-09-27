import json
from pprint import pprint
import os
from twitter_api_creds import get_api


def scrape(tweet_ids):
    api = get_api()
    statuses = api.GetStatuses(tweet_ids)
    return statuses


def transform(status):
    return {
        "id": status.id_str,
        "username": status.user.screen_name,
        "name": status.user.name,
        "likes": status.favorite_count,
        "content": status.full_text,
        "created_at_epoch": status.created_at_in_seconds,
        "location": {
            "coordinates": [],
            "type": "Point"
        },
        "location_name": None,
    }



if __name__ == '__main__':
    DO_OVERRIDE = True
    from tweets import tweets_to_scrape
    for domain, to_scrape_list in tweets_to_scrape.items():
        statuses = scrape(
            list(map(lambda x:x.tweet_id, to_scrape_list))
        )
        transformed = list(map(transform, statuses))
        for x,status in zip(to_scrape_list, transformed):
            status["location"] = x.location
            status["location_name"] = x.location_name

        as_str = json.dumps(transformed, sort_keys=True, indent=4)
        filepath = os.path.join("new_samples", "{}.json".format(domain))
        if not DO_OVERRIDE and os.path.exists(filepath):
            raise Exception("File: {} exists.".format(filepath))
        else:
            with open(filepath, "w") as f:
                f.write(as_str)


