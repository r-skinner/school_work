# Socket client example in python

import socket  # for sockets
import json

s = socket.socket()


host = 'api.coindesk.com'
port = 80

try:
    sock = socket.socket()
    ip_addr = socket.gethostbyname(host)

    sock.connect((ip_addr, port))

    message = b"GET /v1/bpi/supported-currencies.json HTTP/1.1\r\n" \
              b"Accept:application/json\r\n" \
              b"Host:" + bytes(host, "UTF-8") + b"\r\n\r\n"

    sock.sendall(message)
except socket.gaierror:
    print('Hostname could not be resolved. Exiting')
    quit()
except socket.error:
    print('Communication failed')
    quit()

reply = ""
while True:
    s = sock.recv(4096).decode("UTF-8")
    if reply.count(']'):
        break
    reply += s

sock.close()

load = reply[reply.find("["):reply.find("]")+1].splitlines()[0]

#print(load)

j = json.loads(load)
for i in j:
    print(i['currency'] + " = " + i['country'])