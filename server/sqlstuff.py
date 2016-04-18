import sqlite3

def create_table():
	db = sqlite3.connect('saferide.db')
	cur = db.cursor()

	cur.execute("CREATE TABLE UNSCHEDULED ( 'id' INTEGER PRIMARY KEY, 'name' VARCHAR(46) NOT NULL, 'phonenumber' VARCHAR(46) NOT NULL, 'studentid' VARCHAR(46) NOT NULL, 'pickup' VARCHAR(46) NOT NULL, 'dropoff' VARCHAR(46) NOT NULL, 'numberOfPassengers' TINYINT NOT NULL, 'time' VARCHAR(46) NOT NULL)")

def insert_request(name, phonenumber, studentid, pickup, dropoff, numberOfPassengers, time):
	db = sqlite3.connect('saferide.db')
	cur = db.cursor()
	cur.execute("INSERT INTO UNSCHEDULED (name, phonenumber, studentid, pickup, dropoff, numberOfPassengers, time) VALUES (?,?,?,?,?,?,?)", (name, phonenumber, studentid, pickup, dropoff, numberOfPassengers, time))

def insert_request_to_db(user_request):
	db = sqlite3.connect('saferide.db')
	cur = db.cursor()
	qmarks = ', '.join('?' * len(user_request))
	qry = "Insert Into UNSCHEDULED (%s) Values (%s)" % (qmarks, qmarks)
	cur.execute(qry, user_request.keys() + user_request.values())

if __name__ == "__main__":
    create_table()