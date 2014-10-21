#!/usr/bin/python

import sys
import requests
import json
import argparse
import random
import time;

#API key from Google Developer Console
API_KEY = 'REPLACE_WITH_YOUR_APP_ID'


#Replace with your device-specific registration id
REG_ID = 'REPLACE_WITH_YOUR_REG_ID'


def sendMessage(message):
	url = 'https://android.googleapis.com/gcm/send'
	data = { 
		"registration_ids" : [ REG_ID ],

    #This 'data' object is what is actually made available to the Android App
		"data" : {      
			"message" : message
		}
	}

	headers = {
		'Content-Type' : 'application/json',
		'Authorization' : 'key=' + API_KEY 
	}

	r = requests.post(url, data=json.dumps(data), headers=headers)

	print r.text


#if len(sys.argv) > 1:
	#Send the message from the command line
sendMessage({ 
	#"id" : sys.argv[1], 
	"message" : sys.argv[1]
})