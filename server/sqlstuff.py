import sqlite3

# def create_table():
# 	db = sqlite3.connect('saferide.db')
# 	cur = db.cursor()

# 	cur.execute("CREATE TABLE UNSCHEDULED ( 'id' INTEGER PRIMARY KEY, 'name' VARCHAR(46) NOT NULL, 'phonenumber' VARCHAR(46) NOT NULL, 'studentid' VARCHAR(46) NOT NULL, 'pickup' VARCHAR(46) NOT NULL, 'dropoff' VARCHAR(46) NOT NULL, 'numberOfPassengers' TINYINT NOT NULL, 'time' VARCHAR(46) NOT NULL)")

def insert_request_to_db(user_request):
	db = sqlite3.connect('saferide.db')
	cur = db.cursor()
	columns = ', '.join(user_request.keys())
	placeholders = "'"+"', '".join(user_request.values())+"'"
	query = "INSERT INTO UNSCHEDULED (%s) VALUES (%s)" % (columns, placeholders)
	print(query)
	cur.execute(query)
	db.commit()
	db.close()

def select_all():
	db = sqlite3.connect('saferide.db')
	cur = db.cursor()
	cur.execute("SELECT * FROM UNSCHEDULED")
	print(len(cur.fetchall()))


def test_function():
	db = sqlite3.connect('saferide.db')
	dict2 = {'name': 'new', 'phonenumber': '503', 'studentid': '951', 'pickup': 'here', 'dropoff':'there','numberOfPassengers':'3', 'time':'4:15'};
	dict = {'name': 'try', 'phonenumber': '503', 'studentid': '951', 'pickup': 'here', 'dropoff':'there','numberOfPassengers':'3', 'time':'4:15'};
	insert_request_to_db(dict2);
	insert_request_to_db(dict);




if __name__ == "__main__":
	db = sqlite3.connect('saferide.db')
	test_function()
	select_all()