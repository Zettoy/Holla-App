from flask import Flask
import flask
import json
app = Flask(__name__)

@app.route('/')
def hello_world():
    maps_api_key="AIzaSyD3jqGxbp61qda8y7j2S9FlkhziJpShfuI"
    return flask.render_template("map.html", MAPS_API_KEY=maps_api_key)
@app.route('/posts')
def posts_json():
    with open("static/unsw_6.json", encoding="utf8") as fd:
        obj = json.load(fd)

    return flask.jsonify(obj)


if __name__ == '__main__':
    app.run(debug=True)