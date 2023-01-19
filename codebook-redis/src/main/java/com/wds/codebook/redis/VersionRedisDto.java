package com.wds.codebook.redis;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class VersionRedisDto {

    private String appKey;
    private Long versionCode;
    private Long currReleaseNum;
    private Integer forceUpdate;
    private String companyIds;


    public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<>();
        map.put("versionCode",String.valueOf(versionCode));
        map.put("currReleaseNum",String.valueOf(currReleaseNum));
        map.put("forceUpdate",String.valueOf(forceUpdate));
        map.put("companyIds",companyIds);
        return map;
    }
    public VersionRedisDto() {
    }
}
