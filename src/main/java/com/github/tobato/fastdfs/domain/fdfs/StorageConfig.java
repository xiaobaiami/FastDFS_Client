package com.github.tobato.fastdfs.domain.fdfs;

import com.github.tobato.fastdfs.FdfsClientConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = FdfsClientConstants.ROOT_CONFIG_PREFIX)
public class StorageConfig {

    /**
     * 允许的storage IP
     */
    private List<String> storageAllowList = new ArrayList<String>();

    public boolean isEnabled() {
        return !CollectionUtils.isEmpty(storageAllowList);
    }

    public boolean isAllowed(String ip) {
        return !isEnabled() || storageAllowList.contains(ip);
    }

    public List<String> getStorageAllowList() {
        return storageAllowList;
    }

    public void setStorageAllowList(List<String> storageAllowList) {
        this.storageAllowList = storageAllowList;
    }
}
