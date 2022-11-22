package com.geek.microservice.service2.service;

import com.geek.microservice.service2.api.IService2Api;

/**
 * @author geek
 */
@org.apache.dubbo.config.annotation.Service
public class Service2ApiImpl implements IService2Api {

    /**
     * dubbo。
     *
     * @return
     */
    @Override
    public String dubboService2() {
        return " ~ Service2ApiImpl ~ dubboService2;";
    }

}
