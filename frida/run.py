import frida
import argparse

def on_message(message, payload):
    print(message)
    print(payload)

parser = argparse.ArgumentParser()
parser.add_argument('app_name', type=str)
parser.add_argument('script_file', type=str)
args = parser.parse_args()

device = frida.get_usb_device()
pid = device.spawn([args.app_name])
session = device.attach(pid)
with open(args.script_file) as fd:
    script = session.create_script(fd.read())
script.on("message", on_message)
script.load()
device.resume(pid)

input()
