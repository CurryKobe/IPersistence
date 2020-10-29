package com.sc.io;

import java.io.InputStream;

public class Resource {

    /**
     * 根据配置文件路径，将配置文件加载成字节流，存储到内存中
     * @param path
     * @return
     */
    public static InputStream getResourceAsStream(String path) {
        InputStream resourceAsStream = Resource.class.getClassLoader().getResourceAsStream(path);
        return resourceAsStream;
    }

}
