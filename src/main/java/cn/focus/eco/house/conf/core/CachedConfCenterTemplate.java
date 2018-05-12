package cn.focus.eco.house.conf.core;

import cn.focus.eco.house.conf.core.listener.PathChangeListener;
import cn.focus.eco.house.conf.core.listener.TreeChangeListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.log4j.Logger;

/**
 * Copyright (C) 1998 - 2017 SOHU Inc., All Rights Reserved.
 * <p>
 * create {@link PathChildrenCache} fluently
 * @author: leijunhan (leijunhan@sohu-inc.com)
 * @date: 2017/12/15
 */
public class CachedConfCenterTemplate {
    private ConfCenterTemplate confCenterTemplate;
    private String watchPath;
    private final static Logger logger = Logger.getLogger(CachedConfCenterTemplate.class);

    public ConfCenterTemplate getConfCenterTemplate() {
        return confCenterTemplate;
    }

    public String getWatchPath() {
        return watchPath;
    }

    public CachedConfCenterTemplate(Builder builder) {
        confCenterTemplate = builder.getConfCenterTemplate();
        watchPath = builder.getWatchPath();
    }

    public static class Builder{
        private ConfCenterTemplate confCenterTemplate;
        private String watchPath;

        public Builder() {
        }

        public ConfCenterTemplate getConfCenterTemplate() {
            return confCenterTemplate;
        }

        /**
         * register your own conf center template
         * @param confCenterTemplate
         * @return
         */
        public Builder register(ConfCenterTemplate confCenterTemplate) {
            this.confCenterTemplate = confCenterTemplate;
            return this;
        }

        public String getWatchPath() {
            return watchPath;
        }

        /**
         * set the conf path to watch
         * @param watchPath
         * @return
         */
        public Builder watch(String watchPath) {
            this.watchPath = watchPath;
            return this;
        }

        public CachedConfCenterTemplate build(){
            return new CachedConfCenterTemplate(this);
        }
    }


    @Deprecated
    public void withTreeListener(TreeChangeListener listener) throws Exception{
        TreeCache treeCache = new TreeCache(this.getConfCenterTemplate().getOriginClient(), this.getWatchPath());
        treeCache.getListenable().addListener(listener);
        treeCache.start();
    }

    /**
     * register your own listener
     * @param listener
     * @throws Exception
     */
    public void withPathListener(PathChangeListener listener) throws Exception{
        PathChildrenCache childrenCache = new PathChildrenCache(this.getConfCenterTemplate().getOriginClient(), this.getWatchPath(), true);
        childrenCache.getListenable().addListener(listener);
        logger.info("Register zk watcher successfully!");
        childrenCache.start(PathChildrenCache.StartMode.NORMAL);
    }
}
