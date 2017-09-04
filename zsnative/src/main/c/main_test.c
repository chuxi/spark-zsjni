//
// Created by king on 17-7-25.
//

#include <stdlib.h>
#include "zs.h"

int main(int argc, char *argv[]) {
    struct ZS_state		*zs_state;
    long add = 1;
    struct ZS_thread_state		*thd_state = (struct ZS_thread_state		*)&add;
    ZS_status_t			status;
    ZS_container_props_t		props;
    char				cname[32] = "container";
    ZS_cguid_t			cguid;
    uint64_t datalen;
    char				*version;

    if (ZSLoadProperties("zs_sdk-2.0/config/zs_sample.prop") != ZS_SUCCESS) {
        printf("error loading properties.");
        return 1;
    }

    //Get the version ZS the program running with.
    if (ZSGetVersion(&version) == ZS_SUCCESS) {
        printf("This is a sample program using ZS %s\n", version);
        ZSFreeBuffer(version);
    }

    //Initialize ZS state.
    if ((status = ZSInit(&zs_state)) != ZS_SUCCESS) {
        printf("ZSInit failed with error %s\n", ZSStrError(status));
        return 1;
    }

    //Initialize per-thread ZS state for main thread.
    if ((status = ZSInitPerThreadState(zs_state, &thd_state)) != ZS_SUCCESS) {
        printf("ZSInitPerThreadState failed with error %s\n",
               ZSStrError(status));
        return 1;
    }

    //Fill up property with default values.
    ZSLoadCntrPropDefaults(&props);

    //Set size of container to 256MB and retain other values.
    props.size_kb = 6*1024 *1024;
    props.flash_only = (ZS_boolean_t)0;

    //Create container in read/write mode with properties specified.
    status = ZSOpenContainer(thd_state, cname, &props,
                             ZS_CTNR_CREATE | ZS_CTNR_RW_MODE, &cguid);

    status = ZSGetContainerProps(thd_state, cguid, &props);
    printf("Container %s (cguid: %ld) created with size: %ldKB.\n",
           cname, cguid, props.size_kb);

    ZSWriteObject(thd_state, cguid, "key1", 4, "data1", 5, 0);

    char * data = (char *)malloc(100);
    ZSReadObject(thd_state, cguid, "key1", 4, &data, &datalen);

    printf("key: %s, data: %s\n", "key1", data);

    ZSReleasePerThreadState(&thd_state);
    ZSShutdown(zs_state);
    return 0;
}