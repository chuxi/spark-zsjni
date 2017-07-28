cmake_minimum_required(VERSION 3.7)
project(zsnative)

set(CMAKE_C_STANDARD 11)

set(ZS_SDK_VERSION "2.0")

include_directories($ENV{JAVA_HOME}/include)
include_directories($ENV{JAVA_HOME}/include/linux)
include_directories("${CMAKE_SOURCE_DIR}/zs_sdk-${ZS_SDK_VERSION}/include")

set(SOURCE_FILES ZSNative.c ZSNativeUtils.c ZSNativeThread.c ZSNativeContainer.c)

#add_library(zsnative SHARED ${SOURCE_FILES})

set(MAIN_FILE main_test.c)
add_executable(zsnative ${MAIN_FILE})
target_link_libraries(zsnative ${CMAKE_SOURCE_DIR}/zs_sdk-${ZS_SDK_VERSION}/lib/libzsdll.a pthread aio event snappy dl )