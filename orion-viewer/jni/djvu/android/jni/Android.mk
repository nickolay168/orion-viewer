LOCAL_PATH := $(call my-dir)
TOP_LOCAL_PATH := $(LOCAL_PATH)

MY_ROOT := ..

include $(TOP_LOCAL_PATH)/Core.mk
#include $(TOP_LOCAL_PATH)/orion_bitmap.mk

include $(CLEAR_VARS)
LOCAL_ARM_MODE := arm

LOCAL_C_INCLUDES := \
	.. \
	../libdjvu \

LOCAL_MODULE    := djvu
LOCAL_SRC_FILES := djvu.c \
		    orion_bitmap.c
		    
LOCAL_STATIC_LIBRARIES := djvucore
#LOCAL_SHARED_LIBRARIES := orion_bitmap


LOCAL_LDLIBS    :=  -llog -lm 

include $(BUILD_SHARED_LIBRARY)
