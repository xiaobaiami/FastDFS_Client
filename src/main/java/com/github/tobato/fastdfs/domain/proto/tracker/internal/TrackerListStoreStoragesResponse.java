package com.github.tobato.fastdfs.domain.proto.tracker.internal;

import com.github.tobato.fastdfs.domain.fdfs.StorageNode;
import com.github.tobato.fastdfs.domain.proto.FdfsResponse;
import com.github.tobato.fastdfs.domain.proto.OtherConstants;
import com.github.tobato.fastdfs.domain.proto.mapper.FdfsParamMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 列出组存储节点执行结果
 */
public class TrackerListStoreStoragesResponse extends FdfsResponse<List<StorageNode>> {

    private static final int FDFS_STORE_STORAGE_BODY_MAX_LEN = 23;
    private static final int FDFS_STORE_STORAGE_PAHT_INDEX_MAX_LEN = 1;

    /**
     * 解析反馈内容
     */
    @Override
    public List<StorageNode> decodeContent(InputStream in, Charset charset) throws IOException {
        // 解析报文内容
        byte[] bytes = new byte[(int) getContentLength()];
        int contentSize = in.read(bytes);
        if (contentSize != getContentLength()) {
            throw new IOException("读取到的数据长度与协议长度不符");
        }
        return decode(bytes, charset);

    }

    /**
     * 解析存储节点
     * 当获取多个存储节点时，报文的格式为：
     * groupName（16位） +  N组StorageNode(N * 23位) + 存储路径Index（1位）
     *
     * @param bs
     * @param charset
     * @return
     * @throws IOException
     */
    private List<StorageNode> decode(byte[] bs, Charset charset) throws IOException {

        // 获取对象转换定义
        if ((bs.length - FDFS_STORE_STORAGE_PAHT_INDEX_MAX_LEN - OtherConstants.FDFS_GROUP_NAME_MAX_LEN) % FDFS_STORE_STORAGE_BODY_MAX_LEN != 0) {
            throw new IOException("byte array length: " + bs.length + " is invalid!");
        }
        // 计算反馈对象数量
        int count = (bs.length - FDFS_STORE_STORAGE_PAHT_INDEX_MAX_LEN - OtherConstants.FDFS_GROUP_NAME_MAX_LEN) / FDFS_STORE_STORAGE_BODY_MAX_LEN;
        int offset = 0;
        List<StorageNode> results = new ArrayList<StorageNode>(count);
        byte[] groupNameBytes = new byte[OtherConstants.FDFS_GROUP_NAME_MAX_LEN];
        byte[] storePathIndexBytes = new byte[FDFS_STORE_STORAGE_PAHT_INDEX_MAX_LEN];

        System.arraycopy(bs, offset, groupNameBytes, 0, OtherConstants.FDFS_GROUP_NAME_MAX_LEN);
        System.arraycopy(bs, bs.length - 1, storePathIndexBytes, 0, FDFS_STORE_STORAGE_PAHT_INDEX_MAX_LEN);
        offset += OtherConstants.FDFS_GROUP_NAME_MAX_LEN;

        for (int i = 0; i < count; i++) {
            byte[] one = new byte[OtherConstants.FDFS_GROUP_NAME_MAX_LEN + FDFS_STORE_STORAGE_BODY_MAX_LEN + 1];
            System.arraycopy(groupNameBytes, 0, one, 0, OtherConstants.FDFS_GROUP_NAME_MAX_LEN);
            System.arraycopy(bs, offset, one, OtherConstants.FDFS_GROUP_NAME_MAX_LEN, FDFS_STORE_STORAGE_BODY_MAX_LEN);
            System.arraycopy(storePathIndexBytes, 0, one, OtherConstants.FDFS_GROUP_NAME_MAX_LEN + FDFS_STORE_STORAGE_BODY_MAX_LEN, FDFS_STORE_STORAGE_PAHT_INDEX_MAX_LEN);
            results.add(FdfsParamMapper.map(one, StorageNode.class, charset));
            offset += FDFS_STORE_STORAGE_BODY_MAX_LEN;
        }

        return results;
    }
}
