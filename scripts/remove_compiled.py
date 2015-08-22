# File: remove_class.py
# Summary: Removes all class files from the current directory and subdirectories.

import os

def remove_class(trash, dirname, fnames):
	for filename in fnames:
		if not dirname.startswith(".") and (filename.endswith(".class") or filename.endswith(".pyc")):
			path = os.path.join(dirname, filename)
			os.remove(path)

if __name__ == '__main__':
	os.path.walk(os.getcwd(), remove_class, None)