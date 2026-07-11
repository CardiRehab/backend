package com.healthcare.herplatform.entity;

public enum Status {
	JOIN,  //0
    MESSAGE, //1
    MSG_ATTACH, //2
    LEAVE,  //3
    JOIN_CRSPL, //4
    DELETE //5 — realtime "message removed" event (not persisted); keep last to preserve ordinals
}
