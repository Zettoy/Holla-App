from flask import Flask, request
app = Flask(__name__)

@app.route('/exfil', methods=["POST"])
def exfil():
    print(request)
    return ''


if __name__ == '__main__':
    app.run(debug=True)