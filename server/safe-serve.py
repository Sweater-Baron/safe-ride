import flask
from flask import render_template
from flask import request
from flask import url_for
from flask import redirect

import json
import uuid
import logging

import sqlstuff

# Global stuff:
import CONFIG

app = flask.Flask(__name__)
app.secret_key = str(uuid.uuid4())

# Pages:

@app.route("/")
@app.route("/index")
def index():
  app.logger.debug("Index")
  return flask.render_template('index.html')

@app.route("/_createFromApp", method=['POST'])
def handle_app_request():
    fields = ["name", "studentid", "phonenumber", "pickup", "dropoff", "numberOfPassengers"]
    user_request = {}
    for thing in fields:
        user_request[thing] = request.args.get(thing, type=str)
    #user_request["numberOfPassengers"] = request.args.get(
    #    "numberOfPassengers", type=int)
    #print("user request: "+user_request)
    #sqlstuff.insert_request_to_db(user_request)
    #sqlstuff.select_all()

    return flask.redirect("/confirmation", 303)

@app.route("/dispatch")
def dispatch_page():
    ride_request = {
                    "note":"This is an example, remember to remove it",
                    "name":"Chad Thunderdick",
                    "studentid":"951696969",
                    "phonenumber":"541-555-5555",
                    "pickup":"1888 Hilyard St.",
                    "dropoff":"Your place ;) ;) ;)",
                    "numberOfPassengers":"1"
                    }
    flask.g.rides = [ride_request for x in range(5)]
    return flask.render_template("dispatch.html")

@app.route("/confirmation")
def confirmation_page():
    return flask.render_template("confirmation.html")
  
@app.errorhandler(404)
def page_not_found(error):
    app.logger.debug("404")
    return flask.render_template('page_not_found.html'), 404


if __name__ == "__main__":
    app.debug=CONFIG.DEBUG
    app.logger.setLevel(logging.DEBUG)
    app.run(port=CONFIG.PORT,host="0.0.0.0")
