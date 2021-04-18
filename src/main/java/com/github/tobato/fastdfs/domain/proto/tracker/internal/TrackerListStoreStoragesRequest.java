package com.github.tobato.fastdfs.domain.proto.tracker.internal;

import com.github.tobato.fastdfs.domain.proto.CmdConstants;
import com.github.tobato.fastdfs.domain.proto.FdfsRequest;
import com.github.tobato.fastdfs.domain.proto.ProtoHead;

/**
 * 获取所有存储节点请求
 */
public class TrackerListStoreStoragesRequest extends FdfsRequest {

    private static final byte withoutGroupCmd = CmdConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ALL;

    /**
     * 获取存储节点
     */
    public TrackerListStoreStoragesRequest() {
        super();
        this.head = new ProtoHead(withoutGroupCmd);
    }

}

