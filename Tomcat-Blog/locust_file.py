# locust_test.py
# In python, '#' is used to indicate a comment line.
"""
The string within triple-quote is also considered as a comment.
And the triple-quote can be used for multiline comments.
DISCLAIMER: This sample doesn't care about whether the use of API is correct.


In this file, we are simulating the scenario where all requests from users are read intensive. 
The user whose name is cs144 would randomly open one of his posts via /editor/post?action=open&username=cs144&postid={num}, 
where {num} is a random postid.
Note: In this test file, use /editor/post?action=open as the name for the requests. Also, make sure that postid that user opens 
should be between 1 and 500. Since our user "cs144" only has 500 posts, he will get nothing otherwise!

"""

import sys, random
from locust import HttpLocust, TaskSet

'''def getList(locust):
    """ define a function in python whose name is getList and the argument is locust """
    locust.client.get('/api/cs144')
    locust.client.get('/editor/post?action=list&username=cs144')

def previewPage(locust):
    """ define a function in python whose name is previewPage and the argument is locust """
    postid = random.randint(1, 100) # generate a random number from 1 to 100 (include 1 and 100)
    url_prefix = '/blog/cs144/';
    locust.client.get(url_prefix + str(postid), name=url_prefix)'''

def readPost(locust):
    postid = random.randint(1, 500) #generate a random number from 1 to 500 (include 1 and 500)
    locust.client.get('/editor/post?action=open&username=cs144&postid=' + str(postid))


class MyTaskSet(TaskSet):
    """ the class MyTaskSet inherits from the class TaskSet, defining the behavior of the user """
    #tasks = {getList: 2, previewPage: 1}
    tasks = {readPost}
    def on_start(locust):
        """ on_start is called when a Locust start before any task is scheduled """
        #response = locust.client.post("/login", {"username":"cs144", "password": "password"})
        #if response.status_code != 200:
        #    print("FAIL to start with posting data to server. Make sure that your server is running.")
        #    sys.exit();

class MyLocust(HttpLocust):
    """ the class MyLocust inherits from the class HttpLocust, representing an HTTP user """
    task_set = MyTaskSet
    min_wait = 1000
    max_wait = 2000
