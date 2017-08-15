# how to run it

0. compile zetascale on your own platform.

1. copy zs_sdk-xxx to zsnative/src/main/c/zs_sdk-2.0 and copy the lib/libzs.so to /usr/local/lib/libzs.so

2. sudo apt install build-essential libaio-devel libevent-devel libsnappy-devel

mvn compile

mvn test