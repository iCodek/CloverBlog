import re, os
import sys


def alter(file, new_str):
    # p = re.compile(r"(?<=]\().+?(?=/[^/]+\.[^/]+\))|(?<=<img src=\").+?(?=/[^/]+\.[^/]+\")")
    p = re.compile(r"(?<=]\()(?!https*://).+?(?=/[^/]+\.[^/]+\))|(?<=<img src=\")(?!https*://).+?(?=/[^/]+\.[^/]+\")")
    with open(file, "r", encoding="utf-8") as f1, open("%s.bak" % file, "w", encoding="utf-8") as f2:
        for line in f1:
            f2.write(re.sub(p, new_str, line))
    os.remove(file)
    os.rename("%s.bak" % file, file)


if __name__ == '__main__':
    alter(sys.argv[1], sys.argv[2])
