import twitter
def get_api():
    api = twitter.Api(consumer_key="Dh5P9wutT66HRrnpj6AVOTaHH",
                      consumer_secret="IwssiIJQMc4L3oF0I8zXvGZO875DAif0FBu8uobqrCtYDs28mj",
                      access_token_key="827874279375015937-N407Oq785juQ3dn5WjUPo9MSd9b5x1W",
                      access_token_secret="laqDpjuIHo2AhNJ4Jl49lcSbjiqySnrr8eENQ4a6M4u9J",
                      tweet_mode="extended"
                      )
    return api


