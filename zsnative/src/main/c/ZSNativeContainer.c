//
// Created by king on 17-7-26.
//

#include "include/com_windjammer_zetascale_ZSNativeContainer.h"
#include "zs.h"

#include <stdlib.h>

static void setContainerProp(JNIEnv *env, ZS_container_props_t *props, jobject jcontainer_prop) {
    // set props in jcontainer_prop object
    jclass containerPropertyClass = (*env)->GetObjectClass(env, jcontainer_prop);

    // set size
    jfieldID sizeId = (*env)->GetFieldID(env, containerPropertyClass, "size", "J");
    (*env)->SetLongField(env, jcontainer_prop, sizeId, props->size_kb);
    // set fifoMode
    jfieldID fifoModeId = (*env)->GetFieldID(env, containerPropertyClass, "fifoMode", "Z");
    (*env)->SetBooleanField(env, jcontainer_prop, fifoModeId, props->fifo_mode);
    // set persistent
    jfieldID persistentId = (*env)->GetFieldID(env, containerPropertyClass, "persistent", "Z");
    (*env)->SetBooleanField(env, jcontainer_prop, persistentId, props->persistent);
    // set evicting
    jfieldID evictingId = (*env)->GetFieldID(env, containerPropertyClass, "evicting", "Z");
    (*env)->SetBooleanField(env, jcontainer_prop, evictingId, props->evicting);
    // set writethru
    jfieldID writethruId = (*env)->GetFieldID(env, containerPropertyClass, "writethru", "Z");
    (*env)->SetBooleanField(env, jcontainer_prop, writethruId, props->writethru);
    // set durabilityLevel
    jfieldID durabilityLevelId = (*env)->GetFieldID(env, containerPropertyClass, "durabilityLevel", "I");
    (*env)->SetIntField(env, jcontainer_prop, durabilityLevelId, props->durability_level);
    // set shardNumber
    jfieldID shardNumberId = (*env)->GetFieldID(env, containerPropertyClass, "shardNumber", "I");
    (*env)->SetIntField(env, jcontainer_prop, shardNumberId, props->num_shards);
    // set asyncWrite
    jfieldID asyncWriteId = (*env)->GetFieldID(env, containerPropertyClass, "asyncWrite", "Z");
    (*env)->SetBooleanField(env, jcontainer_prop, asyncWriteId, props->async_writes);
    // set flashOnly
    jfieldID flashOnlyId = (*env)->GetFieldID(env, containerPropertyClass, "flashOnly", "Z");
    (*env)->SetBooleanField(env, jcontainer_prop, flashOnlyId, props->flash_only);
    // set cacheOnly
    jfieldID cacheOnlyId = (*env)->GetFieldID(env, containerPropertyClass, "cacheOnly", "Z");
    (*env)->SetBooleanField(env, jcontainer_prop, cacheOnlyId, props->cache_only);
}

static void getContainerProp(JNIEnv *env, ZS_container_props_t *props, jobject jcontainer_prop) {
    // get props in jcontainer_prop object
    jclass containerPropertyClass = (*env)->GetObjectClass(env, jcontainer_prop);

    // get size
    jfieldID sizeId = (*env)->GetFieldID(env, containerPropertyClass, "size", "J");
    props->size_kb = (uint64_t)(*env)->GetLongField(env, jcontainer_prop, sizeId);
    // get fifoMode
    jfieldID fifoModeId = (*env)->GetFieldID(env, containerPropertyClass, "fifoMode", "Z");
    props->fifo_mode = (*env)->GetBooleanField(env, jcontainer_prop, fifoModeId);
    // get persistent
    jfieldID persistentId = (*env)->GetFieldID(env, containerPropertyClass, "persistent", "Z");
    props->persistent = (*env)->GetBooleanField(env, jcontainer_prop, persistentId);
    // get evicting
    jfieldID evictingId = (*env)->GetFieldID(env, containerPropertyClass, "evicting", "Z");
    props->evicting = (*env)->GetBooleanField(env, jcontainer_prop, evictingId);
    // get writethru
    jfieldID writethruId = (*env)->GetFieldID(env, containerPropertyClass, "writethru", "Z");
    props->writethru = (*env)->GetBooleanField(env, jcontainer_prop, writethruId);
    // get durabilityLevel
    jfieldID durabilityLevelId = (*env)->GetFieldID(env, containerPropertyClass, "durabilityLevel", "I");
    props->durability_level = (*env)->GetIntField(env, jcontainer_prop, durabilityLevelId);
    // get shardNumber
    jfieldID shardNumberId = (*env)->GetFieldID(env, containerPropertyClass, "shardNumber", "I");
    props->num_shards = (uint32_t)(*env)->GetIntField(env, jcontainer_prop, shardNumberId);
    // get asyncWrite
    jfieldID asyncWriteId = (*env)->GetFieldID(env, containerPropertyClass, "asyncWrite", "Z");
    props->async_writes = (*env)->GetBooleanField(env, jcontainer_prop, asyncWriteId);
    // get flashOnly
    jfieldID flashOnlyId = (*env)->GetFieldID(env, containerPropertyClass, "flashOnly", "Z");
    props->flash_only = (*env)->GetBooleanField(env, jcontainer_prop, flashOnlyId);
    // get cacheOnly
    jfieldID cacheOnlyId = (*env)->GetFieldID(env, containerPropertyClass, "cacheOnly", "Z");
    props->cache_only = (*env)->GetBooleanField(env, jcontainer_prop, cacheOnlyId);
}

JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSLoadCntrPropDefaults
        (JNIEnv *env, jclass jcls, jobject jcontainer_prop) {
    ZS_container_props_t    props;
    ZS_status_t status = ZSLoadCntrPropDefaults(&props);

    setContainerProp(env, &props, jcontainer_prop);

    return status;
}


JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSOpenContainer
        (JNIEnv *env, jclass jcls, jlong jthreadId, jstring jname, jint flag, jobject jcontainer_prop) {
    ZS_container_props_t    props;
    getContainerProp(env, &props, jcontainer_prop);
    struct ZS_thread_state *thd_state = (struct ZS_thread_state *)jthreadId;
    const char *cname = (*env)->GetStringUTFChars(env, jname, NULL);
    printf("open container with properties: cguid %ld, flags %d, size_kb %ld \n",
           (long)props.cguid, (int)props.flags, props.size_kb);
    ZS_cguid_t * cguid = 0;
    ZS_status_t status = ZSOpenContainer(thd_state, cname, &props, (uint32_t)flag, cguid);
    // set cguid in jcontainer_prop
    jclass containerPropertyClass = (*env)->GetObjectClass(env, jcontainer_prop);
    jfieldID cguidId = (*env)->GetFieldID(env, containerPropertyClass, "containerId", "J");
    (*env)->SetLongField(env, jcontainer_prop, cguidId, (jlong)cguid);

    (*env)->ReleaseStringUTFChars(env, jname, cname);
    return status;
}

/*
 * Class:     com_windjammer_zetascale_ZSNativeContainer
 * Method:    ZSCloseContainer
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSCloseContainer
        (JNIEnv *env, jclass jcls, jlong jthreadId, jlong jcguid) {
    struct ZS_thread_state *thd_state = (struct ZS_thread_state *)jthreadId;
    ZS_cguid_t cguid = (ZS_cguid_t)jcguid;
    ZS_status_t status = ZSCloseContainer(thd_state, cguid);
    return status;
}

/*
 * Class:     com_windjammer_zetascale_ZSNativeContainer
 * Method:    ZSDeleteContainer
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSDeleteContainer
        (JNIEnv *env, jclass jcls, jlong jthreadId, jlong jcontainerId) {
    struct ZS_thread_state *thd_state = (struct ZS_thread_state *)jthreadId;
    ZS_status_t status = ZSDeleteContainer(thd_state, (uint64_t)jcontainerId);
    return status;
}

/*
 * Class:     com_windjammer_zetascale_ZSNativeContainer
 * Method:    ZSGetContainers
 * Signature: (J)[J
 */
JNIEXPORT jlongArray JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSGetContainers
        (JNIEnv *env, jclass jcls, jlong jthreadId) {
    ZS_cguid_t *cguids = (ZS_cguid_t *)malloc(sizeof(ZS_cguid_t) * 10);
    uint32_t count;
    struct ZS_thread_state *thd_state = (struct ZS_thread_state *)jthreadId;
    ZSGetContainers(thd_state, cguids, &count);
    jlongArray ret = (*env)->NewLongArray(env, count);

    jlong *retArr = (*env)->GetLongArrayElements(env, ret, NULL);
    for (int i = 0; i < count; ++i) {
        retArr[i] = cguids[i];
    }
//    ZSFreeBuffer(cguids);
    cfree(cguids);
    (*env)->ReleaseLongArrayElements(env, ret, retArr, 0);
    return ret;
}

/*
 * Class:     com_windjammer_zetascale_ZSNativeContainer
 * Method:    ZSFlushContainer
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSFlushContainer
        (JNIEnv *env, jclass jcls, jlong jthreadId, jlong jcguid) {
    struct ZS_thread_state *thd_state = (struct ZS_thread_state *)jthreadId;
    ZS_status_t status = ZSFlushContainer(thd_state, (uint64_t)jcguid);
    return status;
}

/*
 * Class:     com_windjammer_zetascale_ZSNativeContainer
 * Method:    ZSGetContainerProps
 * Signature: (JJLcom/windjammer/zetascale/type/ContainerProperty;)I
 */
JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSGetContainerProps
        (JNIEnv *env, jclass jcls, jlong jthreadId, jlong jcguid, jobject jcontainer_prop) {
    return 1;
}

/*
 * Class:     com_windjammer_zetascale_ZSNativeContainer
 * Method:    ZSSetContainerProps
 * Signature: (JJLcom/windjammer/zetascale/type/ContainerProperty;)I
 */
JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSSetContainerProps
        (JNIEnv *env, jclass jcls, jlong jthreadId, jlong jcguid, jobject jcontainer_prop) {
    return 1;
}

/*
 * Class:     com_windjammer_zetascale_ZSNativeContainer
 * Method:    ZSWriteObject
 * Signature: (J[B[BJI)I
 */
JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSWriteObject
        (JNIEnv *env, jclass jcls, jlong jthreadId,
         jbyteArray jkey, jbyteArray jdata, jlong jcguid, jint jmode) {
    struct ZS_thread_state *thd_state = (struct ZS_thread_state *)jthreadId;
    jbyte *key = (*env)->GetByteArrayElements(env, jkey, NULL);
    jsize keylen = (*env)->GetArrayLength(env, jkey);
    jbyte *data = (*env)->GetByteArrayElements(env, jdata, NULL);
    jsize datalen = (*env)->GetArrayLength(env, jdata);
    ZS_status_t status = ZSWriteObject(thd_state, (uint64_t)jcguid, key, keylen, data, datalen, jmode);
    return status;
}

/*
 * Class:     com_windjammer_zetascale_ZSNativeContainer
 * Method:    ZSReadObject
 * Signature: (J[BJ)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSReadObject
        (JNIEnv *env, jclass jcls, jlong jthreadId, jbyteArray jkey, jlong jcguid) {
    char *data;
    uint64_t *datalen;

    struct ZS_thread_state *thd_state = (struct ZS_thread_state *)jthreadId;
    jbyte *key = (*env)->GetByteArrayElements(env, jkey, NULL);
    jsize keylen = (*env)->GetArrayLength(env, jkey);

    ZS_status_t status = ZSReadObject(thd_state, (uint64_t)jcguid, key, keylen, &data, datalen);
    if (status != ZS_SUCCESS) {
        char * msg = ZSStrError(status);
        printf("error reading: %s", msg);
        return NULL;
    }

    // todo:: use newDirectByteArray
    jbyteArray ret = (*env)->NewByteArray(env, (jsize)(*datalen));
    (*env)->SetByteArrayRegion(env, ret, 0, *datalen, data);

    ZSFreeBuffer(data);
    return ret;
}

/*
 * Class:     com_windjammer_zetascale_ZSNativeContainer
 * Method:    ZSDeleteObject
 * Signature: (J[BJ)I
 */
JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSDeleteObject
        (JNIEnv *env, jclass jcls, jlong jthreadId, jbyteArray jkey, jlong jcguid) {
    struct ZS_thread_state *thd_state = (struct ZS_thread_state *)jthreadId;
    jbyte *key = (*env)->GetByteArrayElements(env, jkey, NULL);
    jsize keylen = (*env)->GetArrayLength(env, jkey);
    ZS_status_t status = ZSDeleteObject(thd_state, (uint64_t)jcguid, key, keylen);
    return status;
}

/*
 * Class:     com_windjammer_zetascale_ZSNativeContainer
 * Method:    ZSFlushObject
 * Signature: (J[BJ)I
 */
JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSFlushObject
        (JNIEnv *env, jclass jcls, jlong jthreadId, jbyteArray jkey, jlong jcguid) {
    struct ZS_thread_state *thd_state = (struct ZS_thread_state *)jthreadId;
    jbyte *key = (*env)->GetByteArrayElements(env, jkey, NULL);
    jsize keylen = (*env)->GetArrayLength(env, jkey);
    ZS_status_t status = ZSFlushObject(thd_state, (uint64_t)jcguid, key, keylen);
    return status;
}

/*
 * Class:     com_windjammer_zetascale_ZSNativeContainer
 * Method:    ZSFlushCache
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_windjammer_zetascale_ZSNativeContainer_ZSFlushCache
        (JNIEnv *env, jclass jcls, jlong jthreadId) {
    struct ZS_thread_state *thd_state = (struct ZS_thread_state *)jthreadId;
    ZS_status_t status = ZSFlushCache(thd_state);
    return status;
}

