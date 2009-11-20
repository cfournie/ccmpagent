#!/usr/bin/python
import subprocess
import os
import sys
import re

REPO = "http://svn.scesoc.ca/sysc5103-ccmp/trunk/"
USERNAME = "svndailymailer"
PASSWORD = "STEga2uHUwrad9Pa"

RE_REVISION = re.compile(r"^r(\d{1,6}) \|")

def main():
  try:
    next_revision_f = open("next_revision", "r")
  except:
    next_revision = 1
  else:
    next_revision = int(next_revision_f.read())
    next_revision_f.close()

  devnull = open(os.devnull)
  
  # FIXME: long-lasting svn processes reveal username/password in `ps`
  svn_args = [
    "svn", "log",
    "-v", "-r", "HEAD:%d" % next_revision,
    "--username", USERNAME,
    "--password", PASSWORD,
    REPO
  ]
  proc = subprocess.Popen(svn_args,
    stdin=devnull, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
  output, error = proc.communicate()
  
  if proc.returncode != 0:
    if error.find("No such revision") != -1:
      # It's cool, nothing to do.
      return 0
    else:
      print "SVN Error:"
      print "Command:", " ".join(svn_args)
      print output
      print error
      return 1

  lines = output.splitlines()
  next_revision = int(lines[1].split()[0][1:]) + 1

  open("next_revision", "w").write(str(next_revision))

  print output
  
  return 0

if __name__ == "__main__":
  sys.exit(main())
