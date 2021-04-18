package com.github.tobato.fastdfs.domain.proto.tracker;

import com.github.tobato.fastdfs.domain.proto.tracker.internal.TrackerListStoreStoragesRequest;
import com.github.tobato.fastdfs.domain.proto.tracker.internal.TrackerListStoreStoragesResponse;
import com.github.tobato.fastdfs.domain.proto.tracker.internal.TrackerListStoreStoragesWithGroupRequest;
import com.github.tobato.fastdfs.domain.fdfs.StorageNode;
import com.github.tobato.fastdfs.domain.proto.AbstractFdfsCommand;

import java.util.List;

/**
 * 获取所有存储节点命令
 */
public class TrackerListStoreStoragesCommand extends AbstractFdfsCommand<List<StorageNode>> {

    public TrackerListStoreStoragesCommand() {
        super.request = new TrackerListStoreStoragesRequest();
        super.response = new TrackerListStoreStoragesResponse();
    }

    public TrackerListStoreStoragesCommand(String groupName) {
        super.request = new TrackerListStoreStoragesWithGroupRequest(groupName);
        super.response = new TrackerListStoreStoragesResponse();
    }

}
