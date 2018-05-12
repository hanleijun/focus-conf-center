package cn.focus.eco.house.conf;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * Copyright (C) 1998 - 2017 SOHU Inc., All Rights Reserved.
 * <p>
 *
 * @Author: leijunhan (leijunhan@sohu-inc.com)
 * @Date: 2017/12/15
 */
public class Test {
    private static final String PATH = "/consumers/callcenter/ids";
    public static void main(String[] args) throws Exception{
        CuratorFramework client = CuratorFrameworkFactory.newClient("ddddd",
                new ExponentialBackoffRetry(1000, 3));
        client.start();
        PathChildrenCache cache = new PathChildrenCache(client, PATH, true);
        cache.start();
        PathChildrenCacheListener cacheListener = (client1, event) -> {
            System.out.println("事件类型：" + event.getType());
            if (null != event.getData()) {
                System.out.println("节点数据：" + event.getData().getPath() + " = " + new String(event.getData().getData()));
            }
        };
        cache.getListenable().addListener(cacheListener);
        byte[] obj = client.getData().forPath("/consumers/callcenter/ids/callcenter_bd09d7bae2fc-1512463403741-57284417");
        List<String> children = client.getChildren().forPath("/consumers/callcenter/ids");
        System.out.println(children);
//        client.create().creatingParentsIfNeeded().forPath("/example/pathCache/test01", "01".getBytes());
//        Thread.sleep(10);
//        client.create().creatingParentsIfNeeded().forPath("/example/pathCache/test02", "02".getBytes());
//        Thread.sleep(10);
//        client.setData().forPath("/example/pathCache/test01", "01_V2".getBytes());
//        Thread.sleep(10);
//        for (ChildData data : cache.getCurrentData()) {
//            System.out.println("getCurrentData:" + data.getPath() + " = " + new String(data.getData()));
//        }
//        client.delete().forPath("/example/pathCache/test01");
//        Thread.sleep(10);
//        client.delete().forPath("/example/pathCache/test02");
//        Thread.sleep(1000 * 5);
//        cache.close();
//        client.close();
//        System.out.println("OK!");
    }
}
