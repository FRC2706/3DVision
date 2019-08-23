# Usage

First you must download and compile my forked version of libfreenect

Step 1: do a git clone of https://github.com/thatmadhacker/libfreenect
Step 2: run `cd libfreenect && mkdir build && cd build`
Step 3: run `cmake ..`
Step 4: run `make && sudo make install`
Step 5: run `sudo ln -sf ../include/* /usr/include/`

`fix.o` must be run every time the connect is plugged in.
This can be done through a udev rule

Before running `fix.o` you must run `python fwfetcher.py` in the same directory as `fix.o`, it will download `audios.bin` and make the kinect work properly
