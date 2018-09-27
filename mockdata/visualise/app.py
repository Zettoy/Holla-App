from flask import Flask
import random
import flask
import json
import copy
app = Flask(__name__)

@app.route('/')
def hello_world():
    maps_api_key="AIzaSyD3jqGxbp61qda8y7j2S9FlkhziJpShfuI"
    return flask.render_template("map.html", MAPS_API_KEY=maps_api_key)


def get_delta(base):
    magnitude = 2
    return base * random.uniform(-magnitude,magnitude)
@app.route('/posts')
def posts_json():
    with open("static/unsw_6.json", encoding="utf8") as fd:
        obj = json.load(fd)

    posts = obj['posts']
    new_posts = []
    delta_base = 0.0001
    for p in posts:
        new_p = copy.deepcopy(p)
        new_p["coordinates"]["longitude"] = new_p["coordinates"]["longitude"] + get_delta(delta_base)
        new_p["coordinates"]["latitude"] = new_p["coordinates"]["latitude"] + get_delta(delta_base)
        new_posts.append(new_p)
    return flask.jsonify({"posts" : new_posts})


if __name__ == '__main__':
    app.run(debug=True)