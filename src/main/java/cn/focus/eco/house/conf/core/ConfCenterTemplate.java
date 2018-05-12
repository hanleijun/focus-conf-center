package cn.focus.eco.house.conf.core;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;

import java.util.List;

import static cn.focus.eco.house.conf.Constants.ConfConstants.UTF8;

/**
 * Copyright (C) 1998 - 2017 SOHU Inc., All Rights Reserved.
 * <p>
 * 对zk的操作类，只允许获取型操作
 * @author: leijunhan (leijunhan@sohu-inc.com)
 * @date: 2017/12/15
 */
public class ConfCenterTemplate {

    private final static Logger logger = Logger.getLogger(ConfCenterTemplate.class);

    private final CuratorFramework client;

    public ConfCenterTemplate(Builder builder) {
        this.client = builder.getCuratorFramework();
    }

    public static class Builder{
        private CuratorFramework curatorFramework;

        public Builder() {
        }

        public CuratorFramework getCuratorFramework() {
            return curatorFramework;
        }

        /**
         * connect zk address
         * @param zkAddress
         * @return
         */
        public ConfCenterTemplate.Builder connect(String zkAddress) {
            this.curatorFramework = CuratorFrameworkFactory.newClient(zkAddress,
                    new ExponentialBackoffRetry(1000, 3));
            curatorFramework.start();
            return this;
        }

        public ConfCenterTemplate build(){
            return new ConfCenterTemplate(this);
        }
    }
    /**
     * close the template manually
     */
    public void close(){
        client.close();
    }

    /**
     * get current data for absolute path
     * @param absolutePath
     * @return
     */
    public String getData(String absolutePath){
        String pathData = "";
        try {
            byte[] data = client.getData().forPath(absolutePath);
            if(null == data){
                return pathData;
            }
            pathData = new String(data, UTF8);
        } catch (Exception e) {
            logger.info("[conf-center] getting data failed.");
            e.printStackTrace();
        }
        return pathData;
    }

    /**
     * get all children name list for absolute path
     * @param absolutePath
     * @return
     */
    public List<String> getChildren(String absolutePath){
        List<String> children = Lists.newArrayList();
        try {
            children = client.getChildren().forPath(absolutePath);
        } catch (Exception e) {
            logger.info("[conf-center] getting children failed");
            e.printStackTrace();
        }
        return children;
    }

    /**
     * get original curator zk client
     * @return
     */
    protected CuratorFramework getOriginClient(){
        return client;
    }
}
