package com.github.tobato.fastdfs.service;

import com.github.tobato.fastdfs.domain.conn.TrackerConnectionManager;
import com.github.tobato.fastdfs.domain.fdfs.*;
import com.github.tobato.fastdfs.domain.proto.tracker.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 目录服务客户端默认实现
 *
 * @author tobato
 */
@Service
public class DefaultTrackerClient implements TrackerClient {
    /**
     * 日志
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultTrackerClient.class);

    @Autowired
    private TrackerConnectionManager trackerConnectionManager;

    @Autowired
    private StorageConfig storageConfig;

    /**
     * 获取存储节点
     */
    @Override
    public StorageNode getStoreStorage() {
        if (storageConfig.isEnabled()) {
            return getStorageNode(null);
        } else {
            TrackerGetStoreStorageCommand command = new TrackerGetStoreStorageCommand();
            return trackerConnectionManager.executeFdfsTrackerCmd(command);
        }
    }

    /**
     * 按组获取存储节点
     */
    @Override
    public StorageNode getStoreStorage(String groupName) {
        if (storageConfig.isEnabled()) {
            List<StorageNode> storageNodes = listStoreStorages(groupName);
            List<StorageNode> list = storageNodes.stream()
                    .filter(x -> storageConfig.isAllowed(x.getIp()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(list)) {
                if (!CollectionUtils.isEmpty(storageNodes)) {
                    LOGGER.error("tracker return " + storageNodes.size() + " storage, but none pass storage-allow-list");
                }
                return null;
            }

            return list.get(new Random().nextInt(list.size()));
        } else {
            TrackerGetStoreStorageCommand command;
            if (StringUtils.isBlank(groupName)) {
                command = new TrackerGetStoreStorageCommand();
            } else {
                command = new TrackerGetStoreStorageCommand(groupName);
            }

            return trackerConnectionManager.executeFdfsTrackerCmd(command);
        }
    }

    @Override
    public List<StorageNode> listStoreStorages() {
        TrackerListStoreStoragesCommand command = new TrackerListStoreStoragesCommand();
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    @Override
    public List<StorageNode> listStoreStorages(String groupName) {
        TrackerListStoreStoragesCommand command;
        if (StringUtils.isBlank(groupName)) {
            command = new TrackerListStoreStoragesCommand();
        } else {
            command = new TrackerListStoreStoragesCommand(groupName);
        }

        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 获取源服务器
     */
    @Override
    public StorageNodeInfo getFetchStorage(String groupName, String filename) {
        TrackerGetFetchStorageCommand command = new TrackerGetFetchStorageCommand(groupName, filename, false);
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 获取更新服务器
     */
    @Override
    public StorageNodeInfo getUpdateStorage(String groupName, String filename) {
        TrackerGetFetchStorageCommand command = new TrackerGetFetchStorageCommand(groupName, filename, true);
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 列出组
     */
    @Override
    public List<GroupState> listGroups() {
        TrackerListGroupsCommand command = new TrackerListGroupsCommand();
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 按组列出存储状态
     */
    @Override
    public List<StorageState> listStorages(String groupName) {
        TrackerListStoragesCommand command = new TrackerListStoragesCommand(groupName);
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 按ip列出存储状态
     */
    @Override
    public List<StorageState> listStorages(String groupName, String storageIpAddr) {
        TrackerListStoragesCommand command = new TrackerListStoragesCommand(groupName, storageIpAddr);
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 删除存储节点
     */
    @Override
    public void deleteStorage(String groupName, String storageIpAddr) {
        TrackerDeleteStorageCommand command = new TrackerDeleteStorageCommand(groupName, storageIpAddr);
        trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    private StorageNode getStorageNode(String groupName) {
        List<StorageNode> storageNodes = listStoreStorages(groupName);
        List<StorageNode> list = storageNodes.stream()
                .filter(x -> storageConfig.isAllowed(x.getIp()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            if (!CollectionUtils.isEmpty(storageNodes)) {
                LOGGER.error("tracker return " + storageNodes.size() + " storage, but none pass storage-allow-list");
            }
            return null;
        }

        return list.get(new Random().nextInt(list.size()));
    }

}
