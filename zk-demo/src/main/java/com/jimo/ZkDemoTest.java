package com.jimo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ZkDemoTest {
    /**
     * 多个以逗号分隔
     */
    private static String connectString = "localhost:2181";
    /**
     * 超时时间
     */
    private static int sessionTimeout = 2000;
    /**
     * client
     */
    private static ZooKeeper zk;

    @BeforeClass
    public static void init() throws IOException {
        zk = new ZooKeeper(connectString, sessionTimeout, watchedEvent -> {

        });
    }

    @Test
    public void createNode() throws KeeperException, InterruptedException {
        String path = zk.create("/super", "jimo".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        assertEquals("/super", path);
    }

    @Test
    public void getNode() throws KeeperException, InterruptedException {
        List<String> children = zk.getChildren("/", false);
        children.forEach(System.out::println);
    }

    @Test
    public void getNodeWatch() throws KeeperException, InterruptedException {
        zk.getChildren("/", watcher -> {
            try {
                getNode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        TimeUnit.MINUTES.sleep(1L);
    }

    @Test
    public void nodeExist() throws KeeperException, InterruptedException {
        Stat exists = zk.exists("/super", false);
        assertNotNull(exists);
        Stat existNon = zk.exists("/hehe", false);
        assertNull(existNon);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        zk.close();
    }
}