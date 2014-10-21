pushme
======

Simple implementation of GCM, handling registration and receipt of a notification.  Also demonstrates who to build notifications with Actions (as of Jelly Bean), sound etc.

gcm_push.py is a script that allows oyu to easily send a push request to GCM.
You will of course, need to update the script with you GCM API Key and the device_reg_id.

example use: ./gcm_push.py "This is the message body."

curl is an example of how to send push requests to GCM using curl commands and a data file.
