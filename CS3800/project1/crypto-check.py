#!/usr/bin/env python

# Ryan Skinner
# Fall 2018
# CS3800 - Gershman

import socket
import json
from datetime import datetime
import time

host = 'api.coindesk.com'
port = 80


def main():
    user_input = [".eot--fake"]  # easiest ways to write the loop below

    while not user_input[-1] == ".eot":
        line_input = input()
        if line_input != "":
            user_input.append(line_input)

    parse_input(user_input)


def parse_input(user_input):
    for line in user_input:
        if line.startswith("--"):

            if line.startswith("--help"):
                print("crypto-check.py [--bat=token] | --help | --list | --synopsis | SUBSTR\n\n" +
                      "'crypto-check' returns the price of bitcoin for the listed currencies.\n\n" +
                      "SUBSTR currency ticker name, as many as you want, one per line. \n\n" +
                      "--list shows available currency ticker names \n\n" +
                      "Version\t: 1.0.0\n" +
                      "Dependencies\t: none\n" +
                      "Author\t: Ryan Skinner\n" +
                      "Contact\t: rgskinner@cpp.edu\n")

            elif line.startswith("--synopsis"):
                print("Lists the price of bitcoin in the given currencies. (v1.0.0)")

            elif line.startswith("--bat"):
                print("I don't know how to deal with bat yet.")

            elif line.startswith("--list"):
                print_list()

            else:
                print("No argument '%s'" % line)

        elif not line.startswith(".eot"):
            report = get_report(line.upper())
            print(report)


def send_get(message):
    try:
        sock = socket.socket()
        ip_addr = socket.gethostbyname(host)
        sock.connect((ip_addr, port))
        sock.sendall(message)

    except socket.gaierror:
        print('Hostname could not be resolved. Exiting')
        sock.close()
        quit()
    except socket.error:
        print('Communication failed')
        sock.close()
        quit()

    reply = ""
    while True:
        recv = sock.recv(4096)
        reply += recv.decode("UTF-8")

        if b'\r\n\r\n' == recv[-4:] or b'}}}' == recv[-3:]: break

    sock.close()
    return reply


def get_report(name):
    message = b"GET /v1/bpi/currentprice/" + bytes(name, "UTF-8") + b".json HTTP/1.1\r\n" \
                                                                    b"Accept:application/json\r\n" \
                                                                    b"Host:" + bytes(host, "UTF-8") + b"\r\n\r\n"
    reply = send_get(message)

    try:
        j = json.loads(reply[reply.find("{\"time\":"):].splitlines()[0])
    except json.JSONDecodeError:
        return "No conversion found for " + name

    d = datetime.strptime(j['time']['updated'], '%b %d, %Y %H:%M:%S %Z')
    d = utc2local(d)

    return"1 BTC = " + j['bpi'][name]['rate'] + " " + name + ", as of " + d.strftime("%b %d, %Y %I:%M:%S %p")


def print_list():
    message = b"GET /v1/bpi/supported-currencies.json HTTP/1.1\r\n" \
              b"Accept:application/json\r\n" \
              b"Host:" + bytes(host, "UTF-8") + b"\r\n\r\n"

    reply = send_get(message)

    load = reply[reply.find("["):reply.find("]") + 1].splitlines()[0]

    j = json.loads(load)
    for i in j:
        print(i['currency'] + " = " + i['country'])


def utc2local(utc):
    epoch = time.mktime(utc.timetuple())
    offset = datetime.fromtimestamp(epoch) - datetime.utcfromtimestamp(epoch)
    return utc + offset


if __name__ == '__main__':
    main()
