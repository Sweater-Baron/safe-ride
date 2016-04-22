import sqlite3

# def create_table():
#     db = sqlite3.connect('saferide.db')
#     cur = db.cursor()

#     cur.execute("CREATE TABLE UNSCHEDULED ( 'id' INTEGER PRIMARY KEY, 'name' VARCHAR(46) NOT NULL, 'phonenumber' VARCHAR(46) NOT NULL, 'studentid' VARCHAR(46) NOT NULL, 'pickup' VARCHAR(46) NOT NULL, 'dropoff' VARCHAR(46) NOT NULL, 'numberOfPassengers' TINYINT NOT NULL, 'time' VARCHAR(46) NOT NULL)")

def insert_request_to_db(user_request):
    db = sqlite3.connect('saferide.db')
    cur = db.cursor()
    user_request = value_cleanse(user_request)
    columns = ', '.join(user_request.keys())
    placeholders = "'"+"', '".join(user_request.values())+"'"
    query = "INSERT INTO UNSCHEDULED (%s) VALUES (%s)" % (columns, placeholders)
    print(query)
    cur.execute(query)
    db.commit()
    db.close()

def select_unscheduled():
    db = sqlite3.connect('saferide.db')
    cur = db.cursor()

    dictList = []
    fields = ["id","name", "phonenumber", "studentid", "pickup", "dropoff", "numberOfPassengers", "time"]
    for row in cur.execute("SELECT * FROM UNSCHEDULED"):
        newDict = {}
        i = 0;
        for thing in fields:
            newDict[thing] = row[i]
            i = i + 1
        dictList.append(newDict)
    db.close()
    return dictList

def delete_request(id):
    db = sqlite3.connect('saferide.db')
    cur = db.cursor()
    cur.execute("DELETE FROM UNSCHEDULED WHERE id=?", (id,))
    db.commit()
    cur.close()

def value_cleanse(dictionary):
    for keys, values in dictionary.items():
        dictionary[key] = dictionary[key].replace("'","")
        dictionary[key] = dictionary[key].replace('"',"")
    return dictionary
    



# def test_function():
#     db = sqlite3.connect('saferide.db')
#     dict2 = {'name': 'new', 'phonenumber': '503', 'studentid': '951', 'pickup': 'here', 'dropoff':'there','numberOfPassengers':'3', 'time':'4:15'};
#     dict = {'name': 'try', 'phonenumber': '503', 'studentid': '951', 'pickup': 'here', 'dropoff':'there','numberOfPassengers':'3', 'time':'4:15'};
#     insert_request_to_db(dict2);
#     insert_request_to_db(dict);




if __name__ == "__main__":
    # db = sqlite3.connect('saferide.db')
    # test_function()
    # select_all()

    print(select_unscheduled())
    # delete_request(3)
    # print(select_unscheduled())



