### 主要特性
基于  [tobato / FastDFS_Client](https://github.com/tobato/FastDFS_Client.git) 构建而成。

支持文件读写偏向特定机房。

有场景：机房A，机房B，互相不连通，跨机房搭建了FastDFS，两个机房都可以访问到相同的文件。
但是机房A中的应用在使用FastDFS时，也需要申请机房B的防火墙，因为在读写文件的时候，tracker可能返回另外一个机房的节点，
实际情况下存在希望应用只读写机房A的机器，不希望读写机房B的机器。FastDFS_Client未提供选择storage IP的能力。
其实FastDFS服务端提供了获取所有storage节点的命令，基于这些命令，构建可以偏向机房的客户端。

实现原理：扩展了获取所有 storage 节点的命令，在向 tracker 获取 storage 节点时，如果配置了 `storage-allow-list` 参数，那么会请求服务器获取所有 storage 节点，根据 `storage-allow-list` 过滤后随机返回一个 storage 作为后续文件操作的机器。


### 3.在 application.yml 当中配置 Fdfs 相关参数

    # ===================================================================
    # 分布式文件系统FDFS配置
    # ===================================================================
    fdfs:
      so-timeout: 10000
      connect-timeout: 10000
      thumb-image:             #缩略图生成参数
        width: 150
        height: 150
      tracker-list:            #TrackerList参数,支持多个
        - 192.168.1.105:22122
      storage-allow-list:      #新增配置项，可选参数，过滤tracker节点返回的storage，读写文件时使用的节点，双机房配置时，存在只申请单一机房防火墙时，通过这个配置来限定服务器范围，默认允许所有节点
        - 192.168.1.105
