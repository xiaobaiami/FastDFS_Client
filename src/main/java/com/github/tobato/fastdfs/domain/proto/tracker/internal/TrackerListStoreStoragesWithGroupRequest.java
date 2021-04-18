package com.github.tobato.fastdfs.domain.proto.tracker.internal;

import com.github.tobato.fastdfs.domain.proto.CmdConstants;
import com.github.tobato.fastdfs.domain.proto.FdfsRequest;
import com.github.tobato.fastdfs.domain.proto.OtherConstants;
import com.github.tobato.fastdfs.domain.proto.ProtoHead;
import com.github.tobato.fastdfs.domain.proto.mapper.FdfsColumn;
import org.apache.commons.lang3.Validate;

/**
 * 按分组获取所有存储节点
 */
public class TrackerListStoreStoragesWithGroupRequest extends FdfsRequest {

    private static final byte withGroupCmd = CmdConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ALL;

    /**
     * 分组定义
     */
    @FdfsColumn(index = 0, max = OtherConstants.FDFS_GROUP_NAME_MAX_LEN)
    private final String groupName;

    /**
     * 获取存储节点
     *
     * @param groupName
     */
    public TrackerListStoreStoragesWithGroupRequest(String groupName) {
        Validate.notBlank(groupName, "分组不能为空");
        this.groupName = groupName;
        this.head = new ProtoHead(withGroupCmd);
    }

    public String getGroupName() {
        return groupName;
    }

}
